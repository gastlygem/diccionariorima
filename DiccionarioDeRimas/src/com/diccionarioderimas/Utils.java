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
package com.diccionarioderimas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLConnection;
import java.util.Vector;

import com.diccionarioderimas.generator.ErrorDialog;
import com.diccionarioderimas.gui.MainWindow;
import com.diccionarioderimas.gui.browser.BrowserPane;
import com.diccionarioderimas.gui.browser.SearchEngine;

public class Utils {

	private static Vector<SearchEngine> engines;



	public static Vector<SearchEngine> loadEngines() {
		if (engines != null)
			return engines;
		
		Vector<SearchEngine> result = new Vector<SearchEngine>();
		try {
			if (!new File(MainWindow.getBasePath() + "searchList.txt").exists()) {
				com.diccionarioderimas.generator.Utils.copyFile(BrowserPane.class
						.getResourceAsStream("searchList.txt"), new File(MainWindow
						.getBasePath()
						+ "searchListOr.txt"));
				com.diccionarioderimas.generator.Utils.copyFileToPlatformCharacterEncoding(new File(MainWindow
						.getBasePath()
						+ "searchListOr.txt"), "ISO-8859-1", new File(MainWindow
						.getBasePath()
						+ "searchList.txt"));
				new File(MainWindow
						.getBasePath()
						+ "searchListOr.txt").delete();
			}

		
			BufferedReader in = new BufferedReader(new FileReader(MainWindow
					.getBasePath()
					+ "searchList.txt"));
			String line = "";
			while ((line = in.readLine()) != null) {
				if(line.length()>0){
					String[] parts = line.split("\\|");
					result.add(new SearchEngine(parts[0], parts[1], parts[2]));
				}
			}

			in.close();
		} catch (Exception e) {
			new ErrorDialog(null,e);
		}

		return result;
	}

	
	public static void setupProxy(URLConnection connection){
		if (MainWindow.getUserPreferences().isProxy()) {
			String password = MainWindow.getUserPreferences()
					.getProxyUser()
					+ MainWindow.getUserPreferences()
							.getProxyPassword();
			String encodedPassword = Base64.encodeBytes(password
					.getBytes());
			connection.setRequestProperty("Proxy-Authorization",
					encodedPassword);

		}
	}
	
	
}
