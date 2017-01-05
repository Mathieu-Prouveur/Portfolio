import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CanvasMenu extends Canvas{

/////ATTRIBUTS/////////
private BufferedImage img;
double fact;
/////CONSTRUCTEUR////////////
	public CanvasMenu(String path,int hVoulu){
		super();
		
		img = null;
		
		try {
		
		img = ImageIO.read(new File(path));
		} 
		catch (IOException e) {System.err.println(e.getMessage());}
		fact=(double)(hVoulu)/(double)(img.getHeight());
		//System.err.println(fact);
		//System.err.println(img);
		setSize((int)(fact*img.getWidth()),hVoulu);
		
		//setLocation(getX(),getY());
		this.repaint();
		
		
	}
	public void paint(Graphics g)  {
		////ON MET DE JOLIES IMAGES
		
		g.drawImage(img ,0,0,(int)(fact*img.getWidth()),(int)(img.getHeight()*fact),null);
			
		}
}
