import java.awt.event.ActionEvent;

public class FenetreJeuUnOrdi extends FenetreJeu {
		
	///////////////////////////
	////////////ATTRIBUTS/////
	/////////////////////////
	String joueurOrdi;
	
	
	
///////////////////////////
///////CONSTRUCTEUR///////
/////////////////////////
	/**
	 * 
	 **/
	public FenetreJeuUnOrdi(String placeOrdi,int taille,String JoueurNoir,String joueurBlanc){
		super(taille, JoueurNoir,joueurBlanc);
		if(placeOrdi.equals("Noir")){
			joueurOrdi=this.getJoueurNoir();
			AlgoOrdinateur.ordiJoue(this);
		}
		else{
			joueurOrdi=this.getJoueurBlanc();
		}
		
	///on affiche la merveille
		/*
		setLocation(450,100);
		setSize(1100,800);
		
		setVisible(true);*/
		
	}
	
	

public void poserPierre(int i,int j){
	if(getTableauEtat()[i][j]=="vide"&&this.estAutorise(i,j)){
		///////////////////////
		setNbPierrecaptureesCeTour(0);
		setaPasse(false);
		setKo(false);
		Groupe groupe=new Groupe(i,j,this);
		this.actualiserLibertesGroupes(i, j);
		//System.err.println(groupe);
		groupe.plusIndestructible();
		setJoueurActuel(this.getProchainJoueur());
		this.actualiserLabel();
		if(getNbPierrecaptureesCeTour()==1&&groupe.getLibertes().size()==1&&groupe.getElements().size()==1){//ie si la seule liberte du groupe a un element cree est celle de la pierre captureee
			setKo(true);
			setPosKo(getIntersections()[i][j]);
		}
		this.getHistorique().empiler(this);
		//System.err.println(historique.getDernierCoup().getNumeroCoup());
		//this.getHistorique().afficher();
		if(getJoueurActuel().equals(joueurOrdi)){
			AlgoOrdinateur.ordiJoue(this);
		}
	}
	
}

public void actionPerformed(ActionEvent e) {
	///////////reaction au fait de passer son tour
	if (e.getActionCommand().equals("Passer son tour")){
		if(isaPasse()==false){
		setJoueurActuel(this.getProchainJoueur());
		setaPasse(true);
		this.actualiserLabel();
		this.getHistorique().empiler(this);
		//this.getHistorique().afficher();
		AlgoOrdinateur.ordiJoue(this);
		this.actualiserLabel();
		this.getPlateau().repaint();
		}
		else{
			///////////////////////
			/////////////////SEQUENCE FIN DE JEU A IMPLEMENTER
			/////////////////SEQUENCE FIN DE JEU A IMPLEMENTER
			///////////////////////////////
		}
	}

else if(e.getActionCommand().equals("Quitter")){
	this.dispose();
	 System.exit(0);
}

else if(e.getActionCommand().equals("Retourner au menu")){
	this.dispose();
	new Menu();
}
else if(e.getActionCommand().equals("Activer affichage influence")){
	getPlateau().setAfficherInfluence(true);
	getPlateau().repaint();
}
else if(e.getActionCommand().equals("Activer affichage territoire")){
	getPlateau().setAfficherTerritoire(true);
	getPlateau().repaint();
}

else if(e.getActionCommand().equals("Desactiver affichages supplementaires")){
	getPlateau().repaint();}
}
}

	
	
	
	
	
	

