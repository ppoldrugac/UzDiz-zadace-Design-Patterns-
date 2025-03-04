package org.foi.uzdiz.decorator;

//ako ce biti u 3. zadaći dodano kašnjenje npr.
public class KasnjenjeDecorator extends VrijemeDecorator{
	private final int dodatnoVrijeme;

    public KasnjenjeDecorator(Vrijeme dekoriranoVrijeme, int dodatnoVrijeme) {
        super(dekoriranoVrijeme);
        this.dodatnoVrijeme = dodatnoVrijeme;
    }

    @Override
    public int dohvatiVrijeme() {
        return super.dohvatiVrijeme() + dodatnoVrijeme;
    }

    @Override
    public String formatirajVrijeme() {
        int ukupno = dohvatiVrijeme();
        int sati = ukupno / 60;
        int min = ukupno % 60;
        return String.format("%02d:%02d", sati, min);
    }
}
