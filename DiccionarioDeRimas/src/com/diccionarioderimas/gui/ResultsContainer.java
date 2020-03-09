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

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class ResultsContainer extends JTabbedPane {

	private static final long serialVersionUID = -253062185906219135L;
	private ResultsPanel nouns = new ResultsPanel();
	private ResultsPanel verbs = new ResultsPanel();
	private ResultsPanel participles = new ResultsPanel();

	public ResultsContainer() {
		super(LEFT);
		// setBackground(Color.WHITE);
		JScrollPane nSP = new JScrollPane(nouns);
		JScrollPane vSP = new JScrollPane(verbs);
		JScrollPane pSP = new JScrollPane(participles);
		nSP.getVerticalScrollBar().addAdjustmentListener(nouns);
		vSP.getVerticalScrollBar().addAdjustmentListener(verbs);
		pSP.getVerticalScrollBar().addAdjustmentListener(participles);
		addComponentListener(nouns);
		addComponentListener(verbs);
		addComponentListener(participles);
		nouns.setScroll(nSP);
		verbs.setScroll(vSP);
		participles.setScroll(pSP);

		addTab("Sust. y adj.", null, nSP, "Sustantivos y adjetivos");
		addTab("Verbos", null, vSP,
				"Verbos sin participios ni formas enclíticas");
		addTab("Part. y encl.", null, pSP, "Participios y formas enclíticas");

	}

	public ResultsPanel getNouns() {
		return nouns;
	}

	public void setNouns(ResultsPanel nouns) {
		this.nouns = nouns;
	}

	public ResultsPanel getVerbs() {
		return verbs;
	}

	public void setVerbs(ResultsPanel verbs) {
		this.verbs = verbs;
	}

	public ResultsPanel getParticiples() {
		return participles;
	}

	public void setParticiples(ResultsPanel participles) {
		this.participles = participles;
	}

}
