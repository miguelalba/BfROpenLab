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
package de.bund.bfr.knime.openkrise.views.gisgraphview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.InvalidSettingsException;

import de.bund.bfr.knime.gis.views.canvas.GraphCanvas;
import de.bund.bfr.knime.gis.views.canvas.LocationCanvas;
import de.bund.bfr.knime.gis.views.canvas.element.Edge;
import de.bund.bfr.knime.gis.views.canvas.element.GraphNode;
import de.bund.bfr.knime.gis.views.canvas.element.LocationNode;
import de.bund.bfr.knime.gis.views.canvas.element.RegionNode;
import de.bund.bfr.knime.openkrise.TracingColumns;
import de.bund.bfr.knime.openkrise.TracingUtils;

public class GisGraphViewCanvasCreator {

	private BufferedDataTable shapeTable;
	private BufferedDataTable nodeTable;
	private BufferedDataTable edgeTable;
	private GisGraphViewSettings set;

	public GisGraphViewCanvasCreator(BufferedDataTable shapeTable,
			BufferedDataTable nodeTable, BufferedDataTable edgeTable,
			GisGraphViewSettings set) {
		this.shapeTable = shapeTable;
		this.nodeTable = nodeTable;
		this.edgeTable = edgeTable;
		this.set = set;
	}

	public GraphCanvas createGraphCanvas() throws InvalidSettingsException {
		Map<String, Class<?>> nodeProperties = TracingUtils
				.getTableColumns(nodeTable.getSpec());
		Map<String, Class<?>> edgeProperties = TracingUtils
				.getTableColumns(edgeTable.getSpec());
		Map<String, GraphNode> nodes = TracingUtils.readGraphNodes(nodeTable,
				nodeProperties);
		List<Edge<GraphNode>> edges = TracingUtils.readEdges(edgeTable,
				edgeProperties, nodes);
		GraphCanvas canvas = new GraphCanvas(new ArrayList<>(nodes.values()),
				edges, nodeProperties, edgeProperties, TracingColumns.ID,
				TracingColumns.ID, TracingColumns.FROM, TracingColumns.TO,
				false);

		canvas.setNodeName(TracingUtils.NODE_NAME);
		canvas.setEdgeName(TracingUtils.EDGE_NAME);
		canvas.setNodesName(TracingUtils.NODES_NAME);
		canvas.setEdgesName(TracingUtils.EDGES_NAME);
		set.getGraphSettings().setToCanvas(canvas, nodeProperties,
				edgeProperties, true);

		return canvas;
	}

	public LocationCanvas createGisCanvas() throws InvalidSettingsException {
		List<RegionNode> regionNodes = TracingUtils.readRegionNodes(shapeTable);
		Map<String, Class<?>> nodeProperties = TracingUtils
				.getTableColumns(nodeTable.getSpec());
		Map<String, Class<?>> edgeProperties = TracingUtils
				.getTableColumns(edgeTable.getSpec());
		Map<String, LocationNode> nodes = TracingUtils.readLocationNodes(
				nodeTable, nodeProperties);
		List<Edge<LocationNode>> edges = TracingUtils.readEdges(edgeTable,
				edgeProperties, nodes);
		LocationCanvas canvas = new LocationCanvas(new ArrayList<>(
				nodes.values()), edges, nodeProperties, edgeProperties,
				TracingColumns.ID, TracingColumns.ID, TracingColumns.FROM,
				TracingColumns.TO, regionNodes);

		canvas.setNodeName(TracingUtils.NODE_NAME);
		canvas.setEdgeName(TracingUtils.EDGE_NAME);
		canvas.setNodesName(TracingUtils.NODES_NAME);
		canvas.setEdgesName(TracingUtils.EDGES_NAME);
		set.getGraphSettings().setToCanvas(canvas, nodeProperties,
				edgeProperties, true);
		set.getGisSettings().setToCanvas(canvas, nodeProperties, false);

		return canvas;
	}
}
