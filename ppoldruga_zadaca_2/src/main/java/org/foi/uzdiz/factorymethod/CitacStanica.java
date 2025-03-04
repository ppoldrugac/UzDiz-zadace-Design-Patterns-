package org.foi.uzdiz.factorymethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.foi.uzdiz.Stanica;
import org.foi.uzdiz.singleton.*;

public class CitacStanica implements CitacDatoteka {

	@Override
	public boolean ucitajPodatke(String nazivDatoteke) {

		boolean uspjeh = true;

		UpraviteljGresaka.getInstance().resetirajBrojacDatoteke();
		File datoteka = new File(nazivDatoteke);

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(datoteka), "UTF-8"))) {
			String prviRedak = reader.readLine();

			if (prviRedak != null && prviRedak.startsWith("\uFEFF")) {
				prviRedak = prviRedak.substring(1);
			}

			if (prviRedak == null) {
				UpraviteljGresaka.getInstance()
						.greskaSDatotekama("Datoteka s podacima o željezničkim stanicama je prazna!");
				return false;
			}

			if (!prviRedak.equals(
					"Stanica;Oznaka pruge;Vrsta stanice;Status stanice;Putnici ul/iz;Roba ut/ist;Kategorija pruge;"
							+ "Broj perona;Vrsta pruge;Broj kolosjeka;DO po osovini;DO po duznom m;Status pruge;Dužina;Vrijeme normalni vlak;Vrijeme ubrzani vlak;Vrijeme brzi vlak")) {
				UpraviteljGresaka.getInstance().greskaSDatotekama("Neispravan informativni redak u datoteci stanica!");
				return false;
			}

			String redak;
			while ((redak = reader.readLine()) != null) {
				redak = redak.trim();

				if (redak.isEmpty() || redak.startsWith("#") || redak.replace(";", "").isEmpty()) {
					continue;
				}

				String[] atributi = redak.split(";", -1);
				if (atributi.length != 17) {
					UpraviteljGresaka.getInstance().greskaURetku(redak, "Neispravan broj atributa u retku.");
					continue;
				}

				boolean ispravanRedak = provjeriIspravnostRedka(atributi, redak);
				if (ispravanRedak) {
					Stanica stanica = kreirajStanicu(atributi);
					if (stanica != null) {
						Tvrtka.getInstance().dodajStanicu(stanica);
					}
				}
			}
		} catch (Exception e) {
			UpraviteljGresaka.getInstance()
					.greskaSDatotekama("Neuspješno učitavanje datoteke s željezničkim stanicama!");
			return false;
		}
		return uspjeh;
	}

	private boolean provjeriIspravnostRedka(String[] atributi, String redak) {
		boolean ispravanNazivStanice = provjeriNazivStanice(atributi[0], redak);
		boolean ispravnaOznakaPruge = provjeriOznakuPruge(atributi[1], redak);
		boolean ispravnaVrstaStanice = provjeriVrstuStanice(atributi[2], redak);
		boolean ispravanStatusStanice = provjeriStatusStanice(atributi[3], redak);
		boolean ispravnaAktivnost = provjeriAktivnosti(atributi[4], atributi[5], redak);
		boolean ispravanBrojPerona = provjeriBrojPerona(atributi[7], redak);

		return ispravanNazivStanice && ispravnaOznakaPruge && ispravnaVrstaStanice && ispravanStatusStanice
				&& ispravnaAktivnost && ispravanBrojPerona;
	}

	private boolean provjeriNazivStanice(String nazivStanice, String redak) {
		nazivStanice = nazivStanice.trim();
		if (nazivStanice.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Naziv stanice nije unesen!");
			return false;
		}

		Pattern pattern = Pattern.compile("^[a-zA-ZčćžšđČĆŽŠĐ]+([ -][a-zA-ZčćžšđČĆŽŠĐ]+)*$");
		Matcher matcher = pattern.matcher(nazivStanice);
		if (matcher.matches()) {
			return true;
		} else {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Naziv stanice sadrži nevažeće znakove: " + nazivStanice);
			return false;
		}
	}

	private boolean provjeriOznakuPruge(String oznakaPruge, String redak) {
		oznakaPruge = oznakaPruge.trim();
		if (oznakaPruge.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Oznaka pruge nije unesena!");
			return false;
		}

		Pattern pattern = Pattern.compile("^[A-Z]\\d{3}$");
		Matcher matcher = pattern.matcher(oznakaPruge);
		if (matcher.matches()) {
			return true;
		} else {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Neispravna oznaka pruge: očekivan format oznake pruge je npr. 'L100' ili 'R200' ili 'M500', ali je pronađeno"
							+ oznakaPruge);
			return false;
		}
	}

	private boolean provjeriVrstuStanice(String vrsta, String redak) {
		vrsta = vrsta.trim();
		if (vrsta.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Vrsta stanice nije unesena!");
			return false;
		}

		if (vrsta.equals("kol.") || vrsta.equals("staj.")) {
			return true;
		} else {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Neispravna vrsta stanice: očekivano 'kol.' ili 'staj.', ali je pronađeno:" + vrsta);
			return false;
		}
	}

	private boolean provjeriStatusStanice(String status, String redak) {
		status = status.trim();
		if (status.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Status stanice nije unesen!");
			return false;
		}

		if (status.equals("O") || status.equals("Z")) {
			return true;
		} else {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Neispravan status stanice: očekivano 'O' ili 'Z', ali je pronađeno:" + status);
			return false;
		}
	}

	private boolean provjeriAktivnosti(String putnici, String roba, String redak) {
		putnici = putnici.trim();
		roba = roba.trim();
		if (putnici.isEmpty() || roba.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Aktivnosti putnici/roba nisu unesene!");
			return false;
		}

		boolean putniciValid = putnici.equals("DA") || putnici.equals("NE");
		boolean robaValid = roba.equals("DA") || roba.equals("NE");

		if (putniciValid && robaValid) {
			return true;
		} else {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Neispravna vrijednost aktivnosti putnici/roba (očekivano 'DA' ili 'NE'), ali je pronađeno: Putnici - "
							+ putnici + ", Roba - " + roba);
			return false;
		}
	}

	private boolean provjeriBrojPerona(String brojPerona, String redak) {
		brojPerona = brojPerona.trim();
		if (brojPerona.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Broj perona nije unesen!");
		}

		try {
			int broj = Integer.parseInt(brojPerona);
			if (broj >= 1 && broj <= 99) {
				return true;
			} else {
				UpraviteljGresaka.getInstance().greskaURetku(redak,
						"Broj perona izvan dozvoljenog raspona (1-99): " + broj);
				return false;
			}
		} catch (NumberFormatException e) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Neispravan format broja perona: " + brojPerona);
			return false;
		}
	}

	private Stanica kreirajStanicu(String[] atributi) {
		try {
			String nazivStanice = atributi[0];
			String oznakaPruge = atributi[1];
			String vrstaStanice = atributi[2];
			String statusStanice = atributi[3];
			String putniciUlIz = atributi[4];
			String robaUtIst = atributi[5];
			int brojPerona = Integer.parseInt(atributi[7]);

			return new Stanica(nazivStanice, oznakaPruge, vrstaStanice, statusStanice, putniciUlIz, robaUtIst,
					brojPerona);
		} catch (Exception e) {
			UpraviteljGresaka.getInstance().sustavskaGreska(e);
			return null;
		}
	}
}