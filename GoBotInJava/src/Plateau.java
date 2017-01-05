import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.text.Position;

public class Plateau extends Canvas implements MouseListener{
	//////////Attributs
	private FenetreJeu fenetreJeu;
	private int taille;
	private int tailleCarre;
	private boolean afficherInfluence=false;
	private boolean afficherTerritoire=false;
	/////////Constructeurs
	public Plateau(FenetreJeu fenJeu,int n){
		super();
		fenetreJeu=fenJeu;
		setBackground(new Color(90,10,10));
		setSize(tailleCarre+tailleCarre*taille,tailleCarre+tailleCarre*taille);
		addMouseListener(this);
		
		taille=n;
		tailleCarre=(int)(600/n);
		
	}
	
	public Plateau(FenetreJeu fenJeu){
		this(fenJeu,9);
	}
	////Setter
	public void setAfficherInfluence(boolean bool){
		afficherInfluence=bool;
	}
	
	public boolean isAfficherTerritoire() {
		return afficherTerritoire;
	}

	public void setAfficherTerritoire(boolean afficherTerritoire) {
		this.afficherTerritoire = afficherTerritoire;
	}

	/////////Dessin
	public void paint(Graphics g)  {
		
		////////PARTIE 1/ CREATION DE L'ARRIERE PLAN
		g.setColor(new Color(255,127,36));
		g.fillRect((int)(tailleCarre/2),(int)(tailleCarre/2), tailleCarre*taille,tailleCarre*taille);///carre de tailleCarre pixels par tailleCarre pixels+ bord de tailleCarre pixels
		
		g.setColor(Color.BLACK);//trace de la grille
		for(int i=0;i<taille-1;i++){
			for(int j=0;j<taille-1;j++){
				g.drawRect(tailleCarre+i*tailleCarre, tailleCarre+j*tailleCarre, tailleCarre,tailleCarre);
			}
		}
		
		/////// AJOUT DES POINTS SUR LES PLATEAUX TRADITIONNELS 9x9 13x13 19x19 
		if(taille==9){       ///////ajout des points dans le cas des tailles traditionnelles
			g.fillOval(tailleCarre*3-5, tailleCarre*3-5 , 10,10);
			g.fillOval(tailleCarre*3-5, tailleCarre*7-5 , 10,10);
			g.fillOval(tailleCarre*7-5, tailleCarre*3-5 , 10,10);
			g.fillOval(tailleCarre*7-5, tailleCarre*7-5 , 10,10);
		}
		
		if(taille==13){
			g.fillOval(tailleCarre*4-5, tailleCarre*4-5 , 10,10);
			g.fillOval(tailleCarre*4-5, tailleCarre*10-5 , 10,10);
			g.fillOval(tailleCarre*10-5, tailleCarre*4-5 , 10,10);
			g.fillOval(tailleCarre*10-5, tailleCarre*10-5 , 10,10);
			g.fillOval(tailleCarre*7-5, tailleCarre*7-5 , 10,10);
		}
		
		if(taille==19){
			g.fillOval(tailleCarre*5-5, tailleCarre*5-5 , 10,10);
			g.fillOval(tailleCarre*5-5, tailleCarre*15-5 , 10,10);
			g.fillOval(tailleCarre*15-5, tailleCarre*5-5 , 10,10);
			g.fillOval(tailleCarre*15-5, tailleCarre*15-5 , 10,10);
			g.fillOval(tailleCarre*10-5, tailleCarre*10-5 , 10,10);
		}
			
	  ///////////////PARTIE 2/ PRISE EN COMPTE DE L'ETAT DE LA PARTIE
		for(int i=0;i<taille;i++){
			for(int j=0;j<taille;j++){
				if (fenetreJeu.getTableauEtat()[i][j]=="Blanc"){
					g.setColor(Color.white);
					g.fillOval(tailleCarre*j+(int)(tailleCarre/2), tailleCarre*i+(int)(tailleCarre/2),tailleCarre,tailleCarre);
					
				}
				if(fenetreJeu.getTableauEtat()[i][j]=="Noir"){
					g.setColor(Color.black);
					g.fillOval(tailleCarre*j+(int)(tailleCarre/2), tailleCarre*i+(int)(tailleCarre/2),tailleCarre,tailleCarre);
				}		
			}
		}
		
		////PARTIE 3/AFFICHAGE, SI DEMANDE, DE L'INFLUENCE
		if(afficherInfluence){
			int[][] tableauInfluence=AlgoOrdinateur.evaluationInfluence(fenetreJeu);
			for(int i=0;i<taille;i++){
				for(int j=0;j<taille;j++){
					if (tableauInfluence[i][j]<0){
						g.setColor(Color.white);
						//g.drawChars("b".toCharArray(),tailleCarre*j+(int)(tailleCarre/2), tailleCarre*i+(int)(tailleCarre/2),tailleCarre,tailleCarre);
						g.fillRect(tailleCarre*(j+1)-(int)(tailleCarre/6), tailleCarre*(i+1)-(int)(tailleCarre/6),(int)(tailleCarre/3),(int)(tailleCarre/3));
						
					}
					if(tableauInfluence[i][j]>0){
						g.setColor(Color.black);
						//g.drawChars("n".toCharArray(),tailleCarre*j+(int)(tailleCarre/2), tailleCarre*i+(int)(tailleCarre/2),tailleCarre,tailleCarre);
						g.fillRect(tailleCarre*(j+1)-(int)(tailleCarre/6), tailleCarre*(i+1)-(int)(tailleCarre/6),(int)(tailleCarre/3),(int)(tailleCarre/3));

					}	
				}
				
			}
			afficherInfluence=false;
		}
			//PARTIE 4 AFFICHAGE,SI DEMANDE,DU TERRITOIRE
			if(afficherTerritoire){
				int[][] tableauTerritoire=AlgoOrdinateur.evaluationTerritoire(fenetreJeu);
				for(int i=0;i<taille;i++){
					for(int j=0;j<taille;j++){
						if (tableauTerritoire[i][j]<0){
							g.setColor(Color.white);
							//g.drawChars("b".toCharArray(),tailleCarre*j+(int)(tailleCarre/2), tailleCarre*i+(int)(tailleCarre/2),tailleCarre,tailleCarre);
							g.fillRect(tailleCarre*(j+1)-(int)(tailleCarre/6), tailleCarre*(i+1)-(int)(tailleCarre/6),(int)(tailleCarre/3),(int)(tailleCarre/3));
							
						}
						if(tableauTerritoire[i][j]>0){
							g.setColor(Color.black);
							//g.drawChars("n".toCharArray(),tailleCarre*j+(int)(tailleCarre/2), tailleCarre*i+(int)(tailleCarre/2),tailleCarre,tailleCarre);
							g.fillRect(tailleCarre*(j+1)-(int)(tailleCarre/6), tailleCarre*(i+1)-(int)(tailleCarre/6),(int)(tailleCarre/3),(int)(tailleCarre/3));

						}	
					}
					
				}
				
			afficherTerritoire=false;}
			
			
					
		
						
	}
		

		
////////////////////Reactions aux evenements
	public void mouseClicked(MouseEvent evt) {
		for(int i=1;i<taille+1;i++){
			for(int j=1;j<taille+1;j++){
		if(evt.getX()<=(int)(tailleCarre/2)-1+tailleCarre*j&&(int)(tailleCarre/2)+1+tailleCarre*(j-1)<=evt.getX()&&evt.getY()<=(int)(tailleCarre/2)-1+tailleCarre*i&&(int)(tailleCarre/2)+1+tailleCarre*(i-1)<=evt.getY()){ ////on prend 24 et 26 pour que les zones soient bien disjointes
		//	System.out.println("reçu I="+i+" J="+j);
			fenetreJeu.poserPierre(i-1, j-1);
		//	System.out.println(fenetreJeu.getTableauEtat()[i-1][j-1]);
		}
		}
			}
		//System.out.println("X="+evt.getX()+"Y="+evt.getY());
		repaint();
		/*
		Iterator<Groupe> iterator=this.fenetreJeu.groupes.iterator();
		while(iterator.hasNext()){
			System.out.println(iterator.next().toString());
		}
		*/
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
