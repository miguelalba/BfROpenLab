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
package de.bund.bfr.knime.openkrise.db.generated.public_;


import de.bund.bfr.knime.openkrise.db.generated.public_.tables.Chargen;
import de.bund.bfr.knime.openkrise.db.generated.public_.tables.Chargenverbindungen;
import de.bund.bfr.knime.openkrise.db.generated.public_.tables.Extrafields;
import de.bund.bfr.knime.openkrise.db.generated.public_.tables.Importmetadata;
import de.bund.bfr.knime.openkrise.db.generated.public_.tables.Lieferungen;
import de.bund.bfr.knime.openkrise.db.generated.public_.tables.Lookups;
import de.bund.bfr.knime.openkrise.db.generated.public_.tables.Produktkatalog;
import de.bund.bfr.knime.openkrise.db.generated.public_.tables.Station;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in PUBLIC
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

	/**
	 * The table PUBLIC.Chargen
	 */
	public static final Chargen CHARGEN = de.bund.bfr.knime.openkrise.db.generated.public_.tables.Chargen.CHARGEN;

	/**
	 * The table PUBLIC.ChargenVerbindungen
	 */
	public static final Chargenverbindungen CHARGENVERBINDUNGEN = de.bund.bfr.knime.openkrise.db.generated.public_.tables.Chargenverbindungen.CHARGENVERBINDUNGEN;

	/**
	 * The table PUBLIC.ExtraFields
	 */
	public static final Extrafields EXTRAFIELDS = de.bund.bfr.knime.openkrise.db.generated.public_.tables.Extrafields.EXTRAFIELDS;

	/**
	 * The table PUBLIC.ImportMetadata
	 */
	public static final Importmetadata IMPORTMETADATA = de.bund.bfr.knime.openkrise.db.generated.public_.tables.Importmetadata.IMPORTMETADATA;

	/**
	 * The table PUBLIC.Lieferungen
	 */
	public static final Lieferungen LIEFERUNGEN = de.bund.bfr.knime.openkrise.db.generated.public_.tables.Lieferungen.LIEFERUNGEN;

	/**
	 * The table PUBLIC.LookUps
	 */
	public static final Lookups LOOKUPS = de.bund.bfr.knime.openkrise.db.generated.public_.tables.Lookups.LOOKUPS;

	/**
	 * The table PUBLIC.Produktkatalog
	 */
	public static final Produktkatalog PRODUKTKATALOG = de.bund.bfr.knime.openkrise.db.generated.public_.tables.Produktkatalog.PRODUKTKATALOG;

	/**
	 * The table PUBLIC.Station
	 */
	public static final Station STATION = de.bund.bfr.knime.openkrise.db.generated.public_.tables.Station.STATION;
}