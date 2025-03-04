package org.foi.uzdiz.chainofresponsibility;

import java.util.*;
import org.foi.uzdiz.OznakaDana;
import org.foi.uzdiz.composite.EtapaLeaf;
import org.foi.uzdiz.composite.VlakComposite;
import org.foi.uzdiz.composite.VozniRedComposite;
import org.foi.uzdiz.decorator.OsnovnoVrijeme;
import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

public class KomandaIEVD extends LanacKomandi {

    private static final Set<String> DOZVOLJENI_DANI = Set.of("Po", "U", "Sr", "Č", "Pe", "Su", "N");

    @Override
    protected boolean mozeObraditi(String komanda) {
        return komanda.startsWith("IEVD ");
    }

    @Override
    protected void obradi(String komanda) {
        String[] dijelovi = komanda.split(" ", 2);

        if (dijelovi.length < 2 || dijelovi[1].trim().isEmpty()) {
            UpraviteljGresaka.getInstance().greskaURadu("Greška: Nedostaju dani. Sintaksa: IEVD <dani>");
            return;
        }

        String daniFilter = dijelovi[1].trim();
        Set<String> filterDani = parsirajDaneFilter(daniFilter);
        if (filterDani == null) {
            UpraviteljGresaka.getInstance().greskaURadu("Greška: Neispravan format dana. Dozvoljeni dani: Po,U,Sr,Č,Pe,Su,N");
            return;
        }

        VozniRedComposite vozniRed = Tvrtka.getInstance().dohvatiVozniRed();
        if (vozniRed == null) {
            UpraviteljGresaka.getInstance().greskaURadu("Nema podataka o voznom redu.");
            return;
        }

        ispisiVlakoveZaDane(vozniRed, filterDani);
    }

    private Set<String> parsirajDaneFilter(String daniFilter) {
        Set<String> filterDani = new HashSet<>();
        for (int i = 0; i < daniFilter.length(); ) {
            String dan;
            if (i + 2 <= daniFilter.length() && DOZVOLJENI_DANI.contains(daniFilter.substring(i, i + 2))) {
                dan = daniFilter.substring(i, i + 2); 
                i += 2;
            } else if (DOZVOLJENI_DANI.contains(daniFilter.substring(i, i + 1))) {
                dan = daniFilter.substring(i, i + 1); 
                i += 1;
            } else {
                return null; 
            }
            filterDani.add(dan);
        }
        return filterDani;
    }

    private void ispisiVlakoveZaDane(VozniRedComposite vozniRed, Set<String> filterDani) {
        List<VlakComposite> vlakovi = vozniRed.dohvatiSveVlakove();

        System.out.println();
        System.out.printf("%-15s %-15s %-30s %-30s %-20s %-20s %-10s\n",
                "Oznaka vlaka", "Oznaka pruge", "Polazna stanica", "Odredišna stanica",
                "Vrijeme polaska", "Vrijeme dolaska", "Dani");
        System.out.println("-".repeat(155));

        for (VlakComposite vlak : vlakovi) {
            boolean ispisanVlak = false;

            for (var component : vlak.dohvatiEtape()) {
                if (component instanceof EtapaLeaf) {
                    EtapaLeaf etapa = (EtapaLeaf) component;
                    OznakaDana oznakaDana = Tvrtka.getInstance().dohvatiOznakuDana(etapa.dohvatiOznakuDana());
                    if (oznakaDana == null) continue;

                    Set<String> daniEtape = new HashSet<>(oznakaDana.getDaniVožnje());
                    daniEtape.retainAll(filterDani); 

                    if (!daniEtape.isEmpty()) {
                        if (!ispisanVlak) { 
                            System.out.println("-".repeat(155)); 
                            ispisanVlak = true;
                        }
                        ispisiEtapu(vlak, etapa, oznakaDana.formatirajDane());
                    }
                }
            }
        }
        System.out.println();
    }

    private void ispisiEtapu(VlakComposite vlak, EtapaLeaf etapa, String daniEtape) {
        String oznakaPruge = etapa.dohvatiPrugu().dohvatiOznakuPruge();
        String polaznaStanica = etapa.dohvatiPolaznuStanicu().dohvatiNazivStanice();
        String odredisnaStanica = etapa.dohvatiOdredisnuStanicu().dohvatiNazivStanice();
        int vrijemePolaska = etapa.dohvatiVrijemePolaska();
        int vrijemeDolaska = vrijemePolaska + etapa.izracunajUkupnoVrijeme("N");

        String formatiranoVrijemePolaska = new OsnovnoVrijeme(vrijemePolaska).formatirajVrijeme();
        String formatiranoVrijemeDolaska = new OsnovnoVrijeme(vrijemeDolaska).formatirajVrijeme();

        System.out.printf("%-15s %-15s %-30s %-30s %-20s %-20s %-10s\n",
                vlak.dohvatiOznakuVlak(), oznakaPruge, polaznaStanica, odredisnaStanica,
                formatiranoVrijemePolaska, formatiranoVrijemeDolaska, daniEtape);
    }
}
