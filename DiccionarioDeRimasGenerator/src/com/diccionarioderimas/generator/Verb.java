/***************************************************************************
 *   Copyright (C) 2009 by Eduardo Rodriguez Lorenzo                       *
 *   edu.tabula@gmail.com                                                  *
 *   http://www.cronopista.com                                             *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/
package com.diccionarioderimas.generator;
/**
 * Hay muchas concatenaciones de cadenas con +, pero StringBuilder no ganaba tiempo
 * y hacía el código menos legible.
 * 
 * voseo
 * qué hacer con todo lo que no se está conjugando
 * 
 * @author Eduardo Rodríguez
 *
 */
public class Verb extends Word {

	
	public static final int FIRST = 0;
	public static final int SECOND = 1;
	public static final int THIRD = 2;

	private String root;
	private String root2;
	private String root3;
	private String ar;
	private int conj = 0;

	public Verb(String or, String flags, WordHandler wh) {
		super(or, flags, wh);
		ar = "";
		char m = getChar(or, -2);
		switch (m) {
		case 'a':
			conj = FIRST;
			ar = "a";
			break;
		case 'e':
		case 'é':
			conj = SECOND;
			ar = "e";
			break;
		case 'i':
		case 'í':
			conj = THIRD;
			ar = "i";
			break;
		default:
			throw new RuntimeException(or + " doesn't seem to be an infinitive");

		}

		ar += "r";
		root = cutEnd(or, -2);
		root2 = root3 = root;
		if (hasFlag("eqc"))
			root2 = substituteLast(root2, 'e', "ie");
		if (hasFlag("qc"))
			root3 = substituteLast(root3, 'e', "i");
		if (hasFlag('r'))
			root2 = substituteLast(root2, 'i', "ie");
		if (hasFlag("on"))
			root2 = substituteLast(root2, 'o', "ue");
		if (hasFlag('n'))
			root3 = substituteLast(root3, 'o', "u");
		if (hasFlag('i')) {
			root2 = substituteLast(root2, 'e', "i");
			root3 = substituteLast(root3, 'e', "i");
		}
		if (hasFlag('h')) {
			root2 = substituteLast(root2, 'e', "í");
			root3 = substituteLast(root3, 'e', "i");
		}
		if (hasFlag('y')) {
			root2 = substituteFinal(root2, 'ü', "u");
			root2 += "y";
			root3 = substituteFinal(root3, 'ü', "u");
			root3 += "y";
		}
		//System.out.println(root+" "+root2+" "+root3);
		

	}

