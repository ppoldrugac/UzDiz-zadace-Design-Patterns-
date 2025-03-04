package org.foi.uzdiz.facade;

import org.foi.uzdiz.chainofresponsibility.*;
import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class SustavFacade {

    private final Tvrtka tvrtka = Tvrtka.getInstance();
    private LanacKomandi lanacKomandi;

    public boolean inicijalizirajArgumente(String[] args) {
        if (args.length < 6) {
            UpraviteljGresaka.getInstance().greskaUlazneKomande("Nedovoljan broj argumenata!");
            return false;
        }

        Map<String, String> opcije = new HashMap<>();
        Set<String> dozvoljeneOpcije = Set.of("--zs", "--zps", "--zk", "--zvr","--zod");

        for (int i = 0; i < args.length; i += 2) {
            if (i + 1 >= args.length) {
                UpraviteljGresaka.getInstance().greskaUlazneKomande("Argument za opciju '" + args[i] + "' nedostaje!");
                return false;
            }

            String opcija = args[i];
            String vrijednost = args[i + 1];

            if (!dozvoljeneOpcije.contains(opcija)) {
                UpraviteljGresaka.getInstance().greskaUlazneKomande("Nepoznata opcija: " + opcija);
                return false;
            }

            if (opcije.containsKey(opcija)) {
                UpraviteljGresaka.getInstance().greskaUlazneKomande("Opcija '" + opcija + "' je duplicirana!");
                return false;
            }

            if (!vrijednost.endsWith(".csv")) {
                UpraviteljGresaka.getInstance()
                        .greskaUlazneKomande("Neispravan format datoteke za opciju '" + opcija + "': " + vrijednost);
                return false;
            }

            opcije.put(opcija, vrijednost);
        }

        tvrtka.ucitajDatotekuStanica(opcije.get("--zs"));
        tvrtka.ucitajDatotekuVozila(opcije.get("--zps"));
        tvrtka.ucitajDatotekuKompozicija(opcije.get("--zk"));
        tvrtka.ucitajDatotekuOznakaDana(opcije.get("--zod"));
        tvrtka.ucitajDatotekuVoznogReda(opcije.get("--zvr"));


        return true;
    }


    public boolean ucitajPodatke() {
        tvrtka.ucitajDatoteke();
        return tvrtka.sveDatotekeUspjesnoUcitane();
    }
    
    public void inicijalizirajKorisnickiRegistar() {
        Tvrtka.getInstance().dohvatiKorisnickiRegistar();
        System.out.println("Korisnički registar je spreman.");
    }
    
    public void inicijalizirajMediator() {
        Tvrtka.getInstance().dohvatiMediator();
        System.out.println("Mediator za obavijesti je spreman.");
    }

    public void inicijalizirajLanacKomandi() {
        LanacKomandi komandaIP = new KomandaIP();
        LanacKomandi komandaISP = new KomandaISP();
        LanacKomandi komandaISI2S = new KomandaISI2S();
        LanacKomandi komandaIK = new KomandaIK();
        LanacKomandi komandaIV = new KomandaIV();
        LanacKomandi komandaIEV = new KomandaIEV();
        LanacKomandi komandaIEVD = new KomandaIEVD();
        LanacKomandi komandaIVRV = new KomandaIVRV();
        LanacKomandi komandaDK = new KomandaDK();
        LanacKomandi komandaPK = new KomandaPK();
        LanacKomandi komandaDPK = new KomandaDPK();
        LanacKomandi komandaNO = new KomandaNO();

        komandaIP.postaviSljedeceg(komandaISP);
        komandaISP.postaviSljedeceg(komandaISI2S);
        komandaISI2S.postaviSljedeceg(komandaIK);
        komandaIK.postaviSljedeceg(komandaIV);
        komandaIV.postaviSljedeceg(komandaIEV);
        komandaIEV.postaviSljedeceg(komandaIEVD);
        komandaIEVD.postaviSljedeceg(komandaIVRV);
        komandaIVRV.postaviSljedeceg(komandaDK);
        komandaDK.postaviSljedeceg(komandaPK);
        komandaPK.postaviSljedeceg(komandaDPK);
        komandaDPK.postaviSljedeceg(komandaNO);
 

        lanacKomandi = komandaIP;
    }

    public void pokreniObraduKomandi() {
        Scanner scanner = new Scanner(System.in);
        String komanda;

        System.out.println("Unesite komandu (ili 'Q' za izlaz):");

        while (true) {
            komanda = scanner.nextLine().trim();

            if (komanda.equalsIgnoreCase("Q")) {
                System.out.println("Program završen.");
                break;
            }

            lanacKomandi.obradiKomandu(komanda);
            System.out.println("Unesite komandu (ili 'Q' za izlaz):");
        }

        scanner.close();
    }
}
