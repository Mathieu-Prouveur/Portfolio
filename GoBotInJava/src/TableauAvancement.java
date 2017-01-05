
public class TableauAvancement {
	private boolean[][] tableauAvancement;

	public TableauAvancement(int n){
		tableauAvancement=new boolean[n][n];
	}
	public void setij(int i,int j, boolean valeur){
		tableauAvancement[i][j]=valeur;
	}
	
	public boolean get(int i,int j){
		return(this.tableauAvancement[i][j]);
	}
	
	/*
	public static void main(String[] args) {
		TableauAvancement tab=new TableauAvancement(1);
		tab.setij(0, 0, true);
		tab.setij(0, 0, false);
		System.out.println(tab.get(0, 0));
	}
*/	
}
