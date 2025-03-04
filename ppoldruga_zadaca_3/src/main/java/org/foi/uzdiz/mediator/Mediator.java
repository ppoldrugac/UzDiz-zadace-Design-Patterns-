package org.foi.uzdiz.mediator;

import org.foi.uzdiz.observer.Korisnik;

public interface Mediator {
	
	void posaljiObavijest(String oznakaVlaka, String poruka);

	void dodajPretplatu(String oznakaVlaka, Korisnik korisnik);
}
