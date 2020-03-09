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
package com.diccionarioderimas.updater;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFrame;

import com.diccionarioderimas.UserPreferences;
import com.diccionarioderimas.Utils;
import com.diccionarioderimas.gui.dialogs.AboutDialog;

public class Updater implements Runnable {

	private UserPreferences preferences;
	private String basePath;
	private JFrame frame;

	public Updater(UserPreferences preferences, JFrame frame) {
		this.preferences = preferences;
		basePath = preferences.getBasePath();
		this.frame = frame;
		new Thread(this).start();
	}

	public void run() {

		try {
			StringBuffer params = new StringBuffer("?id=").append(
					URLEncoder.encode(preferences.getUniqueId() + "", "UTF-8"))
					.append("&lastUpdate=").append(
							URLEncoder.encode(preferences.getLastUpdated()
									.getTime()
									+ "", "UTF-8")).append("&version=").append(
							URLEncoder.encode(AboutDialog.VERSION, "UTF-8"))
					.append("&build=").append(
							URLEncoder.encode(AboutDialog.DATE, "UTF-8"));

			URL url = new URL(
					"http://www.cronopista.com/diccionario2/checkUpdates.php"
							+ params.toString());
			URLConnection connection = url.openConnection();
			Utils.setupProxy(connection);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			Vector<Update> updates = new Vector<Update>();
			Update dictionaryUpdate = null;
			Update generatorUpdate = null;
			Update resbcUpdate = null;
			Update helpUpdate = null;
			String line;
			while ((line = in.readLine()) != null) {
				if (line.length() > 0) {
					Update update = new Update(line);
					switch (update.getType()) {
					case Update.DICTIONARY:
						dictionaryUpdate = update;
						break;
					case Update.GENERATOR:
						generatorUpdate = update;
						break;
					case Update.RESBC:
						resbcUpdate = update;
						break;
					case Update.HELP:
						helpUpdate = update;
						break;
					default:
						updates.add(update);
						break;
					}
				}

			}
			in.close();

			if (dictionaryUpdate != null || generatorUpdate != null
					|| resbcUpdate != null || helpUpdate!=null) {
				new UpdateGUI(frame, basePath, dictionaryUpdate,
						generatorUpdate, resbcUpdate,helpUpdate);
			}

			for (Iterator iterator = updates.iterator(); iterator.hasNext();) {
				Update update = (Update) iterator.next();
				if (update.getType() == Update.ENGINE) {
					if (update.getParam1AsNumber() == Update.INSTALL) {
						try {
							FileWriter out = new FileWriter(basePath
									+ "searchList.txt", true);
							out.append('\n').append(update.getAction());
							out.close();
						} catch (Exception e) {
						}
					} else {
						try {
							StringBuffer newList = new StringBuffer();
							BufferedReader input = new BufferedReader(
									new FileReader(basePath + "searchList.txt"));
							String nline = null;
							while ((nline = input.readLine()) != null) {
								if (nline.length() > 0
										&& !nline.equals(update.getAction()))
									newList.append(nline).append('\n');
							}
							input.close();
							FileWriter out = new FileWriter(basePath
									+ "searchList.txt");
							out.write(newList.toString());
							out.close();
						} catch (Exception e) {
						}

					}
				} else if (update.getType() == Update.WORD) {
					if (update.getParam1AsNumber() == Update.INSTALL) {
						try {
							FileWriter out = new FileWriter(basePath
									+ update.getAction(), true);
							out.append(update.getParam2()).append('\n');
							out.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						try {
							StringBuffer newList = new StringBuffer();
							BufferedReader input = new BufferedReader(
									new FileReader(basePath
											+ update.getAction()));
							String nline = null;
							while ((nline = input.readLine()) != null) {
								if (nline.length() > 0) {
									String[] s = nline.split("\t");
									if (!s[0].equals(update.getParam2()))
										newList.append(nline).append('\n');
								}
							}
							input.close();
							FileWriter out = new FileWriter(basePath
									+ update.getAction());
							out.write(newList.toString());
							out.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		preferences.setLastUpdated(GregorianCalendar.getInstance().getTime());
	}

}
