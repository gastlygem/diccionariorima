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
package com.diccionarioderimas.gui.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AboutDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -6126197909649859126L;
	public static final String VERSION = "2.0.3";
	public static final String DATE = "05/11/2009";

	public AboutDialog(JFrame parent) {
		super(parent, "Sobre", true);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.WHITE);
		JPanel content=new JPanel();
		JLabel info = new JLabel(
				"<html><b>MERimas V."
						+ VERSION
						+ "</b><br/> build "
						+ DATE
						+ "<br/><br/>"
						+ "(c)Eduardo Rodríguez Lorenzo<br/><b>www.cronopista.com</b><br/>Distribuído bajo la licencia GPL</html>");

		content.add(info);
		JTextArea thanks=new JTextArea();
		JScrollPane thanksSCR=new JScrollPane(thanks);
		content.add(thanksSCR);
		content.setLayout(null);
		info.setBounds(10, 0, 270, 150);
		thanksSCR.setBounds(10, 140, 260, 60);
		thanks.setEditable(false);
		thanks.setText("Gracias por hacer pruebas a:\n" +
				"Luis Acuña\n" +
				"Germán Steczack\n" +
				"Tino\n" +
				"Joakín Catalán\n" +
				"Eugenia Gozalo\n" +
				"Ronald Pestmal\n" +
				"Alissa Fernández Pinal");
		
		JButton ok = new JButton("Aceptar");

		getContentPane().add(content);
		getContentPane().add(ok);

		
		
		content.setBounds(5, 5, 280, 215);
		

		ok.setBounds(100, 235, 100, 30);
		ok.addActionListener(this);
		
		
		content.setBorder(BorderFactory.createTitledBorder(""));
		// setResizable(false);
		setSize(new Dimension(300, 320));
	}

	public void setVisible(boolean v) {
		if (v == true) {
			Dimension dim = getToolkit().getScreenSize();
			setLocation((dim.width - 300) / 2, (dim.height - 150) / 2);
		}
		super.setVisible(v);

	}

	public void actionPerformed(ActionEvent e) {
		setVisible(false);

	}
}
