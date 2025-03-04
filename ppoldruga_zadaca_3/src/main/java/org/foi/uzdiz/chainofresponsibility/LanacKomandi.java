package org.foi.uzdiz.chainofresponsibility;

import org.foi.uzdiz.singleton.UpraviteljGresaka;


public abstract class LanacKomandi {
    protected LanacKomandi sljedeci;

    public void postaviSljedeceg(LanacKomandi sljedeci) {
        this.sljedeci = sljedeci;
    }

    public void obradiKomandu(String komanda) {
        if (mozeObraditi(komanda)) {
            obradi(komanda);
        } else if (sljedeci != null) {
            sljedeci.obradiKomandu(komanda);
        } else {
        	UpraviteljGresaka.getInstance().greskaURadu("Nepoznata komanda: " + komanda);
        }
    }

    protected abstract boolean mozeObraditi(String komanda);

    protected abstract void obradi(String komanda);
}