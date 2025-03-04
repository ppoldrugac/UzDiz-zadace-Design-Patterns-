package org.foi.uzdiz.memento;

import java.util.List;

public class ProdajaKarataOriginator {

	private SkladisteKarataCaretaker skladisteKarata = new SkladisteKarataCaretaker();
	private int brojacKarata = 0;

	public KartaMemento prodajKartu(String oznakaVlaka, String polaznaStanica, String odredisnaStanica, String datum,
			String nacinKupovine, int trajanjeVoznje, double cijenaKarte, String vrijemePolaska,
			String vrijemeDolaska, String relacija, List<String> popusti) {

		KartaMemento kartaMemento = new KartaMemento(++brojacKarata, oznakaVlaka, polaznaStanica, odredisnaStanica,
				datum, nacinKupovine, trajanjeVoznje, cijenaKarte, vrijemePolaska, vrijemeDolaska, relacija, popusti);

		skladisteKarata.dodajKartuMemento(kartaMemento);

		return kartaMemento;
	}

	public KartaMemento dohvatiKartu(int redniBrojKarte) {
		return skladisteKarata.dohvatiKartuMemento(redniBrojKarte);
	}

	public int brojKarata() {
        return skladisteKarata.brojKarata();
    }
}
