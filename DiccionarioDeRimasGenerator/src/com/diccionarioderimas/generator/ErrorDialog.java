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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * @author Eduardo Rodríguez
 * 
 */
public class ErrorDialog extends JDialog implements ActionListener {

	
	private static final long serialVersionUID = -6886171185160146930L;
	private static final String HEADER = "<html><b>Ha ocurrido un error imprevisto.</b><br/>"
			+ "Por favor, contacta conmigo a través de <b>www.cronopista.com</b><br/>"
			+ "o usando <b>Menú->Contacto...</b> y adjunta (copia y pega) el mensaje que aparece debajo<br/>"
			+ "de estas líneas. Gracias.</html>";
	private JButton button = new JButton("OK");

	public ErrorDialog(JFrame parent, Exception e) {
		super(parent, "Error imprevisto", true);
		Container cp = getContentPane();
		cp.setLayout(null);

		JLabel header = new JLabel(HEADER);
		CopyPasteTextArea area = new CopyPasteTextArea();
		area.setEditable(false);
		area.setText(getStackTrace(e));
		JScrollPane scroll = new JScrollPane(area);

		cp.add(header);
		cp.add(scroll);
		cp.add(button);

		button.addActionListener(this);

		header.setBounds(10, 10, 370, 100);
		scroll.setBounds(10, 120, 370, 200);
		button.setBounds(150, 330, 100, 25);
		
		
		setSize(new Dimension(400,400));
		Dimension dim = getToolkit().getScreenSize();
		

		setLocation((dim.width - 400) / 2, (dim.height - 400) / 2);
		setVisible(true);
	}

	public String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}

	public void actionPerformed(ActionEvent e) {
		setVisible(false);
		dispose();
	}

	public static void main(String[] args) {
		new ErrorDialog(null, new Exception("una excepción"));
	}

}
