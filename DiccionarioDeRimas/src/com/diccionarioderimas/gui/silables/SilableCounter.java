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
package com.diccionarioderimas.gui.silables;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.StringTokenizer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.diccionarioderimas.generator.CopyPasteTextArea;
import com.diccionarioderimas.generator.RhymeFinder;

public class SilableCounter extends JPanel implements DocumentListener,
		KeyListener {

	private static final long serialVersionUID = 2073689843266639027L;
	private CopyPasteTextArea editableArea;
	private JTextArea silArea;
	private JPanel countPanel;
	private static final int COUNTER_WIDTH = 50;


	public SilableCounter() {
		
		SpringLayout spring = new SpringLayout();
		SpringLayout contSpring = new SpringLayout();
		setLayout(contSpring);
		editableArea = new CopyPasteTextArea();
		editableArea.getDocument().addDocumentListener(this);
		editableArea.addKeyListener(this);
	
		silArea = new JTextArea();
		silArea.setEditable(false);
		silArea.setPreferredSize(new Dimension(COUNTER_WIDTH, silArea
				.getPreferredSize().height));
		countPanel = new JPanel();

		countPanel.setLayout(spring);

		countPanel.add(editableArea);
		countPanel.add(silArea);

		JScrollPane sc = new JScrollPane(countPanel);

		add(sc);

		spring.putConstraint(SpringLayout.NORTH, editableArea, 0,
				SpringLayout.NORTH, countPanel);
		spring.putConstraint(SpringLayout.WEST, editableArea, 0,
				SpringLayout.WEST, countPanel);
		spring.putConstraint(SpringLayout.EAST, editableArea, -COUNTER_WIDTH,
				SpringLayout.EAST, countPanel);
		spring.putConstraint(SpringLayout.SOUTH, editableArea, 0,
				SpringLayout.SOUTH, countPanel);

		spring.putConstraint(SpringLayout.NORTH, silArea, 0,
				SpringLayout.NORTH, countPanel);
		spring.putConstraint(SpringLayout.EAST, silArea, 0, SpringLayout.EAST,
				countPanel);
		spring.putConstraint(SpringLayout.WEST, silArea, 0, SpringLayout.EAST,
				editableArea);
		spring.putConstraint(SpringLayout.SOUTH, silArea, 0,
				SpringLayout.SOUTH, countPanel);

		contSpring.putConstraint(SpringLayout.NORTH, sc, 0, SpringLayout.NORTH,
				this);
		contSpring.putConstraint(SpringLayout.WEST, sc, 0, SpringLayout.WEST,
				this);
		contSpring.putConstraint(SpringLayout.EAST, sc, 0, SpringLayout.EAST,
				this);
		contSpring.putConstraint(SpringLayout.SOUTH, sc, 0, SpringLayout.SOUTH,
				this);

	}

	private String removePunctuation(String s) {
		char[] chars = s.toCharArray();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			if (Character.isLetter(chars[i])
					|| Character.isWhitespace(chars[i]))
				buffer.append(chars[i]);
		}

		return buffer.toString().toLowerCase();
	}

	private void updateSizeAndCount() {

		String[] lines = editableArea.getText().split("\n");
		StringBuffer countList = new StringBuffer();
		for (int i = 0; i < lines.length; i++) {

			StringTokenizer tk = new StringTokenizer(
					removePunctuation(lines[i]));
			String lastToken = null;
			String lastLastToken = null;
			int count = 0;

			while (tk.hasMoreTokens()) {
				String word = tk.nextToken();
				RhymeFinder finder = new RhymeFinder(word);
				count += finder.countSil();
				// System.out.print(word+":"+finder.countSil()+" ");
				if (lastToken != null) {
					boolean last = false;
					boolean lastY = false;
					boolean lastLast = false;

					if (lastToken != null) {
						last = RhymeFinder.isVowelOrH(lastToken
								.charAt(lastToken.length() - 1));
						lastY = lastToken.equals("y");
						if (lastLastToken != null) {
							lastLast = RhymeFinder.isVowelOrH(lastLastToken
									.charAt(lastLastToken.length() - 1));
						}
					}

					boolean first = finder.startsWithVoH();

					boolean isY = word.equals("y");

					if ((first && last) || (isY && last)
							|| (first && lastY && !lastLast)) {
						// System.out.print("-");
						count--;

					}
				}

				lastLastToken = lastToken;
				lastToken = word;
				if (!tk.hasMoreTokens()) {
					if (finder.getAccentType() == RhymeFinder.AGUDA)
						count++;
					else if (finder.getAccentType() == RhymeFinder.ESDRU)
						count--;
				}
			}
			// System.out.println();
			if (count == 0)
				countList.append("\n");
			else
				countList.append(count + "\n");

		}

		silArea.setText(countList.toString());
	}

	public void changedUpdate(DocumentEvent e) {
		updateSizeAndCount();

	}

	public void insertUpdate(DocumentEvent e) {
		updateSizeAndCount();

	}

	public void removeUpdate(DocumentEvent e) {
		updateSizeAndCount();

	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {
		countPanel.setPreferredSize(new Dimension(editableArea
				.getPreferredSize().width + 70,
				editableArea.getPreferredSize().height));
		countPanel.setSize(new Dimension(
				editableArea.getPreferredSize().height, editableArea
						.getPreferredSize().width + 70));

	}

	



}
