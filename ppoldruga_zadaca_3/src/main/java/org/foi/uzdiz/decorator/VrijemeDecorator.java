package org.foi.uzdiz.decorator;

public abstract class VrijemeDecorator implements Vrijeme {
    protected Vrijeme dekoriranoVrijeme;

    public VrijemeDecorator(Vrijeme dekoriranoVrijeme) {
        this.dekoriranoVrijeme = dekoriranoVrijeme;
    }

    @Override
    public int dohvatiVrijeme() {
        return dekoriranoVrijeme.dohvatiVrijeme();
    }

    @Override
    public String formatirajVrijeme() {
        return dekoriranoVrijeme.formatirajVrijeme();
    }
}