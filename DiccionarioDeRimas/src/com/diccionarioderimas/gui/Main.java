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
package com.diccionarioderimas.gui;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.UIManager;

import com.diccionarioderimas.UserPreferences;
import com.diccionarioderimas.generator.Utils;
import com.diccionarioderimas.updater.Updater;

public class Main {

	public static void main(String[] args) {
		UserPreferences preferences = new UserPreferences(Utils
				.getApplicationPath());
		if(preferences.isProxy()){
			System.getProperties().put( "proxySet", "true" );
			System.getProperties().put( "proxyHost", preferences.getProxyHTTP());
			System.getProperties().put( "proxyPort", preferences.getProxyPort() );
		}
		try {
			UIManager.setLookAndFeel(preferences.getTheme());
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception ex) {
			}
		}
		MainWindow mainWindow = new MainWindow(preferences);
		Calendar next = GregorianCalendar.getInstance();
		next.setTime(preferences.getLastUpdated());
		next.add(Calendar.DATE, 2);
		Calendar now = GregorianCalendar.getInstance();
		if (now.after(next)) {
			new Updater(preferences, mainWindow.getFrame());
			
		}

	}

}
