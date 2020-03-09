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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

/**
 * @author Eduardo Rodríguez
 *
 */
public class CopyPasteTextArea extends JTextArea implements ActionListener,MouseListener{

	
	private static final long serialVersionUID = 1375878562002504874L;
	private JPopupMenu menu;
	private JMenuItem copy, paste, cut;
	
	public CopyPasteTextArea(){
		menu = new JPopupMenu();
		copy = new JMenuItem("Copiar");
		paste = new JMenuItem("Pegar");
		cut = new JMenuItem("Cortar");
		menu.add(copy);
		menu.add(paste);
		menu.add(cut);
		copy.addActionListener(this);
		paste.addActionListener(this);
		cut.addActionListener(this);
		addMouseListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == copy)
			copy();
		else if (e.getSource() == paste)
			paste();
		else if (e.getSource() == cut)
			cut();

	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger())
			menu.show(e.getComponent(), e.getX(), e.getY());

	}
	
}
