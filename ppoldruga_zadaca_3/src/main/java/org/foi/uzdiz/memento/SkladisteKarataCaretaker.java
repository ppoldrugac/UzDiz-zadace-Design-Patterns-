package org.foi.uzdiz.memento;

import java.util.ArrayList;
import java.util.List;

public class SkladisteKarataCaretaker {

	private List<KartaMemento> spremljeneKarte = new ArrayList<>();

	public void dodajKartuMemento(KartaMemento kartaMemento) {
		spremljeneKarte.add(kartaMemento);
	}

	public KartaMemento dohvatiKartuMemento(int index) {
		if (index < 0 || index >= spremljeneKarte.size()) {
			return null;
		}
		return spremljeneKarte.get(index);
	}

	public int brojKarata() {
		return spremljeneKarte.size();
	}
}
