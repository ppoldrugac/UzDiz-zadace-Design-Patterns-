package org.foi.uzdiz.chainofresponsibility;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;
import org.foi.uzdiz.strategy.KontekstCijena;
import org.foi.uzdiz.Cjenik;
import org.foi.uzdiz.SegmentPruge;
import org.foi.uzdiz.composite.EtapaLeaf;
import org.foi.uzdiz.composite.VlakComposite;
import org.foi.uzdiz.composite.VozniRedComponent;
import org.foi.uzdiz.decorator.OsnovnoVrijeme;
import org.foi.uzdiz.memento.KartaMemento;
import org.foi.uzdiz.memento.ProdajaKarataOriginator;

public class KomandaKKPV2S extends LanacKomandi {

	private ProdajaKarataOriginator prodajaKarataOriginator = Tvrtka.getInstance().dohvatiProdajaKarataOriginator();

	@Override
	protected boolean mozeObraditi(String komanda) {
		return komanda.startsWith("KKPV2S");
	}

	@Override
	protected void obradi(String komanda) {
		String[] dijelovi = komanda.split(" - ");
		if (dijelovi.length != 5) {
			UpraviteljGresaka.getInstance().greskaURadu(
					"Neispravan format komande. Očekivani format: KKPV2S oznaka - polaznaStanica - odredišnaStanica - datum - načinKupovine");
			return;
		}

		String oznakaVlaka = dijelovi[0].split(" ")[1].trim();
		String polaznaStanica = dijelovi[1].trim();
		String odredisnaStanica = dijelovi[2].trim();
		String datum = dijelovi[3].trim();
		String nacinKupovine = dijelovi[4].trim();

		if (!inicijalneProvjere(oznakaVlaka, polaznaStanica, odredisnaStanica, nacinKupovine)) {
			return;
		}

		VlakComposite vlak = Tvrtka.getInstance().dohvatiVozniRed().dohvatiVlak(oznakaVlaka);

		List<StanicaPodaci> stanice = simulirajIVRV(vlak);

		double udaljenost = izracunajUdaljenost(stanice, polaznaStanica, odredisnaStanica);
		int trajanje = izracunajTrajanje(stanice, polaznaStanica, odredisnaStanica);

		Cjenik cjenik = Tvrtka.getInstance().dohvatiCjenik();
		KontekstCijena kontekst = new KontekstCijena(cjenik);
		kontekst.postaviStrategijuNaOsnovuDatumaiNacina(datum, nacinKupovine);
		double cijena = kontekst.izracunajKonacnuCijenu(udaljenost, vlak.dohvatiVrstaVlak());


		if (udaljenost >= 0 && trajanje >= 0) {

			String vrijemePolaska = dohvatiVrijemePolaska(stanice, polaznaStanica);
			String vrijemeDolaska = dohvatiVrijemeDolaska(stanice, odredisnaStanica);
			String relacija = dohvatiRelaciju(stanice);
			List<String> popusti = kontekst.dobaviPopuste();

			KartaMemento prodanaKarta = prodajaKarataOriginator.prodajKartu(oznakaVlaka, polaznaStanica,
					odredisnaStanica, datum, nacinKupovine, trajanje, cijena, vrijemePolaska, vrijemeDolaska, relacija,
					popusti);

			System.out.println("Karta je uspješno kupljena!");
			System.out.println();
			ispisiDetaljeKarte(prodanaKarta);
		} else {
			UpraviteljGresaka.getInstance().greskaURadu("Nije moguće pronaći put između zadanih stanica.");
		}

	}

