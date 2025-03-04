package org.foi.uzdiz.chainofresponsibility;

import java.util.List;

import org.foi.uzdiz.SegmentPruge;
import org.foi.uzdiz.composite.EtapaLeaf;
import org.foi.uzdiz.composite.VlakComposite;
import org.foi.uzdiz.composite.VozniRedComponent;
import org.foi.uzdiz.decorator.OsnovnoVrijeme;
import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

public class KomandaIVRV extends LanacKomandi {

	@Override
	protected boolean mozeObraditi(String komanda) {
		return komanda.startsWith("IVRV ");
	}

	@Override
	protected void obradi(String komanda) {
		String[] dijelovi = komanda.split(" ", 2);
		if (dijelovi.length < 2) {
			UpraviteljGresaka.getInstance().greskaURadu("Greška: Nedostaje oznaka vlaka. Sintaksa: IVRV oznakaVlaka");
			return;
		}

		String oznakaVlaka = dijelovi[1].trim();

		if (!Tvrtka.getInstance().postojiVlak(oznakaVlaka)) {
			UpraviteljGresaka.getInstance().greskaURadu("Greška: Vlak s oznakom " + oznakaVlaka + " ne postoji.");
			return;
		}

		VlakComposite vlak = Tvrtka.getInstance().dohvatiVozniRed().dohvatiVlak(oznakaVlaka);
		if (vlak != null) {
			ispisiVozniRed(vlak);
		} else {
			UpraviteljGresaka.getInstance()
					.greskaURadu("Greška: Problem pri dohvaćanju voznog reda za vlak " + oznakaVlaka + ".");
		}
	}

	private void ispisiVozniRed(VlakComposite vlak) {
		var vrstaVlaka = vlak.dohvatiVrstaVlak();
	    double ukupniKm = 0;
	    int trenutnoVrijeme = 0;

	    if (!vlak.dohvatiEtape().isEmpty() && vlak.dohvatiEtape().get(0) instanceof EtapaLeaf) {
	        EtapaLeaf prvaEtapa = (EtapaLeaf) vlak.dohvatiEtape().get(0);
	        trenutnoVrijeme = prvaEtapa.dohvatiVrijemePolaska();
	    } else {
	        System.out.println("Greška: Vlak nema etapa ili su etape neispravne.");
	        return;
	    }

	    System.out.printf("%-15s %-15s %-30s %-20s %-10s\n", "Oznaka vlaka", "Oznaka pruge", "Željeznička stanica",
	            "Vrijeme polaska", "Broj km");
	    System.out.println("-".repeat(95));

	    boolean krenuli = false;

	    for (VozniRedComponent etapaComponent : vlak.dohvatiEtape()) {
	        if (etapaComponent instanceof EtapaLeaf) {
	            EtapaLeaf etapa = (EtapaLeaf) etapaComponent;
	            List<SegmentPruge> segmenti = etapa.dohvatiPrugu().dohvatiSegmentePruge();

	            for (SegmentPruge segment : segmenti) {
	                if (!krenuli) {
	                    System.out.printf("%-15s %-15s %-30s %-20s %-10.2f\n", vlak.dohvatiOznakuVlak(),
	                            etapa.dohvatiPrugu().dohvatiOznakuPruge(),
	                            segment.getPocetnaStanica().dohvatiNazivStanice(),
	                            new OsnovnoVrijeme(trenutnoVrijeme).formatirajVrijeme(), ukupniKm);
	                    krenuli = true;
	                }

	                ukupniKm += segment.dohvatiDuzinu();

	                int trajanjeSegmenta = 0;
	                switch (vrstaVlaka) {
	                    case "U":
	                        trajanjeSegmenta = segment.dohvatiVrijemeUbrzaniVlak() != null 
	                            ? segment.dohvatiVrijemeUbrzaniVlak() : segment.dohvatiVrijemeNormalniVlak();
	                        break;
	                    case "B":
	                        trajanjeSegmenta = segment.dohvatiVrijemeBrziVlak() != null 
	                            ? segment.dohvatiVrijemeBrziVlak() : segment.dohvatiVrijemeNormalniVlak();
	                        break;
	                    default:
	                        trajanjeSegmenta = segment.dohvatiVrijemeNormalniVlak();
	                        break;
	                }
	                trenutnoVrijeme += trajanjeSegmenta;

	                System.out.printf("%-15s %-15s %-30s %-20s %-10.2f\n", vlak.dohvatiOznakuVlak(),
	                        etapa.dohvatiPrugu().dohvatiOznakuPruge(),
	                        segment.getZavrsnaStanica().dohvatiNazivStanice(),
	                        new OsnovnoVrijeme(trenutnoVrijeme).formatirajVrijeme(), ukupniKm);

	                if (segment.getZavrsnaStanica().equals(etapa.dohvatiOdredisnuStanicu())) {
	                    break;
	                }
	            }
	        }
	    }
	    System.out.println();
	}
}
