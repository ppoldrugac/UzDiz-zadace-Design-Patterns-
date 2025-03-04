package org.foi.uzdiz.chainofresponsibility;

import org.foi.uzdiz.mediator.Mediator;
import org.foi.uzdiz.observer.KorisnickiRegistar;
import org.foi.uzdiz.observer.Korisnik;
import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

public class KomandaDPK extends LanacKomandi {
	
	private Mediator mediator;
	private KorisnickiRegistar registar;

    public KomandaDPK() {
    	this.mediator = Tvrtka.getInstance().dohvatiMediator();
    	this.registar = Tvrtka.getInstance().dohvatiKorisnickiRegistar();
    }

    @Override
    protected boolean mozeObraditi(String komanda) {
        return komanda.startsWith("DPK ");
    }

    @Override
    protected void obradi(String komanda) {
        String[] dijelovi = komanda.split(" - ");
        if (dijelovi.length < 2) {
            UpraviteljGresaka.getInstance().greskaURadu("Greška: Nedostaju parametri. Sintaksa: DPK ime prezime - oznakaVlaka [- stanica]");
            return;
        }

        String korisnikInfo = dijelovi[0].substring(4).trim(); 
        String[] korisnikPodaci = korisnikInfo.split(" ", 2);
        if (korisnikPodaci.length < 2) {
            UpraviteljGresaka.getInstance().greskaURadu("Greška: Neispravno ime i prezime. Sintaksa: DPK ime prezime - oznakaVlaka [- stanica]");
            return;
        }

        String ime = korisnikPodaci[0];
        String prezime = korisnikPodaci[1];
        String oznakaVlaka = dijelovi[1].trim();
        String stanica = (dijelovi.length == 3) ? dijelovi[2].trim() : null;

        Korisnik korisnik = registar.pronadiKorisnika(ime, prezime);
        if (korisnik == null) {
            UpraviteljGresaka.getInstance().greskaURadu("Korisnik " + ime + " " + prezime + " nije pronađen u registru.");
            return;
        }

        if (!Tvrtka.getInstance().postojiVlak(oznakaVlaka)) {
            UpraviteljGresaka.getInstance().greskaURadu("Vlak s oznakom " + oznakaVlaka + " ne postoji.");
            return;
        }

        if (stanica != null && !Tvrtka.getInstance().postojiStanica(stanica)) {
            UpraviteljGresaka.getInstance().greskaURadu("Stanica " + stanica + " ne postoji.");
            return;
        }

        korisnik.pratiVlak(oznakaVlaka);
        if (stanica != null) {
            korisnik.pratiStanicu(stanica);
        }
        

        mediator.dodajPretplatu(oznakaVlaka, korisnik);
        
        
        System.out.println();
        System.out.println("Korisnik " + korisnik + " prati vlak " + oznakaVlaka +
                (stanica != null ? " i stanicu " + stanica : "") + ".");
        System.out.println();
    }
}
