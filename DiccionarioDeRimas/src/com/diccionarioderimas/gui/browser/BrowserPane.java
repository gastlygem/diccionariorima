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
package com.diccionarioderimas.gui.browser;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.diccionarioderimas.Utils;

public class BrowserPane extends JPanel implements ActionListener {

	private static final long serialVersionUID = -2712949932508464048L;
	private JEditorPane editor;
	private JTextField textField;
	private JButton searchButton;
	private Vector<JRadioButton> searchEngines = new Vector<JRadioButton>();
	private JProgressBar progressBar;
	private Vector<SearchEngine> engineList;
	private JFrame frame;

	public BrowserPane(JFrame frame) {

		engineList = Utils.loadEngines();
		ButtonGroup group = new ButtonGroup();
		JPanel engines = new JPanel();
		engines.setBackground(Color.WHITE);
		for (int i = 0; i < engineList.size(); i++) {
			SearchEngine engine = engineList.get(i);
			JRadioButton se = new JRadioButton(engine.getId());
			searchEngines.add(se);
			if (i == 0)
				se.setSelected(true);
			se.setBackground(Color.WHITE);
			se.setToolTipText(engine.getName());
			se.setPreferredSize(new Dimension(se.getPreferredSize().width + 20,
					se.getPreferredSize().height));
			se.addActionListener(this);
			engines.add(se);
			group.add(se);
		}

		SpringLayout slo = new SpringLayout();
		setLayout(slo);

		textField = new JTextField();
		textField.addActionListener(this);
		searchButton = new JButton("Buscar");
		searchButton.addActionListener(this);
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);

		editor = new JEditorPane();
		editor.setEditable(false);
		editor.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("page")) {
					progressBar.setIndeterminate(false);
					progressBar.setString("");
					setCursor(Cursor.getDefaultCursor());
					textField.setEnabled(true);
					searchButton.setEnabled(true);
					for (int i = 0; i < searchEngines.size(); i++) {
						searchEngines.get(i).setEnabled(true);
					}
				}
			}
		});

		JScrollPane scroll = new JScrollPane(editor);
		add(scroll);
		add(textField);
		add(searchButton);
		add(engines);
		add(progressBar);

		slo.putConstraint(SpringLayout.NORTH, textField, 5, SpringLayout.NORTH,
				this);
		slo.putConstraint(SpringLayout.WEST, textField, 5, SpringLayout.WEST,
				this);
		slo.putConstraint(SpringLayout.EAST, textField, -5, SpringLayout.WEST,
				searchButton);
		slo.putConstraint(SpringLayout.EAST, searchButton, -5,
				SpringLayout.EAST, this);
		slo.putConstraint(SpringLayout.NORTH, searchButton, 5,
				SpringLayout.NORTH, this);
		slo.putConstraint(SpringLayout.EAST, engines, -5, SpringLayout.EAST,
				this);
		slo.putConstraint(SpringLayout.WEST, engines, 5, SpringLayout.WEST,
				this);
		slo.putConstraint(SpringLayout.NORTH, engines, 5, SpringLayout.SOUTH,
				textField);

		slo.putConstraint(SpringLayout.NORTH, scroll, 5, SpringLayout.SOUTH,
				engines);
		slo.putConstraint(SpringLayout.EAST, scroll, -5, SpringLayout.EAST,
				this);
		slo
				.putConstraint(SpringLayout.WEST, scroll, 5, SpringLayout.WEST,
						this);

		slo.putConstraint(SpringLayout.SOUTH, scroll, -5, SpringLayout.NORTH,
				progressBar);
		slo.putConstraint(SpringLayout.EAST, progressBar, -5,
				SpringLayout.EAST, this);
		slo.putConstraint(SpringLayout.WEST, progressBar, 5, SpringLayout.WEST,
				this);
		slo.putConstraint(SpringLayout.SOUTH, progressBar, -5,
				SpringLayout.SOUTH, this);

	}

	public void doSearch(String word, int engineIndex) {
		textField.setText(word);
		try {

			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			progressBar.setIndeterminate(true);
			progressBar.setString("Buscando");
			textField.setEnabled(false);
			searchButton.setEnabled(false);
			for (int i = 0; i < searchEngines.size(); i++) {
				searchEngines.get(i).setEnabled(false);
				if (i == engineIndex)
					searchEngines.get(i).setSelected(true);
			}

			editor.setPage(engineList.get(engineIndex).getURLFor(word));

		} catch (Exception e) {

			try {
				editor.setPage(this.getClass().getResource("error.html"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(
					frame,
					"No se ha podido abrir la página web.\n" +
					"Asegúrate de que estás conectado a internet.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (!textField.getText().trim().equals("")) {
			for (int i = 0; i < searchEngines.size(); i++) {
				JRadioButton button = searchEngines.get(i);
				if (button.isSelected()) {
					doSearch(textField.getText().trim(), i);
					break;
				}
			}
		}

	}

}
