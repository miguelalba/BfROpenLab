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
package de.bund.bfr.knime.nls.chart;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class NlsChartUtils {

	public static final String ID = "ID";
	public static final String SELECTED = "Selected";
	public static final String COLOR = "Color";
	public static final String SHAPE = "Shape";
	public static final String STATUS = "Status";

	private NlsChartUtils() {
	}

	public static Set<String> getVariables(Collection<Plotable> plotables) {
		Set<String> variables = new LinkedHashSet<>();

		for (Plotable plotable : plotables) {
			variables.addAll(plotable.getIndependentVariables().keySet());
		}

		return variables;
	}

	public static Set<String> getParameters(Collection<Plotable> plotables) {
		Set<String> parameters = new LinkedHashSet<>();

		for (Plotable plotable : plotables) {
			parameters.addAll(plotable.getParameters().keySet());
		}

		return parameters;
	}
}