/*******************************************************************************
 * Copyright (c) 2018 German Federal Institute for Risk Assessment (BfR)
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
/**
 * This class is generated by jOOQ
 */
package de.bund.bfr.knime.openkrise.db.generated.public_.tables.records;


import de.bund.bfr.knime.openkrise.db.generated.public_.tables.Chargenverbindungen;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ChargenverbindungenRecord extends UpdatableRecordImpl<ChargenverbindungenRecord> implements Record6<Integer, Integer, Integer, Double, String, String> {

	private static final long serialVersionUID = 2008536847;

	/**
	 * Setter for <code>PUBLIC.ChargenVerbindungen.ID</code>.
	 */
	public void setId(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>PUBLIC.ChargenVerbindungen.ID</code>.
	 */
	public Integer getId() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>PUBLIC.ChargenVerbindungen.Zutat</code>.
	 */
	public void setZutat(Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>PUBLIC.ChargenVerbindungen.Zutat</code>.
	 */
	public Integer getZutat() {
		return (Integer) getValue(1);
	}

	/**
	 * Setter for <code>PUBLIC.ChargenVerbindungen.Produkt</code>.
	 */
	public void setProdukt(Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>PUBLIC.ChargenVerbindungen.Produkt</code>.
	 */
	public Integer getProdukt() {
		return (Integer) getValue(2);
	}

	/**
	 * Setter for <code>PUBLIC.ChargenVerbindungen.MixtureRatio</code>.
	 */
	public void setMixtureratio(Double value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>PUBLIC.ChargenVerbindungen.MixtureRatio</code>.
	 */
	public Double getMixtureratio() {
		return (Double) getValue(3);
	}

	/**
	 * Setter for <code>PUBLIC.ChargenVerbindungen.ImportSources</code>.
	 */
	public void setImportsources(String value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>PUBLIC.ChargenVerbindungen.ImportSources</code>.
	 */
	public String getImportsources() {
		return (String) getValue(4);
	}

	/**
	 * Setter for <code>PUBLIC.ChargenVerbindungen.Kommentar</code>.
	 */
	public void setKommentar(String value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>PUBLIC.ChargenVerbindungen.Kommentar</code>.
	 */
	public String getKommentar() {
		return (String) getValue(5);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Integer> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record6 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row6<Integer, Integer, Integer, Double, String, String> fieldsRow() {
		return (Row6) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row6<Integer, Integer, Integer, Double, String, String> valuesRow() {
		return (Row6) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return Chargenverbindungen.CHARGENVERBINDUNGEN.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return Chargenverbindungen.CHARGENVERBINDUNGEN.ZUTAT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field3() {
		return Chargenverbindungen.CHARGENVERBINDUNGEN.PRODUKT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Double> field4() {
		return Chargenverbindungen.CHARGENVERBINDUNGEN.MIXTURERATIO;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field5() {
		return Chargenverbindungen.CHARGENVERBINDUNGEN.IMPORTSOURCES;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field6() {
		return Chargenverbindungen.CHARGENVERBINDUNGEN.KOMMENTAR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value2() {
		return getZutat();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value3() {
		return getProdukt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double value4() {
		return getMixtureratio();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value5() {
		return getImportsources();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value6() {
		return getKommentar();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChargenverbindungenRecord value1(Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChargenverbindungenRecord value2(Integer value) {
		setZutat(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChargenverbindungenRecord value3(Integer value) {
		setProdukt(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChargenverbindungenRecord value4(Double value) {
		setMixtureratio(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChargenverbindungenRecord value5(String value) {
		setImportsources(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChargenverbindungenRecord value6(String value) {
		setKommentar(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChargenverbindungenRecord values(Integer value1, Integer value2, Integer value3, Double value4, String value5, String value6) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		value6(value6);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ChargenverbindungenRecord
	 */
	public ChargenverbindungenRecord() {
		super(Chargenverbindungen.CHARGENVERBINDUNGEN);
	}

	/**
	 * Create a detached, initialised ChargenverbindungenRecord
	 */
	public ChargenverbindungenRecord(Integer id, Integer zutat, Integer produkt, Double mixtureratio, String importsources, String kommentar) {
		super(Chargenverbindungen.CHARGENVERBINDUNGEN);

		setValue(0, id);
		setValue(1, zutat);
		setValue(2, produkt);
		setValue(3, mixtureratio);
		setValue(4, importsources);
		setValue(5, kommentar);
	}
}