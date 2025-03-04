package org.foi.uzdiz.factorymethod;

import org.foi.uzdiz.OznakaDana;
import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CitacOznakaDana implements CitacDatoteka {

    @Override
    public boolean ucitajPodatke(String nazivDatoteke) {
    	
    	boolean uspjeh = true;
    	
        File datoteka = new File(nazivDatoteke);
        

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(datoteka), "UTF-8"))) {
            String prviRedak = reader.readLine(); 

            if (prviRedak != null && prviRedak.startsWith("\uFEFF")) {
				prviRedak = prviRedak.substring(1);
			}
            
            if (prviRedak == null) {
                UpraviteljGresaka.getInstance().greskaSDatotekama("Datoteka s podacima o oznakama dana je prazna!");
                return false;
            }
            
            if (!prviRedak.equals(
					"Oznaka dana;Dani vožnje")) {
				UpraviteljGresaka.getInstance().greskaSDatotekama("Neispravan informativni redak u datoteci oznaka dana!");
				return false;
			}


            String redak;
            while ((redak = reader.readLine()) != null) {
                redak = redak.trim();
                if (redak.isEmpty() || redak.startsWith("#")) {
                    continue;
                }

                String[] atributi = redak.split(";", -1);
                if (atributi.length != 2) {
                    UpraviteljGresaka.getInstance().greskaURetku(redak, "Neispravan broj atributa u retku.");
                    continue;
                }
                
                String oznaka = atributi[0].trim();
                if (!jeBroj(oznaka)) {
                    UpraviteljGresaka.getInstance().greskaURetku(redak, "Oznaka dana mora biti brojčana vrijednost!");
                    continue;
                }
                
                String daniString = atributi[1].trim();
                if (!jeStringBezSpecijalnihZnakova(daniString)) {
                    UpraviteljGresaka.getInstance().greskaURetku(redak, "Dani vožnje moraju biti string vrijednost bez specijalnih znakova!");
                    continue;
                }

                List<String> daniVožnje = parsirajDane(daniString);

                OznakaDana oznakaDana = new OznakaDana(oznaka, daniVožnje);

                Tvrtka.getInstance().dodajOznakuDana(oznakaDana);
            }
        } catch (Exception e) {
            UpraviteljGresaka.getInstance().greskaSDatotekama("Neuspješno učitavanje datoteke s oznakama dana!");
            uspjeh = false;
        }

        return uspjeh;
    }

    private List<String> parsirajDane(String daniString) {
        List<String> daniVožnje = new ArrayList<>();
        if (daniString.contains("Po")) daniVožnje.add("Po");
        if (daniString.contains("U")) daniVožnje.add("U");
        if (daniString.contains("Sr")) daniVožnje.add("Sr");
        if (daniString.contains("Č")) daniVožnje.add("Č");
        if (daniString.contains("Pe")) daniVožnje.add("Pe");
        if (daniString.contains("Su")) daniVožnje.add("Su");
        if (daniString.contains("N")) daniVožnje.add("N");

        if (daniVožnje.isEmpty()) {
            daniVožnje.add("Po");
            daniVožnje.add("U");
            daniVožnje.add("Sr");
            daniVožnje.add("Č");
            daniVožnje.add("Pe");
            daniVožnje.add("Su");
            daniVožnje.add("N");
        }

        return daniVožnje;
    }
    
    private boolean jeBroj(String oznaka) {
        try {
            Integer.parseInt(oznaka);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean jeStringBezSpecijalnihZnakova(String input) {
        return input.matches("^[a-zA-ZČčŠšŽžPoUČPeSuN\\s,]*$");
    }
}
