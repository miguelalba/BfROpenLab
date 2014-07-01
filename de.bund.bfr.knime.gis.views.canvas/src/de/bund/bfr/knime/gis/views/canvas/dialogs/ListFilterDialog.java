/*******************************************************************************
 * Copyright (c) 2014 Federal Institute for Risk Assessment (BfR), Germany 
 * 
 * Developers and contributors are 
 * Christian Thoens (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
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
package de.bund.bfr.knime.gis.views.canvas.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import de.bund.bfr.knime.UI;

public class ListFilterDialog<T> extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private List<T> elements;

	private boolean approved;
	private Set<T> filtered;

	private JCheckBox allBox;
	private JList<T> list;
	private JButton okButton;
	private JButton cancelButton;

	public ListFilterDialog(Component parent, List<T> elements) {
		super(SwingUtilities.getWindowAncestor(parent), "Filter",
				DEFAULT_MODALITY_TYPE);
		this.elements = elements;

		allBox = new JCheckBox("Select All");
		allBox.setSelected(true);
		allBox.addActionListener(this);
		list = new JList<>(new Vector<>(elements));
		list.setEnabled(false);
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);

		setLayout(new BorderLayout());
		add(UI.createWestPanel(allBox), BorderLayout.NORTH);
		add(new JScrollPane(list,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER),
				BorderLayout.CENTER);
		add(UI.createEastPanel(UI.createHorizontalPanel(okButton, cancelButton)),
				BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(parent);

		approved = false;
		filtered = null;
	}

	public boolean isApproved() {
		return approved;
	}

	public Set<T> getFiltered() {
		return filtered;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okButton) {
			approved = true;

			if (allBox.isSelected()) {
				filtered = new LinkedHashSet<>(elements);
			} else {
				filtered = new LinkedHashSet<>(list.getSelectedValuesList());
			}

			dispose();
		} else if (e.getSource() == cancelButton) {
			dispose();
		} else if (e.getSource() == allBox) {
			list.setEnabled(!allBox.isSelected());
		}
	}
}