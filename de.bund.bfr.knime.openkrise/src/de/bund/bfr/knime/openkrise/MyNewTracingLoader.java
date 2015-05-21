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
package de.bund.bfr.knime.openkrise;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import de.bund.bfr.knime.openkrise.db.DBKernel;
import de.bund.bfr.knime.openkrise.db.MyDBI;

public class MyNewTracingLoader {

	private static HashMap<Integer, MyDelivery> allDeliveries;

	public static MyNewTracing getNewTracingModel(MyDBI myDBi, Connection conn) {
		allDeliveries = new HashMap<>();
		// Firstly: get all deliveries
		String sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Empf�nger")
				+ "," + DBKernel.delimitL("Station") + "," + DBKernel.delimitL("dd_day") + ","
				+ DBKernel.delimitL("dd_month") + "," + DBKernel.delimitL("dd_year") + " FROM "
				+ DBKernel.delimitL("Lieferungen") + " LEFT JOIN " + DBKernel.delimitL("Chargen")
				+ " ON " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Charge")
				+ "=" + DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("ID")
				+ " LEFT JOIN " + DBKernel.delimitL("Produktkatalog") + " ON "
				+ DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("Artikel") + "="
				+ DBKernel.delimitL("Produktkatalog") + "." + DBKernel.delimitL("ID");
		try {
			ResultSet rs = DBKernel.getResultSet(conn, sql, false);
			if (rs != null && rs.first()) {
				do {
					MyDelivery md = new MyDelivery(rs.getInt("ID"), rs.getInt("Station"),
							rs.getInt("Empf�nger"), (Integer) rs.getObject("dd_day"),
							(Integer) rs.getObject("dd_month"), (Integer) rs.getObject("dd_year"));
					allDeliveries.put(rs.getInt("ID"), md);
				} while (rs.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Secondly: prev/next deliveries
		sql = "SELECT " + DBKernel.delimitL("ZutatLieferungen") + "." + DBKernel.delimitL("ID")
				+ "," + DBKernel.delimitL("ProduktLieferungen") + "." + DBKernel.delimitL("ID")
				+ " FROM " + DBKernel.delimitL("ChargenVerbindungen") + " LEFT JOIN "
				+ DBKernel.delimitL("Lieferungen") + " AS " + DBKernel.delimitL("ZutatLieferungen")
				+ " ON " + DBKernel.delimitL("ZutatLieferungen") + "." + DBKernel.delimitL("ID")
				+ "=" + DBKernel.delimitL("ChargenVerbindungen") + "." + DBKernel.delimitL("Zutat")
				+ " LEFT JOIN " + DBKernel.delimitL("Lieferungen") + " AS "
				+ DBKernel.delimitL("ProduktLieferungen") + " ON "
				+ DBKernel.delimitL("ProduktLieferungen") + "." + DBKernel.delimitL("Charge") + "="
				+ DBKernel.delimitL("ChargenVerbindungen") + "." + DBKernel.delimitL("Produkt");
		try {
			ResultSet rs = DBKernel.getResultSet(conn, sql, false);
			if (rs != null && rs.first()) {
				do {
					MyDelivery mdZ = allDeliveries.get(rs.getInt(1));
					MyDelivery mdP = allDeliveries.get(rs.getInt(2));
					if (mdZ != null && mdP != null) {
						mdZ.getAllNextIDs().add(mdP.getId());
						mdP.getAllPreviousIDs().add(mdZ.getId());
					}
					// if (mdP != null) mdP.addPrevious(mdZ.getId());
				} while (rs.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		MyNewTracing mnt = new MyNewTracing(allDeliveries);
		return mnt;
	}
}
