package org.foi.uzdiz.chainofresponsibility;

import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

public class KomandaIK extends LanacKomandi {

    @Override
    protected boolean mozeObraditi(String komanda) {
        return komanda.startsWith("IK");
    }

    @Override
    protected void obradi(String komanda) {
        String[] dijeloviKomande = komanda.split(" ");
        if (dijeloviKomande.length == 2) {
            String oznaka = dijeloviKomande[1];
            Tvrtka.getInstance().obradiKomanduIK(oznaka);
        } else {
            UpraviteljGresaka.getInstance().greskaURadu("Neispravna komanda IK!");
        }
    }
}