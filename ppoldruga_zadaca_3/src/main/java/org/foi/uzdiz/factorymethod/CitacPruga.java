package org.foi.uzdiz.factorymethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.foi.uzdiz.Pruga;
import org.foi.uzdiz.SegmentPruge;
import org.foi.uzdiz.Stanica;
import org.foi.uzdiz.singleton.*;

public class CitacPruga implements CitacDatoteka {

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
						.greskaSDatotekama("Datoteka s podacima o željezničkim prugama je prazna!");
				return false;
			}

			if (!prviRedak.equals(
					"Stanica;Oznaka pruge;Vrsta stanice;Status stanice;Putnici ul/iz;Roba ut/ist;Kategorija pruge;"
							+ "Broj perona;Vrsta pruge;Broj kolosjeka;DO po osovini;DO po duznom m;Status pruge;Dužina;Vrijeme normalni vlak;Vrijeme ubrzani vlak;Vrijeme brzi vlak")) {
				UpraviteljGresaka.getInstance()
						.greskaSDatotekama("Neispravan informativni redak u datoteci stanica i pruga!");
				return false;
			}

			Stanica pocetnaStanica = null;
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
					String oznakaPruge = atributi[1];
					Pruga trenutnaPruga = Tvrtka.getInstance().dohvatiPrugu(oznakaPruge);

					if (trenutnaPruga == null) {
						trenutnaPruga = dodajIliAzurirajPrugu(atributi);
						Tvrtka.getInstance().dodajPrugu(trenutnaPruga);

						pocetnaStanica = null;
					}

					Stanica zavrsnaStanica = new Stanica(atributi[0].trim(), atributi[1].trim(), atributi[2].trim(),
							atributi[3].trim(), atributi[4].trim(), atributi[5].trim(),
							Integer.parseInt(atributi[7].trim()));

					if (pocetnaStanica == null) {
						pocetnaStanica = zavrsnaStanica;
						continue;
					}

					SegmentPruge segment = kreirajSegment(atributi, pocetnaStanica, zavrsnaStanica);
					trenutnaPruga.dodajSegment(segment);

					pocetnaStanica = zavrsnaStanica;
				}
			}

			provjeriMinimalniBrojStanica();
		} catch (Exception e) {
			UpraviteljGresaka.getInstance().greskaSDatotekama("Neuspješno učitavanje datoteke s željezničkim prugama!");
			return false;
		}
		return uspjeh;
	}

	private boolean provjeriIspravnostRedka(String[] atributi, String redak) {
		boolean ispravnaOznakaPruge = provjeriOznakuPruge(atributi[1], redak);
		boolean ispravnaKategorija = provjeriKategorijuPruge(atributi[6], redak);
		boolean ispravnaVrstaPrijevoza = provjeriVrstuPrijevoza(atributi[8], redak);
		boolean ispravanBrojKolosijeka = provjeriBrojKolosijeka(atributi[9], redak);
		boolean ispravnoOpterecenjeOsovina = provjeriDozvoljenoOpterecenjePoOsovini(atributi[10], redak);
		boolean ispravnoOpterecenjeDuzniM = provjeriDozvoljenoOpterecenjePoDuznomM(atributi[11], redak);
		boolean ispravanStatusPruge = provjeriStatusPruge(atributi[12], redak);
		boolean ispravnaDuzina = provjeriDuzinu(atributi[13], redak);
		boolean ispravnoVrijemeNormalni = provjeriVrijemeNormalniVlak(atributi[14], redak);
		boolean ispravnoVrijemeUbrzani = provjeriVrijemeOpcionalno(atributi[15], redak, "ubrzani vlak");
		boolean ispravnoVrijemeBrzi = provjeriVrijemeOpcionalno(atributi[16], redak, "brzi vlak");

		return ispravnaOznakaPruge && ispravnaKategorija && ispravnaVrstaPrijevoza && ispravanBrojKolosijeka
				&& ispravnoOpterecenjeOsovina && ispravnoOpterecenjeDuzniM && ispravanStatusPruge && ispravnaDuzina
				&& ispravnoVrijemeNormalni && ispravnoVrijemeUbrzani && ispravnoVrijemeBrzi;
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
					"Neispravna oznaka pruge: očekivan format npr. 'L100', 'R200', 'M500', ali je pronađeno: "
							+ oznakaPruge);
			return false;
		}
	}

	private boolean provjeriKategorijuPruge(String kategorija, String redak) {
		kategorija = kategorija.trim();
		if (kategorija.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Kategorija pruge nije unesena!");
			return false;
		}

		if (kategorija.equals("M") || kategorija.equals("L") || kategorija.equals("R")) {
			return true;
		} else {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Neispravna kategorija pruge: očekivano 'M', 'L' ili 'R', ali je pronađeno: " + kategorija);
			return false;
		}
	}

	private boolean provjeriVrstuPrijevoza(String prijevoz, String redak) {
		prijevoz = prijevoz.trim();
		if (prijevoz.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Vrsta prijevoza nije unesena!");
			return false;
		}

		if (prijevoz.equals("K") || prijevoz.equals("E")) {
			return true;
		} else {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Neispravna vrsta prijevoza: očekivano 'K' za klasično' ili 'E' za električno, ali je pronađeno: "
							+ prijevoz);
			return false;
		}
	}

	private boolean provjeriBrojKolosijeka(String brojKolosijeka, String redak) {
		try {
			int broj = Integer.parseInt(brojKolosijeka.trim());
			if (broj == 1 || broj == 2) {
				return true;
			} else {
				UpraviteljGresaka.getInstance().greskaURetku(redak,
						"Broj kolosijeka može biti samo 1 ili 2, pronađeno: " + broj);
				return false;
			}
		} catch (NumberFormatException e) {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Neispravan format broja kolosijeka: " + brojKolosijeka);
			return false;
		}
	}

	private boolean provjeriDozvoljenoOpterecenjePoOsovini(String osovina, String redak) {
		try {
			float opterecenje = Float.parseFloat(osovina.trim().replace(",", "."));
			if (opterecenje >= 10 && opterecenje <= 50) {
				return true;
			} else {
				UpraviteljGresaka.getInstance().greskaURetku(redak,
						"Opterećenje po osovini mora biti između 10 i 50 t/os, pronađeno: " + opterecenje);
				return false;
			}
		} catch (NumberFormatException e) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Neispravan format opterećenja po osovini: " + osovina);
			return false;
		}
	}

	private boolean provjeriDozvoljenoOpterecenjePoDuznomM(String duzniM, String redak) {
		try {
			float opterecenje = Float.parseFloat(duzniM.trim().replace(",", "."));
			if (opterecenje >= 2 && opterecenje <= 10) {
				return true;
			} else {
				UpraviteljGresaka.getInstance().greskaURetku(redak,
						"Opterećenje po dužnom metru mora biti između 2 i 10 t/m, pronađeno: " + opterecenje);
				return false;
			}
		} catch (NumberFormatException e) {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Neispravan format opterećenja po dužnom metru: " + duzniM);
			return false;
		}
	}

	private boolean provjeriStatusPruge(String statusPruge, String redak) {
		statusPruge = statusPruge.trim();
		if (statusPruge.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Status pruge nije unesen!");
			return false;
		}

		if (statusPruge.equals("I") || statusPruge.equals("U") || statusPruge.equals("Z")) {
			return true;
		} else {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Neispravan format statusa pruge: očekivano 'I' za ispravno, 'U' za u kvaru, ili 'Z' za zatvoreno, ali je pronađeno: "
							+ statusPruge);
			return false;
		}
	}

	private boolean provjeriDuzinu(String duzina, String redak) {
		try {
			float vrijednostDuzine = Float.parseFloat(duzina.trim().replace(",", "."));
			if (vrijednostDuzine >= 0 && vrijednostDuzine <= 999) {
				return true;
			} else {
				UpraviteljGresaka.getInstance().greskaURetku(redak,
						"Dužina pruge mora biti između 0 i 999 km, pronađeno: " + vrijednostDuzine);
				return false;
			}
		} catch (NumberFormatException e) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Neispravan format dužine pruge: " + duzina);
			return false;
		}
	}

	private boolean provjeriVrijemeNormalniVlak(String vrijeme, String redak) {
		vrijeme = vrijeme.trim();
		if (vrijeme.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Vrijeme za normalni vlak nije uneseno!");
			return false;
		}
		try {
			Integer.parseInt(vrijeme);
			return true;
		} catch (NumberFormatException e) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Vrijeme za normalni vlak nije broj: " + vrijeme);
			return false;
		}
	}

	private boolean provjeriVrijemeOpcionalno(String vrijeme, String redak, String tipVlaka) {
		vrijeme = vrijeme.trim();
		if (vrijeme.isEmpty() || vrijeme == null) {
			return true;
		}
		try {
			Integer.parseInt(vrijeme);
			return true;
		} catch (NumberFormatException e) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Vrijeme za " + tipVlaka + " nije broj: " + vrijeme);
			return false;
		}
	}

	private void provjeriMinimalniBrojStanica() {
		for (Pruga pruga : Tvrtka.getInstance().mapaPruga.values()) {
			if (pruga.dohvatiStaniceURedu().size() < 2) {
				UpraviteljGresaka.getInstance().greskaSDatotekama(
						"Greška: Pruga '" + pruga.dohvatiOznakuPruge() + "' mora imati minimalno dvije stanice!");

				Tvrtka.getInstance().mapaPruga.remove(pruga.dohvatiOznakuPruge());
			}
		}
	}

	private Pruga dodajIliAzurirajPrugu(String[] atributi) {
		String oznakaPruge = atributi[1];
		String kategorijaPruge = atributi[6];
		String vrstaPrijevoza = atributi[8];
		String statusPruge = atributi[12];

		Pruga pruga = Tvrtka.getInstance().dohvatiPrugu(oznakaPruge);

		if (pruga == null) {
			pruga = new Pruga(oznakaPruge, kategorijaPruge, vrstaPrijevoza, statusPruge);
			Tvrtka.getInstance().dodajPrugu(pruga);
		}

		return pruga;
	}

	private SegmentPruge kreirajSegment(String[] atributi, Stanica pocetnaStanica, Stanica zavrsnaStanica) {
		int brojKolosijeka = Integer.parseInt(atributi[9].trim());
		float doPoOsovini = Float.parseFloat(atributi[10].trim().replace(",", "."));
		float doPoDuznomM = Float.parseFloat(atributi[11].trim().replace(",", "."));
		float duzina = Float.parseFloat(atributi[13].trim().replace(",", "."));
		int vrijemeNormalniVlak = Integer.parseInt(atributi[14].trim());
		Integer vrijemeUbrzaniVlak = atributi[15].isEmpty() ? null : Integer.parseInt(atributi[15].trim());
		Integer vrijemeBrziVlak = atributi[16].isEmpty() ? null : Integer.parseInt(atributi[16].trim());
	

		return new SegmentPruge(pocetnaStanica, zavrsnaStanica, brojKolosijeka, duzina, doPoOsovini, doPoDuznomM,
				vrijemeNormalniVlak, vrijemeUbrzaniVlak, vrijemeBrziVlak);
	}
}