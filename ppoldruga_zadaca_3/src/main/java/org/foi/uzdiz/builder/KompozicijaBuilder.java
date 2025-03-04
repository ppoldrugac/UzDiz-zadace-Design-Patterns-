package org.foi.uzdiz.builder;

import org.foi.uzdiz.Vozilo;
import org.foi.uzdiz.Kompozicija;

public interface KompozicijaBuilder {
    KompozicijaBuilder postaviOznaku(String oznaka);
    KompozicijaBuilder dodajPrijevoznoSredstvo(Vozilo vozilo, String uloga);
    Kompozicija build();
}
