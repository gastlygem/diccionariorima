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
package com.diccionarioderimas.gui.help;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.diccionarioderimas.generator.ErrorDialog;
import com.diccionarioderimas.gui.MainWindow;
import com.diccionarioderimas.gui.dialogs.AboutDialog;

public class Help implements HyperlinkListener{

	private JEditorPane pane;
	
	public Help(){

		JFrame frame = new JFrame("Ayuda");
		frame.setIconImage(new ImageIcon(MainWindow.class
				.getResource("logoSmall.png")).getImage());
		SpringLayout spring = new SpringLayout();
		frame.getContentPane().setLayout(spring);
		JLabel info = new JLabel(
				"<html><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Ayuda para MERimas</b> versión "
						+ AboutDialog.VERSION + " build " + AboutDialog.DATE
						+ "</html>");

		pane = new JEditorPane();
		pane.setEditable(false);
		pane.addHyperlinkListener(this);
		JScrollPane scroll=new JScrollPane(pane);
		
		frame.getContentPane().add(info);
		frame.getContentPane().add(scroll);
		info.setBackground(Color.WHITE);
		info.setOpaque(true);
		info.setPreferredSize(new Dimension(info.getPreferredSize().width,30));
		
		spring.putConstraint(SpringLayout.NORTH, info, 0,
				SpringLayout.NORTH, frame.getContentPane());
		spring.putConstraint(SpringLayout.WEST, info, 0,
				SpringLayout.WEST, frame.getContentPane());
		spring.putConstraint(SpringLayout.EAST, info, 0,
				SpringLayout.EAST, frame.getContentPane());
	
		
		spring.putConstraint(SpringLayout.NORTH, scroll, 0,
				SpringLayout.SOUTH, info);
		spring.putConstraint(SpringLayout.WEST, scroll, 0,
				SpringLayout.WEST, frame.getContentPane());
		spring.putConstraint(SpringLayout.EAST, scroll, 0,
				SpringLayout.EAST, frame.getContentPane());
		spring.putConstraint(SpringLayout.SOUTH, scroll, 0,
				SpringLayout.SOUTH, frame.getContentPane());

		Dimension dim = frame.getToolkit().getScreenSize();
		frame.setSize(new Dimension(dim.width - 100, dim.height - 100));
		frame.setVisible(true);

		try {
			String bp=MainWindow.getBasePath();
			if(bp==null)
				bp="";
			pane.setPage("file:"+bp+"help.html");
		} catch (IOException e) {

			new ErrorDialog(frame,e);
		}

	}

	
	
	public void hyperlinkUpdate(HyperlinkEvent event) {
	    if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	      try {
	        pane.setPage(event.getURL());
	        
	      } catch(IOException ioe) {
	        
	      }
	    }
	  }

	
	public static void main(String[] args) {
		new Help();
	}

	

}