	public void conjugate() {
		// -------------------------------------PRESENTE DE
		// INDICATIVO------------------------------------
		// YO
		
		String result = root2 + "o";
		if (!hasFlag("bcfx") ) {
			if (hasFlag("tu"))
				result = putTilde(result, -2);
			if (conj == SECOND || conj == THIRD)
				result = substituteFinal(result, "guo", "go");
			if (hasFlag('l'))
				result = addTildeToWeak(result);
			if (hasFlag('z'))
				result = substituteFinal(result, "co", "zco");
			
			wh.wordProduced(word, result,"VIP1");
		}
		// TU
		result = root2;
		if (conj == FIRST)
			result += "as";
		else
			result += "es";
		wh.wordProduced(word, result,"VIP2");
		// EL
		result = root2;
		if (conj == FIRST)
			result += "a";
		else
			result += "e";
		wh.wordProduced(word, result,"VIP3");
		// NOSOTROS
		result = root;
		if (conj == FIRST)
			result += "amos";
		else if (conj == SECOND)
			result += "emos";
		else
			result += "imos";
		if (hasFlag('h'))
			result = substituteFinal(result, 'i', "í");
		wh.wordProduced(word, result,"VIP4");
		// VOSOTROS
		result = root;
		if (conj == FIRST)
			result += "áis";
		else if (conj == SECOND)
			result += "éis";
		else
			result += "ís";
		wh.wordProduced(word, result,"VIP5");
		// ELLOS
		result = root2;
		if (conj == FIRST)
			result += "an";
		else
			result += "en";
		wh.wordProduced(word, result,"VIP6");

		// -------------------------------------PRETÉRITO
		// IMPERFECTO------------------------------------
		// YO
		result = root;
		if (conj == FIRST)
			result += "aba";
		else
			result += "ía";
		wh.wordProduced(word, result,"VII1");
		// TU
		result = root;
		if (conj == FIRST)
			result += "abas";
		else
			result += "ías";
		wh.wordProduced(word, result,"VII2");
		// NOSOTROS
		result = root;
		if (conj == FIRST)
			result += "ábamos";
		else
			result += "íamos";
		wh.wordProduced(word, result,"VII4");
		// VOSOTROS
		result = root;
		if (conj == FIRST)
			result += "abais";
		else
			result += "íais";
		wh.wordProduced(word, result,"VII5");
		// ELLOS
		result = root;
		if (conj == FIRST)
			result += "aban";
		else
			result += "ían";
		wh.wordProduced(word, result,"VII6");

		//-------------------------------------FUTURO----------------------------
		// --------
		// pasando de los irregulares
		if (!hasFlag('c')) {
			// YO
			result = root + ar + "é";
			wh.wordProduced(word, result,"VIF1");
			// TÚ
			result = root + ar + "ás";
			wh.wordProduced(word, result,"VIF2");
			// ÉL
			result = root + ar + "á";
			wh.wordProduced(word, result,"VIF3");
			// NOSOTROS
			result = root + ar + "emos";
			wh.wordProduced(word, result,"VIF4");
			// VOSOTROS
			result = root + ar + "éis";
			wh.wordProduced(word, result,"VIF5");
			// ELLOS
			result = root + ar + "án";
			wh.wordProduced(word, result,"VIF6");

			//-------------------------------------CONDICIONAL------------------
			// ------------------
			// YO
			result = root + ar + "ía";
			wh.wordProduced(word, result,"VIC1");
			// TÚ
			result = root + ar + "ías";
			wh.wordProduced(word, result,"VIC2");
			// ÉL
			result = root + ar + "ía";
			wh.wordProduced(word, result,"VIC3");
			// NOSOTROS
			result = root + ar + "íamos";
			wh.wordProduced(word, result,"VIC4");
			// VOSOTROS
			result = root + ar + "íais";
			wh.wordProduced(word, result,"VIC5");
			// ELLOS
			result = root + ar + "ían";
			wh.wordProduced(word, result,"VIC6");

		}

		// -------------------------------------PERFECTO
		// SIMPLE------------------------------------
		if (!hasFlag("bcx") ) {
			// YO
			if (conj == FIRST)
				result = root + "é";
			else
				result = root + "í";

			if (conj == FIRST) {
				char lc = getChar(result, -2);
				if (lc == 'c')
					result = substituteLast(result, 'c', "qu");
				else if (lc == 'z')
					result = substituteLast(result, 'z', "c");
			}
			if(hasFlag('k'))
				result = substituteFinal(result, "gé", "gué");
			
			wh.wordProduced(word, result,"VIX1");
			// TU
			if (conj == FIRST)
				result = root + "aste";
			else {
				char lc = getChar(result, -5);
				if (lc == 'a' || lc == 'e')
					result = root + "íste";
				else
					result = root + "iste";
			}
			wh.wordProduced(word, result,"VIX2");
			// ÉL
			if (conj == FIRST || hasFlag("ym") || getChar(root3, -1) == 'i')
				result = root3 + "ó";
			else if (hasFlag("d"))
				result = root3 + "yó";
			else
				result = root3 + "ió";

			wh.wordProduced(word, result,"VIX3");
			// NOSOTROS
			char lc = getChar(root, -1);
			if (conj != FIRST && (lc == 'a' || lc == 'e')) {
				result = root + "imos";
				wh.wordProduced(word, result,"VIX4");
			}
			
			// VOSOTROS
			if (conj == FIRST)
				result = root + "asteis";
			else if (lc == 'a' || lc == 'e')
				result = root + "ísteis";
			else
				result = root + "isteis";
			wh.wordProduced(word, result,"VIX5");
			// ELLOS
			lc = getChar(root3, -1);
			if (conj == FIRST)
				result = root3 + "aron";
			else if (hasFlag("ym") || lc == 'i')
				result = root3 + "eron";
			else if (hasFlag("d"))
				result = root3 + "yeron";
			else
				result = root3 + "ieron";
			wh.wordProduced(word, result,"VIX6");

		}
		// -------------------------------------PRESENTE DE
		// SUBJUNTIVO------------------------------------
		if(!hasFlag("fcx")){
			
			//YO
			if(conj==FIRST)
				result=root2+"e";
			else
				result=root2+"a";
			
			if(hasFlag('z'))
				result=substituteLast(result, 'c', "zc");	//parecer, parezca
			if(hasFlag('k'))
				result=substituteLast(result, 'u', "ü");	//aguar, agüe
			if (conj == SECOND || conj == THIRD)
				result = substituteFinal(result, "gua", "ga");	//seguir, siga
			else{
				result = substituteFinal(result, "ge", "gue");	//pagar, pague
				result = substituteFinal(result, "ce", "que");	//aparcar, aparque
				result = substituteFinal(result, "ze", "ce");	//rezar, rece
			}
			if (hasFlag("tu"))
				result = putTilde(result, -2);
			if (hasFlag('l'))
				result = addTildeToWeak(result);
			wh.wordProduced(word, result,"VSP1");
			//TU
			if(conj==FIRST)
				result=root2+"es";
			else
				result=root2+"as";
			
			if(hasFlag('z'))
				result=substituteLast(result, 'c', "zc");	//parecer, parezca
			if(hasFlag('k'))
				result=substituteLast(result, 'u', "ü");	//aguar, agüe
			if (conj == SECOND || conj == THIRD)
				result = substituteFinal(result, "guas", "gas");	//seguir, siga
			else{
				result = substituteFinal(result, "ges", "gues");	//pagar, pague
				result = substituteFinal(result, "ces", "ques");	//aparcar, aparque
				result = substituteFinal(result, "zes", "ces");	//rezar, rece
			}
			if (hasFlag("tu"))
				result = putTilde(result, -3);
			if (hasFlag('l'))
				result = addTildeToWeak(result);
			wh.wordProduced(word, result,"VSP2");
			//NOSOTROS
			if(conj==FIRST)
				result=root3+"emos";
			else
				result=root3+"amos";
			
			if(hasFlag('z'))
				result=substituteLast(result, 'c', "zc");	//parecer, parezca
			if(hasFlag('k'))
				result=substituteLast(result, 'u', "ü");	//aguar, agüe
			if (conj == SECOND || conj == THIRD)
				result = substituteFinal(result, "guamos", "gamos");	//seguir, siga
			else{
				result = substituteFinal(result, "gemos", "guemos");	//pagar, pague
				result = substituteFinal(result, "cemos", "quemos");	//aparcar, aparque
				result = substituteFinal(result, "zemos", "cemos");	//rezar, rece
			}
			
			wh.wordProduced(word, result,"VSP4");
			//VOSOTROS
			if(conj==FIRST)
				result=root3+"éis";
			else
				result=root3+"áis";
			
			if(hasFlag('z'))
				result=substituteLast(result, 'c', "zc");	//parecer, parezca
			if(hasFlag('k'))
				result=substituteLast(result, 'u', "ü");	//aguar, agüe
			if (conj == SECOND || conj == THIRD)
				result = substituteFinal(result, "guáis", "gáis");	//seguir, siga
			else{
				result = substituteFinal(result, "géis", "guéis");	//pagar, pague
				result = substituteFinal(result, "céis", "quéis");	//aparcar, aparque
				result = substituteFinal(result, "zéis", "céis");	//rezar, rece
			}
			wh.wordProduced(word, result,"VSP5");
			//ELLOS
			if(conj==FIRST)
				result=root2+"en";
			else
				result=root2+"an";
			
			if(hasFlag('z'))
				result=substituteLast(result, 'c', "zc");	//parecer, parezca
			if(hasFlag('k'))
				result=substituteLast(result, 'u', "ü");	//aguar, agüe
			if (conj == SECOND || conj == THIRD)
				result = substituteFinal(result, "guan", "gan");	//seguir, siga
			else{
				result = substituteFinal(result, "gen", "guen");	//pagar, pague
				result = substituteFinal(result, "cen", "quen");	//aparcar, aparque
				result = substituteFinal(result, "zen", "cen");	//rezar, rece
			}
			if (hasFlag("tu"))
				result = putTilde(result, -3);
			if (hasFlag('l'))
				result = addTildeToWeak(result);
			wh.wordProduced(word, result,"VSP6");
			
			
		}
		
		
		 //---------------------PRETERITO DE SUBJUNTIVO(1)
		if(!hasFlag("bcx")){
			String nroot=null;
			if(conj==FIRST)
				nroot=root3+"ar";
			else
				nroot=root3+"ier";
			nroot = substituteFinal(nroot,"yier", "yer");
			nroot = substituteFinal(nroot,"iier", "ier");
			nroot = substituteFinal(nroot,"eier", "eyer");
			if(hasFlag('m'))
				nroot = substituteFinal(nroot,"ier", "er");
			
			//YO
			result=nroot+"a";
			wh.wordProduced(word, result,"VSX1");
			
			//TU
			result=nroot+"as";
			wh.wordProduced(word, result,"VSX2");
			//NOSOTROS
			result=nroot+"amos";
			
			result=putTilde(result, -6);
			wh.wordProduced(word, result,"VSX4");
			
			//VOSOTROS
			result=nroot+"ais";
			wh.wordProduced(word, result,"VSX5");
			
			//ELLOS
			result=nroot+"an";
			wh.wordProduced(word, result,"VSX6");
		
			
			
			//---------------------FUTURO DE SUBJUNTIVO
			
			//YO
			result=nroot+"e";
			wh.wordProduced(word, result,"VSF1");
			
			//TU
			result=nroot+"es";
			wh.wordProduced(word, result,"VSF2");
			//NOSOTROS
			result=nroot+"emos";
			
			result=putTilde(result, -6);
			wh.wordProduced(word, result,"VSF4");
			
			//VOSOTROS
			result=nroot+"eis";
			wh.wordProduced(word, result,"VSF5");
			
			//ELLOS
			result=nroot+"en";
			wh.wordProduced(word, result,"VSF6");
			
			
			
			
			//---------------------PRETERITO DE SUBJUNTIVO(2)
			
			
			if(conj==FIRST)
				nroot=root3+"as";
			else
				nroot=root3+"ies";
			nroot = substituteFinal(nroot,"yies", "yes");
			nroot = substituteFinal(nroot,"iies", "ies");
			nroot = substituteFinal(nroot,"eies", "eyes");
			if(hasFlag('m'))
				nroot = substituteFinal(nroot,"ies", "es");
			
			//YO
			result=nroot+"e";
			wh.wordProduced(word, result,"VSI1");
			
			//TU
			result=nroot+"es";
			wh.wordProduced(word, result,"VSI2");
			//NOSOTROS
			result=nroot+"emos";
			
			result=putTilde(result, -6);
			wh.wordProduced(word, result,"VSI4");
			
			//VOSOTROS
			result=nroot+"eis";
			wh.wordProduced(word, result,"VSI5");
			
			//ELLOS
			result=nroot+"en";
			wh.wordProduced(word, result,"VSI6");
			
		}
		
		//--------------------- IMPERATIVO PLURAL
		if(conj==FIRST)
			result=root+"ad";
		else if(conj==SECOND)
			result=root+"ed";
		else
			result=root+"id";
		
		wh.wordProduced(word, result,"VMP",2);
		
		//-------------------- OI
		wh.wordProduced(word, result+"me","VMSE",3);
		wh.wordProduced(word, result+"le","VMSE",3);
		wh.wordProduced(word, result+"nos","VMPE",3);
		wh.wordProduced(word, cutEnd(word,-1)+"os","VMPE",3);
		wh.wordProduced(word, result+"les","VMPE",3);
		//-------------------- OD
		if(hasFlag('T')){
			String yroot=putTilde(cutEnd(word,-1),-1);
			wh.wordProduced(word, result+"lo","VMMSE",3);
			wh.wordProduced(word, result+"la","VMFSE",3);
			wh.wordProduced(word, result+"los","VMMPE",3);
			wh.wordProduced(word, result+"las","VMFPE",3);
			String nroot=putTilde(result, -2);
			wh.wordProduced(word, nroot+"melo","VMMSE",3);
			wh.wordProduced(word, nroot+"selo","VMMSE",3);
			wh.wordProduced(word, nroot+"noslo","VMMSE",3);
			wh.wordProduced(word, yroot+"oslo","VMMSE",3);
			
			wh.wordProduced(word, nroot+"melos","VMMPE",3);
			wh.wordProduced(word, nroot+"selos","VMMPE",3);
			wh.wordProduced(word, nroot+"noslos","VMMPE",3);
			wh.wordProduced(word, yroot+"oslos","VMMPE",3);
			
			wh.wordProduced(word, nroot+"mela","VMFSE",3);
			wh.wordProduced(word, nroot+"sela","VMFSE",3);
			wh.wordProduced(word, nroot+"nosla","VMFSE",3);
			wh.wordProduced(word, yroot+"osla","VMFSE",3);
			
			wh.wordProduced(word, nroot+"melas","VMFPE",3);
			wh.wordProduced(word, nroot+"selas","VMFPE",3);
			wh.wordProduced(word, nroot+"noslas","VMFPE",3);
			wh.wordProduced(word, yroot+"oslas","VMFPE",3);
		
			
		}
			
	
		
		if(!hasFlag('p')){
			//--------------------- PARTICIPIOS REGULARES
			String nroot=null;
			if(conj==FIRST)
				nroot=root+"ad";
			else{
				nroot=root+"id";
				nroot = substituteFinal(nroot,"aid", "aíd");
				nroot = substituteFinal(nroot,"eid", "eíd");
			}
			
			wh.wordProduced(word, nroot+"o","VPM",2);
			wh.wordProduced(word, nroot+"os","VPMP",2);
			wh.wordProduced(word, nroot+"a","VPF",2);
			wh.wordProduced(word, nroot+"as","VPFP",2);
			
			
			
		}else{
			//--------------------- PARTICIPIOS IRREGULARES
			irregularParticiple("[au]brir","rir" ,"iert");
			irregularParticiple("ibir","bir" ,"t");
			irregularParticiple("oner","oner" ,"uest");
			irregularParticiple("romper","mper" ,"t");
			irregularParticiple("ecir","ecir" ,"ich");
			irregularParticiple("[fh]acer","acer" ,"ech");
			irregularParticiple("veer","eer" ,"ist");//desproveer
			irregularParticiple("ver","er" ,"ist");
			irregularParticiple("olver","olver" ,"uelt");
			irregularParticiple("orir","orir" ,"uert");
	
			
		}
		
		
		//--------------------- GERUNDIO
		if(conj==FIRST)
			result=root3+"ando";
		else
			result=root3+"iendo";
		result = substituteFinal(result,"iiendo", "iendo");
		if(hasFlag('m'))
			substituteFinal(result,"iendo", "endo");
		wh.wordProduced(word, result,"VG",2);
		
		//---------------------GERUNDIO OI
		String nroot=putTilde(result, -4);
		wh.wordProduced(word, nroot+"me","VGSE",3);
		wh.wordProduced(word, nroot+"te","VGSE",3);
		wh.wordProduced(word, nroot+"le","VGSE",3);
		wh.wordProduced(word, nroot+"nos","VGPE",3);
		wh.wordProduced(word, nroot+"os","VGPE",3);
		//-------------------- OD
		if(hasFlag('T')){
			wh.wordProduced(word, nroot+"lo","VGMSE",3);
			wh.wordProduced(word, nroot+"la","VGFSE",3);
			wh.wordProduced(word, nroot+"los","VGMPE",3);
			wh.wordProduced(word, nroot+"las","VGFPE",3);
			
			wh.wordProduced(word, nroot+"melo","VGMSE",3);
			wh.wordProduced(word, nroot+"telo","VGMSE",3);
			wh.wordProduced(word, nroot+"selo","VGMSE",3);
			wh.wordProduced(word, nroot+"noslo","VGMSE",3);
			wh.wordProduced(word, nroot+"oslo","VGMSE",3);
			
			wh.wordProduced(word, nroot+"melos","VGMPE",3);
			wh.wordProduced(word, nroot+"telos","VGMPE",3);
			wh.wordProduced(word, nroot+"selos","VGMPE",3);
			wh.wordProduced(word, nroot+"noslos","VGMPE",3);
			wh.wordProduced(word, nroot+"oslos","VGMPE",3);
			
			wh.wordProduced(word, nroot+"mela","VGFSE",3);
			wh.wordProduced(word, nroot+"tela","VGFSE",3);
			wh.wordProduced(word, nroot+"sela","VGFSE",3);
			wh.wordProduced(word, nroot+"nosla","VGFSE",3);
			wh.wordProduced(word, nroot+"osla","VGFSE",3);
			
			wh.wordProduced(word, nroot+"melas","VGFPE",3);
			wh.wordProduced(word, nroot+"telas","VGFPE",3);
			wh.wordProduced(word, nroot+"selas","VGFPE",3);
			wh.wordProduced(word, nroot+"noslas","VGFPE",3);
			wh.wordProduced(word, nroot+"oslas","VGFPE",3);
		
			
		}
		
	}
	
