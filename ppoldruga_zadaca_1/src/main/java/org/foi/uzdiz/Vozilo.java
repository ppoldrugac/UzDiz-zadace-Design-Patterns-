package org.foi.uzdiz;

import org.foi.uzdiz.builder.VoziloBuilderImpl;

public class Vozilo {
	private String oznaka;
	private String opis;
	private String proizvodjac;
	private int godinaProizvodnje;
	private String namjena;
	private String vrstaPrijevoza;
	private String vrstaPogona;
	private int maxBrzina;
	private String status;

	private double maxSnaga;
	private int brojSjedala;
	private int brojStajacihMjesta;
	private int brojBicikala;
	private int brojKreveta;
	private int brojAutomobila;
	private double nosivost;
	private double povrsina;
	private double zapremina;

	public Vozilo(VoziloBuilderImpl builder) {
		this.oznaka = builder.getOznaka();
		this.opis = builder.getOpis();
		this.proizvodjac = builder.getProizvodjac();
		this.godinaProizvodnje = builder.getGodinaProizvodnje();
		this.namjena = builder.getNamjena();
		this.vrstaPrijevoza = builder.getVrstaPrijevoza();
		this.vrstaPogona = builder.getVrstaPogona();
		this.maxBrzina = builder.getMaxBrzina();
		this.status = builder.getStatus();
		this.maxSnaga = builder.getMaxSnaga();
		this.brojSjedala = builder.getBrojSjedala();
		this.brojStajacihMjesta = builder.getBrojStajacihMjesta();
		this.brojBicikala = builder.getBrojBicikala();
		this.brojKreveta = builder.getBrojKreveta();
		this.brojAutomobila = builder.getBrojAutomobila();
		this.nosivost = builder.getNosivost();
		this.povrsina = builder.getPovrsina();
		this.zapremina = builder.getZapremina();
	}

	public String dohvatiOznaku() {
		return oznaka;
	}

	public String dohvatiOpis() {
		return opis;
	}

	public String dohvatiProizvodjaca() {
		return proizvodjac;
	}

	public int dohvatiGodinuProizvodnje() {
		return godinaProizvodnje;
	}

	public String dohvatiNamjenu() {
		return namjena;
	}

	public String dohvatiVrstuPrijevoza() {
		return vrstaPrijevoza;
	}

	public String dohvatiVrstuPogona() {
		return vrstaPogona;
	}

	public int dohvatiMaxBrzinu() {
		return maxBrzina;
	}

	public String dohvatiStatus() {
		return status;
	}

	public double dohvatiMaxSnagu() {
		return maxSnaga;
	}

	public int dohvatiBrojSjedala() {
		return brojSjedala;
	}

	public int dohvatiBrojStajacihMjesta() {
		return brojStajacihMjesta;
	}

	public int dohvatiBrojBicikala() {
		return brojBicikala;
	}

	public int dohvatiBrojKreveta() {
		return brojKreveta;
	}

	public int dohvatiBrojAutomobila() {
		return brojAutomobila;
	}

	public double dohvatiNosivost() {
		return nosivost;
	}

	public double dohvatiPovrsinu() {
		return povrsina;
	}

	public double dohvatiZapreminu() {
		return zapremina;
	}

	public boolean imaPogon() {
		return vrstaPogona != null && !vrstaPogona.equals("N");
	}
}
