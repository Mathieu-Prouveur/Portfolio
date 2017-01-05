float vg, vd, length;
PFont fontA;
import processing.serial.*;
// The serial port:
Serial myPort;
long temps=millis();
String message, myString;
int i;
boolean commande=false;
boolean isScanning=false;
int deg=5;
int nbDiv=1+180/5;//tout les 10 degrés
float portee=100;
long dernierScan;
float[] scan=new float[nbDiv];
////Sound to maximize fun
SoundFile fileM,fileS;

import processing.sound.*;

void setup() {
  
  fileM = new SoundFile(this, "MissionImpossible.mp3");
  fileM.loop(0.5);
  fileM.amp(0.3);
  fileS = new SoundFile(this, "scan.mp3");

  
  for (int i=0; i<nbDiv; i++) {
    scan[i]=portee;
    println(scan[i]);
  }
  printArray(Serial.list());
  myPort = new Serial(this, Serial.list()[0], 9600);
  i=0;

  //size(1080, 640);
  fullScreen();
  vg=0.0;
  vd=0.0;
  println("vg= " + vg + "  vd= "+ vd);


  background(150, 150, 220);
  myPort.write("A"+"14"+"B"+"14"+"C");
  delay(100);

  myPort.write("A"+"14"+"B"+"14"+"C");
}

void draw() {
  if (millis()-temps>2000&isScanning==true) {
    fileM.amp(0.1);
    fileS.play(0.5);
    fileS.amp(1.0);
    isScanning=false;
    updateScan();
    commande=true;
    dernierScan=millis();
   fileM.amp(0.3);
  }

  if (millis()-temps>2000&commande) {
    message="A"+vg+"B"+vd+"C";
    myPort.write(message);
    
    temps=millis();
    commande=false;
  }

  while (myPort.available() > 0) {
    myString=myPort.readString();
    if (myString!=null) {
      println(myString);
    }

}

  pushMatrix();
  translate(width/4, 0);
  drawFondV();
  drawV(vg, vd);

  translate(0, height/2);
  drawFondScan();
  drawScan();

  popMatrix();

  drawText();

  stroke(230, 230, 255);
  strokeWeight(10);
  line(width/2+3*width/12, height/2+width/6+height/30, width/2+3*width/12+((vg-vd)/28)*width/6.0, height/2+width/6+height/30);
}

void keyPressed() {

  if (key==CODED) {
    if (keyCode==LEFT) {
      vd+=1.0;
      vg-=1.0;
      commande=true;
    }
    if (keyCode==UP) {
      vd+=1.0;
      vg+=1.0;
      commande=true;
    }
    if (keyCode==DOWN) {
      vd-=1.0;
      vg-=1.0;
      commande=true;
    }
    if (keyCode==RIGHT) {
      vd-=1.0;
      vg+=1.0;  
      commande=true;
    }
    vg=min(14, vg);
    vg=max(-14, vg);
    vd=min(14, vd);
    vd=max(-14, vd);
    println("vg= " + vg + "  vd= "+ vd);
  }
  if (keyCode== ENTER) {
    vg=0.0;
    vd=0.0;
    commande=true;

    println("vg= " + vg + "  vd= "+ vd);
  }
  if (keyCode==TAB) {
    dernierScan=millis();
    isScanning=true;
  }
}

void drawV(float vg, float vd) {

  pushMatrix();
  fill(230, 230, 255);
  length=width/6*(abs(vg+vd)/28.0);
  noStroke();
  translate(width/2, height/2);

  if (vg+vd>0) {
      rotate(atan((vg-vd)/(vg+vd)));
    }
  else if (vg+vd<0){
          rotate(PI+atan((vg-vd)/(vg+vd)));
  }   
  



  triangle(-width/180.0, -length+width/180, width/180, -length+width/180, 0, -length);
  rect(-width/180, 0, width/90, -length+width/180);
  ellipse(0, 0, width/90, width/90);

  translate(width/2, height/2);
  popMatrix();
}

void drawFondV() {
  background(150, 150, 220);

  stroke(255, 100, 100);
  strokeWeight(3);

  fill(0, 155, 0);
  ellipse(width/2, height/2, width/3, width/3);
  stroke(50);

  for ( int i=0; i<5; i++) {
    ellipse(width/2, height/2, width/3-i*width/15, width/3-i*width/15);
  }

  stroke(50);
  line(width/2-width/6, height/2, width/2+width/6, height/2);
  line(width/2, height/2-width/6, width/2, height/2+width/6);
}

void drawFondScan() {
  pushMatrix();
  fill(0, 155, 0);
  stroke(0, 155, 0);
  strokeWeight(1);

  arc(0, 0, width/3, width/3, -PI, 0);
  line(-width/6, 0, width/6, 0);
  popMatrix();
}

void drawScan() {
  pushMatrix();
  stroke(230, 230, 255);
  strokeWeight(3);
  for (int i=0; i<nbDiv; i++) {
    fill(230, 230, 255);
    noStroke();
    arc(0, 0, (width/3)*scan[i]/portee, (width/3)*scan[i]/portee, PI+max(i-0.5, 0)*PI/(nbDiv-1), PI+min(i+0.5, nbDiv-1)*PI/(nbDiv-1));

    fill(0, 155, 0);
    ellipse(cos(PI-i*PI/(nbDiv-1))*(width/6)*scan[i]/portee, -sin(PI-i*PI/(nbDiv-1))*(width/6)*scan[i]/portee, width/180, width/180);

    noFill();
    stroke(0, 155, 0);
    arc(0, 0, (width/3)*scan[i]/portee, (width/3)*scan[i]/portee, PI+max(i-0.5, 0)*PI/(nbDiv-1), PI+min(i+0.5, nbDiv-1)*PI/(nbDiv-1));
  }
  popMatrix();
}

void updateScan() {
  myPort.write("S");

  String fullScan = "";
  while (split(fullScan, 'S').length<nbDiv+2) {

    if (myPort.available()>0) {
      fullScan += myPort.readString();
    }
  }
  println(fullScan);


  String[] result=split(fullScan, 'S');
  for (int i=1; i<result.length-1; i++) {
    if (float(result[i])!=0.0) {
      scan[i-1]=float(result[i]);
    } else {
      scan[i-1]=portee;
    }
  }
}

void drawText() {

  strokeWeight(10);
  stroke(200, 200, 255);
  line(width/2, 0, width/2, height);

  textSize(40);
  fill(200, 200, 255);
  text("Scan", width/5, height/10);

  textSize(40);
  fill(200, 200, 255);
  text("Command", width/2+width/6, height/10); 

  textSize(30);
  fill(200, 200, 255);
  text("Detection range : "+portee+" cm", width/8, height-height/5); 

  textSize(30);
  fill(200, 200, 255);
  text("Last Scan : "+str((millis()-dernierScan)/1000.0)+" s", width/7, height-height/10); 

  textSize(30);
  fill(200, 200, 255);
  text("Speed : "+str((vg+vd)/2)+"", width/2+width/6, height-height/10);
}