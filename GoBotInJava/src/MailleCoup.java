import java.util.List;

public class MailleCoup {
	
/////////////////////////////
///////ATTRIBUTS////////////
///////////////////////////
protected final MailleCoup coupPrecedent;
protected final String[][] tableauEtat;
protected final int numeroCoup,nbPierresBlanchesCapturees,nbPierresNoiresCapturees;
protected final int[] posKo;///on prend les coordonees de l'intersection et non une reference a un element d'un tableau 
					///pour pouvoir reutiliser ces donnees après effacement de la fenetre de jeu
protected final boolean ko,aPasse;
	
	
/////////////////////////
////////CONSTRUCTEUR////
///////////////////////


public MailleCoup(FenetreJeu fenetreJeu){///coup 0
	tableauEtat=new String[fenetreJeu.getTaille()][fenetreJeu.getTaille()];
	for(int i=0;i<fenetreJeu.getTaille();i++){
	this.tableauEtat[i] =(String[])fenetreJeu.getTableauEtat()[i].clone();
}
	coupPrecedent=null;
	nbPierresBlanchesCapturees=fenetreJeu.getNbPierresBlanchesCapturees();
	nbPierresNoiresCapturees=fenetreJeu.getNbPierresNoiresCapturees();
	ko=fenetreJeu.isKo();
	aPasse=fenetreJeu.isaPasse();
	posKo=new int[]{fenetreJeu.getPosKo()[0].intValue(),fenetreJeu.getPosKo()[1].intValue()};
	numeroCoup=0;
	
}

public MailleCoup(MailleCoup previousCoup,FenetreJeu fenetreJeu){
	tableauEtat=new String[fenetreJeu.getTaille()][fenetreJeu.getTaille()];
	for(int i=0;i<fenetreJeu.getTaille();i++){
		tableauEtat[i] =(String[])fenetreJeu.getTableauEtat()[i].clone();
	}
	nbPierresBlanchesCapturees=fenetreJeu.getNbPierresBlanchesCapturees();
	nbPierresNoiresCapturees=fenetreJeu.getNbPierresNoiresCapturees();
	ko=fenetreJeu.isKo();
	aPasse=fenetreJeu.isaPasse();
	posKo=new int[]{fenetreJeu.getPosKo()[0].intValue(),fenetreJeu.getPosKo()[1].intValue()};
	
	coupPrecedent=previousCoup;
	numeroCoup=1+coupPrecedent.getNumeroCoup();
}
/////////////////////////
////GETTER//////////////
///////////////////////


/**
 * @return the coupPrecedent
 */

public MailleCoup getCoupPrecedent() {
	return coupPrecedent;
}


/**
 * @param coupPrecedent the coupPrecedent to set
 */


/**
 * @return the tableauEtat
 */
public String[][] getTableauEtat() {
	return tableauEtat;
}


/**
 * @param tableauEtat the tableauEtat to set
 */

/**
 * @return the numeroCoup
 */
public int getNumeroCoup() {
	return numeroCoup;
}


/**
 * @param numeroCoup the numeroCoup to set
 */

/**
 * @return the nbPierresBlanchesCapturees
 */
public int getNbPierresBlanchesCapturees() {
	return nbPierresBlanchesCapturees;
}


/**
 * @param nbPierresBlanchesCapturees the nbPierresBlanchesCapturees to set
 */



/**
 * @return the nbPierresNoiresCapturees
 */
public int getNbPierresNoiresCapturees() {
	return nbPierresNoiresCapturees;
}


/**
 * @param nbPierresNoiresCapturees the nbPierresNoiresCapturees to set
 */


/**
 * @return the posKo
 */
public int[] getPosKo() {
	return posKo;
}


/**
 * @param posKo the posKo to set
 */


/**
 * @return the ko
 */
public boolean isKo() {
	return ko;
}


/**
 * @param ko the ko to set
 */


/**
 * @return the aPasse
 */
public boolean isaPasse() {
	return aPasse;
}




///////////////////////////////
//FONCTIONS DE RECONSTITUTION/
/////////////////////////////


}
