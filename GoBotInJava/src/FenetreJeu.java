import java.awt.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class FenetreJeu extends Frame implements ActionListener{
	//////////////////////////////
	/////////ATTRIBUTS////////////
	//////////////////////////////
	private Integer[][][] intersections;///les ensembles referenceront des elements de ce tableau
	private Plateau plateau;
	private Label labelNbPierresBlanchesCapturees,labelNbPierresNoiresCapturees,messageSuperieur;
	private String[][] tableauEtat;////tableau avec "vide" si pas de pierres, "Noir" si noir et "Blanc" si blanc
	private String joueurActuel,joueurNoir,joueurBlanc;
	private int taille,nbPierresBlanchesCapturees,nbPierresNoiresCapturees;
	private Groupe dernierGroupeMort;
	private boolean mort,aPasse,ko;
	private Integer[] posKo;
	private int nbPierrecaptureesCeTour;//permet de definir le ko
								/////DEFINITION ko: prend 1 seule pierre adversaire, n'est relie a personne et a 1 liberte
	private HashSet<Groupe> groupes;
	private Historique historique;
	
	/////////////////////////////
	/////////CONSTRUCTEUR////////
	/////////////////////////////
	public FenetreJeu(int n,String joueurNoir,String joueurBlanc){//n desiigne la taille
		super("DinGO");
		
		/////On definit d'abord tous les Attributs
		mort=false;
		intersections=new Integer[n][n][2];
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				intersections[i][j] =new Integer[]{new Integer(i),new Integer(j)};
			}
		}
		groupes=new HashSet<Groupe>();
		ko=false;
		posKo=intersections[0][0];
		taille=n;
		tableauEtat=new String[n][n];
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				tableauEtat[i][j]="vide";//le tableau est rempli par les String vide , Noir , Blanc
			}
		}
		this.joueurNoir=joueurNoir;
		this.joueurBlanc=joueurBlanc;
		joueurActuel=this.joueurNoir;
		nbPierresNoiresCapturees=0;
		nbPierresBlanchesCapturees=0;
		labelNbPierresBlanchesCapturees=new Label("Nombre de pierres capturées: "+nbPierresBlanchesCapturees);
		labelNbPierresNoiresCapturees=new Label("Nombre de pierres capturées: "+nbPierresNoiresCapturees);
		setPlateau(new Plateau(this,n));
		aPasse=false;
		
		////on cree ici la fenetre
		
		setLayout(new BorderLayout());
		
		add(getPlateau(),BorderLayout.CENTER);
		add(new Label("DinGO",Label.CENTER),BorderLayout.SOUTH);
		Panel north=new Panel(new GridLayout(4,1));
		Panel boutons=new Panel(new FlowLayout());
		boutons.setForeground(Color.BLACK);
		Button quitter=new Button("Quitter");
		
		Button menu=new Button("Retourner au menu");
		Button enregistrer=new Button("Enregistrer");
		Button influence=new Button("Activer affichage influence");
		Button territoire=new Button("Activer affichage territoire");
		Button repaint=new Button("Desactiver affichages supplementaires");
		
		boutons.add(quitter);
		boutons.add(menu);
		boutons.add(enregistrer);
		boutons.add(influence);
		boutons.add(territoire);
		boutons.add(repaint);
		quitter.addActionListener(this);
		menu.addActionListener(this);
		enregistrer.addActionListener(this);
		influence.addActionListener(this);
		territoire.addActionListener(this);
		repaint.addActionListener(this);
		boutons.setBackground(new Color(90,10,10));
		north.add(boutons);
		
		north.add(new Label(joueurNoir+" Versus "+joueurBlanc,Label.CENTER));
		north.add(new Label("  May the Force be with you",Label.CENTER));
		messageSuperieur=new Label("Au joueur:"+joueurActuel,Label.CENTER);
		messageSuperieur.setFont(new Font(Font.DIALOG,Font.ITALIC|Font.BOLD|Font.PLAIN,16));
		north.add(messageSuperieur,Label.CENTER);
		
		
		Panel gauche=new Panel(new GridLayout(3,1));
		gauche.add(new Label("Joueur Noir : "+joueurNoir));
		gauche.add(labelNbPierresBlanchesCapturees);
		Button gauchePasse = new Button("Passer son tour");
		gauchePasse.setForeground(Color.BLACK);
		gauchePasse.addActionListener(this);
		gauche.add(gauchePasse);
		
		Panel droite=new Panel(new GridLayout(3,1));
		droite.add(new Label("Joueur Blanc : "+joueurBlanc));
		droite.add(labelNbPierresNoiresCapturees);
		Button coupPrecedent = new Button("Revenir au coup precedent"); 
		coupPrecedent.setForeground(Color.BLACK);
		
		coupPrecedent.addActionListener(this);
		droite.add(coupPrecedent);
		add(north,BorderLayout.NORTH);
		add(gauche,BorderLayout.WEST);
		add(droite,BorderLayout.EAST);
		setBackground(new Color(205,0,0));
		setForeground(Color.WHITE);
		setFont(new Font("Tout",Font.BOLD|Font.ROMAN_BASELINE,13));
		addWindowListener(new EcouteurPourFermetureFenetre());//pour fermer le programme quand la fenetre se ferme
		getPlateau().repaint();
		
		
		
		///on cree un historique 

		setHistorique(new Historique(this));
		
		///on affiche la merveille de technologie
		
		setLocation(250,0);
		setSize(1100,850);
		
		setVisible(true);


	}
	
	public FenetreJeu(){///Par defaut on jouera sur du 9x9
		this(9,"Missa","Tissa");
	}
	
	
	
	/////////////////////////////
	////ACCESSEURS///////////////
	/////////////////////////////

	/**
	 * Ajoute n au nombre de pierres blanches capturees
	 */
	public void ajouterNbPierresBlanchesCapturees(int n) {
		nbPierresBlanchesCapturees+=n;
	}

	/**
	 * Ajoute n au nombre de pierres noires capturees.
	 */
	public void ajouterNbPierresNoiresCapturees(int n) {
		nbPierresNoiresCapturees+=n;
	}

	public Historique getHistorique() {
		return historique;
	}

	public void setHistorique(Historique historique) {
		this.historique = historique;
	}

	public Plateau getPlateau() {
		return plateau;
	}

	public void setPlateau(Plateau plateau) {
		this.plateau = plateau;
	}
	
	/**
	 * @return the mort
	 */
	public boolean isMort() {
		return mort;
	}

	/**
	 * @return the groupes
	 */
	public HashSet<Groupe> getGroupes() {
		return groupes;
	}

	/**
	 * @param groupes the groupes to set
	 */
	public void setGroupes(HashSet<Groupe> groupes) {
		this.groupes = groupes;
	}

	/**
	 * @param mort the mort to set
	 */
	public void setMort(boolean mort) {
		this.mort = mort;
	}

	public String[][] getTableauEtat(){
		return(tableauEtat);
	}
	
	/**
	 * @return the intersections
	 */
	public Integer[][][] getIntersections() {
		return intersections;
	}

	/**
	 * @return the dernierGroupeMort
	 */
	public Groupe getDernierGroupeMort() {
		return dernierGroupeMort;
	}

	/**
	 * @param dernierGroupeMort the dernierGroupeMort to set
	 */
	public void setDernierGroupeMort(Groupe dernierGroupeMort) {
		this.dernierGroupeMort = dernierGroupeMort;
	}

	public int getTaille(){
		return(taille);
	}
	
	/**
	 * @return the nbPierrecaptureesCeTour
	 */
	public int getNbPierrecaptureesCeTour() {
		return nbPierrecaptureesCeTour;
	}

	/**
	 * @param nbPierrecaptureesCeTour the nbPierrecaptureesCeTour to set
	 */
	public void setNbPierrecaptureesCeTour(int nbPierrecaptureesCeTour) {
		this.nbPierrecaptureesCeTour = nbPierrecaptureesCeTour;
	}

	public String getJoueurActuel(){
		return joueurActuel;
	}
	
	/**
	 * @param joueurActuel the joueurActuel to set
	 */
	public void setJoueurActuel(String joueurActuel) {
		this.joueurActuel = joueurActuel;
	}

	public String getProchainJoueur(){
		if(joueurActuel==joueurNoir){
			return(joueurBlanc);
			
		}
		else{return(joueurNoir);}
	}
	

	public String getJoueurNoir(){
		return joueurNoir;
	}
	public String getJoueurBlanc(){
		return joueurBlanc;
	}
	
	
	
	/**
	 * @return the aPasse
	 */
	public boolean isaPasse() {
		return aPasse;
	}

	/**
	 * @param aPasse the aPasse to set
	 */
	public void setaPasse(boolean aPasse) {
		this.aPasse = aPasse;
	}

	/**
	 * @return the ko
	 */
	public boolean isKo() {
		return ko;
	}

	/**
	 * @param ko the ko to set
	 */
	public void setKo(boolean ko) {
		this.ko = ko;
	}

	/**
	 * @return the posKo
	 */
	public Integer[] getPosKo() {
		return posKo;
	}

	/**
	 * @param posKo the posKo to set
	 */
	public void setPosKo(Integer[] posKo) {
		this.posKo = posKo;
	}

	/**
	 * @return the nbPierresBlanchesCapturees
	 */
	public int getNbPierresBlanchesCapturees() {
		return nbPierresBlanchesCapturees;
	}

	/**
	 * @param nbPierresBlanchesCapturees the nbPierresBlanchesCapturees to set
	 */
	public void setNbPierresBlanchesCapturees(int nbPierresBlanchesCapturees) {
		this.nbPierresBlanchesCapturees = nbPierresBlanchesCapturees;
	}

	/**
	 * @return the nbPierresNoiresCapturees
	 */
	public int getNbPierresNoiresCapturees() {
		return nbPierresNoiresCapturees;
	}

	/**
	 * @param nbPierresNoiresCapturees the nbPierresNoiresCapturees to set
	 */
	public void setNbPierresNoiresCapturees(int nbPierresNoiresCapturees) {
		this.nbPierresNoiresCapturees = nbPierresNoiresCapturees;
	}

	public String couleurJoueurActuel(){
		if(joueurActuel==joueurNoir){
			return("Noir");
		}
		else return("Blanc");
	}
	
	public String couleurJoueurNonActuel(){

		if(joueurActuel==joueurNoir){
			return("Blanc");
		}
		else return("Noir");
	}

	//////////////////////////////////
	//////FONCTION DIVERS/////////////
	//////////////////////////////////
	
	
	
	
	
	/**Retourne true si le coup suit les règles du go et false sinon.
	 * Un coup est autorise si:
	 * -Au moins une case aux alentours est vide;
	 * OU
	 * -(Au moins un des voisins est non hostile) ET (la pierre posee n'entraine pas un suicide);
	 * OU
	 * -(La pierre est posee sur la derniere liberte d'une pierre adversaire) ET (il ne s'agit pas d'un KO) 
	 * 
	 * 
	 * */
	public boolean estAutorise(int i,int j){
		if(tableauEtat[i][j]!="vide"){
			return(false);
		}
		
		boolean derniereLibertePierreHostile;
		
		derniereLibertePierreHostile=false;
		for(Integer[] L:this.intersections(i, j)){
			int a=L[0].intValue();
			int	b=L[1].intValue();
			if(tableauEtat[a][b].equals("vide")){
				return(true);}
		}
		
			///On regarde seulement maintenant les autres groupes pour verifiier que ce
			////n'est pas un kamikaze ou si une pierre adverse disparait bien, car le cas où il y a une case vide est tres frequent.
		
		for(Integer[] L:this.intersections(i, j)){
			int a=L[0].intValue();
			int	b=L[1].intValue();
			if(tableauEtat[a][b].equals(couleurJoueurActuel())){//si allie on verifie que ce n'est pas un kamikaze
			if(getGroupe(a,b).getLibertes().size()!=1){
					return(true);//car pas kamikaze
				}
			}
			else{
				if(getGroupe(a,b).getLibertes().size()==1){//ie si derniere liberte groupe ennemi
					if(ko==false||posKo[0]!=a||posKo[1]!=b){//on verifie que le coupe est conforme a la regle du Ko
					return(true);}
				}
			}
		}
		
		
		return(false);
	}
	
	/**Cette fonction permet d'actualiser les labels dans l'interface de jeu*/
	public void actualiserLabel(){
		messageSuperieur.setText("Au joueur:"+joueurActuel);
		labelNbPierresBlanchesCapturees.setText("Nombre de pierres capturées: "+nbPierresBlanchesCapturees);
		labelNbPierresNoiresCapturees.setText("Nombre de pierres capturées: "+nbPierresNoiresCapturees);
	}
	
	/**Cette fontion permet d'actualiser les libertes des groupes vis a vis du dernier coup joue*/
	public void actualiserLibertesGroupes(int i,int j){
		Iterator<Groupe> iterator=groupes.iterator();
		while(iterator.hasNext()){
		iterator.next().enleverLiberte(i, j);
		if(mort==true){
			iterator.remove();
			this.actualiserLibertesGroupesApresMort(dernierGroupeMort);
			dernierGroupeMort.kill();
			mort=false;
			}
		}
	}
	
	/**Cette fontion permet d'actualiser les libertes des groupes vis a vis du groupe qui vient de disparaitre*/
	public void actualiserLibertesGroupesApresMort(Groupe mort){
		Iterator<Groupe> iterator=groupes.iterator();
		while(iterator.hasNext()){
		iterator.next().ajouterLibertes(mort);
		}
	}
	/**Retourne le groupe lie a l'element en (i,j)*/
	public Groupe getGroupe(int i,int j){
		
		for(Groupe g:groupes){
			if(g.getElements().contains(intersections[i][j])){
				return(g);
			}
			}
		
		System.err.println("recherche un groupe inexistant");
		return(new Groupe(0,0,this));///n'arrive normalement jamais
	}
	
	
	
	
	/**
	 * @param tableauEtat the tableauEtat to set
	 */
	public void setTableauEtat(String[][] tableauEtat) {
		this.tableauEtat = tableauEtat;
	}

	/**Retourne l'enszmble des groupes voisins de la couleur col*/
	
	public HashSet<Groupe> getGroupesVoisins(int i,int j, String col){
		HashSet<Groupe> groupesVoisins=new HashSet<Groupe>();
		for(Integer[] L:this.intersections(i,j)){
			int a,b;
			a=L[0].intValue();
			b=L[1].intValue();
			if(tableauEtat[a][b].equals(col)){
				groupesVoisins.add(getGroupe(a,b));
			}
		}
		
		return(groupesVoisins);
	}
	
	/**Fontion de jeu: actualise le jeu apres un coup en (i,j) apres avoir verifie sa legalite */
	public void poserPierre(int i, int j){	
		////////on remet la condition tableau vide car 
			if(tableauEtat[i][j]=="vide"&&this.estAutorise(i,j)){
				///////////////////////
				nbPierrecaptureesCeTour=0;
				aPasse=false;
				ko=false;
				Groupe groupe=new Groupe(i,j,this);
				this.actualiserLibertesGroupes(i, j);				
				//System.err.println(groupe);
				groupe.plusIndestructible();
				joueurActuel=this.getProchainJoueur();
				this.actualiserLabel();
				if(nbPierrecaptureesCeTour==1&&groupe.getLibertes().size()==1&&groupe.getElements().size()==1){//ie si la seule liberte du groupe a un element cree est celle de la pierre captureee
					ko=true;
					posKo=intersections[i][j];
				}
				this.getHistorique().empiler(this);
				//System.err.println(historique.getDernierCoup().getNumeroCoup());
				this.getHistorique().afficher();
			}
	}
	
	/**Retourne l'ensemble des intersections voisines*/	
	public HashSet<Integer[]> intersections(int i,int j){
		HashSet<Integer[]> result=new HashSet<Integer[]>();
		if(i!=0){
			result.add(intersections[i-1][j]);
			
		}
		if(i!=taille-1){
			result.add(intersections[i+1][j]);
		}
		if(j!=0){
			result.add(intersections[i][j-1]);
		}
		if(j!=taille-1){
			result.add(intersections[i][j+1]);
		}
		return(result);
	}

	/**Retourne l'ensemble des libertes d'une case i,j du tableau*/
	public HashSet<Integer[]> libertes(int i,int j){
		HashSet<Integer[]> libertes=new HashSet<Integer[]>();
		if(i!=0){
			if(tableauEtat[i-1][j].equals("vide")){
			libertes.add(intersections[i-1][j]);}
		}
		if(i!=taille-1){
			if(tableauEtat[i+1][j].equals("vide")){
			libertes.add(intersections[i+1][j]);}
		}
		if(j!=0){
			if(tableauEtat[i][j-1].equals("vide")){
			libertes.add(intersections[i][j-1]);}
		}
		if(j!=taille-1){
			if(tableauEtat[i][j+1].equals("vide")){
				libertes.add(intersections[i][j+1]);}
		}
		return(libertes);
	}

	
	//////////////////////////////////
	/////////REACTIONS AUX EVENEMENTS/
	//////////////////////////////////
	
	public void actionPerformed(ActionEvent e) {
		///////////reaction au fait de passer son tour
		if (e.getActionCommand().equals("Passer son tour")){
			if(aPasse==false){
			joueurActuel=this.getProchainJoueur();
			aPasse=true;
			this.actualiserLabel();
			
			this.getHistorique().empiler(this);
			//this.getHistorique().afficher();
			}
			else{
				///////////////////////
				/////////////////SEQUENCE FIN DE JEU A IMPLEMENTER
				/////////////////SEQUENCE FIN DE JEU A IMPLEMENTER
				///////////////////////////////
			}
		}
		
		///reaction a la volonte de revenir au coup precedent
		else if(e.getActionCommand().equals("Revenir au coup precedent")&&getHistorique().getDernierCoup().getNumeroCoup()!=0){
			getHistorique().reprendrePartieAuCoupPrecedent(this);
			this.actualiserLabel();
			this.getPlateau().repaint();
			System.err.println(getHistorique().getDernierCoup().getNumeroCoup());
			//getHistorique().afficher();
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
			this.plateau.setAfficherInfluence(true);
			this.plateau.repaint();
		}
		else if(e.getActionCommand().equals("Activer affichage territoire")){
			this.plateau.setAfficherTerritoire(true);
			this.plateau.repaint();
		}
		
		else if(e.getActionCommand().equals("Desactiver affichages supplementaires")){
			this.plateau.repaint();}
		
	}
	
	/////////////////////////////////
	/////////////MAIN////////////////
	/////////////////////////////////
	
	public static void main(String[] args) {
		new FenetreJeu(9,"missa","tissa");
	}

}
