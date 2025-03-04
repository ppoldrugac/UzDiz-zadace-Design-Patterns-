package org.foi.uzdiz.chainofresponsibility;

import org.foi.uzdiz.decorator.OsnovnoVrijeme;
import org.foi.uzdiz.memento.KartaMemento;
import org.foi.uzdiz.memento.ProdajaKarataOriginator;
import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

public class KomandaIKKPV extends LanacKomandi {
	ProdajaKarataOriginator prodajaKarataOriginator = Tvrtka.getInstance().dohvatiProdajaKarataOriginator();

	public KomandaIKKPV() {

	}

	@Override
	protected boolean mozeObraditi(String komanda) {
		return komanda.startsWith("IKKPV ") || komanda.startsWith("IKKPV");
	}

	@Override
	protected void obradi(String komanda) {

		String[] dijelovi = komanda.split(" ");
		try {
			if (dijelovi.length == 1) {
				ispisiSveKarte();
			} else if (dijelovi.length == 2) {
				int redniBroj = Integer.parseInt(dijelovi[1]);
				ispisiKartu(redniBroj);
			} else {
				UpraviteljGresaka.getInstance().greskaURadu("Neispravan format komande. Očekivani format: IKKPV [n]");
			}
		} catch (NumberFormatException e) {
			UpraviteljGresaka.getInstance().greskaURadu("Neispravan unos, očekivan je numerički parametar.");
		} catch (IllegalArgumentException e) {
			UpraviteljGresaka.getInstance().greskaURadu(e.getMessage());
		}
	}

	private void ispisiSveKarte() {
		int brojKarata = prodajaKarataOriginator.brojKarata();
		System.out.println();

		if (brojKarata == 0) {
			System.out.println("Trenutno nema kupljenih karata.");
			System.out.println();
		} else {
			System.out.println("Ispis svih kupljenih karata:");
			for (int i = 0; i < brojKarata; i++) {
				ispisiDetaljeKarte(prodajaKarataOriginator.dohvatiKartu(i));
				System.out.println();
			}
			System.out.println();
			System.out.println();
		}
	}

	private void ispisiKartu(int redniBroj) {
		if (redniBroj > 0 && redniBroj <= prodajaKarataOriginator.brojKarata()) {
			KartaMemento karta = prodajaKarataOriginator.dohvatiKartu(redniBroj - 1);

			if (karta != null) {
				ispisiDetaljeKarte(karta);
			} else {
				System.out.println();
				UpraviteljGresaka.getInstance().greskaURadu("Karta s rednim brojem " + redniBroj + " nije pronađena.");
				System.out.println();
			}
		} else {
			System.out.println();
			UpraviteljGresaka.getInstance().greskaURadu("Ne postoji karta s rednim brojem: " + redniBroj);
			System.out.println();
		}
	}

	public void ispisiDetaljeKarte(KartaMemento karta) {
		if (karta == null) {
			UpraviteljGresaka.getInstance().greskaURadu("Karta nije pronađena.");
			return;
		}
		int width = 60;
		String format = "| %-" + width + "s |%n";

		String nacinKupovineTekst;
		switch (karta.dohvatiNacinKupovine()) {
		case "WM":
			nacinKupovineTekst = "putem web/mobilne aplikacije";
			break;
		case "B":
			nacinKupovineTekst = "na blagajni";
			break;
		case "V":
			nacinKupovineTekst = "u vlaku";
			break;
		default:
			nacinKupovineTekst = "nepoznato";
			break;
		}

		System.out.println("================================================================");
		System.out.println("|                       Detalji Karte                          |");
		System.out.println("================================================================");
		System.out.printf(format, "Redni broj: " + karta.dohvatiRedniBrojKarte());
		System.out.printf(format, "Oznaka vlaka: " + karta.dohvatiOznakuVlaka());
		System.out.printf(format, "Relacija: " + karta.dohvatiRelaciju());
		System.out.printf(format, "");
		System.out.printf(format, "Polazna stanica: " + karta.dohvatiPolaznuStanicu());
		System.out.printf(format, "Odredišna stanica: " + karta.dohvatiOdredisnuStanicu());
		System.out.printf(format, "Vrijeme polaska: " + karta.dohvatiVrijemePolaska());
		System.out.printf(format, "Vrijeme dolaska: " + karta.dohvatiVrijemeDolaska());
		System.out.printf(format, "");
		System.out.printf(format,
				"Trajanje vožnje (sati): " + new OsnovnoVrijeme(karta.dohvatiTrajanjeVoznje()).formatirajVrijeme());
		System.out.printf(format, "Datum putovanja: " + karta.dohvatiDatum());
		System.out.printf(format, "Način kupovine: " + nacinKupovineTekst);
		System.out.printf(format, "");
		System.out.println("| Izračun cijene:                                              |");
		for (String popust : karta.dohvatiPopuste()) {
			System.out.printf("| - %-58s |%n", popust);
		}
		System.out.printf(format, "");
		System.out.printf(format, "Konačna cijena karte : " + String.format("%.2f EUR", karta.dohvatiCijenuKarte()));
		System.out.printf(format, "Datum i vrijeme kupovine: " + karta.dohvatiDatumKupovine());
		System.out.println("================================================================");
		System.out.println();
	}

}
