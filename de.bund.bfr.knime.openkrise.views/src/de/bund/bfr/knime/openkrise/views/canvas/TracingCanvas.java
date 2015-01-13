/*******************************************************************************
 * Copyright (c) 2014 Federal Institute for Risk Assessment (BfR), Germany 
 * 
 * Developers and contributors are 
 * Christian Thoens (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Annemarie Kaesbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package de.bund.bfr.knime.openkrise.views.canvas;

import java.util.Map;
import java.util.Set;

import de.bund.bfr.knime.gis.views.canvas.CanvasOptionsPanel;
import de.bund.bfr.knime.gis.views.canvas.CanvasPopupMenu;
import de.bund.bfr.knime.gis.views.canvas.element.Edge;
import de.bund.bfr.knime.gis.views.canvas.element.Node;

public interface TracingCanvas<V extends Node> {

	public abstract Set<V> getNodes();

	public abstract Set<Edge<V>> getEdges();

	public abstract Map<String, Set<String>> getCollapsedNodes();

	public abstract void setCollapsedNodes(
			Map<String, Set<String>> collapsedNodes);

	public abstract CanvasOptionsPanel getOptionsPanel();

	public abstract void setOptionsPanel(CanvasOptionsPanel optionsPanel);

	public abstract CanvasPopupMenu getPopupMenu();

	public abstract void setPopupMenu(CanvasPopupMenu popup);

	public abstract void applyChanges();
}
