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
    
    public void dodajPracenjeVlaka(String oznakaVlaka) {
        vlakoviZaPracenje.add(oznakaVlaka);
    }
    
    public void dodajPracenjeStanice(String nazivStanice) {
        staniceZaPracenje.add(nazivStanice);
    }
    
    public boolean pratiVlak(String oznakaVlaka) {
    	return vlakoviZaPracenje.contains(oznakaVlaka);
    }
    
    public boolean pratiStanicu(String nazivStanice) {
    	 return staniceZaPracenje.contains(nazivStanice);
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
