package org.foi.uzdiz.factorymethod;

import org.foi.uzdiz.Vozilo;
import org.foi.uzdiz.builder.VoziloBuilderImpl;
import org.foi.uzdiz.builder.VoziloDirector;
import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CitacVozila implements CitacDatoteka {

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
				UpraviteljGresaka.getInstance().greskaSDatotekama("Datoteka s podacima o vozilima je prazna!");
				return false;
			}

			if (!prviRedak.equals(
					"Oznaka;Opis;Proizvođač;Godina;Namjena;Vrsta prijevoza;Vrsta pogona;Maks brzina;Maks snaga;Broj sjedećih mjesta;Broj stajaćih mjesta;Broj bicikala;Broj kreveta;Broj automobila;Nosivost;Površina;Zapremina;Status")) {
				UpraviteljGresaka.getInstance().greskaSDatotekama("Neispravan informativni redak u datoteci vozila!");
				return false;
			}

			String redak;
			while ((redak = reader.readLine()) != null) {
				redak = redak.trim();

				if (redak.isEmpty() || redak.startsWith("#")) {
					continue;
				}

				String[] atributi = redak.split(";");
				if (atributi.length < 18) {
					UpraviteljGresaka.getInstance().greskaURetku(redak, "Neispravan broj atributa u retku.");
					continue;
				}

				if (!provjeriIspravnostRedka(atributi, redak)) {
					continue;
				}

				Vozilo vozilo = kreirajVozilo(atributi);
				if (vozilo != null) {
					Tvrtka.getInstance().dodajVozilo(vozilo);
				}
			}
		} catch (Exception e) {
			UpraviteljGresaka.getInstance()
					.greskaSDatotekama("Neuspješno učitavanje datoteke s vozilima! Greška: " + e.getMessage());
			return false;
		}
		return uspjeh;
	}

	private boolean provjeriIspravnostRedka(String[] atributi, String redak) {
		return provjeriOznaku(atributi[0], redak) && provjeriOpis(atributi[1], redak)
				&& provjeriProizvodjaca(atributi[2], redak) && provjeriGodinuProizvodnje(atributi[3], redak)
				&& provjeriNamjenu(atributi[4], redak) && provjeriVrstuPrijevoza(atributi[5], redak)
				&& provjeriVrstuPogona(atributi[6], redak) && provjeriMaxBrzinu(atributi[7], redak)
				&& provjeriMaxSnagu(atributi[8], redak) && provjeriBroj(atributi[9], "Broj sjedala", redak)
				&& provjeriBroj(atributi[10], "Broj stajaćih mjesta", redak)
				&& provjeriBroj(atributi[11], "Broj bicikala", redak)
				&& provjeriBroj(atributi[12], "Broj kreveta", redak)
				&& provjeriBroj(atributi[13], "Broj automobila", redak)
				&& provjeriDecimalniBroj(atributi[14], "Nosivost", redak)
				&& provjeriDecimalniBroj(atributi[15], "Površina", redak)
				&& provjeriDecimalniBroj(atributi[16], "Zapremina", redak) && provjeriStatus(atributi[17], redak);
	}

	private Vozilo kreirajVozilo(String[] atributi) {

		VoziloBuilderImpl builder = new VoziloBuilderImpl();
		VoziloDirector director = new VoziloDirector(builder);

		String oznaka = atributi[0].trim();
		String opis = atributi[1].trim();
		String proizvodjac = atributi[2].trim();
		int godinaProizvodnje = Integer.parseInt(atributi[3].trim());
		String namjena = atributi[4].trim();
		String vrstaPrijevoza = atributi[5].trim();
		String vrstaPogona = atributi[6].trim();
		int maxBrzina = Integer.parseInt(atributi[7].trim());
		String status = atributi[17].trim();

		int brojSjedala = Integer.parseInt(atributi[9].trim());
		int brojStajacihMjesta = Integer.parseInt(atributi[10].trim());
		int brojBicikala = Integer.parseInt(atributi[11].trim());
		int brojKreveta = Integer.parseInt(atributi[12].trim());
		int brojAutomobila = Integer.parseInt(atributi[13].trim());

		double maxSnaga = Double.parseDouble(atributi[8].trim().replace(",", "."));
		double nosivost = Double.parseDouble(atributi[14].trim().replace(",", "."));
		double povrsina = Double.parseDouble(atributi[15].trim().replace(",", "."));
		double zapremina = Double.parseDouble(atributi[16].trim().replace(",", "."));

		if (namjena.equals("PSVPVK")) {

			return director.createLokomotiva(oznaka, opis, proizvodjac, godinaProizvodnje, namjena, vrstaPrijevoza,
					vrstaPogona, maxBrzina, status, maxSnaga);

		} else if (namjena.equals("PSVP")) {

			return director.createPutnickiVagon(oznaka, opis, proizvodjac, godinaProizvodnje, namjena, vrstaPrijevoza,
					vrstaPogona, maxBrzina, status, brojSjedala, brojStajacihMjesta, brojBicikala, brojKreveta);

		} else {

			return director.createTeretniVagon(oznaka, opis, proizvodjac, godinaProizvodnje, namjena, vrstaPrijevoza,
					vrstaPogona, maxBrzina, status, brojAutomobila, nosivost, povrsina, zapremina);
		}
	}

	private boolean provjeriOznaku(String oznaka, String redak) {
		oznaka = oznaka.trim();
		if (oznaka.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Oznaka vozila nije unesena!");
			return false;
		}

		Pattern pattern = Pattern.compile("^[A-Za-z]+\\d*(-\\d+)?$");
		Matcher matcher = pattern.matcher(oznaka);
		if (matcher.matches()) {
			return true;
		} else {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Neispravna oznaka vozila: očekivan format npr. 'D2044-1', 'VP-2', ali je pronađeno: " + oznaka);
			return false;
		}
	}

	private boolean provjeriOpis(String opis, String redak) {
		opis = opis.trim();
		if (opis.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Opis vozila nije unesen!");
			return false;
		}
		return true;
	}

	private boolean provjeriProizvodjaca(String proizvodjac, String redak) {
		proizvodjac = proizvodjac.trim();
		if (proizvodjac.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Proizvođač nije unesen!");
			return false;
		}
		return true;
	}

	private boolean provjeriGodinuProizvodnje(String godina, String redak) {
		godina = godina.trim();
		if (godina.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Godina proizvodnje nije unesena!");
			return false;
		}

		try {
			int god = Integer.parseInt(godina);
			if (god < 1800 || god > java.time.Year.now().getValue()) {
				UpraviteljGresaka.getInstance().greskaURetku(redak, "Neispravna godina proizvodnje!");
				return false;
			}
			return true;
		} catch (NumberFormatException e) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Godina proizvodnje nije broj!");
			return false;
		}
	}

	private boolean provjeriNamjenu(String namjena, String redak) {
		namjena = namjena.trim();
		if (namjena.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Namjena vozila nije unesena!");
			return false;
		}

		if (!namjena.matches("PSVPVK|PSVP|PSBP")) {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Neispravna namjena vozila: očekivan format 'PSVPVK', 'PSVP', ili 'PSBP', ali je pronađeno: "
							+ namjena);
			return false;
		}
		return true;
	}

	private boolean provjeriVrstuPrijevoza(String vrstaPrijevoza, String redak) {
		vrstaPrijevoza = vrstaPrijevoza.trim();
		if (vrstaPrijevoza.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Vrsta prijevoza nije unesena!");
			return false;
		}
		if (!vrstaPrijevoza.matches("N|P|TA|TK|TRS|TTS")) {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Neispravna vrsta prijevoza: očekivane vrijednosti su 'N', 'P', 'TA', 'TK', 'TRS', ili 'TTS', ali je pronađeno: "
							+ vrstaPrijevoza);
			return false;
		}
		return true;
	}

	private boolean provjeriVrstuPogona(String vrstaPogona, String redak) {
		vrstaPogona = vrstaPogona.trim();
		if (vrstaPogona.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Vrsta pogona nije unesena!");
			return false;
		}
		if (!vrstaPogona.matches("N|D|B|E")) {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Neispravna vrsta pogona: očekivane vrijednosti su 'N', 'D', 'B', ili 'E', ali je pronađeno: "
							+ vrstaPogona);
			return false;
		}
		return true;
	}

	private boolean provjeriMaxBrzinu(String maxBrzina, String redak) {
		maxBrzina = maxBrzina.trim();
		if (maxBrzina.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Maksimalna brzina nije unesena!");
			return false;
		}
		try {
			int brzina = Integer.parseInt(maxBrzina);
			if (brzina < 1 || brzina > 200) {
				UpraviteljGresaka.getInstance().greskaURetku(redak,
						"Neispravna maksimalna brzina: očekivana vrijednost između 1 i 200 km/h, pronađeno: " + brzina);
				return false;
			}
			return true;
		} catch (NumberFormatException e) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Maksimalna brzina nije broj!");
			return false;
		}
	}

	private boolean provjeriMaxSnagu(String maxSnaga, String redak) {
		maxSnaga = maxSnaga.trim();

		maxSnaga = maxSnaga.replace(",", ".");

		if (maxSnaga.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Maksimalna snaga nije unesena!");
			return false;
		}

		try {
			maxSnaga = maxSnaga.replace(",", ".");

			double snaga = Double.parseDouble(maxSnaga);

			if (snaga == -1.0 || (snaga >= 0.0 && snaga <= 10.0)) {
				return true;
			} else {
				UpraviteljGresaka.getInstance().greskaURetku(redak,
						"Maksimalna snaga mora biti -1 ili u rasponu od 0,0 do 10 MW!");
				return false;
			}
		} catch (NumberFormatException e) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Maksimalna snaga mora biti broj!");
			return false;
		}
	}

	private boolean provjeriBroj(String broj, String nazivAtributa, String redak) {
		broj = broj.trim();
		if (broj.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, nazivAtributa + " nije unesen!");
			return false;
		}
		try {
			int vrijednost = Integer.parseInt(broj);
			if (vrijednost < 0) {
				UpraviteljGresaka.getInstance().greskaURetku(redak,
						nazivAtributa + " mora biti pozitivan cijeli broj!");
				return false;
			}
			return true;
		} catch (NumberFormatException e) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, nazivAtributa + " nije ispravan cijeli broj!");
			return false;
		}
	}

	private boolean provjeriDecimalniBroj(String broj, String nazivAtributa, String redak) {
		broj = broj.trim();
		if (broj.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, nazivAtributa + " nije unesen!");
			return false;
		}
		try {
			broj = broj.replace(",", ".");

			Double.parseDouble(broj);

			return true;
		} catch (NumberFormatException e) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, nazivAtributa + " mora biti broj!");
			return false;
		}
	}

	private boolean provjeriStatus(String status, String redak) {
		status = status.trim();
		if (status.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Status vozila nije unesen!");
			return false;
		}
		if (!status.matches("I|K")) {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Neispravan status vozila: očekivano 'I' za ispravno ili 'K' za u kvaru, ali je pronađeno: "
							+ status);
			return false;
		}
		return true;
	}
}
