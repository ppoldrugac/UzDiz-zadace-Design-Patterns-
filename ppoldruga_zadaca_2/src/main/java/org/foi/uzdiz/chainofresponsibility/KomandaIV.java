package org.foi.uzdiz.chainofresponsibility;

import java.util.List;

import org.foi.uzdiz.composite.VlakComposite;
import org.foi.uzdiz.composite.VozniRedComposite;
import org.foi.uzdiz.decorator.OsnovnoVrijeme;
import org.foi.uzdiz.singleton.Tvrtka;

public class KomandaIV extends LanacKomandi {

	@Override
	protected boolean mozeObraditi(String komanda) {
		return komanda.trim().equals("IV");
	}

	@Override
	protected void obradi(String komanda) {

		VozniRedComposite vozniRed = Tvrtka.getInstance().dohvatiVozniRed();

		if (vozniRed == null) {
			System.out.println("Nema podataka o voznom redu.");
			return;
		}

		System.out.println();
		System.out.printf("%-15s %-40s %-40s %-20s %-20s %-10s\n", "Oznaka vlaka", "Polazna stanica",
				"Odredišna stanica", "Vrijeme polaska", "Vrijeme dolaska", "Ukupno km");
		System.out.println("-".repeat(149));

		List<VlakComposite> vlakovi = vozniRed.dohvatiSveVlakove();

		for (VlakComposite vlak : vlakovi) {
			String oznakaVlaka = vlak.dohvatiOznakuVlak();
			String polaznaStanica = vlak.dohvatiPolaznuStanicu();
			String odredisnaStanica = vlak.dohvatiOdredisnuStanicu();
			int vrijemePolaska = vlak.dohvatiVrijemePolaska();
			int ukupnoTrajanje = vlak.izracunajUkupnoVrijeme(vlak.dohvatiVrstaVlak());
			double ukupnoKm = vlak.izracunajUkupnoKilometara();

			int vrijemeDolaska = vrijemePolaska + ukupnoTrajanje;

			String formatiranoVrijemePolaska = new OsnovnoVrijeme(vrijemePolaska).formatirajVrijeme();
			String formatiranoVrijemeDolaska = new OsnovnoVrijeme(vrijemeDolaska).formatirajVrijeme();

			
			System.out.printf("%-15s %-40s %-40s %-20s %-20s %-10.2f\n", oznakaVlaka, polaznaStanica, odredisnaStanica,
					formatiranoVrijemePolaska, formatiranoVrijemeDolaska, ukupnoKm);
		}
		System.out.println();
	}

}
