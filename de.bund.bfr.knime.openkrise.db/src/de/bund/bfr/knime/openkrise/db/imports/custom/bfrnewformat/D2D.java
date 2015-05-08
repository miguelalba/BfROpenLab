package de.bund.bfr.knime.openkrise.db.imports.custom.bfrnewformat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import de.bund.bfr.knime.openkrise.db.DBKernel;

public class D2D {

	private HashMap<String, String> flexibles = new HashMap<>();
	
	private Delivery targetDelivery;
	public Delivery getTargetDelivery() {
		return targetDelivery;
	}

	public void setTargetDelivery(Delivery targetDelivery) {
		this.targetDelivery = targetDelivery;
	}

	public Delivery getIngredient() {
		return ingredient;
	}

	public void setIngredient(Delivery ingredient) {
		this.ingredient = ingredient;
	}

	private Delivery ingredient;

	public void addFlexibleField(String key, String value) {
		flexibles.put(key, value);
	}
	
	public void getId(Integer miDbId) throws Exception {
		getId(ingredient.getDbId(), targetDelivery.getLot().getDbId(), miDbId);
	}

	public void getId(Integer dbId, Integer dbPId, Integer miDbId) throws Exception {
		String sql = "SELECT " + DBKernel.delimitL("ID") + " FROM " + DBKernel.delimitL("ChargenVerbindungen") +
				" WHERE " + DBKernel.delimitL("Zutat") + "=" + dbId + " AND " + DBKernel.delimitL("Produkt") + "=" + dbPId;
		ResultSet rs = DBKernel.getResultSet(sql, false);
		Integer result = null;
		if (rs != null && rs.first()) {
			result = rs.getInt(1);
		}
		
		if (result != null) {
			DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("ChargenVerbindungen") + " SET " + DBKernel.delimitL("ImportSources") + "=CASEWHEN(INSTR(';" + miDbId + ";'," + DBKernel.delimitL("ImportSources") + ")=0,CONCAT(" + DBKernel.delimitL("ImportSources") + ", '" + miDbId + ";'), " + DBKernel.delimitL("ImportSources") + ") WHERE " + DBKernel.delimitL("ID") + "=" + result, false);
		}
		else {
			sql = "INSERT INTO " + DBKernel.delimitL("ChargenVerbindungen") +
					" (" + DBKernel.delimitL("Zutat") + "," + DBKernel.delimitL("Produkt") + "," + DBKernel.delimitL("ImportSources") +
					") VALUES (" + dbId + "," + dbPId + ",';" + miDbId + ";')";
			//DBKernel.sendRequest(sql, false);
			PreparedStatement ps = DBKernel.getDBConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			if (ps.executeUpdate() > 0) {
				result = DBKernel.getLastInsertedID(ps);
				
				// Further flexible cells
				if (result != null) {
					for (Entry<String, String> es : flexibles.entrySet()) {
						DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("ExtraFields") +
								" (" + DBKernel.delimitL("tablename") + "," + DBKernel.delimitL("id") + "," + DBKernel.delimitL("attribute") + "," + DBKernel.delimitL("value") +
								") VALUES ('ChargenVerbindungen'," + result + ",'" + es.getKey() + "','" + es.getValue() + "')", false);
					}
				}
			}
		}		

	}
	public HashSet<Integer> getIngredients(Integer lotId) throws SQLException {
		HashSet<Integer> result = new HashSet<>();
		// Checke, if there are already ingredients defined! Are they the same? No? make warning!
		String sql = "SELECT " + DBKernel.delimitL("Zutat") + " FROM " + DBKernel.delimitL("ChargenVerbindungen") +
				" WHERE " + DBKernel.delimitL("Produkt") + "=" + lotId;
		ResultSet rs = DBKernel.getResultSet(sql, false);
		if (rs != null && rs.first()) {
			do {
				result.add(rs.getInt("Zutat"));
			} while(rs.next());
		}	
		return result;
	}
}
