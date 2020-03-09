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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.diccionarioderimas.UserPreferences;
import com.diccionarioderimas.generator.ErrorDialog;
import com.diccionarioderimas.generator.Main;
import com.diccionarioderimas.gui.browser.BrowserPane;
import com.diccionarioderimas.gui.dialogs.AboutDialog;
import com.diccionarioderimas.gui.dialogs.ContactDialog;
import com.diccionarioderimas.gui.dialogs.FoneticOptions;
import com.diccionarioderimas.gui.dialogs.ProxyDialog;
import com.diccionarioderimas.gui.help.Help;
import com.diccionarioderimas.gui.silables.SilableCounter;

public class MainWindow implements WindowListener, ActionListener {

	private JFrame frame;
	private static String basePath;
	private static MainWindow thisInstance;
	private BrowserPane browserPane;
	private DictionaryPane dictionaryPane;
	private JTabbedPane tabs;
	private Vector<JRadioButtonMenuItem> themes;
	private JMenuItem foneticsMenu,silCountMenu,stayOnTopMenu,helpMenu,aboutMenu,contactMenu,exitMenu,proxyMenu;
	private Vector<String> themeNames;
	private static UserPreferences preferences;
	private FoneticOptions foneticOptions;
	private AboutDialog aboutDialog;
	private ContactDialog contactDialog;
	private SilableCounter silableCounter;
	private JSplitPane splitPane;

	public MainWindow(UserPreferences preferences) {
		MainWindow.preferences=preferences;
		
		thisInstance = this;
		basePath = preferences.getBasePath();

		if (!new File(basePath + "DDBB").exists()) {
			JOptionPane
					.showMessageDialog(
							frame,
							"Es necesario generar la base de datos.\n"
									+ "Esto puede llevar varios segundos, pero sólo hay\n"
									+ "que hacerlo la primera vez que se ejecuta el diccionario de rimas.",
							"Aviso", JOptionPane.INFORMATION_MESSAGE);
			try{
				new Main(null, basePath, false);
			}catch (Exception e) {
				new ErrorDialog(null,e);
				System.exit(0);
			}
		}

		frame = new JFrame("MERimas "+AboutDialog.VERSION);
		foneticOptions=new FoneticOptions(frame,preferences);
		aboutDialog=new AboutDialog(frame);
		contactDialog=new ContactDialog(frame);
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menú");
		
		foneticsMenu=new JMenuItem("Opciones fonéticas...");
		foneticsMenu.addActionListener(this);
		menu.add(foneticsMenu);
		
		
		
		silCountMenu=new JCheckBoxMenuItem("Ver contador de versos");
		silCountMenu.addActionListener(this);
		menu.add(silCountMenu);
		if(preferences.isViewCounter())
			silCountMenu.setSelected(true);
		
		stayOnTopMenu=new JCheckBoxMenuItem("Mantenerse encima de otras ventanas");
		stayOnTopMenu.addActionListener(this);
		menu.add(stayOnTopMenu);
		if(preferences.isStayOnTop())
			stayOnTopMenu.setSelected(true);
		
		proxyMenu=new JMenuItem("Configurar proxy...");
		proxyMenu.addActionListener(this);
		menu.add(proxyMenu);
		
		menu.addSeparator();
		
		helpMenu=new JMenuItem("Ayuda...");
		helpMenu.addActionListener(this);
		menu.add(helpMenu);
		
		aboutMenu=new JMenuItem("Sobre...");
		aboutMenu.addActionListener(this);
		menu.add(aboutMenu);
		
		contactMenu=new JMenuItem("Contacto...");
		contactMenu.addActionListener(this);
		menu.add(contactMenu);
		
		menu.addSeparator();
		
		exitMenu=new JMenuItem("Salir");
		exitMenu.addActionListener(this);
		menu.add(exitMenu);
		
		
		JMenu themeMenu = new JMenu("Temas");
		ButtonGroup themeGroup = new ButtonGroup();

		themes = new Vector<JRadioButtonMenuItem>();
		themeNames = new Vector<String>();
		UIManager.LookAndFeelInfo plaf[] = UIManager.getInstalledLookAndFeels();
		for (int i = 0, n = plaf.length; i < n; i++) {
			JRadioButtonMenuItem ct = new JRadioButtonMenuItem(plaf[i].getName());
			ct.addActionListener(this);
			themeMenu.add(ct);
			themes.add(ct);
			themeGroup.add(ct);
			themeNames.add(plaf[i].getClassName());
			if(plaf[i].getClassName().equals(preferences.getTheme()))
				ct.setSelected(true);
		}

		menuBar.add(menu);
		menuBar.add(themeMenu);

		frame.setJMenuBar(menuBar);
		silableCounter =new SilableCounter();
		
		browserPane = new BrowserPane(frame);
		dictionaryPane = new DictionaryPane();
		tabs = new JTabbedPane();
		tabs
				.addTab("Diccionario", null, dictionaryPane,
						"Diccionario de rimas");

		tabs.addTab("Internet", null, browserPane, "Diccionarios en internet");

		splitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,silableCounter,tabs);
		splitPane.setOneTouchExpandable(true);
		int width=345;
		if(!preferences.isViewCounter()){
			splitPane.setDividerLocation(0);
			silableCounter.setVisible(false);
			splitPane.setDividerSize(0);
		}else{
			splitPane.setDividerLocation(300);
			width+=300;
		}
		
