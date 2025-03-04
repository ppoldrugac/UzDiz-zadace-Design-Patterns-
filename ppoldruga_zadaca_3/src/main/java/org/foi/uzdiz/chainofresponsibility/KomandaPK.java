package org.foi.uzdiz.chainofresponsibility;

import org.foi.uzdiz.observer.KorisnickiRegistar;
import org.foi.uzdiz.singleton.Tvrtka;

public class KomandaPK extends LanacKomandi {

	private KorisnickiRegistar registar;

	public KomandaPK() {
		this.registar = Tvrtka.getInstance().dohvatiKorisnickiRegistar();
	}

	@Override
	protected boolean mozeObraditi(String komanda) {
		return komanda.equals("PK");
	}

	@Override
	protected void obradi(String komanda) {
		if (registar == null || registar.jePrazan()) {
			System.out.println();
            System.out.println("Registar korisnika je prazan. Koristite komandu DK za dodavanje korisnika.");
            System.out.println();
        } else {
            registar.ispisiKorisnike();
        }
	}
}
