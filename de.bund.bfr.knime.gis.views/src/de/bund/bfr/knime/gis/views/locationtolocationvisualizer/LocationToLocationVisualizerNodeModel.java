/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.gis.views.locationtolocationvisualizer;

import java.io.File;
import java.io.IOException;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.image.ImagePortObject;

import de.bund.bfr.knime.gis.views.LocationSettings.GisType;
import de.bund.bfr.knime.gis.views.canvas.Canvas;
import de.bund.bfr.knime.gis.views.canvas.CanvasUtils;
import de.bund.bfr.knime.gis.views.canvas.GraphCanvas;
import de.bund.bfr.knime.gis.views.canvas.element.LocationNode;

/**
 * This is the model implementation of LocationToLocationVisualizer.
 * 
 * 
 * @author Christian Thoens
 */
public class LocationToLocationVisualizerNodeModel extends NodeModel {

	private LocationToLocationVisualizerSettings set;

	/**
	 * Constructor for the node model.
	 */
	protected LocationToLocationVisualizerNodeModel() {
		super(new PortType[] { BufferedDataTable.TYPE_OPTIONAL, BufferedDataTable.TYPE,
				BufferedDataTable.TYPE }, new PortType[] { ImagePortObject.TYPE,
				ImagePortObject.TYPE });
		set = new LocationToLocationVisualizerSettings();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
		BufferedDataTable shapeTable = (BufferedDataTable) inObjects[0];
		BufferedDataTable nodeTable = (BufferedDataTable) inObjects[1];
		BufferedDataTable edgeTable = (BufferedDataTable) inObjects[2];
		GisType originalGisType = set.getGisSettings().getGisType();

		if (shapeTable == null && set.getGisSettings().getGisType() == GisType.SHAPEFILE) {
			set.getGisSettings().setGisType(GisType.MAPNIK);
		}

		LocationToLocationVisualizerCanvasCreator creator = new LocationToLocationVisualizerCanvasCreator(
				shapeTable, nodeTable, edgeTable, set);
		GraphCanvas graphCanvas = creator.createGraphCanvas();
		Canvas<LocationNode> gisCanvas = creator.createGisCanvas();

		set.getGisSettings().setGisType(originalGisType);

		return new PortObject[] { CanvasUtils.getImage(set.isExportAsSvg(), graphCanvas),
				CanvasUtils.getImage(set.isExportAsSvg(), gisCanvas) };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return new PortObjectSpec[] { CanvasUtils.getImageSpec(set.isExportAsSvg()),
				CanvasUtils.getImageSpec(set.isExportAsSvg()) };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		set.saveSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		set.loadSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}
}
