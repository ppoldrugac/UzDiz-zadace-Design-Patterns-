package org.foi.uzdiz.builder;

import org.foi.uzdiz.Vozilo;

public interface VoziloBuilder {
	VoziloBuilder postaviOznaku(String oznaka);

	VoziloBuilder postaviOpis(String opis);

	VoziloBuilder postaviProizvodjaca(String proizvodjac);

	VoziloBuilder postaviGodinuProizvodnje(int godinaProizvodnje);

	VoziloBuilder postaviNamjenu(String namjena);

	VoziloBuilder postaviVrstuPrijevoza(String vrstaPrijevoza);

	VoziloBuilder postaviVrstuPogona(String vrstaPogona);

	VoziloBuilder postaviMaxBrzinu(int maxBrzina);

	VoziloBuilder postaviStatus(String status);

	VoziloBuilder postaviMaxSnagu(double maxSnaga);

	VoziloBuilder postaviBrojSjedala(int brojSjedala);

	VoziloBuilder postaviBrojStajacihMjesta(int brojStajacihMjesta);

	VoziloBuilder postaviBrojBicikala(int brojBicikala);

	VoziloBuilder postaviBrojKreveta(int brojKreveta);

	VoziloBuilder postaviBrojAutomobila(int brojAutomobila);

	VoziloBuilder postaviNosivost(double nosivost);

	VoziloBuilder postaviPovrsinu(double povrsina);

	VoziloBuilder postaviZapreminu(double zapremina);

	Vozilo build();
}
