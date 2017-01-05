import java.awt.*;
import java.awt.event.*;

public class Options extends Frame implements ActionListener,ItemListener {
	//////////ATTRIBUTS
	private Menu menu;
	Choice choixTaillePlateau;
	Choice choixNombrePierresHandicaps;
	Choice choixAlgoOrdi;
	Choice choixPositionOrdi;
	TextField nomJoueurBlanc;
	TextField nomJoueurNoir;
	TextField nomJoueurHumain;
	
	///CONSTRUCTEUR////
	public Options (Menu menu){
		super("Options");
		this.menu=menu;
		setLayout(new GridLayout(3,1,1,10));
		Panel panelSup=new Panel(new GridLayout(5,1,1,10));
		Panel panelInf=new Panel(new GridLayout(1,2,1,10));
		Panel panelInfGauche=new Panel(new GridLayout(4,1,1,10));
		Panel panelInfDroite=new Panel(new GridLayout(6,1,1,10));
		Panel panelBoutons=new Panel (new GridLayout(2,1,1,10));
		
		Font titre=new Font(Font.SERIF,Font.BOLD|Font.ROMAN_BASELINE,20);
		Font normal=new Font(Font.SERIF,Font.ITALIC|Font.PLAIN,16);
		setFont(normal);
		setBackground(new Color(205,0,0));
		
		////////creation des differentes options
		choixTaillePlateau = new Choice();
		for(int i=3;i<20;i++){
			choixTaillePlateau.add(String.valueOf(i));
		}
		choixTaillePlateau.select(String.valueOf(9));
		
		choixAlgoOrdi=new Choice();
		choixAlgoOrdi.add("Aleatoire");
		
		choixPositionOrdi=new Choice();
		choixPositionOrdi.add("Noir");
		choixPositionOrdi.add("Blanc");
		
		choixNombrePierresHandicaps=new Choice();
		choixNombrePierresHandicaps.add("0");
		
		nomJoueurBlanc=new TextField("Preciser ici nom du joueur Blanc (Par defaut : Missa)");
		nomJoueurNoir=new TextField("Preciser ici nom du joueur Noir (Par defaut : Tissa)");
		nomJoueurHumain=new TextField("Preciser ici nom du joueur Humain (Par defaut : Humain)");
		
		choixTaillePlateau.addItemListener(this);
		choixNombrePierresHandicaps.addItemListener(this);
		choixAlgoOrdi.addItemListener(this);
		choixPositionOrdi.addItemListener(this);
		nomJoueurBlanc.addActionListener(this);
		nomJoueurNoir.addActionListener(this);
		nomJoueurHumain.addActionListener(this);
		
		/////////Creation des differents labels
		Label titre1=new Label("Options generales",Label.CENTER);
		titre1.setFont(titre);
		titre1.setForeground(Color.WHITE);
		Label titre2=new Label("Options Humain vs Ordi",Label.LEFT);
		titre2.setFont(titre);
		titre2.setForeground(Color.WHITE);
		Label titre3=new Label("Options Humain vs Humain",Label.LEFT);
		titre3.setFont(titre);
		titre3.setForeground(Color.WHITE);
		Label taille=new Label("Taille du plateau: NxN avec N= :  ",Label.LEFT);
		taille.setForeground(Color.WHITE);
		Label algoOrdi=new Label("Choisissez l'algo de l'Ordinateur:  ",Label.LEFT);
		algoOrdi.setForeground(Color.WHITE);
		Label positionOrdi=new Label("Choisissez position de l'Ordinateur:  ",Label.LEFT);
		positionOrdi.setForeground(Color.WHITE);
		Label pierresHandicaps=new Label("Choisissez le nombre de pierres de Handicap:  ",Label.LEFT);
		pierresHandicaps.setForeground(Color.WHITE);
		/////boutons appliquer et annuler
		Button appliquer=new Button("Appliquer les changements");
		Button annuler=new Button("Annuler et revenir au menu");
		appliquer.setBackground(Color.white);
		annuler.setBackground(Color.white);
		
		appliquer.setFont(titre);
		annuler.setFont(titre);
		
		appliquer.addActionListener(this);
		annuler.addActionListener(this);
		
		/////////On met tout dans les boites!!!!!!!!!!!!
		panelSup.add(titre1);
		panelInfDroite.add(titre2);
		panelInfGauche.add(titre3);
		
		panelSup.add(taille);
		panelSup.add(choixTaillePlateau);
		panelSup.add(pierresHandicaps);
		panelSup.add(choixNombrePierresHandicaps);
		
		panelInfGauche.add(nomJoueurNoir);
		panelInfGauche.add(nomJoueurBlanc);
		
		panelInfDroite.add(algoOrdi);
		panelInfDroite.add(choixAlgoOrdi);
		panelInfDroite.add(positionOrdi);
		panelInfDroite.add(choixPositionOrdi);
		panelInfDroite.add(nomJoueurHumain);
		
		panelInf.add(panelInfGauche);
		panelInf.add(panelInfDroite);
		
		panelBoutons.add(appliquer);
		panelBoutons.add(annuler);
		
		this.add(panelSup);
		this.add(panelInf);
		this.add(panelBoutons);
		
		///affichage merveille
		
		setLocation(0,0);
		setSize(1700,1000);
		setVisible(true);
		addWindowListener(new EcouteurPourFermetureFenetre()); 

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		/*
		Choice choixTaillePlateau;
		Choice choixNombrePierresHandicaps;
		Choice choixAlgoOrdi;
		Choice choixPositionOrdi;
		TextField nomJoueurBlanc;
		TextField nomJoueurNoir;
		TextField nomJoueurHumain;
		
		*/
		
		if(e.getActionCommand().equals("Annuler et revenir au menu")){
			this.dispose();
			menu.setVisibleExt(true);
		}
	
		if(e.getActionCommand().equals("Appliquer les changements")){
			menu.setTaille(choixTaillePlateau.getSelectedIndex()+3);
			//menu.setNbPierresHandicaps(choixNombrePierresHandicaps.);
			
			if(nomJoueurBlanc.getText().equals("Preciser ici nom du joueur Blanc (Par defaut : Missa)")==false){
				menu.setNomJoueurBlanc(nomJoueurBlanc.getText());
			}
			
			if(nomJoueurNoir.getText().equals("Preciser ici nom du joueur Noir (Par defaut : Tissa)")==false){
				menu.setNomJoueurNoir(nomJoueurNoir.getText());
			}
			
			if(nomJoueurHumain.getText().equals("Preciser ici nom du joueur Humain (Par defaut : Humain)")==false){
				menu.setNomJoueurNoir(nomJoueurNoir.getText());
			}
			
			menu.setPlaceOrdi(choixPositionOrdi.getSelectedItem());
			
			this.dispose();
			menu.setVisibleExt(true);

		}
		
		
	}
	
}
