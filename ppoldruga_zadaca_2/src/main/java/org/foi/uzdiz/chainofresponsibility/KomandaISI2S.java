package org.foi.uzdiz.chainofresponsibility;

import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

public class KomandaISI2S extends LanacKomandi {

    @Override
    protected boolean mozeObraditi(String komanda) {
        return komanda.startsWith("ISI2S");
    }

    @Override
    protected void obradi(String komanda) {
        String[] dijeloviKomande = komanda.split(" ");
        if (dijeloviKomande.length >= 2) { 
            String spojenaKomanda = String.join(" ", dijeloviKomande).substring(6).trim();
            int separatorIndex = spojenaKomanda.indexOf(" - ");

            if (separatorIndex != -1) {
                String polaznaStanica = spojenaKomanda.substring(0, separatorIndex).trim();
                String odredisnaStanica = spojenaKomanda.substring(separatorIndex + 3).trim();

                if (!Tvrtka.getInstance().postojiStanica(polaznaStanica)) {
                    UpraviteljGresaka.getInstance()
                            .greskaURadu("Polazna stanica '" + polaznaStanica + "' ne postoji.");
                    return;
                }
                if (!Tvrtka.getInstance().postojiStanica(odredisnaStanica)) {
                    UpraviteljGresaka.getInstance()
                            .greskaURadu("Odredišna stanica '" + odredisnaStanica + "' ne postoji.");
                    return;
                }

                Tvrtka.getInstance().obradiKomanduISI2S(polaznaStanica, odredisnaStanica);
            } else {
                UpraviteljGresaka.getInstance().greskaURadu(
                        "Neispravna komanda ISI2S! Očekivani format je: 'ISI2S pocetnaStanica - odredisnaStanica'");
            }
        } else {
            UpraviteljGresaka.getInstance().greskaURadu(
                    "Neispravna komanda ISI2S! Očekivani format je: 'ISI2S pocetnaStanica - odredisnaStanica'");
        }
    }
}