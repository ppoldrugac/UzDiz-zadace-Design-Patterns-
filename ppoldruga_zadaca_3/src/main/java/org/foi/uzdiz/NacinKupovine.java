package org.foi.uzdiz;

public enum NacinKupovine {
	BLAGAJNA("B"), 
	WEB_MOBILNA("WM"), 
	VLAK("V");

	private final String oznaka;

	NacinKupovine(String oznaka) {
		this.oznaka = oznaka;
	}

	public String getOznaka() {
		return oznaka;
	}

	public static NacinKupovine fromOznaka(String oznaka) {
		for (NacinKupovine nk : values()) {
			if (nk.getOznaka().equalsIgnoreCase(oznaka)) {
				return nk;
			}
		}
		return null;
	}
}
