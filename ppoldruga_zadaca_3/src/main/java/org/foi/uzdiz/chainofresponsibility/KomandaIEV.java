package org.foi.uzdiz.chainofresponsibility;

import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.OznakaDana;
import org.foi.uzdiz.composite.EtapaLeaf;
import org.foi.uzdiz.composite.VlakComposite;
import org.foi.uzdiz.composite.VozniRedComponent;
import org.foi.uzdiz.composite.VozniRedComposite;
import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;
import org.foi.uzdiz.decorator.OsnovnoVrijeme;

public class KomandaIEV extends LanacKomandi {

    @Override
    protected boolean mozeObraditi(String komanda) {
        return komanda.startsWith("IEV") && !komanda.startsWith("IEVD ");
    }

    @Override
    protected void obradi(String komanda) {
        String[] dijelovi = komanda.split(" ", 2);
        if (dijelovi.length < 2 || dijelovi[1].trim().isEmpty()) {
        	UpraviteljGresaka.getInstance().greskaURadu("Greška: Nedostaje oznaka vlaka. Sintaksa: IEV <oznaka_vlaka>");
            return;
        }

        String oznakaVlaka = dijelovi[1].trim();

        VozniRedComposite vozniRed = Tvrtka.getInstance().dohvatiVozniRed();
        if (vozniRed == null) {
        	UpraviteljGresaka.getInstance().greskaURadu("Nema podataka o voznom redu.");
            return;
        }

        VlakComposite vlak = vozniRed.dohvatiVlak(oznakaVlaka);
        if (vlak == null) {
        	UpraviteljGresaka.getInstance().greskaURadu("Vlak s oznakom '" + oznakaVlaka + "' ne postoji.");
            return;
        }

        System.out.println();
        System.out.printf("%-15s %-15s %-30s %-30s %-20s %-20s %-20s %-10s\n",
                "Oznaka vlaka", "Oznaka pruge", "Polazna stanica", "Odredišna stanica",
                "Vrijeme polaska", "Vrijeme dolaska", "Udaljenost (km)", "Dani");
        System.out.println("-".repeat(175));

        List<EtapaLeaf> etape = new ArrayList<>();
        for (VozniRedComponent component : vlak.dohvatiEtape()) {
            if (component instanceof EtapaLeaf) {
                etape.add((EtapaLeaf) component);
            }
        }
        int trenutniPolazak = 0;

        for (EtapaLeaf etapa : etape) {
            String oznakaPruge = etapa.dohvatiPrugu().dohvatiOznakuPruge();
            String polaznaStanica = etapa.dohvatiPolaznuStanicu().dohvatiNazivStanice();
            String odredisnaStanica = etapa.dohvatiOdredisnuStanicu().dohvatiNazivStanice();
            int vrijemePolaska = etapa.dohvatiVrijemePolaska();
            double udaljenost = etapa.izracunajUkupnoKilometara();
            OznakaDana oznaka = Tvrtka.getInstance().dohvatiOznakuDana(etapa.dohvatiOznakuDana());
            String formatiraniDani = (oznaka != null) ? oznaka.formatirajDane() : "Po,U,Sr,Č,Pe,Su,N";

            
            trenutniPolazak = (trenutniPolazak == 0) ? vrijemePolaska : trenutniPolazak;
            int vrijemeDolaska = trenutniPolazak + etapa.izracunajUkupnoVrijeme("N");
            trenutniPolazak = vrijemeDolaska; 

            String formatiranoPolazak = new OsnovnoVrijeme(vrijemePolaska).formatirajVrijeme();
            String formatiranoDolazak = new OsnovnoVrijeme(vrijemeDolaska).formatirajVrijeme();

            System.out.printf("%-15s %-15s %-30s %-30s %-20s %-20s %-20.2f %-10s\n",
                    oznakaVlaka, oznakaPruge, polaznaStanica, odredisnaStanica,
                    formatiranoPolazak, formatiranoDolazak, udaljenost, formatiraniDani);
        }
        System.out.println();
    }
}
