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

public class FrequencyWordHandler implements WordHandler{

	
	private String basePath;
	private int count;
	private Hashtable<String, StringBuffer> entries = null;
	
	public FrequencyWordHandler(String basePath) {
		this.basePath=basePath;
	}
	
	


	


	public void wordProduced(String oldWord, String newWord, String type) {
		wordProduced(oldWord, newWord, type, 0);

	}

	public void wordProduced(String oldWord, String newWord, String type,
			int category) {

		
		
		if (entries == null)
			entries = new Hashtable<String, StringBuffer>();
		String key=null;
		if(newWord.length()>1)
			key=newWord.substring(0,2);
		else
			key=newWord.substring(0,1);
		StringBuffer cb = entries.get(key);
		if (cb == null) {
			cb = new StringBuffer();
			entries.put(key, cb);
		}

		cb.append(newWord).append('\t').append(oldWord).append('\t').append(
				type).append('\n');

		count++;

		if (count % 350000 == 0) {
			save();
		}
		
		
	}

	

	public void save() {

		Enumeration<String> keys = entries.keys();
		if (!new File(basePath+"FDDBB").exists())
			new File(basePath+"FDDBB").mkdir();
		while (keys.hasMoreElements()) {
			String file = (String) keys.nextElement();
			try {
				FileWriter out = new FileWriter(basePath+"FDDBB/" + file + ".txt", true);
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