		frame.getContentPane().add(splitPane);
		
		frame.setIconImage(new ImageIcon(this.getClass().getResource(
				"logoSmall.png")).getImage());
		frame.setSize(new Dimension(width, 600));
		frame.setLocation(50, 50);
		frame.setMinimumSize(new Dimension(250, 200));
		frame.addWindowListener(this);
		if(preferences.isStayOnTop())
			frame.setAlwaysOnTop(true);
		frame.setVisible(true);

	}

	public static void search(String word, int engine) {
		thisInstance.tabs.setSelectedComponent(thisInstance.browserPane);
		thisInstance.browserPane.doSearch(word, engine);

	}
	
	public static void searchRhymes() {
		thisInstance.tabs.setSelectedComponent(thisInstance.dictionaryPane);
		thisInstance.dictionaryPane.actionPerformed(null);

	}

	public static String getBasePath() {
		return basePath;
	}
	
	public static UserPreferences getUserPreferences(){
		return preferences;
	}

	
	public void windowActivated(WindowEvent arg0) {
		dictionaryPane.focus();
	}

	public void windowClosed(WindowEvent arg0) {
	}

	
	public void windowClosing(WindowEvent arg0) {
		preferences.save();
		System.exit(0);
		
	}

	
	public void windowDeactivated(WindowEvent arg0) {
	}

	
	public void windowDeiconified(WindowEvent arg0) {
	}

	
	public void windowIconified(WindowEvent arg0) {
	}


	public void windowOpened(WindowEvent arg0) {
	}

	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==stayOnTopMenu){
			preferences.setStayOnTop(stayOnTopMenu.isSelected());
			frame.setAlwaysOnTop(stayOnTopMenu.isSelected());
		}else if(e.getSource()==exitMenu){
			windowClosing(null);
		}else if(e.getSource()==foneticsMenu){
			foneticOptions.setVisible(true);
		}else if(e.getSource()==aboutMenu){
			aboutDialog.setVisible(true);
		}else if(e.getSource()==contactMenu){
			contactDialog.setVisible(true);
		}else if(e.getSource()==helpMenu){
			new Help();
		}else if(e.getSource()==silCountMenu){
			preferences.setViewCounter(silCountMenu.isSelected());
			if(silCountMenu.isSelected()){
				splitPane.setDividerLocation(300);
				silableCounter.setVisible(true);
				splitPane.setDividerSize(10);
				if(frame.getExtendedState()!=JFrame.MAXIMIZED_BOTH)
					frame.setSize(new Dimension(frame.getWidth()+300,frame.getHeight()));
			}else{
				if(frame.getExtendedState()!=JFrame.MAXIMIZED_BOTH)
					frame.setSize(new Dimension(frame.getWidth()-splitPane.getDividerLocation(),frame.getHeight()));
				splitPane.setDividerLocation(0);
				silableCounter.setVisible(false);
				splitPane.setDividerSize(0);
				
			}
		}else if(e.getSource()==proxyMenu){
			new ProxyDialog(frame,preferences);
		}else{
		
			for (int i = 0; i < themes.size(); i++) {
				if (e.getSource() == themes.get(i)) {
					try {
						preferences.setTheme(themeNames.get(i));
						UIManager.setLookAndFeel(themeNames.get(i));
						SwingUtilities.updateComponentTreeUI(frame);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					break;
				}
			}
		}

	}

	public JFrame getFrame() {
		return frame;
	}

}
