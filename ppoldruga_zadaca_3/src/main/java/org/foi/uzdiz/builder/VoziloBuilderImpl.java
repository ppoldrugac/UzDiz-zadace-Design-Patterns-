package org.foi.uzdiz.builder;

import org.foi.uzdiz.Vozilo;

public class VoziloBuilderImpl implements VoziloBuilder {
	private String oznaka;
	private String opis;
	private String proizvodjac;
	private int godinaProizvodnje;
	private String namjena;
	private String vrstaPrijevoza;
	private String vrstaPogona;
	private int maxBrzina;
	private String status;

	private double maxSnaga = -1;
	private int brojSjedala = 0;
	private int brojStajacihMjesta = 0;
	private int brojBicikala = 0;
	private int brojKreveta = 0;
	private int brojAutomobila = 0;
	private double nosivost = 0;
	private double povrsina = 0;
	private double zapremina = 0;

	@Override
	public VoziloBuilder postaviOznaku(String oznaka) {
		this.oznaka = oznaka;
		return this;
	}

	@Override
	public VoziloBuilder postaviOpis(String opis) {
		this.opis = opis;
		return this;
	}

	@Override
	public VoziloBuilder postaviProizvodjaca(String proizvodjac) {
		this.proizvodjac = proizvodjac;
		return this;
	}

	@Override
	public VoziloBuilder postaviGodinuProizvodnje(int godinaProizvodnje) {
		this.godinaProizvodnje = godinaProizvodnje;
		return this;
	}

	@Override
	public VoziloBuilder postaviNamjenu(String namjena) {
		this.namjena = namjena;
		return this;
	}

	@Override
	public VoziloBuilder postaviVrstuPrijevoza(String vrstaPrijevoza) {
		this.vrstaPrijevoza = vrstaPrijevoza;
		return this;
	}

	@Override
	public VoziloBuilder postaviVrstuPogona(String vrstaPogona) {
		this.vrstaPogona = vrstaPogona;
		return this;
	}

	@Override
	public VoziloBuilder postaviMaxBrzinu(int maxBrzina) {
		this.maxBrzina = maxBrzina;
		return this;
	}

	@Override
	public VoziloBuilder postaviStatus(String status) {
		this.status = status;
		return this;
	}


	@Override
	public VoziloBuilder postaviMaxSnagu(double maxSnaga) {
		this.maxSnaga = maxSnaga;
		return this;
	}

	@Override
	public VoziloBuilder postaviBrojSjedala(int brojSjedala) {
		this.brojSjedala = brojSjedala;
		return this;
	}

	@Override
	public VoziloBuilder postaviBrojStajacihMjesta(int brojStajacihMjesta) {
		this.brojStajacihMjesta = brojStajacihMjesta;
		return this;
	}

	@Override
	public VoziloBuilder postaviBrojBicikala(int brojBicikala) {
		this.brojBicikala = brojBicikala;
		return this;
	}

	@Override
	public VoziloBuilder postaviBrojKreveta(int brojKreveta) {
		this.brojKreveta = brojKreveta;
		return this;
	}

	@Override
	public VoziloBuilder postaviBrojAutomobila(int brojAutomobila) {
		this.brojAutomobila = brojAutomobila;
		return this;
	}

	@Override
	public VoziloBuilder postaviNosivost(double nosivost) {
		this.nosivost = nosivost;
		return this;
	}

	@Override
	public VoziloBuilder postaviPovrsinu(double povrsina) {
		this.povrsina = povrsina;
		return this;
	}

	@Override
	public VoziloBuilder postaviZapreminu(double zapremina) {
		this.zapremina = zapremina;
		return this;
	}

	@Override
	public Vozilo build() {
		return new Vozilo(this);
	}


	public String getOznaka() {
		return oznaka;
	}

	public String getOpis() {
		return opis;
	}

	public String getProizvodjac() {
		return proizvodjac;
	}

	public int getGodinaProizvodnje() {
		return godinaProizvodnje;
	}

	public String getNamjena() {
		return namjena;
	}

	public String getVrstaPrijevoza() {
		return vrstaPrijevoza;
	}

	public String getVrstaPogona() {
		return vrstaPogona;
	}

	public int getMaxBrzina() {
		return maxBrzina;
	}

	public String getStatus() {
		return status;
	}

	public double getMaxSnaga() {
		return maxSnaga;
	}

	public int getBrojSjedala() {
		return brojSjedala;
	}

	public int getBrojStajacihMjesta() {
		return brojStajacihMjesta;
	}

	public int getBrojBicikala() {
		return brojBicikala;
	}

	public int getBrojKreveta() {
		return brojKreveta;
	}

	public int getBrojAutomobila() {
		return brojAutomobila;
	}

	public double getNosivost() {
		return nosivost;
	}

	public double getPovrsina() {
		return povrsina;
	}

	public double getZapremina() {
		return zapremina;
	}
}
