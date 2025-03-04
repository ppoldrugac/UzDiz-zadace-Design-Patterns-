package org.foi.uzdiz.chainofresponsibility;

import org.foi.uzdiz.observer.KorisnickiRegistar;
import org.foi.uzdiz.observer.Korisnik;
import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

public class KomandaDK extends LanacKomandi {

    @Override
    protected boolean mozeObraditi(String komanda) {
        return komanda.startsWith("DK ");
    }

    @Override
    protected void obradi(String komanda) {
        String[] dijelovi = komanda.split(" ", 3);

        if (dijelovi.length < 3) {
            UpraviteljGresaka.getInstance().greskaURadu("Greška: Nedostaju ime i prezime. Sintaksa: DK ime prezime");
            return;
        }

        String ime = dijelovi[1];
        String prezime = dijelovi[2];

        Korisnik korisnik = new Korisnik(ime, prezime);

        KorisnickiRegistar registar = Tvrtka.getInstance().dohvatiKorisnickiRegistar();
        if (registar != null) {
            registar.dodajPromatraca(korisnik);
            System.out.println();
            System.out.println("Korisnik " + korisnik + " je uspješno dodan u registar korisnika!");
            System.out.println();
        } else {
        	UpraviteljGresaka.getInstance().greskaURadu("Greška: Korisnički registar nije inicijaliziran.");
        }
    }
}
