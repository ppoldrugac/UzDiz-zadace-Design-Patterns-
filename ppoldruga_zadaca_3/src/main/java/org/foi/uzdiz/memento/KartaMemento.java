package org.foi.uzdiz.memento;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class KartaMemento {
	private int redniBrojKarte;
	private String oznakaVlaka;
	private String polaznaStanica;
	private String odredisnaStanica;
	private String datum;
	private String nacinKupovine;
	private int trajanjeVoznje;
	private double cijenaKarte;
	private LocalDateTime datumKupovine;
	private String vrijemePolaska;
	private String vrijemeDolaska;
	private String relacija;
	private List<String> popusti;

	public KartaMemento(int redniBrojKarte, String oznakaVlaka, String polaznaStanica, String odredisnaStanica,
			String datum, String nacinKupovine, int trajanjeVoznje, double cijenaKarte, String vrijemePolaska,
			String vrijemeDolaska, String relacija, List<String> popusti) {
		this.redniBrojKarte = redniBrojKarte;
		this.oznakaVlaka = oznakaVlaka;
		this.polaznaStanica = polaznaStanica;
		this.odredisnaStanica = odredisnaStanica;
		this.datum = datum;
		this.nacinKupovine = nacinKupovine;
		this.trajanjeVoznje = trajanjeVoznje;
		this.cijenaKarte = cijenaKarte;
		this.datumKupovine = LocalDateTime.now();
		this.vrijemePolaska = vrijemePolaska;
		this.vrijemeDolaska = vrijemeDolaska;
		this.relacija = relacija;
		this.popusti = popusti;
	}

	public int dohvatiRedniBrojKarte() {
		return redniBrojKarte;
	}

	public String dohvatiOznakuVlaka() {
		return oznakaVlaka;
	}

	public String dohvatiPolaznuStanicu() {
		return polaznaStanica;
	}

	public String dohvatiOdredisnuStanicu() {
		return odredisnaStanica;
	}

	public String dohvatiDatum() {
		return datum;
	}

	public String dohvatiNacinKupovine() {
		return nacinKupovine;
	}

	public int dohvatiTrajanjeVoznje() {
		return trajanjeVoznje;
	}

	public double dohvatiCijenuKarte() {
		return cijenaKarte;
	}

	public String dohvatiDatumKupovine() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm ");
		return datumKupovine.format(formatter);
	}

	public String dohvatiVrijemePolaska() {
		return vrijemePolaska;
	}

	public String dohvatiVrijemeDolaska() {
		return vrijemeDolaska;
	}

	public String dohvatiRelaciju() {
		return relacija;
	}

	public List<String> dohvatiPopuste() {
		return popusti;
	}
}
