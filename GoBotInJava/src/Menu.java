import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Menu extends Frame implements ActionListener {
	/////////////Attributs
	private int taille=9;
	private String nomJoueurBlanc="Tissa";
	private String nomJoueurNoir="Missa";
	private String nomJoueurHumain="Humain";
	private double komi=6.5;
	private int algoOrdi=0;//chaque int correspondra à un algo bien precis
	private int nbPierresHandicaps=0;
	private String placeOrdi="Noir";//pour savoir si l'ord joue les noirs ou les blancs;
	private CanvasMenu c,d;
	/////////////Constructeur
	public Menu(){
		super("Projet MiniGO");
		setLayout(new BorderLayout(5,5));
		c=new CanvasMenu("src/samurai-09.jpg",800);
		d=new CanvasMenu("src/samurai-09-miroir.jpg",800);
		setBackground(new Color(205,0,0));
		setFont(new Font(Font.SERIF,Font.BOLD,16));		
		add(c,BorderLayout.EAST);
		add(d,BorderLayout.WEST);
		Color col=new Color(245,245,220);
		Label titre=new Label("Projet DinGo",Label.CENTER);
		titre.setFont(new Font(Font.SERIF,Font.BOLD,24));
		add(titre,BorderLayout.NORTH);
		titre.setForeground(Color.white);
		Panel panelCentral=new Panel(new GridLayout(5,1,1,3));
		
		Button unJoueur = new Button("JOUEUR contre ORDINATEUR");
		unJoueur.setBackground(col);
		Button deuxJoueurs= new Button("JOUEUR contre JOUEUR");
		deuxJoueurs.setBackground(col);
		Button option= new Button("OPTIONS");
		option.setBackground(col);
		Button aide= new Button("AIDE GO");
		aide.setBackground(col);
		
		Button quitter= new Button("QUITTER");
		option.setFont(new Font(Font.SERIF,Font.ITALIC|Font.BOLD,14));
		option.setBackground(col);
		aide.setFont(new Font(Font.SERIF,Font.ITALIC|Font.BOLD,14));
		quitter.setFont(new Font(Font.SERIF,Font.ITALIC|Font.BOLD,14));
		quitter.setBackground(col);

		unJoueur.addActionListener(this);
		deuxJoueurs.addActionListener(this);
		option.addActionListener(this);
		aide.addActionListener(this);
		quitter.addActionListener(this);
		panelCentral.add(unJoueur);
		panelCentral.add(deuxJoueurs);
		panelCentral.add(option);
		panelCentral.add(aide);
		panelCentral.add(quitter);

		add(panelCentral,BorderLayout.CENTER);
	
		addWindowListener(new EcouteurPourFermetureFenetre()); 

		////affichage merveille
		setLocation(0, 0);
		setSize(1700,1000);
		setVisible(true);

	}
	

	
	/////////////Reactions aux evenements
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("JOUEUR contre JOUEUR")){
			new FenetreJeu(taille,nomJoueurNoir,nomJoueurBlanc);
			
			this.dispose();//on ferme la fenetre mais on n'arrete surtout pas le programme
		}
		
		else if(e.getActionCommand().equals("JOUEUR contre ORDINATEUR")){
			new FenetreJeuUnOrdi(placeOrdi,taille,"ordi",nomJoueurHumain);

			this.dispose();//on ferme la fenetre mais on n'arrete surtout pas le programme
		}
		else if(e.getActionCommand().equals("OPTIONS")){
			setVisible(false);
			new Options(this);
		}
		else if(e.getActionCommand().equals("QUITTER")){
			this.dispose() ;
		    System.exit(0);
		}
		
		
		}
	
////////////////GETTERS AND SETTERS
	/**
	 * @return the taille
	 */
	public int getTaille() {
		return taille;
	}



	/**
	 * @param taille the taille to set
	 */
	public void setTaille(int taille) {
		this.taille = taille;
	}



	/**
	 * @return the nomJoueurBlanc
	 */
	public String getNomJoueurBlanc() {
		return nomJoueurBlanc;
	}



	/**
	 * @param nomJoueurBlanc the nomJoueurBlanc to set
	 */
	public void setNomJoueurBlanc(String nomJoueurBlanc) {
		this.nomJoueurBlanc = nomJoueurBlanc;
	}



	/**
	 * @return the nomJoueurNoir
	 */
	public String getNomJoueurNoir() {
		return nomJoueurNoir;
	}



	/**
	 * @param nomJoueurNoir the nomJoueurNoir to set
	 */
	public void setNomJoueurNoir(String nomJoueurNoir) {
		this.nomJoueurNoir = nomJoueurNoir;
	}



	/**
	 * @return the komi
	 */
	public double getKomi() {
		return komi;
	}



	/**
	 * @param komi the komi to set
	 */
	public void setKomi(double komi) {
		this.komi = komi;
	}



	/**
	 * @return the algoOrdi
	 */
	public int getAlgoOrdi() {
		return algoOrdi;
	}



	/**
	 * @param algoOrdi the algoOrdi to set
	 */
	public void setAlgoOrdi(int algoOrdi) {
		this.algoOrdi = algoOrdi;
	}



	/**
	 * @return the nbPierresHandicaps
	 */
	public int getNbPierresHandicaps() {
		return nbPierresHandicaps;
	}



	/**
	 * @param nbPierresHandicaps the nbPierresHandicaps to set
	 */
	public void setNbPierresHandicaps(int nbPierresHandicaps) {
		this.nbPierresHandicaps = nbPierresHandicaps;
	}



	/**
	 * @return the placeOrdi
	 */
	public String getPlaceOrdi() {
		return placeOrdi;
	}



	/**
	 * @param placeOrdi the placeOrdi to set
	 */
	public void setPlaceOrdi(String placeOrdi) {
		this.placeOrdi = placeOrdi;
	}
	
	/**Permet de rendre le menu invisible*/
	
	public void setVisibleExt(boolean bool){
		setVisible(bool);
	}


	/////////////Main
	public static void main(String[] args) {
		new Menu();
	}
	
	
	
	
	
}

