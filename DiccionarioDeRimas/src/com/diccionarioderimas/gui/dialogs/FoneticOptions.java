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
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.diccionarioderimas.UserPreferences;

public class FoneticOptions extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 300;
	private JCheckBox yOpt, zOpt, ceOpt, niOpt, liOpt;
	private JButton ok, cancel;
	private UserPreferences preferences;

	public FoneticOptions(JFrame parent, UserPreferences pref) {
		super(parent, "Opciones Fonéticas", true);
		preferences = pref;
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.WHITE);

		JLabel info = new JLabel(
				"<html><b>Selecciona las equivalencias que mejor te parezcan </b></html>");

		getContentPane().add(info);
		info.setBounds(5, 5, 280, 55);
		info.setBorder(BorderFactory.createTitledBorder(""));

		yOpt = new JCheckBox("y = ll");
		zOpt = new JCheckBox("z = s");
		ceOpt = new JCheckBox("ce/ci = se/si");
		niOpt = new JCheckBox("ñi = ni");
		liOpt = new JCheckBox("li = ll");
		
		
		yOpt.setSelected(preferences.isYEquivalence());
		zOpt.setSelected(preferences.isZEquivalence());
		ceOpt.setSelected(preferences.isCeEquivalence());
		niOpt.setSelected(preferences.isNiEquivalence());
		liOpt.setSelected(preferences.isLiEquivalence());

		getContentPane().add(yOpt);
		getContentPane().add(zOpt);
		getContentPane().add(ceOpt);
		getContentPane().add(niOpt);
		getContentPane().add(liOpt);

		yOpt.setBounds(40, 70, 90, 30);
		zOpt.setBounds(40, 100, 90, 30);
		ceOpt.setBounds(40, 130, 90, 30);
		niOpt.setBounds(40, 160, 90, 30);
		liOpt.setBounds(40, 190, 90, 30);

		yOpt.setBackground(Color.WHITE);
		zOpt.setBackground(Color.WHITE);
		ceOpt.setBackground(Color.WHITE);
		liOpt.setBackground(Color.WHITE);
		niOpt.setBackground(Color.WHITE);

		ok = new JButton("Aceptar");
		cancel = new JButton("Cancelar");

		getContentPane().add(ok);
		getContentPane().add(cancel);

		ok.setBounds(30, 230, 100, 25);
		cancel.setBounds(160, 230, 100, 25);

		ok.addActionListener(this);
		cancel.addActionListener(this);

		setResizable(false);
		
		setSize(new Dimension(WIDTH, 300));
	}

	
	public void setVisible(boolean v){
		if(v==true){
			Dimension dim = getToolkit().getScreenSize();
			setLocation((dim.width-300)/2,(dim.height-300)/2);
		}
		super.setVisible(v);
		
	}
	
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == cancel) {
			setVisible(false);
			
			yOpt.setSelected(preferences.isYEquivalence());
			zOpt.setSelected(preferences.isZEquivalence());
			ceOpt.setSelected(preferences.isCeEquivalence());
			niOpt.setSelected(preferences.isNiEquivalence());
			liOpt.setSelected(preferences.isLiEquivalence());
		} else {
			preferences.setYEquivalence(yOpt.isSelected());
			preferences.setZEquivalence(zOpt.isSelected());
			preferences.setCeEquivalence(ceOpt.isSelected());
			preferences.setNiEquivalence(niOpt.isSelected());
			preferences.setLiEquivalence(liOpt.isSelected());
			setVisible(false);
		}

	}

}
