package org.foi.uzdiz.observer;

import java.util.ArrayList;
import java.util.List;

public class Korisnik implements Observer {
	
    private String ime;
    private String prezime;
    
    private List<String> vlakoviZaPracenje = new ArrayList<>();
    private List<String> staniceZaPracenje = new ArrayList<>();

    public Korisnik(String ime, String prezime) {
        this.ime = ime;
        this.prezime = prezime;
    }
    
    public void pratiVlak(String oznakaVlaka) {
    	vlakoviZaPracenje.add(oznakaVlaka);
    }
    
    public void pratiStanicu(String nazivStanice) {
    	staniceZaPracenje.add(nazivStanice);
    }
    
    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    @Override
    public void update(String message) {
        System.out.println("Obavijest za korisnika " + ime + " " + prezime + ": " + message);
    }

    @Override
    public String toString() {
        return ime + " " + prezime;
    }
    
    
}
