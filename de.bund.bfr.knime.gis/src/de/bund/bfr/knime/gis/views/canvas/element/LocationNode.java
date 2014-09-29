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
package de.bund.bfr.knime.gis.views.canvas.element;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Map;

public class LocationNode extends Node {

	private Point2D.Double center;
	private Point transformedCenter;

	public LocationNode(String id, Map<String, Object> properties,
			Point2D.Double center) {
		super(id, properties);
		this.center = center;
	}

	public Point2D.Double getCenter() {
		return center;
	}

	public Point getTransformedCenter() {
		return transformedCenter;
	}

	public void setTransform(double translationX, double translationY,
			double scaleX, double scaleY) {
		transformedCenter = new Point();
		transformedCenter.x = (int) (center.x * scaleX + translationX);
		transformedCenter.y = (int) (center.y * scaleY + translationY);
	}
}