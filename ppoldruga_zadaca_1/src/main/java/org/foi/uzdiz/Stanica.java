package org.foi.uzdiz;

public class Stanica {
	private String nazivStanice;
	private String oznakaPruge;
	private String vrstaStanice;
	private String statusStanice; 
	private String putniciUlIz; 
	private String robaUtIst; 
	private int brojPerona;

	public Stanica(String nazivStanice, String oznakaPruge, String vrstaStanice, String statusStanice,
			String putniciUlIz, String robaUtIst, int brojPerona) {
		this.nazivStanice = nazivStanice;
		this.oznakaPruge = oznakaPruge;
		this.vrstaStanice = vrstaStanice;
		this.statusStanice = statusStanice;
		this.putniciUlIz = putniciUlIz;
		this.robaUtIst = robaUtIst;
		this.brojPerona = brojPerona;

	}

	public String dohvatiNazivStanice() {
		return nazivStanice;
	}

	public String dohvatiOznakuPruge() {
		return oznakaPruge;
	}

	public String dohvatiVrstuStanice() {
		return vrstaStanice;
	}

	public String dohvatiStatusStanice() {
		return statusStanice;
	}

	public String dohvatiPutniciUlIz() {
		return putniciUlIz;
	}

	public String dohvatiRobaUtIst() {
		return robaUtIst;
	}

	public int dohvatiBrojPerona() {
		return brojPerona;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;
	    Stanica stanica = (Stanica) obj;
	    return nazivStanice.equalsIgnoreCase(stanica.nazivStanice);
	}

	@Override
	public int hashCode() {
	    return nazivStanice.toLowerCase().hashCode();
	}

}
