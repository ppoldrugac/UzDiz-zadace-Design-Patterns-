package org.foi.uzdiz.strategy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.foi.uzdiz.Cjenik;

public class KontekstCijena {
	private StrategijaCijene strategija;
	private Cjenik cjenik;

	public KontekstCijena(Cjenik cjenik) {
		this.cjenik = cjenik;
	}

	public void postaviStrategijuNaOsnovuDatumaiNacina(String datum, String nacinKupovine) {

		datum = datum.trim();
		if (datum.endsWith(".")) {
			datum = datum.substring(0, datum.length() - 1);
		}
		LocalDate datumParsed = LocalDate.parse(datum, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		boolean jeVikend = datumParsed.getDayOfWeek().getValue() == 6 || datumParsed.getDayOfWeek().getValue() == 7;

		KompozitnaStrategija kompozitna = new KompozitnaStrategija();

		kompozitna.dodajStrategiju(new OsnovnaStrategija(cjenik.dohvatiCijenaNormalni(), cjenik.dohvatiCijenaUbrzani(),
				cjenik.dohvatiCijenaBrzi()));

		if (jeVikend) {
			kompozitna.dodajStrategiju(new VikendStrategija(cjenik.dohvatiPopustSuN()));
		}

		if (nacinKupovine.equals("WM")) {
			kompozitna.dodajStrategiju(new WebAplikacijaStrategija(cjenik.dohvatiPopustWebMob()));

		} else if (nacinKupovine.equals("V")) {
			kompozitna.dodajStrategiju(new VlakStrategija(cjenik.dohvatiUvecanjeVlak()));
		}

		this.strategija = kompozitna;
	}

	public double izracunajKonacnuCijenu(double kilometri, String vrstaVlaka) {
		double osnovnaCijena = strategija.prilagodiCijenu(kilometri, vrstaVlaka);
		return osnovnaCijena;
	}
	
	public List<String> dobaviPopuste() {
        if (strategija instanceof KompozitnaStrategija) {
            return ((KompozitnaStrategija) strategija).dobaviPromjeneCijena();
        }
        return new ArrayList<>();  
    }
}
