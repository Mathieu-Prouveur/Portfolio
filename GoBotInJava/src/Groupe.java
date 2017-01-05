import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
/**La classe groupe definit les ensembles de pierre, dans toutes ses fonctions i et j correspondent aux positions dans le tabelau et non sur le plateau
  (qui seraent alors i+1 et j+1)*/
public class Groupe {
	///////////////////////////
	//////////ATTRIBUTS///////
	/////////////////////////
	
	private int nbLibertes;
	private int nbElements;
	private HashSet<Integer[]> elements,libertes,intersectionsVoisines;
	private FenetreJeu fenJeu;
	private String couleur;
	private boolean indestructible;///permet de s'assurer qu'une pierre posee entouree de pierre adverse ne disparaisse pas 
	
	/////////////////////////////////
	//////CONSTRUCTEUR//////////////
	///////////////////////////////
	/**Construit un goupe au cours d'une partie*/
	public Groupe(int i,int j, FenetreJeu fenetreJeu){

		indestructible=true;
		fenJeu=fenetreJeu;
		fenJeu.getTableauEtat()[i][j]=fenJeu.couleurJoueurActuel();
		couleur=fenJeu.couleurJoueurActuel().toString();////toString pour fixer la couleur du groupe
		elements=new HashSet<Integer[]>();
		Integer[] element=fenJeu.getIntersections()[i][j];
		elements.add(element);
		this.intersectionsVoisines=fenJeu.intersections(i, j);
		libertes=fenJeu.libertes(i, j);
		
	

		//////////////*
		/////////Bloc lie a la fusion avec groupes adjacents
		for(Groupe g:fenJeu.getGroupesVoisins(i, j, couleur)){
			this.fusion(g);
		}
			
		
		////////////////
		nbLibertes=libertes.size();
		nbElements=elements.size();
		fenJeu.getGroupes().add(this);
		
	}
	
	/**Permet de construire un groupe indépendamment de la partie en cours, cette fonction est utile dans le reconstitution des groupes grâce à l'historique */
	public Groupe(FenetreJeu fenetreJeu,String couleur,HashSet<Integer[]> elements,HashSet<Integer[]> libertes,HashSet<Integer[]> intersectionsVoisines){

		indestructible=false;
		fenJeu=fenetreJeu;
		this.couleur=couleur;
		this.libertes=libertes;
		this.elements=elements;
		this.intersectionsVoisines=intersectionsVoisines;
		////////////////
		nbLibertes=libertes.size();
		nbElements=elements.size();
		fenJeu.getGroupes().add(this);
	}
	
	
	
	
	///////////////////////////////
	/////GETTERS//////////////////
	/////////////////////////////
	
	public HashSet<Integer[]> getIntersectionsVoisines(){
		return(intersectionsVoisines);
	}
	public int getNbLibertes() {
		return nbLibertes;
	}
	public int getNbElements() {
		return nbElements;
	}
	public HashSet<Integer[]> getElements() {
		return elements;
	}
	public HashSet<Integer[]> getLibertes() {
		return libertes;
	}
	public FenetreJeu getFenJeu() {
		return fenJeu;
	}
	public String getCouleur() {
		return couleur;
	}
	
	public void plusIndestructible(){
		indestructible=false;
	}

	////////////////////////////////
	//////////FONCTIONS////////////
	//////////////////////////////
	
	/**Cette fonction renvoie un texte decrivant les elements et les libertes du groupe en question */
	public String toString(){
		StringBuffer s=new StringBuffer("///////////////////// \n Ce groupe "+couleur+" a "+nbElements+" element(s): \n {");
		Iterator<Integer[]> iterator=elements.iterator();

		while(iterator.hasNext()){
			Integer[]L=iterator.next();
			s.append("["+L[0]+"  "+L[1]+"]");
		}
		
		s.append("} \n Ce groupe "+couleur+" a "+nbLibertes+" libertes: \n {");
		
		Iterator<Integer[]> iterator2=libertes.iterator();
		
		while(iterator2.hasNext()){
			Integer[]L=iterator2.next();
			s.append("["+L[0]+"  "+L[1]+"]");
		}
		Iterator<Integer[]> iterator3=intersectionsVoisines.iterator();
		s.append("} \n Et pour voisins {");
			
		while (iterator3.hasNext()){
			Integer[]L=iterator3.next();
			s.append("["+L[0]+"  "+L[1]+"]");
		}
		s.append("} \n /////////////////////////////////////////////////");
		return(s.toString());
	}

	/**Cette fonction permet d'actualiser les libertes d'un groupe quand la case [i][j] du tableau devient occupee.
	 En cas de mort sont actualises:
	 -la liste des groupes
	 -le nb de pierres captures
	 -les libertes des groupes
	 -le tableau d'etat
	 
	 */
	public void enleverLiberte(int i, int j){
		
		if(libertes.remove(fenJeu.getIntersections()[i][j])){
			nbLibertes-=1;
		}
		/////////////////////////
		//////////////BLOC LIE A LA MORT DU GROUPE
		if (nbLibertes==0&&indestructible==false){
			fenJeu.setDernierGroupeMort(this);
			fenJeu.setMort(true);
			///actualisation pierres capturees
			if(couleur=="Noir"){
				fenJeu.ajouterNbPierresNoiresCapturees(nbElements);
			}
			else{
				fenJeu.ajouterNbPierresBlanchesCapturees(nbElements);
			}
			fenJeu.setNbPierrecaptureesCeTour(nbElements+fenJeu.getNbPierrecaptureesCeTour());
			
			
			
			////actualisation liste des groupes
			
			
			////actualisation tableau etat
			Iterator<Integer[]> iterator=this.elements.iterator();
			while(iterator.hasNext()){
				Integer[] M=iterator.next();
				int k=M[0].intValue();
				int l=M[1].intValue();
				fenJeu.getTableauEtat()[k][l]="vide";
			}
			
		
		}
		
	}
	/** Libere espace memoire*/
	public void kill(){
		try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**Cette fonction permet d'actualiser les libertes d'un groupe quand le groupe "mort" meurt et libere ses cases*/
	public void ajouterLibertes(Groupe mort){
		HashSet<Integer[]> copieElementsGroupeMort=new HashSet<Integer[]>();
		copieElementsGroupeMort.addAll(mort.getElements());//on fait une copie pour ne pas modifier le groupe mort
		copieElementsGroupeMort.retainAll(this.intersectionsVoisines);////on modifie la copie de l'ensemble des elements en ne conservant que les elements qui sont voisins du groupe  
		this.libertes.addAll(copieElementsGroupeMort);
		nbLibertes=libertes.size();
	}
	
	/**Cette fonction permet de fusionner le goupe b avec le groupe "this" et de detruire par la suite le groupe b en le retirant de la liste de groupes puis en l'effaçant de la memoire.
	 * Cette fonction tient compte du fait qu'on l'appelle après avoir enleve i,j des libertes possibles */
	public void fusion(Groupe b){
		
		libertes.addAll(b.libertes);
		elements.addAll(b.elements);
		intersectionsVoisines.addAll(b.intersectionsVoisines);
		
		nbLibertes=libertes.size();
		nbElements=elements.size();
		this.intersectionsVoisines.removeAll(b.elements);
		this.intersectionsVoisines.removeAll(this.elements);
		
		fenJeu.getGroupes().remove(b);
		////////////// FAIT BUGER???????
		try{
		b.finalize();}
		catch(Exception e){} catch (Throwable e) {
			
			e.printStackTrace();}
		
	}
	
	
	
}
