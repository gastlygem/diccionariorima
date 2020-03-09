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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.diccionarioderimas.Search;

public class DictionaryPane extends JPanel implements ActionListener,
		MouseListener {

	private static final long serialVersionUID = 5013718582362475473L;
	private JTextField mainTextField = new JTextField(18);
	private JLabel advancedSearchButton = new JLabel(new ImageIcon(this
			.getClass().getResource("iconPlus.png")));

	private OptionsPanel optionsPanel;
	private ResultsContainer resultsContainer;
	private JPanel searchPanel;
	private boolean optionsVisible = false;

	public DictionaryPane() {
		SpringLayout lom = new SpringLayout();
		setLayout(lom);
		mainTextField.addActionListener(this);
		mainTextField.setBackground(new Color(230,230,230));

		// search panel
		mainTextField
				.setToolTipText("<html>Introduce la palabra que quieras rimar, y presiona <b>Entrar</b><br/>Haz click en <b>+</b> para ver opciones avanzadas.</html>");
		advancedSearchButton.setToolTipText("Mostrar búsqueda avanzada");
		searchPanel = new JPanel();
		searchPanel.add(mainTextField);
		searchPanel.add(advancedSearchButton);

		searchPanel.setBackground(Color.WHITE);
		advancedSearchButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		advancedSearchButton.addMouseListener(this);

		// options panel
		optionsPanel = new OptionsPanel();
		optionsPanel
				.setPreferredSize(new Dimension(optionsPanel.getWidth(), 0));

		optionsPanel.setBackground(new Color(220, 220, 220));

		resultsContainer = new ResultsContainer();
		resultsContainer.setVisible(false);

		add(searchPanel);
		add(optionsPanel);
		add(resultsContainer);

		lom.putConstraint(SpringLayout.NORTH, searchPanel, 0,
				SpringLayout.NORTH, this);
		lom.putConstraint(SpringLayout.EAST, searchPanel, 0, SpringLayout.EAST,
				this);
		lom.putConstraint(SpringLayout.WEST, searchPanel, 0, SpringLayout.WEST,
				this);
		lom.putConstraint(SpringLayout.NORTH, optionsPanel, 0,
				SpringLayout.SOUTH, searchPanel);
		lom.putConstraint(SpringLayout.EAST, optionsPanel, 0,
				SpringLayout.EAST, this);
		lom.putConstraint(SpringLayout.WEST, optionsPanel, 0,
				SpringLayout.WEST, this);
		lom.putConstraint(SpringLayout.NORTH, resultsContainer, 20,
				SpringLayout.SOUTH, optionsPanel);
		lom.putConstraint(SpringLayout.EAST, resultsContainer, 0,
				SpringLayout.EAST, this);
		lom.putConstraint(SpringLayout.WEST, resultsContainer, 0,
				SpringLayout.WEST, this);
		lom.putConstraint(SpringLayout.SOUTH, resultsContainer, 0,
				SpringLayout.SOUTH, this);

		setBackground(Color.WHITE);
		
		
	}

	public void mouseClicked(MouseEvent e) {
		Object src = e.getSource();
		if (src == advancedSearchButton) {
			if (optionsVisible) {
				advancedSearchButton.setIcon(new ImageIcon(this.getClass()
						.getResource("iconPlus.png")));
				optionsPanel.setPreferredSize(new Dimension(optionsPanel
						.getWidth(), 0));
				optionsPanel.setSize(new Dimension(optionsPanel.getWidth(), 0));
				advancedSearchButton
						.setToolTipText("Mostrar búsqueda avanzada");

			} else {
				advancedSearchButton.setIcon(new ImageIcon(this.getClass()
						.getResource("iconMinus.png")));
				optionsPanel.setPreferredSize(new Dimension(optionsPanel
						.getWidth(), optionsPanel.calculateNewHeight()));
				optionsPanel.setSize(new Dimension(optionsPanel.getWidth(),
						optionsPanel.calculateNewHeight()));
				advancedSearchButton
						.setToolTipText("Ocultar búsqueda avanzada");

			}

			optionsVisible = !optionsVisible;
			doLayout();

		}

	}

	public void actionPerformed(ActionEvent e) {
		String word = mainTextField.getText().trim().toLowerCase();
		if (word.length() > 0) {

			Search search = new Search(word, optionsPanel.getType(),
					optionsPanel.getSilables(), optionsPanel.getBeggining(),
					MainWindow.getBasePath());
			resultsContainer.getNouns().setResults(search.getSus());
			resultsContainer.getVerbs().setResults(search.getVerbs());
			resultsContainer.getParticiples().setResults(search.getRest());
			if (!resultsContainer.isVisible())
				resultsContainer.setVisible(true);

		}

		mainTextField.requestFocus();
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
	
	public void focus(){
		mainTextField.requestFocus();
	}

}
