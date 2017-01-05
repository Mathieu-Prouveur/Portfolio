
import java.util.HashSet;

public class Historique {
	
	/////////////////////////////
	///////ATTRIBUTS////////////
	///////////////////////////
	
	private MailleCoup dernierCoup;
	final int taille;///on garde la taille pour quand on fermera la fenetre pour garder l'info
	final String joueurNoir,joueurBlanc;

	////////////////////////////
	///////////CONSTRUCTEUR////
	//////////////////////////
	
	
	public Historique(FenetreJeu fenJeu){
		dernierCoup=new MailleCoup(fenJeu);
		taille=fenJeu.getTaille();
		joueurNoir=fenJeu.getJoueurNoir();
		joueurBlanc=fenJeu.getJoueurBlanc();
	}
	
	//////////////////////////
	///GETTERS///////////////
	////////////////////////

	/**
	 * @return the dernierCoup
	 */
	public MailleCoup getDernierCoup() {
		return dernierCoup;
	}

	/**
	 * @param dernierCoup the dernierCoup to set
	 */
	public void setDernierCoup(MailleCoup dernierCoup) {
		this.dernierCoup = dernierCoup;
	}
	
	////////////////////////////
	//FONCTIONS DES PILES//////
	//////////////////////////
	
	public void empiler(FenetreJeu fenJeuActuelle){
		MailleCoup b=new MailleCoup(this.getDernierCoup(),fenJeuActuelle);
		this.setDernierCoup(b);
	}

	public void depiler(){
		if(dernierCoup.getNumeroCoup()!=0){
		MailleCoup c=dernierCoup.getCoupPrecedent();
		this.setDernierCoup(c);
		System.err.println(c);
		}
	}

	///////////////////////////////
	//FONCTIONS DE RECONSTITUTION/
	/////////////////////////////
	
	/**Permet de revenir au coup precedent en reprenant ou recreant les variables presentes au coup precedent*/
	public void reprendrePartieAuCoupPrecedent(FenetreJeu fenJeu){
		if(dernierCoup.getNumeroCoup()!=0){
			this.depiler();
			fenJeu.setaPasse(dernierCoup.isaPasse());
			fenJeu.setKo(dernierCoup.isKo());
			int a,b;
			a=dernierCoup.getPosKo()[0];
			b=dernierCoup.getPosKo()[1];
			fenJeu.setPosKo(fenJeu.getIntersections()[a][b]);
			fenJeu.setNbPierresBlanchesCapturees(dernierCoup.getNbPierresBlanchesCapturees());
			fenJeu.setNbPierresNoiresCapturees(dernierCoup.getNbPierresNoiresCapturees());
			fenJeu.setTableauEtat(dernierCoup.getTableauEtat());//
			if(dernierCoup.getNumeroCoup()%2==0){
				fenJeu.setJoueurActuel(fenJeu.getJoueurNoir());
			}
			else{
				fenJeu.setJoueurActuel(fenJeu.getJoueurBlanc());
			}
			HashSet<Groupe> groupes=new HashSet<Groupe>();//reinitialisation des groupes
			fenJeu.setGroupes(groupes);
			this.recreerGroupes(dernierCoup.getTableauEtat(),fenJeu);
		}
	}
		
	/**Recree les groupes et les ajoute aux groupes de l'ensemble dedie dans FenJeu*/
	public void recreerGroupes(String[][] tableauEtat,FenetreJeu fenJeu){///les groupes sont ajouter au HashSet groupes lors de la construction
		TableauAvancement tableauAvancement=new TableauAvancement(taille);//on mettra false si l'intersection contient une pierre non referencee dans un groupe
		
		for(int i=0;i<taille;i++){
			for(int j=0;j<taille;j++){
				tableauAvancement.setij(i, j,tableauEtat[i][j].equals("vide"));
			}
		}
		
		for(int i=0;i<taille;i++){
			for(int j=0;j<taille;j++){
				if(tableauAvancement.get(i, j)==false){
					String col=tableauEtat[i][j];
					HashSet<Integer[]> elements=new HashSet<Integer[]>();
					HashSet<Integer[]> intersectionsVoisines=new HashSet<Integer[]>();
					HashSet<Integer[]> libertes=new HashSet<Integer[]>();
					intersectionsVoisines.removeAll(elements);
				this.ajouterGroupe(i, j, tableauEtat, tableauAvancement,fenJeu,elements,libertes,intersectionsVoisines);
				//System.out.println(tableauAvancement.get(i, j));
				new Groupe(fenJeu,col,elements,libertes,intersectionsVoisines);
				}
			}
		}
	}

		/**Recree le groupe contenant l'element en i,j l'ajoute aux groupes de fenJeu et actualise le tableauAvancement qui indique si une pierre a deja ete parcourue ou non */
	public void ajouterGroupe(int i,int j,String[][] tableauEtat, TableauAvancement tableauAvancement,FenetreJeu fenJeu,HashSet<Integer[]> elements,HashSet<Integer[]>libertes,HashSet<Integer[]>intersectionsVoisines){
		if(tableauAvancement.get(i, j)==false){
		elements.add(fenJeu.getIntersections()[i][j]);

		intersectionsVoisines.addAll(fenJeu.intersections(i, j));
		//System.out.println(tableauAvancement.get(i, j));
		tableauAvancement.setij(i, j,true);//la pierre est deja referencee on met true pour s'assurer de ne pas reparcourir cette intersction
		for(Integer[] L:fenJeu.intersections(i,j)){
			int a=L[0].intValue();
			int b=L[1].intValue();
			if(tableauEtat[a][b].equals("vide")){
				libertes.add(fenJeu.getIntersections()[a][b]);
			}
			else if(tableauEtat[a][b].equals(tableauEtat[i][j])){
				ajouterGroupe(a,b,tableauEtat,tableauAvancement,fenJeu,elements,libertes,intersectionsVoisines);
			}
		}
	}
	}
	/*
	public static void main(String[] args) {
		TableauAvancement tab=new TableauAvancement(1);
		tab.setij(0, 0, true);
		tab.setij(0, 0, false);
		System.out.println(tab.get(0, 0));
	}*/
	
	public void afficher(){
		MailleCoup a=this.dernierCoup;
		while(true){
			System.out.println("                 "+a.getNumeroCoup()+"             ");
			System.out.println(a);
			System.out.println("//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
				for(int i=0;i<taille;i++){
					System.out.println("] \n [");
					for(int j=0;j<taille;j++){
						System.out.print(" "+a.getTableauEtat()[i][j]+" ");
					}
				}
				if(a.getNumeroCoup()==0){
					break;
				}
				a=a.getCoupPrecedent();
			}
			
		}
	}
	

	
	
	
	
	
	
	

