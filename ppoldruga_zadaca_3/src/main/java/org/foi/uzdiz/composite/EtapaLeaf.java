package org.foi.uzdiz.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.foi.uzdiz.Pruga;
import org.foi.uzdiz.SegmentPruge;
import org.foi.uzdiz.Stanica;

public class EtapaLeaf implements VozniRedComponent {
	private Pruga pruga;
	private Stanica polaznaStanica;
	private Stanica odredisnaStanica;
	private int vrijemePolaska;
	private int trajanjeVoznje;
	private String smjer;
	private String oznakaDana;

	public EtapaLeaf(Pruga pruga, Stanica polaznaStanica, Stanica odredisnaStanica, String smjer, int vrijemePolaska,
			int trajanjeVoznje, String oznakaDana) {
		this.pruga = pruga;
		this.polaznaStanica = polaznaStanica;
		this.odredisnaStanica = odredisnaStanica;
		this.smjer = smjer;
		this.vrijemePolaska = vrijemePolaska;
		this.trajanjeVoznje = trajanjeVoznje;
		this.oznakaDana = oznakaDana;
	}

	@Override
	public void prikaziInformacije() {
		System.out.println("Etapa: " + polaznaStanica.dohvatiNazivStanice() + " -> "
				+ odredisnaStanica.dohvatiNazivStanice() + " | Smjer: " + smjer + " | Vrijeme polaska: "
				+ vrijemePolaska + " | Trajanje: " + trajanjeVoznje + " min" + " | Oznaka dana: " + oznakaDana);
	}

	@Override
	public int izracunajUkupnoVrijeme(String vrstaVlaka) {
	    List<SegmentPruge> segmenti = dohvatiSegmenteZaSmjer();
	    int ukupnoVrijeme = 0;
	    boolean pocetakPronaden = false;

	    for (SegmentPruge segment : segmenti) {
	        if (!pocetakPronaden) {
	            if (segment.getPocetnaStanica().equals(polaznaStanica) ||
	                segment.getZavrsnaStanica().equals(polaznaStanica)) {
	                pocetakPronaden = true;
	            } else {
	                continue; 
	            }
	        }

	        switch (vrstaVlaka) {
	            case "U":
	                ukupnoVrijeme += segment.dohvatiVrijemeUbrzaniVlak() != null 
	                    ? segment.dohvatiVrijemeUbrzaniVlak() : 0;
	                break;
	            case "B":
	                ukupnoVrijeme += segment.dohvatiVrijemeBrziVlak() != null 
	                    ? segment.dohvatiVrijemeBrziVlak() : 0;
	                break;
	            default:
	                ukupnoVrijeme += segment.dohvatiVrijemeNormalniVlak();
	                break;
	        }

	        if (segment.getPocetnaStanica().equals(odredisnaStanica) ||
	            segment.getZavrsnaStanica().equals(odredisnaStanica)) {
	            break; 
	        }
	    }

	    return ukupnoVrijeme;
	}

	@Override
	public double izracunajUkupnoKilometara() {
	    List<SegmentPruge> segmenti = dohvatiSegmenteZaSmjer();
	    double ukupnaUdaljenost = 0;
	    boolean pocetakPronaden = false;

	    for (SegmentPruge segment : segmenti) {
	        if (!pocetakPronaden) {

	            if (segment.getPocetnaStanica().equals(polaznaStanica) ||
	                segment.getZavrsnaStanica().equals(polaznaStanica)) {
	                pocetakPronaden = true;
	            } else {
	                continue; 
	            }
	        }

	        ukupnaUdaljenost += segment.dohvatiDuzinu();

	        if (segment.getPocetnaStanica().equals(odredisnaStanica) ||
	            segment.getZavrsnaStanica().equals(odredisnaStanica)) {
	            break;
	        }
	    }

	    return ukupnaUdaljenost;
	}

	public List<SegmentPruge> dohvatiSegmenteZaSmjer() {
		List<SegmentPruge> segmenti = new ArrayList<>(pruga.dohvatiSegmentePruge());
		if ("O".equalsIgnoreCase(smjer)) {
			Collections.reverse(segmenti);
		}
		return segmenti;
	}

	public int dohvatiVrijemePolaska() {
		return vrijemePolaska;
	}

	public Stanica dohvatiPolaznuStanicu() {
		return polaznaStanica;
	}

	public Stanica dohvatiOdredisnuStanicu() {
		return odredisnaStanica;
	}

	public String dohvatiOznakuDana() {
		return oznakaDana;
	}

	public Pruga dohvatiPrugu() {
		return pruga;
	}

	public String dohvatiSmjer(){
    	return smjer;
    } 
}
