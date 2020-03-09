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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;


import com.diccionarioderimas.SearchResult;
import com.diccionarioderimas.Utils;
import com.diccionarioderimas.gui.browser.SearchEngine;

public class ResultsPanel extends JPanel implements Scrollable,
		AdjustmentListener, ComponentListener, MouseMotionListener,MouseListener,ActionListener,ClipboardOwner {

	private static final long serialVersionUID = -5312454951732255943L;
	private static final int BLOCK=550;
	private static final int BLOCK_HEIGHT=30;
	private SearchResult[] results;
	private int location = 0;
	private int width;
	private JScrollPane scroll;
	private int selected=-1;
	private JPopupMenu popupMenu;
	private JMenuItem copy;
	private Vector<JMenuItem> searchEngines;
	
	public ResultsPanel() {
		setBackground(Color.WHITE);
		addMouseMotionListener(this);
		addMouseListener(this);
		
		popupMenu=new JPopupMenu();
		copy=new JMenuItem("Copiar");
		popupMenu.add(copy);
		copy.addActionListener(this);
		searchEngines=new Vector<JMenuItem>();
		Vector<SearchEngine> engines = Utils.loadEngines();
		for(int i=0;i<engines.size();i++){
			SearchEngine engine = engines.get(i);
			JMenuItem jmi=new JMenuItem("Buscar en '"+engine.getId()+"'");
			popupMenu.add(jmi);
			searchEngines.add(jmi);
			jmi.addActionListener(this);
			
		}
		
		
	}

	public void setResults(SearchResult[] results) {
		this.results = results;
		location = 0;
		width = scroll.getViewport().getWidth();
		if(width<BLOCK)
			width=BLOCK;
		int height = scroll.getViewport().getHeight();
		if (results != null && results.length*BLOCK_HEIGHT+1 > height)
			height = BLOCK_HEIGHT * results.length+BLOCK_HEIGHT;
		setPreferredSize(new Dimension(width, height));
		revalidate();
		repaint();
		

	}
	
	

	public void setScroll(JScrollPane scroll) {
		this.scroll = scroll;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int xLoc=0;
		if(width>BLOCK)
			xLoc=(width-BLOCK)/2;
		int floc=(BLOCK_HEIGHT-g.getFontMetrics().getHeight())/2+g.getFontMetrics().getAscent();
		
			Font oldFont=g.getFont();
			g.setColor(Color.BLUE);
			g.setFont(new Font(oldFont.getFontName(),Font.BOLD,oldFont.getSize()));
			g.drawString(results.length+" resultados ", 10+xLoc, +floc);
			g.setColor(new Color(220,220,220));
			g.drawLine(xLoc, BLOCK_HEIGHT, xLoc+BLOCK, BLOCK_HEIGHT);
			for (int i = location; i < results.length && i < location + 30; i++) {
				if(i==selected){
					g.setColor(new Color(220,220,255));
					g.fill3DRect(xLoc, BLOCK_HEIGHT*i+BLOCK_HEIGHT, BLOCK, BLOCK_HEIGHT, true);
				}
					
				
				g.setFont(new Font(oldFont.getFontName(),Font.BOLD,oldFont.getSize()));
				SearchResult result = results[i];
				String type=result.getTypeString();
				g.setColor(Color.BLACK);
				g.drawString(result.getWord(), 10+xLoc, BLOCK_HEIGHT * i +BLOCK_HEIGHT+ floc);
				int from1=10+g.getFontMetrics().stringWidth(result.getWord())+5+xLoc;
				g.setFont(oldFont);
				if(type.length()>0){
					
					
					int from2=from1+g.getFontMetrics().stringWidth(result.getOriginalWord())+5;
					g.setColor(Color.GRAY);
					g.drawString(result.getOriginalWord(), from1, BLOCK_HEIGHT * i +BLOCK_HEIGHT+ floc);
					g.setColor(Color.LIGHT_GRAY);
					g.drawString(type, from2, BLOCK_HEIGHT * i+BLOCK_HEIGHT + floc);
				}
				
				g.setColor(new Color(220,220,220));
				g.drawLine(xLoc, BLOCK_HEIGHT*i+BLOCK_HEIGHT+BLOCK_HEIGHT, xLoc+BLOCK, BLOCK_HEIGHT*i+BLOCK_HEIGHT+BLOCK_HEIGHT);
	
			}
		

	}

	
	public Dimension getPreferredScrollableViewportSize() {
		if (results == null || results.length == 0)
			return new Dimension(0, 0);
		return new Dimension(width, BLOCK_HEIGHT * results.length+BLOCK_HEIGHT);
	}

	
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {

		return scroll.getViewport().getHeight();
	}


	public boolean getScrollableTracksViewportHeight() {

		return false;
	}


	public boolean getScrollableTracksViewportWidth() {

		return false;
	}

	
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {

		return BLOCK_HEIGHT;
	}

	
	public void adjustmentValueChanged(AdjustmentEvent e) {
		location = e.getValue() / BLOCK_HEIGHT-1;
		if(location<0)
			location=0;
		repaint();

	}

	
	public void componentHidden(ComponentEvent e) {}


	public void componentMoved(ComponentEvent e) {}


	public void componentResized(ComponentEvent e) {
		width = scroll.getViewport().getWidth();
		if (width < BLOCK)
			width = BLOCK;
		int height = scroll.getViewport().getHeight();
		if (results != null && results.length*BLOCK_HEIGHT+1 > height)
			height = BLOCK_HEIGHT * results.length+BLOCK_HEIGHT;
		setPreferredSize(new Dimension(width, height));
		revalidate();
	}

	
	public void componentShown(ComponentEvent e) {}

	
	public void mouseDragged(MouseEvent e) {
		
		
	}

	
	public void mouseMoved(MouseEvent e) {
		int xLoc=0;
		int oldSel=selected;
		if(width>BLOCK)
			xLoc=(width-BLOCK)/2;
		if(e.getX()>=xLoc && e.getX()<=xLoc+BLOCK)
			selected=e.getY()/BLOCK_HEIGHT-1;
		else
			selected=-1;

		if(selected!=oldSel){
			repaint();
			if(popupMenu.isShowing())
				popupMenu.setVisible(false);
		}
	}

	
	public void mouseClicked(MouseEvent e) {
		
		
		
	}

	
	public void mouseEntered(MouseEvent e) {
		
		
	}

	
	public void mouseExited(MouseEvent e) {
//		selected=-1;
//		repaint();
		
	}

	
	public void mousePressed(MouseEvent e) {
		if(e.isPopupTrigger()){
			popup(e);
		}
		
	}

	
	public void mouseReleased(MouseEvent e) {
		
		if(e.isPopupTrigger()){
			popup(e);
		} else if(selected!=-1 && e.getButton()==MouseEvent.BUTTON1 )
			MainWindow.search(results[selected].getOriginalWord(), 0);
		
	}
	
	private void popup(MouseEvent e){
		popupMenu.show(e.getComponent(),e.getX(),e.getY());
		e.consume();
		
	}

	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==copy && selected!=-1){
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		    clipboard.setContents( new StringSelection(results[selected].getWord()), this );

		}else{
			for(int i=0;i<searchEngines.size();i++){
				if(e.getSource()==searchEngines.get(i)){
					MainWindow.search(results[selected].getOriginalWord(), i);
					break;
				}
			}
			
		}
		
		
	}

	
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		
		
	}

}
