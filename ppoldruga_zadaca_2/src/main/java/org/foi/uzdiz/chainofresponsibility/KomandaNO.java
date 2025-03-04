package org.foi.uzdiz.chainofresponsibility;

import org.foi.uzdiz.mediator.Mediator;
import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

public class KomandaNO extends LanacKomandi {

    private Mediator mediator;

    public KomandaNO() {
        this.mediator = Tvrtka.getInstance().dohvatiMediator();
    }

    @Override
    protected boolean mozeObraditi(String komanda) {
        return komanda.startsWith("NO ");
    }

    @Override
    protected void obradi(String komanda) {
        String[] dijelovi = komanda.split(" - ", 2);
        if (dijelovi.length < 2) {
            UpraviteljGresaka.getInstance()
                    .greskaURadu("Greška u komandi NO: Nedostaju parametri. Sintaksa: NO oznakaVlaka - poruka");
            return;
        }

        String oznakaVlaka = dijelovi[0].substring(3).trim();
        String poruka = dijelovi[1];
        
        if (oznakaVlaka.isEmpty()) {
            UpraviteljGresaka.getInstance()
                    .greskaURadu("Greška: Nedostaje oznaka vlaka. Sintaksa: NO oznakaVlaka - poruka");
            return;
        }

        if (poruka.isEmpty()) {
            UpraviteljGresaka.getInstance()
                    .greskaURadu("Greška: Poruka ne može biti prazna.");
            return;
        }

        mediator.posaljiObavijest(oznakaVlaka, poruka);
    }
}
