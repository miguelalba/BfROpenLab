package de.bund.bfr.knime.openkrise.db.imports.custom.bfrnewformat;

public class XlsStation {
	public static String BLOCK_RECIPIENT(String lang) {
		if (lang == null) return null;
		if (lang.equals("de")) return "Empfänger";
		else if (lang.equals("en")) return "Recipient";
		else return null;
	}
	public static String BLOCK_SUPPLIER(String lang) {
		if (lang == null) return null;
		if (lang.equals("de")) return "Lieferant";
		else if (lang.equals("en")) return "Supplier";
		else return null;
	}
	public static String NAME(String lang) {
		if (lang == null) return null;
		if (lang.equals("de")) return "Name";
		else if (lang.equals("en")) return "Name";
		else return null;
	}
	public static String ADDRESS(String lang) {
		if (lang == null) return null;
		if (lang.equals("de")) return "Adresse";
		else if (lang.equals("en")) return "Address";
		else return null;
	}
	public static String COUNTRY(String lang) {
		if (lang == null) return null;
		if (lang.equals("de")) return "Land";
		else if (lang.equals("en")) return "Country";
		else return null;
	}
	public static String TOB(String lang) {
		if (lang == null) return null;
		if (lang.equals("de")) return "Betriebsart";
		else if (lang.equals("en")) return "TypeofBusiness";
		else return null;
	}
	
	public void addField(String fieldname, int index, String lang) {
		if (fieldname != null) {
			String s = fieldname.replaceAll("\\s+","");
			if (s.equals(NAME(lang))) nameCol = index;
			else if (s.startsWith(ADDRESS(lang))) addressCol = index;
			else if (s.equals(COUNTRY(lang))) countryCol = index;
			else if (s.equals(TOB(lang))) tobCol = index;
		}
	}
	private int startCol = -1;
	private int endCol = -1;
	private int nameCol = -1, addressCol = -1, countryCol = -1, tobCol = -1;
	public int getStartCol() {
		return startCol;
	}
	public void setStartCol(int startCol) {
		this.startCol = startCol;
	}
	public int getEndCol() {
		return endCol;
	}
	public void setEndCol(int endCol) {
		this.endCol = endCol;
	}
	public int getNameCol() {
		return nameCol;
	}
	public void setNameCol(int nameCol) {
		this.nameCol = nameCol;
	}
	public int getAddressCol() {
		return addressCol;
	}
	public void setAddressCol(int addressCol) {
		this.addressCol = addressCol;
	}
	public int getCountryCol() {
		return countryCol;
	}
	public void setCountryCol(int countryCol) {
		this.countryCol = countryCol;
	}
	public int getTobCol() {
		return tobCol;
	}
	public void setTobCol(int tobCol) {
		this.tobCol = tobCol;
	}
}
