package org.foi.uzdiz.chainofresponsibility;

import org.foi.uzdiz.SegmentPruge;
import org.foi.uzdiz.composite.*;
import org.foi.uzdiz.decorator.OsnovnoVrijeme;
import org.foi.uzdiz.observer.KorisnickiRegistar;
import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

public class KomandaSVV extends LanacKomandi {
	private KorisnickiRegistar registar = Tvrtka.getInstance().dohvatiKorisnickiRegistar();

	@Override
	protected boolean mozeObraditi(String komanda) {
		return komanda.startsWith("SVV ");
	}

	@Override
	protected void obradi(String komanda) {
		String parametri = komanda.substring(4).trim();
		String[] dijelovi = parametri.split("\\s+-\\s+");

		if (dijelovi.length != 3) {
			UpraviteljGresaka.getInstance().greskaURadu(
					"Neispravan broj parametara za komandu SVV! Očekivani format: 'SVV oznaka - dan - koeficijent'");
			return;
		}

		String oznakaVlaka = dijelovi[0];
		String dan = dijelovi[1];
		int koeficijent;

		try {
			koeficijent = Integer.parseInt(dijelovi[2]);
		} catch (NumberFormatException e) {
			UpraviteljGresaka.getInstance().greskaURadu("Koeficijent mora biti broj.");
			return;
		}

		VlakComposite vlak = Tvrtka.getInstance().dohvatiVozniRed().dohvatiVlak(oznakaVlaka);
		if (vlak == null) {
			UpraviteljGresaka.getInstance().greskaURadu("Vlak s oznakom '" + oznakaVlaka + "' ne postoji.");
			return;
		}

		simulirajVožnju(vlak, dan, koeficijent);

	}

	private void simulirajVožnju(VlakComposite vlak, String dan, int koeficijent) {
		System.out.printf("%-15s %-15s %-30s %-20s %-20s\n", "Oznaka vlaka", "Oznaka pruge", "Željeznička stanica",
				"Vrijeme polaska", "Vrijeme dolaska");
		System.out.println("-".repeat(95));

		int trenutnoVrijeme = vlak.dohvatiVrijemePolaska();

		for (VozniRedComponent etapaComponent : vlak.dohvatiEtape()) {
			if (etapaComponent instanceof EtapaLeaf) {
				EtapaLeaf etapa = (EtapaLeaf) etapaComponent;
				for (SegmentPruge segment : etapa.dohvatiPrugu().dohvatiSegmentePruge()) {

					int vrijemePolaska = trenutnoVrijeme;
					int trajanjeSegmenta = segment.dohvatiVrijemeNormalniVlak();

					if (vlak.dohvatiVrstaVlak().equals("U")) {
						trajanjeSegmenta = segment.dohvatiVrijemeUbrzaniVlak();
					} else if (vlak.dohvatiVrstaVlak().equals("B")) {
						trajanjeSegmenta = segment.dohvatiVrijemeBrziVlak();
					}

					int vrijemeDolaska = vrijemePolaska + trajanjeSegmenta;
					OsnovnoVrijeme vrijemePol = new OsnovnoVrijeme(vrijemePolaska);
					OsnovnoVrijeme vrijemeDol = new OsnovnoVrijeme(vrijemeDolaska);

					System.out.printf("%-15s %-15s %-30s %-20s %-20s\n", vlak.dohvatiOznakuVlak(),
							etapa.dohvatiPrugu().dohvatiOznakuPruge(),
							segment.getZavrsnaStanica().dohvatiNazivStanice(), vrijemePol.formatirajVrijeme(),
							vrijemeDol.formatirajVrijeme());
					System.out.println();

					String message = String.format("Vlak %s stiže u %s u %s.", vlak.dohvatiOznakuVlak(),
							segment.getZavrsnaStanica().dohvatiNazivStanice(), vrijemeDol.formatirajVrijeme());
					registar.obavijestiPromatrace(segment.getZavrsnaStanica().dohvatiNazivStanice(),
							vlak.dohvatiOznakuVlak(), message);

					trenutnoVrijeme = vrijemeDolaska;

					try {
						Thread.sleep(3000 / koeficijent);
					} catch (InterruptedException e) {
					}

				}
			}
		}
		System.out.println();
		System.out.println("Simulacija završena.");
		System.out.println();
	}
}
