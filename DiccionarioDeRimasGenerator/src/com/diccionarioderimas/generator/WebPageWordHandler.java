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

import java.io.File;
import java.io.FileWriter;
import java.util.Enumeration;
import java.util.Hashtable;

public class WebPageWordHandler implements WordHandler{

	private int count;
	private Hashtable<String, StringBuffer> entries = null;

	private String basePath;

	public WebPageWordHandler(String path) {
		basePath = path;
	}


	public void wordProduced(String oldWord, String newWord, String type) {
		wordProduced(oldWord, newWord, type, 0);

	}

	public void wordProduced(String oldWord, String newWord, String type,
			int category) {

		if(category==3)
			return;
		if(category==1)
			category=0;
		else if(category==0)
			category=1;
		RhymeFinder rhyme = new RhymeFinder(newWord);
		
		if (entries == null)
			entries = new Hashtable<String, StringBuffer>();

		String asonance=rhyme.getNormalizedAsonance();
		StringBuffer cb = entries.get(category + "_" + asonance);
		if (cb == null) {
			cb = new StringBuffer();
			entries.put(category + "_" + asonance, cb);
		}

		cb.append(newWord).append('\t').append(oldWord).append('\t').append(
				type).append('\t').append(rhyme.getRhyme()).append('\t')
				.append(rhyme.countSil()).append('\t').append(
						rhyme.startsWithVoH()).append('\n');

		count++;

		if (count % 350000 == 0) {
			save();
		}

	}

	public void save() {

		Enumeration<String> keys = entries.keys();
		if (!new File(basePath + "WDDBB").exists())
			new File(basePath + "WDDBB").mkdir();
		while (keys.hasMoreElements()) {
			String file = (String) keys.nextElement();
			try {
				FileWriter out = new FileWriter(basePath + "WDDBB/" + file
						+ ".txt", true);
				out.append(entries.get(file).toString());
				out.close();
			} catch (Exception e) {

				e.printStackTrace();
			}

		}

		entries = null;
		System.out.println(count);
	}

	public int getCount() {
		return count;
	}

}
