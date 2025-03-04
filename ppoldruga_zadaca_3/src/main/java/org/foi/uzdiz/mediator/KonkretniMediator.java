package org.foi.uzdiz.mediator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.foi.uzdiz.observer.Korisnik;
import org.foi.uzdiz.singleton.Tvrtka;

public class KonkretniMediator implements Mediator {
	
    private Map<String, List<Korisnik>> pretplate = new HashMap<>();

    @Override
    public void posaljiObavijest(String oznakaVlaka, String poruka) {
    	
    	if (!Tvrtka.getInstance().postojiVlak(oznakaVlaka)) {
            System.out.println("\nGreška: Vlak s oznakom " + oznakaVlaka + " ne postoji.\n");
            return;
        }
    	
        List<Korisnik> korisnici = pretplate.get(oznakaVlaka);
        
        if (korisnici == null || korisnici.isEmpty()) {
            System.out.println("\nNema korisnika pretplaćenih na vlak " + oznakaVlaka + ".\n");
            return;
        }
        
        System.out.println();
        for (Korisnik korisnik : korisnici) {
            korisnik.update(poruka);
        }
        System.out.println();
    }

    @Override
    public void dodajPretplatu(String oznakaVlaka, Korisnik korisnik) {
        pretplate.computeIfAbsent(oznakaVlaka, k -> new ArrayList<>()).add(korisnik);
    }

}