	public void ispisiDetaljeKarte(KartaMemento karta) {
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

	private boolean inicijalneProvjere(String oznakaVlaka, String polaznaStanica, String odredisnaStanica,
			String nacinKupovine) {

		if (Tvrtka.getInstance().dohvatiCjenik() == null) {
			return false;
		}

		VlakComposite vlak = Tvrtka.getInstance().dohvatiVozniRed().dohvatiVlak(oznakaVlaka);
		if (vlak == null) {
			UpraviteljGresaka.getInstance().greskaURadu("Vlak s oznakom " + oznakaVlaka + " ne postoji.");
			return false;
		}

		if (!Tvrtka.getInstance().postojiStanica(polaznaStanica)) {
			UpraviteljGresaka.getInstance().greskaURadu("Polazna stanica '" + polaznaStanica + "' ne postoji.");
			return false;
		}
		if (!Tvrtka.getInstance().postojiStanica(odredisnaStanica)) {
			UpraviteljGresaka.getInstance().greskaURadu("Odredišna stanica '" + odredisnaStanica + "' ne postoji.");
			return false;
		}

		Set<String> dozvoljeniNaciniKupovine = Set.of("WM", "B", "V");
		if (!dozvoljeniNaciniKupovine.contains(nacinKupovine)) {
			UpraviteljGresaka.getInstance().greskaURadu(
					"Neispravan način kupovine. Dozvoljeni načini su: WM - putem web/mobilne aplikacije, B - na blagajni, V - u vlaku.");
			return false;
		}

		return true;

	}

	private List<StanicaPodaci> simulirajIVRV(VlakComposite vlak) {

		List<StanicaPodaci> rezultatiSimulacijeIVRV = new ArrayList<>();

		var vrstaVlaka = vlak.dohvatiVrstaVlak();
		double ukupniKm = 0;
		int trenutnoVrijeme = 0;

		if (!vlak.dohvatiEtape().isEmpty() && vlak.dohvatiEtape().get(0) instanceof EtapaLeaf) {
			EtapaLeaf prvaEtapa = (EtapaLeaf) vlak.dohvatiEtape().get(0);
			trenutnoVrijeme = prvaEtapa.dohvatiVrijemePolaska();
		} else {
			UpraviteljGresaka.getInstance().greskaURadu("Vlak nema etapa ili su etape neispravne.");
			return null;
		}

		boolean krenuli = false;

		for (VozniRedComponent etapaComponent : vlak.dohvatiEtape()) {
			if (etapaComponent instanceof EtapaLeaf) {
				EtapaLeaf etapa = (EtapaLeaf) etapaComponent;
				List<SegmentPruge> segmenti = etapa.dohvatiPrugu().dohvatiSegmentePruge();

				for (SegmentPruge segment : segmenti) {
					if (!krenuli) {
						rezultatiSimulacijeIVRV.add(new StanicaPodaci(segment.getPocetnaStanica().dohvatiNazivStanice(),
								ukupniKm, trenutnoVrijeme));
						krenuli = true;
					}

					ukupniKm += segment.dohvatiDuzinu();

					int trajanjeSegmenta = 0;
					switch (vrstaVlaka) {
					case "U":
						trajanjeSegmenta = segment.dohvatiVrijemeUbrzaniVlak() != null
								? segment.dohvatiVrijemeUbrzaniVlak()
								: segment.dohvatiVrijemeNormalniVlak();
						break;
					case "B":
						trajanjeSegmenta = segment.dohvatiVrijemeBrziVlak() != null ? segment.dohvatiVrijemeBrziVlak()
								: segment.dohvatiVrijemeNormalniVlak();
						break;
					default:
						trajanjeSegmenta = segment.dohvatiVrijemeNormalniVlak();
						break;
					}
					trenutnoVrijeme += trajanjeSegmenta;

					rezultatiSimulacijeIVRV.add(new StanicaPodaci(segment.getZavrsnaStanica().dohvatiNazivStanice(),
							ukupniKm, trenutnoVrijeme));

					if (segment.getZavrsnaStanica().equals(etapa.dohvatiOdredisnuStanicu())) {
						break;
					}
				}
			}
		}
		System.out.println();
		return rezultatiSimulacijeIVRV;
	}

	private double izracunajUdaljenost(List<StanicaPodaci> staniceUdaljenosti, String polaznaStanica,
			String odredisnaStanica) {
		StanicaPodaci pocetna = null;
		StanicaPodaci krajnja = null;

		for (StanicaPodaci stanica : staniceUdaljenosti) {
			if (stanica.stanica.equalsIgnoreCase(polaznaStanica)) {
				pocetna = stanica;
			}
			if (stanica.stanica.equalsIgnoreCase(odredisnaStanica)) {
				krajnja = stanica;
			}

			if (pocetna != null && krajnja != null)
				break;
		}

		if (pocetna == null || krajnja == null) {
			return -1;
		}

		return Math.abs(krajnja.udaljenost - pocetna.udaljenost);
	}

	private int izracunajTrajanje(List<StanicaPodaci> staniceUdaljenosti, String polaznaStanica,
			String odredisnaStanica) {
		StanicaPodaci pocetna = null;
		StanicaPodaci krajnja = null;

		for (StanicaPodaci stanica : staniceUdaljenosti) {
			if (stanica.stanica.equalsIgnoreCase(polaznaStanica)) {
				pocetna = stanica;
			}
			if (stanica.stanica.equalsIgnoreCase(odredisnaStanica)) {
				krajnja = stanica;
			}

			if (pocetna != null && krajnja != null)
				break;
		}

		if (pocetna == null || krajnja == null) {
			return -1;
		}

		return Math.abs(krajnja.vrijeme - pocetna.vrijeme);
	}

	private String dohvatiVrijemePolaska(List<StanicaPodaci> stanice, String polaznaStanica) {
		for (StanicaPodaci stanica : stanice) {
			if (stanica.stanica.equalsIgnoreCase(polaznaStanica)) {
				return stanica.dohvatiFormatiranoVrijeme();
			}
		}
		return null;
	}

	private String dohvatiVrijemeDolaska(List<StanicaPodaci> stanice, String odredisnaStanica) {
		for (StanicaPodaci stanica : stanice) {
			if (stanica.stanica.equalsIgnoreCase(odredisnaStanica)) {
				return stanica.dohvatiFormatiranoVrijeme();
			}
		}
		return null;
	}

	private String dohvatiRelaciju(List<StanicaPodaci> stanice) {
		if (stanice == null || stanice.isEmpty()) {
			return "Relacija nije dostupna";
		}
		String pocetnaStanica = stanice.get(0).stanica;
		String krajnjaStanica = stanice.get(stanice.size() - 1).stanica;
		return pocetnaStanica + " - " + krajnjaStanica;
	}

	class StanicaPodaci {
		String stanica;
		double udaljenost;
		int vrijeme;

		StanicaPodaci(String stanica, double udaljenost, int vrijeme) {
			this.stanica = stanica;
			this.udaljenost = udaljenost;
			this.vrijeme = vrijeme;
		}

		public String dohvatiFormatiranoVrijeme() {
			return new OsnovnoVrijeme(vrijeme).formatirajVrijeme();
		}
	}
}