	private void irregularParticiple(String ending, String last, String ne){
		if(checkEnding(ending)){
			String nroot=substituteFinal(word,last ,ne);
			wh.wordProduced(word, nroot+"o","VPMS",2);
			wh.wordProduced(word, nroot+"os","VPMP",2);
			wh.wordProduced(word, nroot+"a","VPFS",2);
			wh.wordProduced(word, nroot+"as","VFP",2);
		}
	}


	public static void main(String[] args) {
		//regulares
		Verb m = new Verb("cantar", "T", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		m = new Verb("comer", "T", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		m = new Verb("vivir", "I", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//f primera persona irregular    
		m = new Verb("abstraer", "TIfb", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//p participio irregular   
		m = new Verb("romper", "Tp", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//t como enviar envío  
		m = new Verb("enviar", "TtR", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//k como averiguar averigüe    
		m = new Verb("averiguar", "TIk", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//r como inquirir inquiero 
		m = new Verb("inquirir", "Tr", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//y como incluir incluyo 
		m = new Verb("incluir", "Ty", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//l como aislar aíslo, ahuchar ahúcho  
		m = new Verb("ahuchar", "Tl", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//c como venir, vengo viene vine   
		m = new Verb("venir", "IcR", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//d como leer
		m = new Verb("leer", "Td", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//h como sonreír 
		m = new Verb("sonreír", "Ih", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//e como entender
		m = new Verb("entender", "TeR", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//q como sentir 
		m = new Verb("sentir", "Iq", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//o como contar   
		m = new Verb("contar", "TIoR", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//n como dormir 
		m = new Verb("dormir", "TINn", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//i como pedir 
		m = new Verb("pedir", "Ti", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//u como actuar actúo 
		m = new Verb("actuar", "TIuR", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//z como agradecer  
		m = new Verb("agradecer", "TzR", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//m como mullir
		m = new Verb("mullir", "Tm", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//pagar pague
		m = new Verb("pagar", "TkR", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//aparcar aparque
		m = new Verb("aparcar", "T", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		//rezar rece
		m = new Verb("rezar", "TI", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		
		m = new Verb("atraer", "Tfb", new FakeWordHandler());
		m.conjugate();
		System.out.println("*****************");
		
		
	}

}
