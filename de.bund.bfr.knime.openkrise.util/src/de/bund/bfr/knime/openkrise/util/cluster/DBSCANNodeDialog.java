/*******************************************************************************
 * Copyright (c) 2014 Federal Institute for Risk Assessment (BfR), Germany 
 * 
 * Developers and contributors are 
 * Armin A. Weiser (BfR)
 * Christian Thoens (BfR)
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
package de.bund.bfr.knime.openkrise.util.cluster;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.UI;
import de.bund.bfr.knime.gis.views.canvas.PropertySchema;
import de.bund.bfr.knime.gis.views.canvas.dialogs.HighlightDialog;
import de.bund.bfr.knime.gis.views.canvas.highlighting.AndOrHighlightCondition;
import de.bund.bfr.knime.openkrise.TracingUtils;
import de.bund.bfr.knime.ui.DoubleTextField;
import de.bund.bfr.knime.ui.IntTextField;

/**
 * <code>NodeDialog</code> for the "DBSCAN" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author BfR
 */
public class DBSCANNodeDialog extends NodeDialogPane implements ActionListener,
		ItemListener {

	private DBSCANNSettings set;
	private PropertySchema schema;

	private JComboBox<String> modelBox;
	private JButton filterButton;
	private IntTextField minPointsField;
	private DoubleTextField maxDistField;
	private IntTextField numClustersField;

	private JPanel panel;

	/**
	 * New pane for configuring the DBSCAN node.
	 */
	public DBSCANNodeDialog() {
		set = new DBSCANNSettings();

		modelBox = new JComboBox<>(DBSCANNSettings.MODEL_CHOICES);
		modelBox.addItemListener(this);
		filterButton = new JButton("Set Filter");
		filterButton.addActionListener(this);
		minPointsField = new IntTextField(false, 5);
		minPointsField.setMinValue(1);
		maxDistField = new DoubleTextField(false, 5);
		maxDistField.setMinValue(0);
		numClustersField = new IntTextField(false, 5);
		numClustersField.setMinValue(1);

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		addTab("Options", UI.createNorthPanel(panel));
	}

	@Override
	protected void loadSettingsFrom(final NodeSettingsRO settings,
			final DataTableSpec[] specs) throws NotConfigurableException {
		schema = new PropertySchema(TracingUtils.getTableColumns(specs[0]));

		set.loadSettings(settings);
		modelBox.setSelectedItem(set.getModel());
		minPointsField.setValue(set.getMinPoints());
		maxDistField.setValue(set.getMaxDistance());
		numClustersField.setValue(set.getNumClusters());
		updatePanel();
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		set.setModel((String) modelBox.getSelectedItem());

		if (set.getModel().equals(DBSCANNSettings.MODEL_DBSCAN)) {
			if (!minPointsField.isValueValid()) {
				throw new InvalidSettingsException(
						"Invalid: Min Number of Points per Cluster");
			}

			if (!maxDistField.isValueValid()) {
				throw new InvalidSettingsException(
						"Invalid: Max Neighborhood Distance (km)");
			}

			set.setMinPoints(minPointsField.getValue());
			set.setMaxDistance(maxDistField.getValue());
		} else if (set.getModel().equals(DBSCANNSettings.MODEL_K_MEANS)) {
			if (!numClustersField.isValueValid()) {
				throw new InvalidSettingsException(
						"Invalid: Number of Clusters");
			}

			set.setNumClusters(numClustersField.getValue());
		}

		set.saveSettings(settings);
	}

	private void updatePanel() {
		String model = (String) modelBox.getSelectedItem();

		panel.removeAll();
		panel.add(UI.createOptionsPanel("Options",
				Arrays.asList(new JLabel("Cluster Algorithm:"), filterButton),
				Arrays.asList(modelBox, new JLabel())));

		if (model.equals(DBSCANNSettings.MODEL_DBSCAN)) {
			panel.add(UI.createOptionsPanel("Algorithm Options", Arrays.asList(
					new JLabel("Min Number of Points per Cluster:"),
					new JLabel("Max Neighborhood Distance (km):")), Arrays
					.asList(minPointsField, maxDistField)));
		} else if (model.equals(DBSCANNSettings.MODEL_K_MEANS)) {
			panel.add(UI.createOptionsPanel("Options",
					Arrays.asList(new JLabel("Number of Clusters")),
					Arrays.asList(numClustersField)));
		}

		panel.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == filterButton) {
			HighlightDialog dialog = HighlightDialog.createFilterDialog(
					filterButton, schema, set.getFilter());

			dialog.setLocationRelativeTo(filterButton);
			dialog.setVisible(true);

			if (dialog.isApproved()) {
				set.setFilter((AndOrHighlightCondition) dialog
						.getHighlightCondition());
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == modelBox) {
			updatePanel();
		}
	}
}
