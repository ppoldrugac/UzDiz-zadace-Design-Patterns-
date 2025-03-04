package org.foi.uzdiz.builder;

import org.foi.uzdiz.Vozilo;

public class VoziloDirector {
    private VoziloBuilder builder;

    public VoziloDirector(VoziloBuilder builder) {
        this.builder = builder;
    }

    public Vozilo createLokomotiva(String oznaka, String opis, String proizvodjac, int godinaProizvodnje, String namjena, String vrstaPrijevoza, String vrstaPogona, int maxBrzina, String status, double maxSnaga) {
        return builder.postaviOznaku(oznaka)
                      .postaviOpis(opis)
                      .postaviProizvodjaca(proizvodjac)
                      .postaviGodinuProizvodnje(godinaProizvodnje)
                      .postaviNamjenu(namjena)
                      .postaviVrstuPrijevoza(vrstaPrijevoza)
                      .postaviVrstuPogona(vrstaPogona)
                      .postaviMaxBrzinu(maxBrzina)
                      .postaviStatus(status)
                      .postaviMaxSnagu(maxSnaga)
                      .build();
    }

    public Vozilo createPutnickiVagon(String oznaka, String opis, String proizvodjac, int godinaProizvodnje, String namjena, String vrstaPrijevoza, String vrstaPogona, int maxBrzina, String status, int brojSjedala, int brojStajacihMjesta, int brojBicikala, int brojKreveta) {
        return builder.postaviOznaku(oznaka)
                      .postaviOpis(opis)
                      .postaviProizvodjaca(proizvodjac)
                      .postaviGodinuProizvodnje(godinaProizvodnje)
                      .postaviNamjenu(namjena)
                      .postaviVrstuPrijevoza(vrstaPrijevoza)
                      .postaviVrstuPogona(vrstaPogona)
                      .postaviMaxBrzinu(maxBrzina)
                      .postaviStatus(status)
                      .postaviBrojSjedala(brojSjedala)
                      .postaviBrojStajacihMjesta(brojStajacihMjesta)
                      .postaviBrojBicikala(brojStajacihMjesta)
                      .postaviBrojKreveta(brojKreveta)
                      .build();
    }

    public Vozilo createTeretniVagon(String oznaka, String opis, String proizvodjac, int godinaProizvodnje, String namjena, String vrstaPrijevoza, String vrstaPogona, int maxBrzina, String status, int brojAutomobila, double nosivost, double povrsina, double zapremina) {
        return builder.postaviOznaku(oznaka)
                      .postaviOpis(opis)
                      .postaviProizvodjaca(proizvodjac)
                      .postaviGodinuProizvodnje(godinaProizvodnje)
                      .postaviNamjenu(namjena)
                      .postaviVrstuPrijevoza(vrstaPrijevoza)
                      .postaviVrstuPogona(vrstaPogona)
                      .postaviMaxBrzinu(maxBrzina)
                      .postaviStatus(status)
                      .postaviBrojAutomobila(brojAutomobila)
                      .postaviNosivost(nosivost)
                      .postaviPovrsinu(povrsina)
                      .postaviZapreminu(zapremina)
                      .build();
    }
}
