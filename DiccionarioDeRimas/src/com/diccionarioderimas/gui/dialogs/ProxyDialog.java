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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.diccionarioderimas.UserPreferences;
import com.diccionarioderimas.generator.Utils;

/**
 * @author Eduardo Rodríguez
 *
 */
public class ProxyDialog extends JDialog implements ActionListener{

	
	private static final long serialVersionUID = -2245461078853563297L;
	private UserPreferences preferences;
	private JButton ok, cancel;
	private JRadioButton noProxy,proxy;
	private JTextField proxyField,userField;
	private JPasswordField passwordField;
	private JSpinner proxyPort;
	private JLabel proxyLabel,portLabel,userLabel,passwordLabel;
	
	
	public ProxyDialog(JFrame frame, UserPreferences preferences){
		super(frame,"Opciones de Internet",true);
		this.preferences=preferences;
		
		Container c= getContentPane();
		c.setLayout(null);
		c.setBackground(Color.WHITE);
		
		ButtonGroup group=new ButtonGroup();
		
		proxy=new JRadioButton("Usar proxy");
		proxy.addActionListener(this);
		proxy.setBackground(Color.WHITE);
		c.add(proxy);
		group.add(proxy);
		proxy.setSelected(preferences.isProxy());
		
		
		noProxy=new JRadioButton("No usar proxy");
		noProxy.addActionListener(this);
		noProxy.setBackground(Color.WHITE);
		c.add(noProxy);
		group.add(noProxy);
		noProxy.setSelected(!preferences.isProxy());
		
		proxyLabel=new JLabel("Proxy:");
		proxyField=new JTextField();
		proxyField.setText(preferences.getProxyHTTP());
		proxyLabel.setLabelFor(proxyLabel);
		c.add(proxyLabel);
		c.add(proxyField);
		
			
		
		portLabel=new JLabel("Puerto:");
		proxyPort=new JSpinner(new SpinnerNumberModel(preferences.getProxyPort(),0,99999,1));
		portLabel.setLabelFor(proxyPort);
		c.add(portLabel);
		c.add(proxyPort);
		
		userLabel=new JLabel("ID:");
		userField=new JTextField();
		userLabel.setLabelFor(userField);
		userField.setText(preferences.getProxyUser());
		c.add(userLabel);
		c.add(userField);
		
		passwordLabel=new JLabel("Clave:");
		passwordField=new JPasswordField();
		passwordLabel.setLabelFor(passwordField);
		passwordField.setText(preferences.getProxyPassword());
		c.add(passwordLabel);
		c.add(passwordField);
		
		if(!preferences.isProxy()){
			proxyPort.setEnabled(false);
			proxyField.setEnabled(false);
			proxyLabel.setEnabled(false);
			portLabel.setEnabled(false);
			userLabel.setEnabled(false);
			userField.setEnabled(false);
			passwordField.setEnabled(false);
			passwordLabel.setEnabled(false);
		}
		
		
		ok=new JButton("Aceptar");
		ok.addActionListener(this);
		c.add(ok);
		
		cancel=new JButton("Cancelar");
		cancel.addActionListener(this);
		c.add(cancel);
		
		
		
		noProxy.setBounds(10, 10, 300, 25);
		proxy.setBounds(10, 40, 300, 25);
		proxyLabel.setBounds(40, 70, 40,25);
		proxyField.setBounds(90, 70, 150, 25);
		portLabel.setBounds(250, 70, 50,25);
		proxyPort.setBounds(300, 70, 70, 25);
		userLabel.setBounds(40, 100, 40, 25);
		userField.setBounds(90, 100, 280, 25);
		passwordLabel.setBounds(40, 130, 40, 25);
		passwordField.setBounds(90, 130, 280, 25);
		
		ok.setBounds(20, 190, 100, 25);
		cancel.setBounds(140, 190, 100, 25);
		
		
		
		
		setSize(new Dimension(400,270));
		setResizable(false);
		
		Dimension dim = getToolkit().getScreenSize();
		setLocation((dim.width - 270) / 2, (dim.height - 240) / 2);
		
		setVisible(true);
	}



	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==proxy){
			proxyPort.setEnabled(true);
			proxyField.setEnabled(true);
			proxyLabel.setEnabled(true);
			portLabel.setEnabled(true);
			userLabel.setEnabled(true);
			userField.setEnabled(true);
			passwordField.setEnabled(true);
			passwordLabel.setEnabled(true);
		
		}else if(e.getSource()==noProxy){
			proxyPort.setEnabled(false);
			proxyField.setEnabled(false);
			proxyLabel.setEnabled(false);
			portLabel.setEnabled(false);
			userLabel.setEnabled(false);
			userField.setEnabled(false);
			passwordField.setEnabled(false);
			passwordLabel.setEnabled(false);
		}else if(e.getSource()==cancel){
			setVisible(false);
		}else if(e.getSource()==ok){
			preferences.setProxy(proxy.isSelected());
			if(proxy.isSelected()) {
				preferences.setProxyHTTP(proxyField.getText());
				preferences.setProxyPort((Integer)proxyPort.getValue());
				preferences.setProxyUser(userField.getText());
				preferences.setProxyPassword(new String(passwordField.getPassword()));
				System.getProperties().put( "proxySet", "true" );
				System.getProperties().put( "proxyHost", preferences.getProxyHTTP());
				System.getProperties().put( "proxyPort", preferences.getProxyPort() );
				
			}else{
				System.getProperties().put( "proxySet", "false" );
				
			}
			
			setVisible(false);
		}
		
	}
	
	public static void main(String[] args) {
		new ProxyDialog(null, new UserPreferences(Utils
				.getApplicationPath()));
	}
	
}
