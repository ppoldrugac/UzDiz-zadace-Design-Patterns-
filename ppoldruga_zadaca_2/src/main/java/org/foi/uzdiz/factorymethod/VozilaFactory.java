package org.foi.uzdiz.factorymethod;

public class VozilaFactory extends DatotekaFactory {

    @Override
    public CitacDatoteka ucitajDatoteku(String nazivDatoteke) {
        CitacDatoteka product = new CitacVozila();
        return product;
    }
}