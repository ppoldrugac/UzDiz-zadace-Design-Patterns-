package org.foi.uzdiz.builder;

import org.foi.uzdiz.Vozilo;
import org.foi.uzdiz.Kompozicija;

import java.util.ArrayList;
import java.util.List;

public class KompozicijaBuilderImpl implements KompozicijaBuilder {
    private String oznaka;
    private List<Vozilo> prijevoznaSredstva = new ArrayList<>();
    private List<String> uloge = new ArrayList<>();

    @Override
    public KompozicijaBuilder postaviOznaku(String oznaka) {
        this.oznaka = oznaka;
        return this;
    }

    @Override
    public KompozicijaBuilder dodajPrijevoznoSredstvo(Vozilo vozilo, String uloga) {
        prijevoznaSredstva.add(vozilo);
        uloge.add(uloga);
        return this;
    }

    @Override
    public Kompozicija build() {
        Kompozicija kompozicija = new Kompozicija(oznaka, prijevoznaSredstva, uloge);
        return kompozicija;
    }
}
