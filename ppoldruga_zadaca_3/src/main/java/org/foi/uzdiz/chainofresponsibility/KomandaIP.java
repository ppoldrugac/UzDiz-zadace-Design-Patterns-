package org.foi.uzdiz.chainofresponsibility;

import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

public class KomandaIP extends LanacKomandi {

    @Override
    protected boolean mozeObraditi(String komanda) {
        return komanda.startsWith("IP");
    }

    @Override
    protected void obradi(String komanda) {
        if (komanda.equals("IP")) {
            Tvrtka.getInstance().obradiKomanduIP();
        } else {
            UpraviteljGresaka.getInstance().greskaURadu("Neispravna komanda IP!");
        }
    }
}