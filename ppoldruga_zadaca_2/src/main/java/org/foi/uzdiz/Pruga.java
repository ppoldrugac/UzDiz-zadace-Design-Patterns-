package org.foi.uzdiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pruga {
	private String oznakaPruge;
	private String kategorijaPruge;
	private String vrstaPrijevoza;
	private String statusPruge;
	private List<SegmentPruge> segmentiPruge = new ArrayList<>();

	public Pruga(String oznakaPruge, String kategorijaPruge, String vrstaPrijevoza, String statusPruge) {
		this.oznakaPruge = oznakaPruge;
		this.kategorijaPruge = kategorijaPruge;
		this.vrstaPrijevoza = vrstaPrijevoza;
		this.statusPruge = statusPruge;
	}

	public void dodajSegment(SegmentPruge segment) {
		segmentiPruge.add(segment);
	}

	public List<SegmentPruge> dohvatiSegmentePruge() {
		return segmentiPruge;
	}

	public String dohvatiOznakuPruge() {
		return oznakaPruge;
	}

	public String dohvatiKategorijuPruge() {
		return kategorijaPruge;
	}

	public String dohvatiVrstuPrijevoza() {
		return vrstaPrijevoza;
	}

	public String dohvatiStatusPruge() {
		return statusPruge;
	}

	public List<Stanica> dohvatiStaniceURedu() {
		List<Stanica> stanice = new ArrayList<>();
		for (SegmentPruge segment : segmentiPruge) {
			if (!stanice.contains(segment.getPocetnaStanica())) {
				stanice.add(segment.getPocetnaStanica());
			}
			if (!stanice.contains(segment.getZavrsnaStanica())) {
				stanice.add(segment.getZavrsnaStanica());
			}
		}
		return stanice;
	}

	public List<Stanica> dohvatiStaniceUObrnutomRedu() {
		List<Stanica> stanice = dohvatiStaniceURedu();
		Collections.reverse(stanice);
		return stanice;
	}

	public double izracunajUkupnuDuljinu() {
		double ukupnaDuljina = 0;
		for (SegmentPruge segment : segmentiPruge) {
			ukupnaDuljina += segment.dohvatiDuzinu();
		}
		return ukupnaDuljina;
	}

	public Stanica dohvatiPrvuStanicu() {
		if (!segmentiPruge.isEmpty()) {
			return segmentiPruge.get(0).getPocetnaStanica();
		}
		return null;
	}

	public Stanica dohvatiZadnjuStanicu() {
		if (!segmentiPruge.isEmpty()) {
			return segmentiPruge.get(segmentiPruge.size() - 1).getZavrsnaStanica();
		}
		return null;
	}
	
	public Stanica dohvatiStanicuNaPrugi(String nazivStanice) {
		 for (SegmentPruge segment : segmentiPruge) {
		        if (segment.getPocetnaStanica().dohvatiNazivStanice().equals(nazivStanice)) {
		            return segment.getPocetnaStanica(); 
		        }
		        if (segment.getZavrsnaStanica().dohvatiNazivStanice().equals(nazivStanice)) {
		            return segment.getZavrsnaStanica(); 
		        }
		    }
		    return null;
	}

}
