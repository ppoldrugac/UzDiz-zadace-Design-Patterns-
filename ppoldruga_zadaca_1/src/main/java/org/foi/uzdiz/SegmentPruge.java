package org.foi.uzdiz;

public class SegmentPruge {

    private Stanica pocetnaStanica;
    private Stanica zavrsnaStanica;
	private int brojKolosijeka; 
	private float duzina; 
	private float doPoOsovini; 
	private float doPoDuznomM; 

    public SegmentPruge(Stanica pocetnaStanica, Stanica zavrsnaStanica, int brojKolosijeka, float duzina, float doPoOsovini, float doPoDuznomM) {
        this.pocetnaStanica = pocetnaStanica;
        this.zavrsnaStanica = zavrsnaStanica;
        this.brojKolosijeka = brojKolosijeka;
        this.duzina = duzina;
        this.doPoOsovini = doPoOsovini;
        this.doPoDuznomM = doPoDuznomM;
    }


    public Stanica getPocetnaStanica() {
        return pocetnaStanica;
    }

    public Stanica getZavrsnaStanica() {
        return zavrsnaStanica;
    }
    
	public int dohvatiBrojKolosijeka() {
		return brojKolosijeka;
	}

	public float dohvatiDuzinu() {
		return duzina;
	}

	public float dohvatiDoPoOsovini() {
		return doPoOsovini;
	}

	public float dohvatiDoPoDuznomM() {
		return doPoDuznomM;
	}

    
}
