package org.foi.uzdiz.factorymethod;

import org.foi.uzdiz.Pruga;
import org.foi.uzdiz.Stanica;
import org.foi.uzdiz.composite.EtapaLeaf;
import org.foi.uzdiz.composite.VlakComposite;
import org.foi.uzdiz.composite.VozniRedComposite;
import org.foi.uzdiz.decorator.OsnovnoVrijeme;
import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class CitacVoznogReda implements CitacDatoteka {

	@Override
	public boolean ucitajPodatke(String nazivDatoteke) {

		boolean uspjeh = true;

		Tvrtka tvrtka = Tvrtka.getInstance();

		VozniRedComposite voznired = new VozniRedComposite();

		File datoteka = new File(nazivDatoteke);

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(datoteka), "UTF-8"))) {

			String prviRedak = reader.readLine();

			if (!provjeriPrviRedak(prviRedak))
				return false;

			String redak;
			while ((redak = reader.readLine()) != null) {
				redak = redak.trim();

				if (redak.isEmpty() || redak.startsWith("#") || redak.replace(";", "").isEmpty()) {
					continue;
				}

				String[] atributi = redak.split(";", -1);
				if (atributi.length != 9) {
					UpraviteljGresaka.getInstance().greskaURetku(redak, "Neispravan broj atributa u retku.");
					continue;
				}

				if (!provjeriIspravnostRedka(atributi, redak))
					continue;

				obradiRedak(atributi, tvrtka, voznired);
			}

			tvrtka.dodajVozniRed(voznired);

		} catch (Exception e) {
			UpraviteljGresaka.getInstance().greskaSDatotekama("Greška pri čitanju datoteke: " + e.getMessage());
			uspjeh = false;
		}
		return uspjeh;
	}

	private boolean provjeriPrviRedak(String prviRedak) {

		if (prviRedak != null && prviRedak.startsWith("\uFEFF")) {
			prviRedak = prviRedak.substring(1);
		}

		if (prviRedak == null) {
			UpraviteljGresaka.getInstance().greskaSDatotekama("Datoteka s podacima o voznom redu je prazna!");
			return false;
		}

		if (!prviRedak.equals(
				"Oznaka pruge;Smjer;Polazna stanica;Odredišna stanica;Oznaka vlaka;Vrsta vlaka;Vrijeme polaska;Trajanje vožnje;Oznaka dana")) {
			UpraviteljGresaka.getInstance().greskaSDatotekama("Neispravan informativni redak u datoteci voznog reda!");
			return false;
		}
		return true;
	}

	private boolean provjeriIspravnostRedka(String[] atributi, String redak) {
		if (!provjeriPruge(atributi[0], redak)) {
			return false;
		}
		Pruga pruga = Tvrtka.getInstance().dohvatiPrugu(atributi[0]);

		if (!provjeriSmjer(atributi[1], redak)) {
			return false;
		}

		if (!provjeriPolaznuStanicu(atributi[2], pruga, redak)) {
			return false;
		}

		if (!provjeriOdredisnuStanicu(atributi[3], pruga, redak)) {
			return false;
		}

		if (!provjeriOznakuVlaka(atributi[4], redak)) {
			return false;
		}

		if (!provjeriVrstuVlaka(atributi[5], redak)) {
			return false;
		}

		if (!provjeriVrijemePolaska(atributi[6], redak)) {
			return false;
		}

		if (!provjeriTrajanje(atributi[7], redak)) {
			return false;
		}

		if (!provjeriOznakuDana(atributi[8], redak)) {
			return false;
		}

		return true;
	}

	private boolean provjeriPruge(String oznakaPruge, String redak) {
		oznakaPruge = oznakaPruge.trim();

		if (oznakaPruge.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Oznaka pruge nije unesena!");
			return false;
		}

		if (Tvrtka.getInstance().dohvatiPrugu(oznakaPruge) == null) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Pruga s oznakom '" + oznakaPruge + "' ne postoji!");
			return false;
		}
		return true;
	}

	private boolean provjeriSmjer(String smjer, String redak) {
		smjer = smjer.trim();

		if (smjer.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Smjer nije unesen!");
			return false;
		}
		if (!smjer.equals("N") && !smjer.equals("O")) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Smjer mora biti 'N' ili 'O'.");
			return false;
		}
		return true;
	}

	private boolean provjeriPolaznuStanicu(String nazivStanice, Pruga pruga, String redak) {
		if (nazivStanice.isEmpty()) {
			return true;
		} else if (pruga.dohvatiStanicuNaPrugi(nazivStanice) == null) {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Polazna stanica '" + nazivStanice + "' ne postoji na pruzi!");
			return false;
		}
		return true;
	}

	private boolean provjeriOdredisnuStanicu(String nazivStanice, Pruga pruga, String redak) {
		if (nazivStanice.isEmpty()) {
			return true;
		} else if (pruga.dohvatiStanicuNaPrugi(nazivStanice) == null) {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Odredišna stanica '" + nazivStanice + "' ne postoji na pruzi!");
			return false;
		}
		return true;
	}

	private boolean provjeriOznakuVlaka(String oznakaVlaka, String redak) {
		if (oznakaVlaka == null || oznakaVlaka.trim().isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Oznaka vlaka nije unesena!");
			return false;
		}
		return true;
	}

	private boolean provjeriVrstuVlaka(String vrstaVlaka, String redak) {
		if (vrstaVlaka.isEmpty()) {
			return true;
		}

		if (!vrstaVlaka.equals("N") && !vrstaVlaka.equals("U") && !vrstaVlaka.equals("B")) {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Vrsta vlaka '" + vrstaVlaka + "' nije ispravna! Dopuštene vrste su: N, U, B.");
			return false;
		}

		return true;
	}

	private boolean provjeriVrijemePolaska(String vrijeme, String redak) {
		vrijeme = vrijeme.trim();

		if (vrijeme.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Vrijeme polaska nije uneseno!");
			return false;
		}

		if (!vrijeme.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Vrijeme '" + vrijeme + "' nije ispravno! Vrijeme polaska mora biti u formatu HH:MM.");
			return false;
		}
		return true;
	}

	private boolean provjeriTrajanje(String trajanje, String redak) {
		trajanje = trajanje.trim();

		if (trajanje.isEmpty()) {
			UpraviteljGresaka.getInstance().greskaURetku(redak, "Trajanje vožnje nije uneseno!");
			return false;
		}

		if (!trajanje.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Trajanje '" + trajanje + "' nije ispravno! Trajanje vožnje mora biti u formatu HH:MM.");
			return false;
		}
		return true;
	}

	private boolean provjeriOznakuDana(String oznakaDana, String redak) {
		oznakaDana = oznakaDana.trim();
		if (oznakaDana.isEmpty()) {
			return true;
		}

		if (Tvrtka.getInstance().dohvatiOznakuDana(oznakaDana) == null) {
			UpraviteljGresaka.getInstance().greskaURetku(redak,
					"Oznaka dana '" + oznakaDana + "' ne postoji u podacima o oznakama dana.");
			return false;
		}

		return true;
	}

	private void obradiRedak(String[] atributi, Tvrtka tvrtka, VozniRedComposite voznired) {
		String oznakaPruge = atributi[0];
		String smjer = atributi[1];
		String polaznaStanicaNaziv = atributi[2];
		String odredisnaStanicaNaziv = atributi[3];
		String oznakaVlaka = atributi[4];
		String vrstaVlaka = atributi[5].isEmpty() ? "N" : atributi[5];

		int vrijemePolaska = OsnovnoVrijeme.parsirajVrijeme(atributi[6]);
		int trajanjeVoznje = OsnovnoVrijeme.parsirajVrijeme(atributi[7]);

		Pruga pruga = tvrtka.dohvatiPrugu(oznakaPruge);
		Stanica polaznaStanica = pronadiPolaznuStanicu(pruga, polaznaStanicaNaziv, smjer);
		Stanica odredisnaStanica = pronadiOdredisnuStanicu(pruga, odredisnaStanicaNaziv, smjer);

		EtapaLeaf novaEtapa = new EtapaLeaf(pruga, polaznaStanica, odredisnaStanica, smjer, vrijemePolaska,
				trajanjeVoznje, atributi[8]);

		VlakComposite postojeciVlak = voznired.dohvatiVlak(oznakaVlaka);

		if (postojeciVlak != null) {
			postojeciVlak.dodajKomponentu(novaEtapa);
		} else {
			VlakComposite noviVlak = new VlakComposite(oznakaVlaka, vrstaVlaka);
			noviVlak.dodajKomponentu(novaEtapa);
			voznired.dodajKomponentu(noviVlak);
		}
	}

	private Stanica pronadiPolaznuStanicu(Pruga pruga, String nazivStanice, String smjer) {
		if (nazivStanice == null || nazivStanice.isEmpty()) {
			return smjer.equals("N") ? pruga.dohvatiPrvuStanicu() : pruga.dohvatiZadnjuStanicu();
		}
		return pruga.dohvatiStanicuNaPrugi(nazivStanice);

	}

	private Stanica pronadiOdredisnuStanicu(Pruga pruga, String nazivStanice, String smjer) {
		if (nazivStanice == null || nazivStanice.isEmpty()) {
			return smjer.equals("N") ? pruga.dohvatiZadnjuStanicu() : pruga.dohvatiPrvuStanicu();
		}
		return pruga.dohvatiStanicuNaPrugi(nazivStanice);

	}

}
