import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

public class AlgoOrdinateur {
	/*
	 * A IMPLEMENTER: done ETAPE 1: SELECTIONNER LES COUPS POSSIBLES DANS UN
	 * ENSEMBLE (DONT PASSER) ETAPE 2: IMPLEMENTER UNE NOTION DE TERRITOIRE?
	 * RETIRER COUPS QUI COMBLENT LEUR PROPRE TERRITOIRE ETAPE 3: IMPLEMENTER
	 * EVALUATION DES POINTS ETAPE 4: IMPLEMENTER MONTE-CARLO ETAPE 5: AUTRES
	 * 
	 * AUTRES: 1)CREER UNE REACTION AU CAS OU IL N'Y A QUE 1 OU 2 LIBERTES
	 * RESTANTES--> ATTAQUER OU FUIR POSSIBLITE DE MODULER AVEC LE NOMBRES
	 * D'ELEMENTS, UN GROUPE DE 5 PIERRES DEVRAIT AVOIR PLUS DE 2 LIBERTES
	 * 2)CREER LA NOTION D'OEIL!!!!!!!!!!!!!!!! ---> LIBERTEE ENTOUREE DE
	 * PIERRES ALLIEES ---> PRENDRE EN COMPTE LES LIBERTES DES PIERRES CREANT
	 * L'OEIIL 3)ETRE CAPABLE DE DIRE SI UN GROUPE EST MORT OU NON
	 *
	 * 
	 * 
	 */

	// cette position absurde sera la position renvoyee pour dire de passer
	public final static Integer[] passer = new Integer[] { new Integer(-1), new Integer(-1) };

	public static void ordiJoue(FenetreJeu fenJeu) {
		Integer[] coup = AlgoOrdinateur.coupAleatoire(fenJeu);
		if (coup == AlgoOrdinateur.passer) {
			System.out.println("ordii passe!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			fenJeu.setJoueurActuel(fenJeu.getProchainJoueur());
		} else {
			fenJeu.poserPierre(coup[0].intValue(), coup[1].intValue());
			System.out.println("coup de l'ordi:  " + coup[0].intValue() + "  " + coup[1].intValue());
		}
	}

	/**
	 * Cette fonction permet à l'ordinateur de proposer un coup aleatoire, si le
	 * joueur ne peut pas jouer, l'ordinateur renvoie un integer[] egal a
	 * [-1,-1] de sorte que cette valeur absurde puisse etre interpretee comme
	 * passer.
	 */
	public static Integer[] coupAleatoire(FenetreJeu fenJeu) {
		long h1 = System.currentTimeMillis();

		Integer[][] coupsPossibles = ensembleCoupsPossibles(fenJeu);
		int nombresPossibilites = coupsPossibles.length;
		/////////////////////////////////////////////////////////
		////// ICI ON ENLEVERA LES COUPS NON PERTINENTS//////////
		///////////////////////////////////////////////////////
		int coupARenvoyer = (int) (Math.random() * (nombresPossibilites));
		System.out.println("temps mis pour trouver coup aleatoire" + (System.currentTimeMillis() - h1));
		return (coupsPossibles[coupARenvoyer]);
	}
	public static Integer[] maxiTerritoire(FenetreJeu fenJeu){
		Integer[][] ensembleCoupsPossibles=AlgoOrdinateur.ensembleCoupsPossibles(fenJeu);
		Integer[] coupOpti=ensembleCoupsPossibles[0];
		int diffOpti=AlgoOrdinateur.diffTerritoire(evaluationTerritoire(fenJeu));
		for(int i=1;i<ensembleCoupsPossibles.length;i++){
			String[][] tab=fenJeu.getTableauEtat().clone();
			int a=ensembleCoupsPossibles[i][0].intValue();
			int b=ensembleCoupsPossibles[i][1].intValue();
			tab[a][b]=fenJeu.getJoueurActuel();
			
		}
		return(coupOpti);
	}
	
	public static int diffTerritoire(int[][] terr){
		int diff=0;
		for(int k=0;k<terr.length;k++){
			for(int l=0;l<terr.length;l++){
				if(terr[k][l]<0){
					diff--;
				}
				else if(terr[k][l]>0){
					diff++;
				}
			}
		}
		return(diff);
	}
	
	/**
	 * Cette fonction renvoie le tableau des coups possibles
	 * 
	 * 
	 */
	public static Integer[][] ensembleCoupsPossibles(FenetreJeu fenJeu) {
		int taille = fenJeu.getTaille();
		Integer[][] coupsPossibles = new Integer[taille * taille + 1][2];
		coupsPossibles[0] = passer;
		int nombresPossibilites = 1;
		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < taille; j++) {
				if (fenJeu.estAutorise(i, j)) {
					coupsPossibles[nombresPossibilites] = fenJeu.getIntersections()[i][j];
					nombresPossibilites++;
				}
			}
		}

