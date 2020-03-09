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

import java.util.Hashtable;

public class Word {

	public static Hashtable<Character, Character> tildes = new Hashtable<Character, Character>();
	public static Hashtable<Character, Character> noTildes = new Hashtable<Character, Character>();
	public static Hashtable<Character, Character> vowels = new Hashtable<Character, Character>();

	static {
		tildes.put(new Character('a'), new Character('á'));
		tildes.put(new Character('e'), new Character('é'));
		tildes.put(new Character('i'), new Character('í'));
		tildes.put(new Character('o'), new Character('ó'));
		tildes.put(new Character('u'), new Character('ú'));

		noTildes.put(new Character('á'), new Character('a'));
		noTildes.put(new Character('é'), new Character('e'));
		noTildes.put(new Character('í'), new Character('i'));
		noTildes.put(new Character('ó'), new Character('o'));
		noTildes.put(new Character('ú'), new Character('u'));

		vowels.put(new Character('a'), new Character('á'));
		vowels.put(new Character('e'), new Character('é'));
		vowels.put(new Character('i'), new Character('í'));
		vowels.put(new Character('o'), new Character('ó'));
		vowels.put(new Character('u'), new Character('ú'));
		vowels.put(new Character('á'), new Character('á'));
		vowels.put(new Character('é'), new Character('é'));
		vowels.put(new Character('í'), new Character('í'));
		vowels.put(new Character('ó'), new Character('ó'));
		vowels.put(new Character('ú'), new Character('ú'));
		vowels.put(new Character('ü'), new Character('ú'));

	}

	protected String word;

	protected String flags;

	protected String result;

	protected int orLength;

	protected WordHandler wh;

	public Word(String word, String flags, WordHandler wh) {
		super();
		this.wh = wh;
		orLength = word.length();
		this.word = word;
		this.flags = flags;

	}

	public void reset() {
		result = "";
	}

	public boolean hasFlag(char c) {
		return flags.indexOf(c) != -1;
	}

	public boolean hasFlag(String c) {
		char[] ch = c.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			if (flags.indexOf(ch[i]) != -1)
				return true;
		}
		return false;
	}

	public static String substituteLast(String or, char c, String in) {
		String res = or;
		char[] chars = or.toCharArray();
		int pl = -1;
		for (int i = chars.length - 1; i >= 0; i--) {
			if (chars[i] == c) {
				pl = i;
				i = -1;
			}
		}
		if (pl != -1) {
			StringBuilder resB = new StringBuilder();
			if (pl < chars.length - 1)
				res = resB.append(res.substring(0, pl)).append(in).append(res.substring(pl + 1)).toString();
			else
				res = resB.append(res.substring(0, pl)).append(in).toString();
		}

		return res;
	}

	public static String substituteFinal(String or, char c, String in) {
		String res = or;
		char[] m = or.toCharArray();
		StringBuilder resB = new StringBuilder();
		if (m[m.length - 1] == c)
			res = resB.append(res.substring(0, m.length - 1)).append(in).toString();

		return res;

	}

	public static char getChar(String word, int pos) {
		int wl = word.length();
		if (pos < 0) {
			if (pos + wl >= 0)
				return word.charAt(wl + pos);
		} else if (wl < pos)
			return word.charAt(pos);

		return 0;
	}

	public static String cutEnd(String word, int where) {
		String res = word;
		int ln = res.length();
		if (where < 0) {
			if (ln + where >= 0)
				res = word.substring(0, ln + where);
		} else if (where < ln)
			res = word.substring(0, where);

		return res;
	}

	private static String tilde(String or, int pos, boolean doTake) {
		String res = or;
		char[] m = or.toCharArray();
		int apos = pos;
		if (pos < 0)
			apos = m.length + pos;
		if (apos >= 0 && apos < m.length) {
			Character nt = null;
			if (doTake)
				nt = (Character) noTildes.get(new Character(m[apos]));
			else
				nt = (Character) tildes.get(new Character(m[apos]));
			if (nt != null) {
				m[apos] = nt.charValue();
				res = new String(m);
			}
		}

		return res;
	}

	public static String takeOutTilde(String or, int pos) {
		return tilde(or, pos, true);
	}

	public static String putTilde(String or, int pos) {
		return tilde(or, pos, false);
	}

	public static String substituteFinal(String or, String ac, String ne) {
		int orl = or.length();
		int acl = ac.length();
		StringBuilder resB = new StringBuilder();
		if (acl <= orl && or.lastIndexOf(ac) == orl - acl)
			return resB.append(or.substring(0, or.length() - ac.length())).append(ne).toString();

		return or;
	}

	public static String addTildeToWeak(String or) {
		char[] m = or.toCharArray();
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < m.length; i++) {
			if (m[i] == 'i' || m[i] == 'u') {
				if ((i < m.length - 1 && (m[i + 1] != 'i' && m[i + 1] != 'u' && m[i + 1] != 'ü'))
						|| (i > 0 && (m[i - 1] != 'i' && m[i - 1] != 'u' && m[i - 1] != 'ü')))
					res.append(tildes.get(new Character(m[i])));
				else
					res.append(m[i]);
			} else
				res.append(m[i]);
		}

		return res.toString();
	}

	public static String killAccent(String or) {
		char[] m = or.toCharArray();
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < m.length; i++) {
			Character nc = (Character) noTildes.get(m[i]);
			if (nc != null)
				res.append(nc.charValue());
			else
				res.append(m[i]);
		}

		return res.toString();
	}

	public static boolean hasAccent(String or) {
		char[] m = or.toCharArray();
		for (int i = 0; i < m.length; i++) {
			if (RhymeFinder.isAnAccent(m[i]))
				return true;
		}

		return false;
	}

	public static int getAccent(String or) {
		char[] m = or.toCharArray();
		for (int i = 0; i < m.length; i++) {
			if (RhymeFinder.isAnAccent(m[i]))
				return i;
		}

		return -1;
	}

	public static boolean isVowel(char c) {
		return vowels.get(new Character(c)) != null;
	}
	
	public boolean checkEnding( String ending){
		char[] chars = word.toCharArray();
		char[] endc= ending.toCharArray();
		int count=endc.length-1;
		for (int i = chars.length-1; i > -1; i--) {
			if(endc[count]==']'){
				boolean found=false;
				while(endc[count]!='[' && count>0){
					count--;
					if(endc[count]==chars[i])
						found=true;
				}
				if(!found)
					return false;
					
			}else if(endc[count]!=chars[i])
				return false;
			
			count--;
			if(count<0)
				return true;
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(new Word("retrógado","",null).checkEnding( "retr[mnoó]gado"));
	}

}
