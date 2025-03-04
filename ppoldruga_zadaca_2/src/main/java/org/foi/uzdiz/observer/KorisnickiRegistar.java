package org.foi.uzdiz.observer;

import java.util.HashMap;
import java.util.Map;

public class KorisnickiRegistar implements Subject {

	private Map<String, Observer> korisnici = new HashMap<>();

	 @Override
	    public void dodajPromatraca(Observer observer) {
	        if (observer instanceof Korisnik korisnik) {
	            String kljuc = korisnik.getIme() + " " + korisnik.getPrezime();
	            korisnici.put(kljuc, observer);
	        }
	    }

	    @Override
	    public void ukloniPromatraca(Observer observer) {
	        if (observer instanceof Korisnik korisnik) {
	            String kljuc = korisnik.getIme() + " " + korisnik.getPrezime();
	            korisnici.remove(kljuc);
	        }
	    }

	    @Override
	    public void obavijestiPromatrace(String message) {
	        for (Observer korisnik : korisnici.values()) {
	            korisnik.update(message);
	        }
	    }
	    
	    public Korisnik pronadiKorisnika(String ime, String prezime) {
	        String kljuc = ime + " " + prezime;
	        Observer observer = korisnici.get(kljuc);
	        if (observer instanceof Korisnik korisnik) {
	            return korisnik;
	        }
	        return null;
	    }

	public void ispisiKorisnike() {
		System.out.println();
		System.out.println("Registrirani korisnici:");
		for (Observer korisnik : korisnici.values()) {
            System.out.println(korisnik.toString());
        }
		System.out.println();
	}
	
	public boolean jePrazan() {
	    return korisnici.isEmpty();
	}
}
