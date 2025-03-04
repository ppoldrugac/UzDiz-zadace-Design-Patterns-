package org.foi.uzdiz.chainofresponsibility;

import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

public class KomandaISP extends LanacKomandi {

    @Override
    protected boolean mozeObraditi(String komanda) {
        return komanda.startsWith("ISP");
    }

    @Override
    protected void obradi(String komanda) {
        String[] dijeloviKomande = komanda.split(" ");
        if (dijeloviKomande.length == 3) {
            String oznakaPruge = dijeloviKomande[1];
            String redoslijed = dijeloviKomande[2];
            Tvrtka.getInstance().obradiKomanduISP(oznakaPruge, redoslijed);
        } else {
            UpraviteljGresaka.getInstance().greskaURadu("Neispravna komanda ISP!");
        }
    }
}