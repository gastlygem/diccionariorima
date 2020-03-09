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

public class RhymeWordHandler implements WordHandler {

	private int count;
	private int fileWriteCount=0;
	private long savingTime=0;
	
	private Hashtable<String, StringBuffer> entries = null;

	private String basePath;

	public RhymeWordHandler(String path) {
		basePath = path;
	}


	public void wordProduced(String oldWord, String newWord, String type) {
		wordProduced(oldWord, newWord, type, 0);

	}

	public void wordProduced(String oldWord, String newWord, String type,
			int category) {
		
		
		
		if(category==3)
			category=2;
		
		RhymeFinder rhyme = new RhymeFinder(newWord);
		
		if(rhyme.getAsonance().isEmpty())
			return;
		
		if (entries == null)
			entries = new Hashtable<String, StringBuffer>();

		String filename=new StringBuffer(basePath).append("DDBB/").append(category)
			.append("_")
			.append(rhyme.getAsonance()).append(".txt")
			.toString();
		StringBuffer cb = entries.get(filename);
		if (cb == null) {
			cb = new StringBuffer();
			entries.put(filename, cb);
		}

		String VoH="0";
		if(rhyme.startsWithVoH())
			VoH="1";
		cb.append(newWord).append('\t').append(oldWord).append('\t').append(
				type).append('\t').append(rhyme.getRhyme()).append('\t')
				.append(rhyme.countSil()).append('\t').append(
						VoH).append('\t').append(rhyme.getNormalized()).append('\n');

		count++;

		if(count%23400==0)
			Main.event(10, "<html><b>Generada palabra: </b>"+newWord+"</html>");
		
		if (count % 350000 == 0) {
			Main.event(0, "<html><b>Guardando en archivos...</b></html>");
			save();
			
		}

	}

	public void save() {
		long started=System.currentTimeMillis();
		Enumeration<String> keys = entries.keys();
		
		while (keys.hasMoreElements()) {
			String file = (String) keys.nextElement();
			String dirs=file.substring(0,file.lastIndexOf('/'));
			if(!new File(dirs).exists()){
				new File(dirs).mkdirs();
			}
			try {
				FileWriter out = new FileWriter(file, true);
				out.append(entries.get(file).toString());
				out.close();
			} catch (Exception e) {

				e.printStackTrace();
			}
			
			Main.event(1, "<html><b>Guardando en archivos...</b></html>");
			fileWriteCount++;
		}

		entries = null;
		
		savingTime+=System.currentTimeMillis()-started;
	}
	
	public int getCount() {
		System.out.println("saving:"+savingTime);
		System.out.println("fwc:"+fileWriteCount);
		return count;
	}

}
