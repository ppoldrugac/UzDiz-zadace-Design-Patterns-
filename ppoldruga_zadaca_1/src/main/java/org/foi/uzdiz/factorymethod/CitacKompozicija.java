package org.foi.uzdiz.factorymethod;

import org.foi.uzdiz.Kompozicija;
import org.foi.uzdiz.Vozilo;
import org.foi.uzdiz.builder.KompozicijaBuilderImpl;
import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CitacKompozicija implements CitacDatoteka {

	private List<Vozilo> trenutnaPrijevoznaSredstva = new ArrayList<>();
	private List<String> trenutneUloge = new ArrayList<>();
	private String trenutnaOznakaKompozicije = null;

	@Override
	public void ucitajPodatke(String nazivDatoteke) {
		File datoteka = new File(nazivDatoteke);

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(datoteka), "UTF-8"))) {

			String prviRedak = pocetneProvjere(reader);
			if (prviRedak == null) {
				return; 
			}

			String redak;
			while ((redak = reader.readLine()) != null) {
				redak = redak.trim();

				if (redak.isEmpty() || redak.replace(";", "").isEmpty()) {

					if (!trenutnaPrijevoznaSredstva.isEmpty() && !trenutneUloge.isEmpty()) {
						if (provjeriIspravnostKompozicije()) {
							kreirajKompoziciju(trenutnaOznakaKompozicije, trenutnaPrijevoznaSredstva, trenutneUloge,
									new KompozicijaBuilderImpl());
						}
					}
					resetirajPrivremenePodatke();
					continue;
				}


				String[] atributi = parsirajIRazvrstajRedak(redak);
				if (atributi == null) {
					continue; 
				}

				dodajPodatkeUPrivremeneListe(atributi);
			}

			if (trenutnaOznakaKompozicije != null && !trenutnaPrijevoznaSredstva.isEmpty()) {
				if (provjeriIspravnostKompozicije()) {
					kreirajKompoziciju(trenutnaOznakaKompozicije, trenutnaPrijevoznaSredstva, trenutneUloge,
							new KompozicijaBuilderImpl());
				}
			}

		} catch (Exception e) {
			UpraviteljGresaka.getInstance()
					.greskaSDatotekama("Neuspješno učitavanje datoteke s kompozicijama! Greška: " + e.getMessage());
		}
	}

	private String pocetneProvjere(BufferedReader reader) throws Exception {
		String prviRedak = reader.readLine();

		if (prviRedak != null && prviRedak.startsWith("\uFEFF")) {
			prviRedak = prviRedak.substring(1);
		}

		if (prviRedak == null) {
			UpraviteljGresaka.getInstance().greskaSDatotekama("Datoteka s podacima o kompozicijama je prazna!");
			return null;
		}

		if (!prviRedak.equals("Oznaka;Oznaka prijevoznog sredstva;Uloga")) {
			UpraviteljGresaka.getInstance().greskaSDatotekama("Neispravan informativni redak u datoteci kompozicija!");
			return null;
		}

		return prviRedak;
	}

	private String[] parsirajIRazvrstajRedak(String redak) {
		String[] atributi = redak.split(";");
		if (atributi.length != 3) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Neispravan broj atributa u retku.");
			return null; 
		}

		if (!provjeriIspravnostRedka(atributi, redak)) {
			return null;
		}

		return atributi;
	}

	private void dodajPodatkeUPrivremeneListe(String[] atributi) {
		String oznakaKompozicije = atributi[0].trim();
		String oznakaVozila = atributi[1].trim();
		String uloga = atributi[2].trim();

		if (trenutnaOznakaKompozicije == null) {
			trenutnaOznakaKompozicije = oznakaKompozicije;
		}

		Vozilo vozilo = Tvrtka.getInstance().dohvatiVoziloPoOznaci(oznakaVozila);
		trenutnaPrijevoznaSredstva.add(vozilo);
		trenutneUloge.add(uloga);
	}

	private void resetirajPrivremenePodatke() {
		trenutnaPrijevoznaSredstva.clear();
		trenutneUloge.clear();
		trenutnaOznakaKompozicije = null;
	}

	private boolean provjeriIspravnostKompozicije() {
		if (trenutnaPrijevoznaSredstva.size() < 2) {
			UpraviteljGresaka.getInstance().greskaURetku("Kompozicija ID: " + trenutnaOznakaKompozicije,
					"Greška: Kompozicija mora imati najmanje dva vozila.");
			return false;
		}

		boolean imaPogon = false;
		boolean prosliPogoni = false; 

		for (int i = 0; i < trenutnaPrijevoznaSredstva.size(); i++) {
			Vozilo vozilo = trenutnaPrijevoznaSredstva.get(i);
			String uloga = trenutneUloge.get(i);

			String zapis = "Vozilo: " + vozilo.dohvatiOznaku() + ", Uloga: " + uloga;

			if (i == 0) {
				if (!uloga.equals("P") || !vozilo.dohvatiNamjenu().equals("PSVPVK")) {
					UpraviteljGresaka.getInstance().greskaURetku(zapis,
							"Greška: Prvo vozilo mora imati ulogu pogona (P) i biti s vlastitim pogonom (PSVPVK).");
					return false;
				}
				imaPogon = true;
			} else {
				if (uloga.equals("P")) {
					if (prosliPogoni) {
						UpraviteljGresaka.getInstance().greskaURetku(zapis,
								"Greška: Vozila s ulogom pogona moraju biti grupirana na početku kompozicije.");
						return false;
					}
				} else if (uloga.equals("V")) {
					prosliPogoni = true;
					if (vozilo.dohvatiNamjenu().equals("PSVPVK") || vozilo.dohvatiNamjenu().equals("PSVP")) {
						UpraviteljGresaka.getInstance().greskaURetku(zapis,
								"Greška: Lokomotiva s ulogom vagona (V) mora doći nakon svih vozila s pogonom.");
						return false;
					}
				}
			}
		}

		if (!imaPogon) {
			UpraviteljGresaka.getInstance().greskaURetku("Kompozicija ID: " + trenutnaOznakaKompozicije,
					"Greška: Kompozicija mora imati barem jedno vozilo s pogonom (P).");
			return false;
		}

		return true;
	}

	private void kreirajKompoziciju(String oznakaKompozicije, List<Vozilo> vozilaUKompoziciji,
			List<String> ulogeUKompoziciji, KompozicijaBuilderImpl builder) {

		if (vozilaUKompoziciji.isEmpty() || ulogeUKompoziciji.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaSDatotekama(
					"Greška: Neuspjelo kreiranje kompozicije '" + oznakaKompozicije + "' jer nema vozila ili uloga.");
			return;
		}

		builder.postaviOznaku(oznakaKompozicije);

		for (int i = 0; i < vozilaUKompoziciji.size(); i++) {
			builder.dodajPrijevoznoSredstvo(vozilaUKompoziciji.get(i), ulogeUKompoziciji.get(i));
		}

		try {
			Kompozicija kompozicija = builder.build();
			Tvrtka.getInstance().dodajKompoziciju(kompozicija);
		} catch (IllegalStateException e) {
			UpraviteljGresaka.getInstance().greskaSDatotekama(
					"Greška: Kompozicija s oznakom '" + oznakaKompozicije + "' nije ispravna i nije dodana.");
		}
	}

	private boolean provjeriIspravnostRedka(String[] atributi, String redak) {
		return provjeriOznakuKompozicije(atributi[0], redak) && provjeriOznakuPrijevoznogSredstva(atributi[1], redak)
				&& provjeriUlogu(atributi[2], redak);
	}

	private boolean provjeriOznakuKompozicije(String oznakaKompozicije, String redak) {
		oznakaKompozicije = oznakaKompozicije.trim();
		if (oznakaKompozicije.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Oznaka kompozicije nije unesena!");
			return false;
		}

		Pattern pattern = Pattern.compile("^\\d{4}$");
		Matcher matcher = pattern.matcher(oznakaKompozicije);
		if (matcher.matches()) {
			return true;
		} else {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Neispravna oznaka kompozicije: očekivan format npr. '8001', '1234', ali je pronađeno: "
							+ oznakaKompozicije);
			return false;
		}
	}

	private boolean provjeriOznakuPrijevoznogSredstva(String oznakaPrijevoznogSredstva, String redak) {
		oznakaPrijevoznogSredstva = oznakaPrijevoznogSredstva.trim();
		if (oznakaPrijevoznogSredstva.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Oznaka prijevoznog sredstva nije unesena!");
			return false;
		}

		if (Tvrtka.getInstance().dohvatiVoziloPoOznaci(oznakaPrijevoznogSredstva) == null) {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Neispravna oznaka prijevoznog sredstva: vozilo s oznakom '" + oznakaPrijevoznogSredstva
							+ "' nije pronađeno (ne postoji).");
			return false;
		}

		return true;
	}

	private boolean provjeriUlogu(String uloga, String redak) {
		uloga = uloga.trim();
		if (uloga.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Uloga prijevoznog sredstva nije unesena!");
			return false;
		}

		if (uloga.equals("P") || uloga.equals("V")) {
			return true;

		} else {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Neispravna uloga prijevoznog sredstva: očekivane vrijednosti su 'P' ili 'V', ali je pronađeno: "
							+ uloga);
			return false;
		}

	}
}
