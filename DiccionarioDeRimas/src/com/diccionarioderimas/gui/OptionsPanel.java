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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.diccionarioderimas.Search;

public class OptionsPanel extends JPanel implements ComponentListener,ActionListener {

	
	private static final long serialVersionUID = 928219312318914797L;
	private JRadioButton asonance = new JRadioButton("Asonante");
	private JRadioButton consonance = new JRadioButton("Consonante");
	private JComboBox silables = new JComboBox(new String[] { "Indiferente",
			"1", "2", "3", "4", "5", "6", "7", "8", "9", "10" });
	private JComboBox VoH = new JComboBox(new String[] { "Indiferente",
			 "Consonante", "V ó H" });
	
	private JPanel line1, line2, line3;

	public OptionsPanel() {
		setLayout(new FlowLayout(FlowLayout.CENTER,0,0));

		asonance.setBackground(new Color(220,220,220));
		consonance.setBackground(new Color(220,220,220));
		consonance.setSelected(true);
		asonance.addActionListener(this);
		consonance.addActionListener(this);
		silables.addActionListener(this);
		VoH.addActionListener(this);
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(consonance);
		radioGroup.add(asonance);

		line1 = new JPanel();
		line1.add(new JLabel("<html><b>RIMA:</b></html>"));
		line1.add(asonance);
		line1.add(consonance);
		line1.setBackground(new Color(220,220,220));
		//line1.setPreferredSize(new Dimension(220,30));
		line1.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		
		

		line2 = new JPanel();
		line2.add(new JLabel("<html><b>Número de Sílabas:</b></html>"));
		line2.add(silables);
		line2.setBackground(new Color(220,220,220));
		//line2.setPreferredSize(new Dimension(220,30));
		line2.setLayout(new FlowLayout(FlowLayout.LEFT));

		line3 = new JPanel();
		line3.add(new JLabel("<html><b>  Comenzando por:</b></html>"));
		line3.add(VoH);
		line3.setBackground(new Color(220,220,220));
		//line3.setPreferredSize(new Dimension(220,30));
		line3.setLayout(new FlowLayout(FlowLayout.LEFT));

		
		
		add(line1);
		add(line2);
		add(line3);
		

		addComponentListener(this);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(0, 0, getWidth() , 0);
		g.drawLine(0, getHeight() - 1, getWidth() , getHeight() - 1);
	}


	public void componentHidden(ComponentEvent arg0) {
	}


	public void componentMoved(ComponentEvent arg0) {
	}

	
	public void componentShown(ComponentEvent arg0) {
	}

	
	public void componentResized(ComponentEvent a) {
		if(getPreferredSize().height!=0){
			setPreferredSize(new Dimension(getWidth(),calculateNewHeight()));
			setSize(new Dimension(getWidth(),calculateNewHeight()));
			getParent().invalidate();
		}

	}
	
	public int calculateNewHeight(){
		int newHeight = line3.getBounds().y + 35;
		if (newHeight != getHeight()) {
			setSize(new Dimension(getWidth(), newHeight));
			setPreferredSize(new Dimension(getWidth(), newHeight));
			getParent().doLayout();
		}
		
		return newHeight;
	}

	public int getType(){
		if(asonance.isSelected())
			return Search.ASONANCE;
		return Search.CONSONANT;
	}
	
	public int getSilables(){
		if(silables.getSelectedIndex()!=0)
			return silables.getSelectedIndex();
		return Search.INDIFERENT;
	}
	
	public int getBeggining(){
		if(VoH.getSelectedIndex()!=0)
			return VoH.getSelectedIndex()-1;
		
		return Search.INDIFERENT;
	}
	
	

	public void actionPerformed(ActionEvent e) {
		MainWindow.searchRhymes();
		
	}
}
