package org.foi.uzdiz;

import java.util.List;

public class Kompozicija {
	private String oznakaKompozicije;
	private List<Vozilo> prijevoznaSredstva;
	private List<String> uloge;

	public Kompozicija(String oznakaKompozicije, List<Vozilo> prijevoznaSredstva, List<String> uloge) {
		this.oznakaKompozicije = oznakaKompozicije;
		this.prijevoznaSredstva = prijevoznaSredstva;
		this.uloge = uloge;

	}

	public String dohvatiOznakuKompozicije() {
		return oznakaKompozicije;
	}

	public List<Vozilo> dohvatiPrijevoznaSredstva() {
		return prijevoznaSredstva;
	}

	public List<String> dohvatiUloge() {
		return uloge;
	}
}
