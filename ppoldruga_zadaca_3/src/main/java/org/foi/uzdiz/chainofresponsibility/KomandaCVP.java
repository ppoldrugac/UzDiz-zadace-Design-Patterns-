package org.foi.uzdiz.chainofresponsibility;

import org.foi.uzdiz.Cjenik;
import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

public class KomandaCVP extends LanacKomandi {
	@Override
	protected boolean mozeObraditi(String komanda) {
		return komanda.startsWith("CVP");
	}

	@Override
	protected void obradi(String komanda) {
		String[] dijelovi = komanda.split(" ");
		if (dijelovi.length != 7) {
			UpraviteljGresaka.getInstance().greskaURadu(
					"Neispravan broj parametara za komandu CVP! Pravilna sintaksa: CVP cijenaNormalni cijenaUbrzani cijenaBrzi popustSuN popustWebMob \n"
							+ "uvecanjeVlak");
			return;
		}

		try {
			double cijenaNormalni = Double.parseDouble(dijelovi[1].replace(",", "."));
			double cijenaUbrzani = Double.parseDouble(dijelovi[2].replace(",", "."));
			double cijenaBrzi = Double.parseDouble(dijelovi[3].replace(",", "."));
			double popustSuN = Double.parseDouble(dijelovi[4].replace(",", "."));
			double popustWebMob = Double.parseDouble(dijelovi[5].replace(",", "."));
			double uvecanjeVlak = Double.parseDouble(dijelovi[6].replace(",", "."));

			Cjenik noviCjenik = new Cjenik();
			noviCjenik.postaviCijene(cijenaNormalni, cijenaUbrzani, cijenaBrzi);
			noviCjenik.postaviPopuste(popustSuN, popustWebMob, uvecanjeVlak);

			Tvrtka.getInstance().postaviCjenik(noviCjenik);

		} catch (NumberFormatException e) {
			UpraviteljGresaka.getInstance().greskaURadu("Neispravan format brojeva u komandi CVP!");
		}
	}
}
