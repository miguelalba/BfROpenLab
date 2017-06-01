package de.bund.bfr.knime.openkrise.db.imports.custom.bfrnewformat;

public class XlsDelivery {
	public static String BLOCK(String lang) {
		if (lang == null) return null;
		if (lang.equals("de")) return "Lieferung";
		else if (lang.equals("en")) return "Delivery";
		else return null;
	}
	public static String DELIVERY_DATE(String lang) {
		if (lang == null) return null;
		if (lang.equals("de")) return "Lieferdatum";
		else if (lang.equals("en")) return "DeliveryDate";
		else return null;
	}
	public static String DAY(String lang) {
		if (lang == null) return null;
		if (lang.equals("de")) return "Tag";
		else if (lang.equals("en")) return "Day";
		else return null;
	}
	public static String MONTH(String lang) {
		if (lang == null) return null;
		if (lang.equals("de")) return "Monat";
		else if (lang.equals("en")) return "Month";
		else return null;
	}
	public static String YEAR(String lang) {
		if (lang == null) return null;
		if (lang.equals("de")) return "Jahr";
		else if (lang.equals("en")) return "Year";
		else return null;
	}
	public static String AMOUNT(String lang) {
		if (lang == null) return null;
		if (lang.equals("de")) return "abgegebeneMenge";
		else if (lang.equals("en")) return "Amount";
		else return null;
	}
	public static String COMMENT(String lang) {
		if (lang == null) return null;
		if (lang.equals("de")) return "Kommentar";
		else if (lang.equals("en")) return "Comments";
		else return null;
	}

	public void addField(String fieldname, int index, String lang) {
		if (fieldname != null) {
			String s = fieldname.replaceAll("\\s+","");
			if (s.equals(DELIVERY_DATE(lang))) {
				dayCol = index;
				monthCol = index+1;
				yearCol = index+2;
			}
			else if (s.equals(DAY(lang))) dayCol = index;
			else if (s.equals(MONTH(lang))) monthCol = index;
			else if (s.equals(YEAR(lang))) yearCol = index;
			else if (s.startsWith(AMOUNT(lang))) amountCol = index;
			else if (s.equals(COMMENT(lang))) commentCol = index;
		}
	}

	private int startCol = -1;
	private int endCol = -1;
	private int dayCol = -1, monthCol = -1, yearCol = -1, amountCol = -1, commentCol = -1, chargenLinkCol = -1;

	public int getChargenLinkCol() {
		return chargenLinkCol;
	}
	public void setChargenLinkCol(int chargenLinkCol) {
		this.chargenLinkCol = chargenLinkCol;
	}
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
	public int getDayCol() {
		return dayCol;
	}
	public void setDayCol(int dayCol) {
		this.dayCol = dayCol;
	}
	public int getMonthCol() {
		return monthCol;
	}
	public void setMonthCol(int monthCol) {
		this.monthCol = monthCol;
	}
	public int getYearCol() {
		return yearCol;
	}
	public void setYearCol(int yearCol) {
		this.yearCol = yearCol;
	}
	public int getAmountCol() {
		return amountCol;
	}
	public void setAmountCol(int amountCol) {
		this.amountCol = amountCol;
	}
	public int getCommentCol() {
		return commentCol;
	}
	public void setCommentCol(int commentCol) {
		this.commentCol = commentCol;
	}
}