		Integer[][] result = new Integer[nombresPossibilites][2];
		for (int i = 0; i < nombresPossibilites; i++) {
			result[i] = coupsPossibles[i];
		}
		return (result);
	}

	/**
	 * 
	 * Retourne un tableau image de l'influence de chacun des parties sur le
	 * plateau:
	 * 
	 */
	public static int[][] evaluationInfluence(FenetreJeu fenJeu) {
		int taille = fenJeu.getTaille();
		int[][] tableauInfluence = new int[taille][taille];
		String[][] tableauEtat = fenJeu.getTableauEtat();
		HashSet<Integer[]> influenceNoir, influenceBlanche;
		influenceNoir = new HashSet<Integer[]>();
		influenceBlanche = new HashSet<Integer[]>();

		//// on initialise le tableau avec 0 si case vide ou +/- 128 sinon;
		
		
		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < taille; j++) {
				if (tableauEtat[i][j].equals("Noir")) {
					tableauInfluence[i][j] = 128;
				} else if (tableauEtat[i][j].equals("vide")) {
					tableauInfluence[i][j] = 0;
				} else {
					tableauInfluence[i][j] = -128;
				}
			}
		}
		
		/// on applique 1)

		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < taille; j++) {
				if (tableauInfluence[i][j] == 0) {

					boolean voisinNoir = false;
					boolean voisinBlanc = false;

					for (Integer[] L : fenJeu.intersections(i, j)) {
						int a, b;
						a = L[0].intValue();
						b = L[1].intValue();
						if (tableauInfluence[a][b] > 0) {
							voisinNoir = true;
						} else if (tableauInfluence[a][b] < 0) {
							voisinBlanc = true;
						}

					}
					if (voisinBlanc != voisinNoir) {// s'il n'a qu'un type de
													// voisin il devient
													// influence
						if (voisinBlanc) {
							influenceBlanche.add(fenJeu.getIntersections()[i][j]);
						} else {
							influenceNoir.add(fenJeu.getIntersections()[i][j]);
						}
					}
				}

			}
		}
		for (Integer[] L : influenceNoir) {
			int a, b;
			a = L[0].intValue();
			b = L[1].intValue();
			tableauInfluence[a][b] = 50;
		}
		for (Integer[] L : influenceBlanche) {
			int a, b;
			a = L[0].intValue();
			b = L[1].intValue();
			tableauInfluence[a][b] = -50;
		}

		for (int k = 0; k < 3; k++) {
			for (int i = 0; i < taille; i++) {
				for (int j = 0; j < taille; j++) {
					if (tableauInfluence[i][j] == 0) {

						boolean voisinNoir = false;
						boolean voisinBlanc = false;

						for (Integer[] L : fenJeu.intersections(i, j)) {
							int a, b;
							a = L[0].intValue();
							b = L[1].intValue();
							if (tableauInfluence[a][b] > 0) {
								voisinNoir = true;
							} else if (tableauInfluence[a][b] < 0) {
								voisinBlanc = true;
							}

						}
						if (voisinBlanc != voisinNoir) {// s'il n'a qu'un type
														// de voisin il devient
														// influence
							if (voisinBlanc) {
								influenceBlanche.add(fenJeu.getIntersections()[i][j]);
							} else {
								influenceNoir.add(fenJeu.getIntersections()[i][j]);
							}
						}
					}

				}
			}
			for (Integer[] L : influenceNoir) {
				int a, b;
				a = L[0].intValue();
				b = L[1].intValue();
				tableauInfluence[a][b] = 50;
			}
			for (Integer[] L : influenceBlanche) {
				int a, b;
				a = L[0].intValue();
				b = L[1].intValue();
				tableauInfluence[a][b] = -50;
			}
		}

		System.out.println("on y est passe");
		///// on renvoit le resultat
		return (tableauInfluence);
	}


	/**
	 * Le but est de retourner un tableau ou une valeur positive(resp negative)
	 * correspond a un territoire noir(resp Blanc) L'idee de base est proche de
	 * celle de l'influence, la difference majeure residant dans le fait qu'on
	 * retire via des "erosions" les zones peu influencees; On applique l'algo
	 * suivant:
	 * 
	 * Etape1: On donne 128 aux intersections noires et -128 aux intersections
	 * blanches Etape2: On applique des "dilatations"; Etape3: On applique des
	 * "erosions";
	 * 
	 * Dilatation:Pour chaque intersection du goban, si une intersection est
	 * positive (resp. négative) ou nulle et n'est pas voisine d'une
	 * intersection négative (resp. positive), il additionne (resp. soustrait)
	 * le nombre de voisines positives (resp. négative) à l'intersection
	 * 
	 * 
	 * Erosion:Pour chaque intersection du goban, si une intersection est
	 * positive (resp. négative), il soustrait (resp. additionne) le nombre de
	 * voisines négatives (resp. positives) ou nulles à l'intersection en
	 * vérifiant qu'elle reste positive (resp. négative) ou nulle.
	 * 
	 */
	public static int[][] evaluationTerritoire(FenetreJeu fenJeu) {
		int taille = fenJeu.getTaille();
		int[][] tableauInfluence = new int[taille][taille];
		int[][] tableauAuxiliaire = new int[taille][taille];// on mettra dans ce
															// tableau les
															// valeurs a ajouter
		String[][] tableauEtat = fenJeu.getTableauEtat();
		int nbDilatations =4;//(int)(taille/2);
		int nbErosions = nbDilatations * (nbDilatations - 1) + 1;

		//// ETAPE 0
		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < taille; j++) {
				if (tableauEtat[i][j].equals("Noir")) {
					tableauInfluence[i][j] = 128;
				} else if (tableauEtat[i][j].equals("vide")) {
					tableauInfluence[i][j] = 0;
				} else {
					tableauInfluence[i][j] = -128;
				}
			}
		}
		/// ETAPE 1
		for (int k = 0; k < nbDilatations; k++) {
			for (int i = 0; i < taille; i++) {
				for (int j = 0; j < taille; j++) {
					boolean voisinNoir = false;
					boolean voisinBlanc = false;
					int val = 0;/// on compte le nombre de zones d'influences
								/// voisines
					for (Integer[] L : fenJeu.intersections(i, j)) {
						int a, b;
						a = L[0].intValue();
						b = L[1].intValue();
						if (tableauInfluence[a][b] > 0) {
							voisinNoir = true;
							val++;
						} else if (tableauInfluence[a][b] < 0) {
							voisinBlanc = true;
							val++;
						}

					}
					if (voisinBlanc != voisinNoir) {// s'il n'a qu'un type de
													// voisin il est influence
						if (voisinBlanc) {
							tableauAuxiliaire[i][j] = -val;
						} else {
							tableauAuxiliaire[i][j] = val;
						}
					} else {
						tableauAuxiliaire[i][j] = 0;
					}
				}
			}
			///// on actualise

			for (int i = 0; i < taille; i++) {
				for (int j = 0; j < taille; j++) {
					tableauInfluence[i][j] += tableauAuxiliaire[i][j];
				}
			}

		}

		/// ETAPE 2
		for (int k = 0; k < nbErosions; k++) {
			for (int i = 0; i < taille; i++) {
				for (int j = 0; j < taille; j++) {
					if (tableauInfluence[i][j] != 0) {
						int val = 0;/// on compte le nombre de zones
									/// d'influences hostiles ou non influencees
						for (Integer[] L : fenJeu.intersections(i, j)) {
							int a, b;
							a = L[0].intValue();
							b = L[1].intValue();
							if (tableauInfluence[i][j]*tableauInfluence[a][b]<= 0) {//vrai que si influence ennemi ou voisin vide
								val++;
							}
						}
						if(tableauInfluence[i][j]<=0){///on veut dimminuer en valeur absolu la valeur de l'influence
						tableauAuxiliaire[i][j]=val;}
						else{
							tableauAuxiliaire[i][j]=-val;
					}
						}
					else {
						tableauAuxiliaire[i][j] = 0;
					}

				}
			}
			///// on actualise

			for (int i = 0; i < taille; i++) {
				for (int j = 0; j < taille; j++) {
					if(tableauInfluence[i][j]<=0){
					tableauInfluence[i][j] = Math.min(0,tableauInfluence[i][j]+tableauAuxiliaire[i][j]);
						}
					else{
						tableauInfluence[i][j] = Math.max(0,tableauInfluence[i][j]+tableauAuxiliaire[i][j]);
					}
			}

		}
			}

		System.out.println("on y est passe");
		///// on renvoit le resultat
		return (tableauInfluence);
	}

}
