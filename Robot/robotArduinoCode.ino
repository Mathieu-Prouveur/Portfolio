#include <OliStepper.h>
#include <Servo.h>
#include <NewPing.h>

float ANGLE_STEP = 5;
float MAX_DISTANCE = 100; // in cm
OliStepper stepper_r(8, 9, 10, 11, 512);
OliStepper stepper_l(4, 5, 6, 7, 512);
float rpm_l;
float rpm_r;
Servo servo;
NewPing sonar(12, 3, MAX_DISTANCE);
//boolean scanning = false;
String instructions = "";
String inst_buffer = "";

void set_rpm(float l, float r) {
  rpm_l = l;
  rpm_r = r;
}

void update_steppers() {
  if (rpm_r > 0) {
    stepper_r.setDirection("CW");
  }
  else {
    stepper_r.setDirection("CCW");
  }
  stepper_r.setRPM(abs(rpm_r));
  if (rpm_l < 0) {
    stepper_l.setDirection("CW");
  }
  else {
    stepper_l.setDirection("CCW");
  }
  stepper_l.setRPM(abs(rpm_l));
}

void oli_run() {
  stepper_l.oliRun();
  stepper_r.oliRun();
}

void update_instructions(String serial_message) {
  inst_buffer += serial_message;
  int first_C = inst_buffer.indexOf('C');
  int last_C = inst_buffer.lastIndexOf('C');
  while (first_C < last_C) {
    inst_buffer = inst_buffer.substring(first_C + 1);
    first_C = inst_buffer.indexOf('C');
    last_C = inst_buffer.lastIndexOf('C');
  }
  int first_A = inst_buffer.indexOf('A');
  instructions = inst_buffer.substring(first_A, first_C + 1);
}

void move_and_send(int pos) {
  servo.write(pos);
//  float distance = sonar.convert_cm(sonar.ping_median(4));
    float distance = sonar.convert_cm(sonar.ping());

  String m=String(distance)+"S";
  Serial.print(m);
  // TODO send the distance
  //analogWrite(3, distance);
}

void scan() {
  servo.write(180);
  Serial.write('S');
  delay(50);
  for (int pos = 180; pos >= 0; pos -= ANGLE_STEP) {
    delay(50);
    move_and_send(pos);
  }
}

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  servo.attach(13);
  servo.write(90);
  rpm_l = 0.0; // no movement to begin with
  rpm_r = 0.0;
  update_steppers();
}

void loop() {
  // put your main code here, to run repeatedly:
  //servo.write(30);
  oli_run();
  if (Serial.available() > 0) {
    String message = Serial.readString();
    if (message[0]=='S') {  
      set_rpm(0.0, 0.0);
      update_steppers();
      scan();
      servo.write(90);
    }
    else {
      update_instructions(message);
      Serial.println(message);
      int ind = instructions.indexOf("B");
      String inst_l = instructions.substring(1, ind);
      String inst_r = instructions.substring(ind + 1, instructions.length() - 1);
      float new_rl = inst_l.toFloat();
      Serial.print(new_rl);
      float new_rr = inst_r.toFloat();
      Serial.print(new_rr);
      if (new_rl != rpm_l | new_rr != rpm_r) {
        set_rpm(new_rl, new_rr);
        update_steppers();
      }
    }
  }
}


