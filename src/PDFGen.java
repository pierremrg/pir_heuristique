import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFileChooser;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFGen 
{
	
//	private static final String OUTPUT_DIR = "./pdf/";
	private static String outputDir;
	
	// -------------------------- METHODE PRINCIPALE -------------------------------
	public static void createPDF (Tournoi tournoi) throws DocumentException, IOException 
	{
		
	    // Sélection du dossier où enregistrer les fiches
	    JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
	    chooser.setDialogTitle("Choisissez un emplacement pour enregistrer les fiches...");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int result = chooser.showOpenDialog(null);
		
		if(result == JFileChooser.APPROVE_OPTION)
			outputDir = chooser.getSelectedFile().getAbsolutePath() + "/";
		
		else
			return;
			
		
		File directory = new File(outputDir);
	    if(!directory.exists())
	        directory.mkdir();
		
		// -------- partie json		
//		String path = outputDir + "donnees_eleves.json" ;
		
		// -------- creation des documents pdf
		createDocListeClasses(tournoi) ;
		createDocListeNiveaux(tournoi) ;
		createDocFicheProf(tournoi) ;
		createDocMatches(tournoi) ;
		createDocFicheEleve(tournoi) ;
	}
	

	
	
	// retourne le tableau contenant la liste de tous les élèves d'un groupe de niveau d'une classe donnée
	public static PdfPTable getGroupeNiveauClasse (Classe classe, int niv, boolean idOui)
	{
		// creation du tableau
		PdfPTable tab ;	
		if(idOui)
		{	
			tab = new PdfPTable(2) ;
		}
		else
		{
			tab = new PdfPTable(1) ;
		}
		
		// ajoute nom du prof au tableau
		PdfPCell cell = new PdfPCell(new Phrase(classe.getProf())) ;
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setColspan(2);
		cell.setBackgroundColor(classe.getCouleur());
		tab.addCell(cell);
			
		// on recup la liste des eleves du groupe donné
		ArrayList<Eleve> liste = classe.getGroupeNiveau(niv) ;
		Iterator<Eleve> it = liste.iterator() ;
		while(it.hasNext())
		{ 
			Eleve eleve = it.next() ;
			if(idOui)
			{
				// on ajoute l'id
				String nb = Integer.toString(eleve.getId()) ;
				cell = new PdfPCell(new Phrase(nb)) ;
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				tab.addCell(cell);
			}
			// on ajoute au tableau
			cell = new PdfPCell(new Phrase(eleve.getNom())) ;
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			tab.addCell(cell);
		}
		
		// on retourne le tableau
		return tab ;
			
	}
	
	
	
	// ------------------------- CREATION DES PDF --------------------------------
	
	// --------- Header commun à tous les pdf ------------------------------------
	public static void addHeader (Tournoi tournoi, Document doc)
	{
		// recupere les infos
		Paragraph pNom = new Paragraph(tournoi.getNom()) ;
		Paragraph pDate = new Paragraph(tournoi.getDate()) ;
		Paragraph pVide = new Paragraph(" ") ;
		// ajoute au document
		try
		{
			doc.add(pNom) ;
			doc.add(pDate) ;
			doc.add(pVide);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// --------- Doc 1 : liste de toutes les classes -----------------------------
	
	// methode créant le pdf avec la liste de toutes les classes
	public static void createDocListeClasses(Tournoi tournoi) throws DocumentException, IOException
	{
		// --------- declarations
		// Creation du document
		Document doc = new Document();
		String chemin = outputDir + "ListeClasses.pdf" ;
		FileOutputStream output = new FileOutputStream(chemin) ;
		
		// --------- ouvre le doc
		PdfWriter wr = PdfWriter.getInstance(doc, output) ;
		doc.open();

		// --------- ajout de contenu		

		addAllClasses(tournoi, doc) ;		
		
		//--------- ferme le doc 
		doc.close();
	}
	

	// retourne le tableau de la liste de classe
	public static void addClasse(Classe classe, PdfPTable tab)
	{	
		// ajoute le nom du prof
		PdfPCell cell = new PdfPCell(new Phrase(classe.getProf())) ;
		cell.setColspan(6);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBackgroundColor(classe.getCouleur());
		tab.addCell(cell);
		
		// légende
		cell = new PdfPCell(new Phrase ("ID Eleve")) ;
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tab.addCell(cell);
		cell = new PdfPCell(new Phrase("Nom de l'élève")) ;
		cell.setColspan(4);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tab.addCell(cell);
		cell = new PdfPCell(new Phrase("Niveau")) ;
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tab.addCell(cell) ;
		
		// pour chaque eleve
		ArrayList<Eleve> listeEleves = classe.getListe() ;
		Iterator<Eleve> it = listeEleves.iterator() ;
		while(it.hasNext())
		{ 
			// on recup les infos
			Eleve eleve = it.next() ;
			
			// on ajoute au tableau son id et son nom
			String nb = Integer.toString(eleve.getId()) ;
			cell = new PdfPCell(new Phrase(nb));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tab.addCell(cell);
			cell = new PdfPCell(new Phrase(eleve.getNom())) ;
			cell.setColspan(4);
			tab.addCell(cell);
			nb = Integer.toString(eleve.getNiveau()) ;
			cell = new PdfPCell(new Phrase(nb));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tab.addCell(cell);
		}		
	}
		
	// ajoute toutes les classes au tableau
	public static void addAllClasses(Tournoi tournoi, Document doc)
	{	
		// recup la liste des classes
		ArrayList<Classe> listeclasse = tournoi.getClasses() ;
		
		// pour chaque classe
		Iterator<Classe> it = listeclasse.iterator() ;
		while(it.hasNext())
		{
			// ------ Declarations
			// Creation du tableau de la classe
			PdfPTable tab = new PdfPTable(6) ;
			
			// Header titre et date
			addHeader(tournoi,doc);
			
			// ------- Contenu
			Classe classe = it.next() ;
						
			// ------- Creation des cellules et ajout au tableau
			addClasse(classe,tab);
					
			// ------- Ajout au document
			try
			{					
				doc.add(tab) ;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			// on saute une page
			doc.newPage() ;
		}
	}
	
	// --------- Doc 2 : listes des groupes de niveau -------------------------
	
	// methode créant le pdf avec la liste de tous les niveaux
	public static void createDocListeNiveaux(Tournoi tournoi) throws DocumentException, IOException
	{
		// --------- déclarations
		// Creation du document
		Document doc = new Document();
		String chemin = outputDir + "ListeNiveaux.pdf" ;
		FileOutputStream output = new FileOutputStream(chemin) ;
					
		// -------- ouvre le doc
		PdfWriter wr = PdfWriter.getInstance(doc, output) ;
		
		Rotate event = new Rotate();
        wr.setPageEvent(event);
		
		
		doc.open();

				
		// --------- ajout de contenu
		// ajout des classes
		addAllClassesNiveau(tournoi, doc) ;	
		

        // step 4
        event.setOrientation(PdfPage.LANDSCAPE);
			
		//--------- ferme le doc 
		doc.close();
	}
		
	
	
	// ajoute tous les groupes de niveaux des classes au tableau
	public static void addAllClassesNiveau(Tournoi tournoi, Document doc)
	{		
		// pour chaque niveau
		for (int i=1;i<4;i++)
		{
			// Header titre et date
			addHeader(tournoi,doc);
			// ------- Declarations ---------------
			// Creation du tableau
			PdfPTable tabNiveau = new PdfPTable(tournoi.getNbClasses()) ;
			tabNiveau.setSpacingBefore(25);
			tabNiveau.setSpacingAfter(15);
			
			// -------- Partie titre du niveau ------
			String texte = "Niveau" + String.valueOf(i) ;
			PdfPCell cell = new PdfPCell(new Phrase(texte)) ;
			cell.setColspan(tournoi.getNbClasses());
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tabNiveau.addCell(cell);

			
			// -------- Partie liste des classes ------
			ArrayList<Classe> listeclasse = tournoi.getClasses() ;
			Iterator<Classe> it = listeclasse.iterator() ;
			while(it.hasNext())
			{
				// recup l'objet classe
				Classe classe = it.next();			
				// recup le tableau de la classe
				PdfPTable tab = getGroupeNiveauClasse(classe,i,false);
				// ajoute le tab au tableau global
				cell = new PdfPCell(tab) ;
				tabNiveau.addCell(cell);
			}
			// Ajout du tableau global au document
			try
			{					
				// Tableau niveaux
				doc.add(tabNiveau) ;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			// on saute une page
			doc.newPage() ;
		}
	}
	// --------- Doc 3 : fiche prof avec la liste des eleves par groupes pour noter le score -------------------------

	// retourne le tableau contenant la liste de tous les élèves d'un groupe de niveau d'une classe donnée
	// fonction getGroupeNiveauClasse (Classe classe, int niv)
	
	// creation du document pdf de fiche prof
	public static void createDocFicheProf(Tournoi tournoi) throws DocumentException, IOException
	{
		// --------- déclarations
		// Creation du document
		Document doc = new Document();
		String chemin = outputDir + "FicheProf.pdf" ;
//		FileOutputStream output = new FileOutputStream(chemin) ;
					
		// -------- ouvre le doc
//		PdfWriter wr = PdfWriter.getInstance(doc, output) ;
		doc.open();
				
		// --------- ajout de contenu
		// ajout des classes
		addAllTabResultats(tournoi,doc) ;
			
		//--------- ferme le doc 
		doc.close();
	}
	// ajoute tous les tableaux de resultats de toutes les classes
	public static void addAllTabResultats(Tournoi tournoi, Document doc)
	{
		// recup la liste des classes
		ArrayList<Classe> listeclasse = tournoi.getClasses();
		
		// parcours et ajoute chaque classe
		Iterator<Classe> it = listeclasse.iterator() ;
		while(it.hasNext())
		{
			// recup l'objet classe
			Classe classe = it.next() ;			
			// ajoute le tableau de classe
			addTabResultats(classe,doc,tournoi) ;
		}
		
	}
	// ajoute un tableau de resultat pour chaque niveau d'une classe
	public static void addTabResultats (Classe classe, Document doc, Tournoi tournoi)
	{
		// Pour chaque niveau, on remplit et ajoute le tableau
		for(int i=1;i<4;i++)
		{		
			// --------- Header titre et date -----------
			addHeader(tournoi,doc);
			// --------- Tableau global ----------------
			// Creation du tableau global
			PdfPTable tab = new PdfPTable(4) ;
			// Creation du tableau
			PdfPTable tabListe = new PdfPTable(1) ;
			// Creation d'une cellule
			PdfPCell cell ;
			// nb cases vides
			int nbVide = classe.getNbElevesNiveau(i) ;
			
			// --------- Colonne 1 : Niveau et Tableau Eleves -----------------
			// Niveau
			cell = new PdfPCell(new Phrase("Niveau "+ i)) ;
			cell.setColspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tabListe.addCell(cell) ;
			// On recupere le tableau d'eleves du niveau i
			PdfPTable tabEleves = new PdfPTable(1);
			tabEleves = getGroupeNiveauClasse(classe, i, true) ;
			// on l'ajoute au tableau liste
			cell = new PdfPCell(tabEleves) ;
			tabListe.addCell(cell);
	
			
			// --------- Colonne 2 : Tableau des Rounds --------------
			// Creation du tableau des rounds
			PdfPTable tabRound = new PdfPTable(6) ;
			// legende rondes
			cell = new PdfPCell(new Phrase("Rounds")) ;
			cell.setColspan(6);
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tabRound.addCell(cell);
			// nb de round
			for (int j=1;j<7;j++)
			{
				String nb = Integer.toString(j);
				cell = new PdfPCell(new Phrase(nb)) ;
				cell.setColspan(1);
				cell.setRowspan(1);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				tabRound.addCell(cell);
			}
			// ajout de cases vides
			for(int n=0;n<nbVide*6;n++)
			{
				cell = new PdfPCell(new Phrase(" ")) ;
				cell.setColspan(1);
				cell.setRowspan(1);
				tabRound.addCell(cell);
			}
			
			
			// ---------- Colonne 3: Total --------------------------
			// Creation du tableau
			PdfPTable tabTotal = new PdfPTable(1);

			// legende total
			cell = new PdfPCell(new Phrase("Total")) ;
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tabTotal.addCell(cell) ;
			// ajout de cases vides
			for(int n=0;n<nbVide+1;n++)
			{
				cell = new PdfPCell(new Phrase(" ")) ;
				cell.setColspan(1);
				cell.setRowspan(1);
				tabTotal.addCell(cell);
			}
			
			// ---------- Ajout des 3 tableaux au tableaux global -----------
			// on ajoute le tableau eleves	
			cell = new PdfPCell(tabListe) ;
			tab.addCell(cell);
			// on ajoute le tableau round
			cell = new PdfPCell(tabRound) ;
			cell.setColspan(2);
			tab.addCell(cell);
			// on ajoute la colonne
			cell = new PdfPCell(tabTotal) ;
			tab.addCell(cell);
			
			// --------------- Ajout au document -------------------
			try
			{
				doc.add(tab) ;
				doc.newPage() ;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}		
	}
	
// --------- Doc 4 : Tableau des matchs -------------------------
	// creation du document pdf du tableau récap des matches pour chaque ronde
	public static void createDocMatches(Tournoi tournoi) throws DocumentException, IOException
	{
		// --------- déclarations
		// Creation du document
		Document doc = new Document();
		String chemin = outputDir + "ListeMatches.pdf" ;
		FileOutputStream output = new FileOutputStream(chemin) ;
						
		// -------- ouvre le doc
		PdfWriter wr = PdfWriter.getInstance(doc, output) ;
		doc.open();
	
		// --------- ajout de contenu
		addHeader (tournoi,doc) ;
		addAllRounds(tournoi,doc) ;
						
		//--------- ferme le doc 
		doc.close();
	}
	
	// ajout du tableau des rounds
	public static void addAllRounds(Tournoi tournoi, Document doc)
	{
		// recup la liste des rounds
		ArrayList<Round> listeRound = tournoi.getRounds();
		int nbRound = listeRound.size() ;
		
		// ------- Tableau global -----------
		PdfPTable tab = new PdfPTable(nbRound+2) ;
		
		// ---------- Ajout des légendes -----------
		// ajoute la légende des tables
		PdfPCell cell = new PdfPCell(new Phrase("Table")) ;
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tab.addCell(cell);
		// ajoute la légende eleves
		cell = new PdfPCell(new Phrase("Elève")) ;
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tab.addCell(cell);
			
		// ajout des numeros de rounds
		for (int i=1;i<nbRound+1;i++)
		{
			String nb = Integer.toString(i);
			cell = new PdfPCell(new Phrase("Round " + nb));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setRowspan(1);
			tab.addCell(cell);
		}
				
		// ajout de la case legende adversaire
		cell = new PdfPCell(new Phrase("Adversaire"));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setColspan(nbRound);
		cell.setRowspan(1);
		tab.addCell(cell);
		
		
		// ---------- Tableau des matches
		
		// parcours tous les eleves statiques
		Iterator<Eleve> itE = tournoi.getGroupeCouleur1().iterator() ;
		while(itE.hasNext())
		{
			boolean tableOk = false ;
			Eleve e = itE.next() ;
			
			// parcours tous les rounds
			Iterator<Round> itR = tournoi.getRounds().iterator();
			while(itR.hasNext())
			{
				Round round = itR.next() ;			
				Iterator<Match> itM = round.getMatches().iterator() ;
				while(itM.hasNext())
				{
					Match m = itM.next() ;
					if(m.getEleve1().getId()==e.getId()) 
					{
						Eleve adv = m.getEleve2() ;
						tableOk = addAdversaire(tournoi,m,e,adv,tab,tableOk) ;
					
					}
					else if (m.getEleve2().getId()==e.getId())
					{
						Eleve adv = m.getEleve1() ;
						tableOk = addAdversaire(tournoi,m,e,adv,tab,tableOk) ;
					}	
				}
			}		
		}
		try
		{
			doc.add(tab) ;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static boolean addAdversaire(Tournoi tournoi, Match m, Eleve e, Eleve adv, PdfPTable tab, boolean tableOk)
	{
		// on ajoute la table et l'eleve statique si ce n'est pas fait
		if(!tableOk)
		{
			// table
			String tabl = Integer.toString(m.getTable()) ;
			PdfPCell cell = new PdfPCell(new Phrase(tabl)) ;
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tab.addCell(cell);
			// eleve statique
			String id = Integer.toString(e.getId());
			BaseColor coul = tournoi.getClasseFromId(e.getClasse()).getCouleur() ;
			cell = new PdfPCell(new Phrase(id));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBackgroundColor(coul);
			tab.addCell(cell);
			tableOk = true ;
		}
		
		// on ajoute l'adversaire
		String advers = Integer.toString(adv.getId());
		PdfPCell cell = new PdfPCell(new Phrase(advers)) ;
		BaseColor coul = tournoi.getClasseFromId(adv.getClasse()).getCouleur() ;
		cell.setBackgroundColor(coul);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tab.addCell(cell);
		
		return tableOk ;
	}
	
	
	
	// --------- Doc 5 : Fiches eleves -------------------------
	// creation du document pdf du tableau récap des matches pour chaque ronde
	public static void createDocFicheEleve(Tournoi tournoi) throws DocumentException, IOException
	{
		// --------- déclarations
		// Creation du document
		Document doc = new Document();
		String chemin = outputDir + "FichesEleves.pdf" ;
		FileOutputStream output = new FileOutputStream(chemin) ;
						
		// -------- ouvre le doc
		PdfWriter wr = PdfWriter.getInstance(doc, output) ;
		doc.open();
					
		// --------- ajout de contenu
		addAllTabEleves(tournoi,doc) ;
						
		//--------- ferme le doc 
		doc.close();
	}
	
	// ajoute tous les tableaux d'eleves
	public static void addAllTabEleves(Tournoi tournoi, Document doc)
	{
		// recup la liste des classes
		ArrayList<Classe> listeclasse = tournoi.getClasses();
		
		// parcours et ajoute chaque classe
		Iterator<Classe> it = listeclasse.iterator() ;
		while(it.hasNext())
		{
			// recup l'objet classe
			Classe classe = it.next() ;			
			// ajoute le tableau de classe
			addTabElevesClasse(classe,tournoi,doc) ;	
			doc.newPage() ;
	
		}
		
	}
	
	// ajoute les tableaux eleves d'une classe
	public static void addTabElevesClasse(Classe classe,Tournoi tournoi, Document doc)
	{
		// recup la liste des eleves
		ArrayList<Eleve> listeEleves = classe.getListe() ;
		
		// compteur pour saut de page
		int compt=0;
				
		// parcours des eleves
		Iterator<Eleve> it = listeEleves.iterator() ;
		while(it.hasNext())
		{
			// ------ Tableau global -------
			PdfPTable tab = new PdfPTable(4) ;
			
			// recup l'eleve
			Eleve eleve = it.next();
			// est il statique ? si oui il commence blanc
			boolean blanc = tournoi.dansGroupCoul1(eleve) ;
			
			// ------------ Tableau 1 : Info Eleve
			 PdfPTable tabEleve = new PdfPTable(1);
			 String nb = Integer.toString(eleve.getId()) ;
			 PdfPCell cell = new PdfPCell(new Phrase(nb)) ;
			 cell.setBackgroundColor(classe.getCouleur());
			 cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			 cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			 tabEleve.addCell(cell);
			 cell = new PdfPCell(new Phrase(eleve.getNom())) ;
			 cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			 cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			 tabEleve.addCell(cell);
			 
			 // ---------- Tableau 2 : Legende
			 PdfPTable tabLegende = new PdfPTable(1);
			 cell = new PdfPCell(new Phrase("Round")) ;
			 cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			 cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			 tabLegende.addCell(cell);
			 cell = new PdfPCell(new Phrase("Table")) ;
			 cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			 cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			 tabLegende.addCell(cell);
			 cell = new PdfPCell(new Phrase("Couleur")) ;
			 cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			 cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			 tabLegende.addCell(cell);
			 cell = new PdfPCell(new Phrase("Resultat")) ;
			 cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			 cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			 tabLegende.addCell(cell);
			 
			 // --------- Tableau 3 : Rounds
			 PdfPTable tabRound = new PdfPTable(tournoi.getRounds().size()) ;
			 // parcours la liste des rounds
			 Iterator<Round> itR = tournoi.getRounds().iterator() ;
			 while(itR.hasNext())
			 {
				 Round round = itR.next() ;
				 addTabRound(round,eleve,tabRound,blanc) ;
				 blanc = !blanc ;
			 }
			 
			 // --------- Tableau 4 : Total
			 PdfPTable tabTotal = new PdfPTable(1) ;
			 // ajoute une case vide
			 cell = new PdfPCell(new Phrase(" ")) ;
			 tabTotal.addCell(cell);
			 tabTotal.addCell(cell);

			 // ajoute case total
			 cell = new PdfPCell(new Phrase("Total"));
			 cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			 cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			 tabTotal.addCell(cell);
			 // ajoute case vide
			 cell = new PdfPCell(new Phrase(" ")) ;
			 tabTotal.addCell(cell);
			 
			 // ------------- Ajoute tous les tableaux
			 cell = new PdfPCell(tabEleve) ;
			 tab.addCell(cell);
			 cell = new PdfPCell(tabLegende) ;
			 tab.addCell(cell);
			 cell = new PdfPCell(tabRound) ;
			 tab.addCell(cell);
			 cell = new PdfPCell(tabTotal) ;
			 tab.addCell(cell);
			 
			// ajoute un espace
			Paragraph pVide = new Paragraph("  ") ;
			 try
			 {
				 doc.add(tab) ;
				 doc.add(pVide) ;
				 compt++;
				 if(compt==9)
				 {
					 compt=0;
					 doc.newPage();
				 }
			 }
			 catch(Exception e)
			 {
				 e.printStackTrace();
			 }
		}
	}
	
	// ajoute un tableau d'un round avec la table et la couleur
	public static void addTabRound(Round round, Eleve eleve, PdfPTable tab, boolean blanc)
	{
		// -- creation du tableau
		PdfPTable tabRound = new PdfPTable(1) ;
		
		// -- ajout numero round
		String nb = Integer.toString(round.getId()) ;
		PdfPCell cell = new PdfPCell(new Phrase(nb)) ;
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tabRound.addCell(cell);
		
		// -- ajout numero table
		// parcours le round pour trouver le match
		Iterator<Match> it = round.getMatches().iterator() ;
		while(it.hasNext())
		{
			// recup match
			Match match = it.next() ;
			// regarde si c'est le bon
			if(match.getEleve1().equals(eleve) || match.getEleve2().equals(eleve))
			{
				// ajoute la table
				nb = Integer.toString(match.getTable()) ;
				cell = new PdfPCell(new Phrase(nb)) ;
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				tabRound.addCell(cell);
			}
		}
		
		// -- ajout case couleur
		// on regarde si il est blanc
		cell = new PdfPCell(new Phrase(" ")) ;
		if(!blanc)
		{
			cell.setBackgroundColor(BaseColor.BLACK);
		}
		tabRound.addCell(cell);
		// -- ajout case vide pour résultat
		tabRound.addCell(new Phrase(" "));		
		
		// ajout au tableau
		cell = new PdfPCell(tabRound) ;
		tab.addCell(cell);
	}
	
	
	

	
}
