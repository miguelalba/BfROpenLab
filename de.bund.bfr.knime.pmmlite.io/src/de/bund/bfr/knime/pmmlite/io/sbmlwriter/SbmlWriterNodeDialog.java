/*******************************************************************************
 * Copyright (c) 2016 German Federal Institute for Risk Assessment (BfR)
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
package de.bund.bfr.knime.pmmlite.io.sbmlwriter;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "SbmlWriter" Node.
 * 
 * @author Christian Thoens
 */
public class SbmlWriterNodeDialog extends DefaultNodeSettingsPane {

	private static final String OUT_HISTORY = "Out History";

	/**
	 * New pane for configuring the SbmlWriter node.
	 */
	protected SbmlWriterNodeDialog() {
		addDialogComponent(
				new DialogComponentFileChooser(new SettingsModelString(SbmlWriterNodeModel.CFG_OUT_PATH, null),
						OUT_HISTORY, JFileChooser.SAVE_DIALOG, true));
	}
}