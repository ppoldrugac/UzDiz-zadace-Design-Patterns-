package org.foi.uzdiz;

public class SegmentPruge {

    private Stanica pocetnaStanica;
    private Stanica zavrsnaStanica;
	private int brojKolosijeka; 
	private float duzina; 
	private float doPoOsovini; 
	private float doPoDuznomM; 
	
	private int vrijemeNormalniVlak; 
    private Integer vrijemeUbrzaniVlak; 
    private Integer vrijemeBrziVlak; 


    public SegmentPruge(Stanica pocetnaStanica, Stanica zavrsnaStanica, int brojKolosijeka, float duzina, float doPoOsovini, float doPoDuznomM, int vrijemeNormalniVlak, 
            Integer vrijemeUbrzaniVlak, Integer vrijemeBrziVlak) {
        this.pocetnaStanica = pocetnaStanica;
        this.zavrsnaStanica = zavrsnaStanica;
        this.brojKolosijeka = brojKolosijeka;
        this.duzina = duzina;
        this.doPoOsovini = doPoOsovini;
        this.doPoDuznomM = doPoDuznomM;
        this.vrijemeNormalniVlak = vrijemeNormalniVlak;
        this.vrijemeUbrzaniVlak = vrijemeUbrzaniVlak;
        this.vrijemeBrziVlak = vrijemeBrziVlak;
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
	
	public Integer dohvatiVrijemeNormalniVlak() {
		return vrijemeNormalniVlak;
	}

	
	public Integer dohvatiVrijemeUbrzaniVlak() {
		return vrijemeUbrzaniVlak;
	}
	
	public Integer dohvatiVrijemeBrziVlak() {
		return vrijemeBrziVlak;
	}
	
	public void setVrijemeUbrzaniVlak(Integer vrijemeUbrzaniVlak) {
	    this.vrijemeUbrzaniVlak = vrijemeUbrzaniVlak;
	}

	public void setVrijemeBrziVlak(Integer vrijemeBrziVlak) {
	    this.vrijemeBrziVlak = vrijemeBrziVlak;
	}
}
