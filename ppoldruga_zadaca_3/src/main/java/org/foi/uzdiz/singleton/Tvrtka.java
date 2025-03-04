package org.foi.uzdiz.singleton;

import org.foi.uzdiz.*;
import org.foi.uzdiz.composite.VozniRedComposite;
import org.foi.uzdiz.factorymethod.CitacDatoteka;
import org.foi.uzdiz.factorymethod.KompozicijeFactory;
import org.foi.uzdiz.factorymethod.OznakeDanaFactory;
import org.foi.uzdiz.factorymethod.PrugeFactory;
import org.foi.uzdiz.factorymethod.StaniceFactory;
import org.foi.uzdiz.factorymethod.VozilaFactory;
import org.foi.uzdiz.factorymethod.VozniRedFactory;
import org.foi.uzdiz.mediator.KonkretniMediator;
import org.foi.uzdiz.mediator.Mediator;
import org.foi.uzdiz.memento.ProdajaKarataOriginator;
import org.foi.uzdiz.observer.KorisnickiRegistar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Tvrtka {

	private static volatile Tvrtka tvrtkaInstance;

	private boolean uspjesnoUcitaneStanice = false;
	private boolean uspjesnoUcitanePruge = false;
	private boolean uspjesnoUcitanaVozila = false;
	private boolean uspjesnoUcitaneKompozicije = false;
	private boolean uspjesnoUcitaneOznakeDana = false;
	private boolean uspjesnoUcitaniVozniRed = false;

	static String datotekaStanice = "";
	static String datotekaVozila = "";
	static String datotekaKompozicije = "";
	static String datotekaOznakeDana = "";
	static String datotekaVozniRedovi = "";

	private KorisnickiRegistar korisnickiRegistar;
	private Mediator mediator;
	private Cjenik cjenik;
	private ProdajaKarataOriginator prodajaKarataOriginator;

	public List<Stanica> listaStanica = new ArrayList<>();

	public Map<String, Pruga> mapaPruga = new LinkedHashMap<>();

	public List<Vozilo> listaVozila = new ArrayList<>();

	public List<Kompozicija> listaKompozicija = new ArrayList<>();

	private VozniRedComposite vozniRedComposite;

	public Map<String, OznakaDana> mapaOznakaDana = new LinkedHashMap<>();

	private Tvrtka() {

	}

	public static Tvrtka getInstance() {
		if (tvrtkaInstance == null) {
			synchronized (Tvrtka.class) {
				if (tvrtkaInstance == null) {
					tvrtkaInstance = new Tvrtka();
				}
			}
		}
		return tvrtkaInstance;
	}

	public ProdajaKarataOriginator dohvatiProdajaKarataOriginator() {
		if (prodajaKarataOriginator == null) {
			prodajaKarataOriginator = new ProdajaKarataOriginator();
		}
		return prodajaKarataOriginator;
	}

	public KorisnickiRegistar dohvatiKorisnickiRegistar() {
		if (korisnickiRegistar == null) {
			korisnickiRegistar = new KorisnickiRegistar();
		}
		return korisnickiRegistar;
	}

	public Mediator dohvatiMediator() {
		if (mediator == null) {
			mediator = new KonkretniMediator();
		}
		return mediator;
	}

	public void postaviCjenik(Cjenik noviCjenik) {
		this.cjenik = noviCjenik;
		System.out.println();
		System.out.println("Cjenik je uspješno ažuriran.");
		System.out.println();
	}

	public Cjenik dohvatiCjenik() {
		if (cjenik == null) {
			UpraviteljGresaka.getInstance()
					.greskaURadu("Cjenik nije inicijaliziran. Molimo postavite cjenik korištenjem komande CVP.");
			return null;
		}
		return cjenik;
	}

	public void ucitajDatoteke() {
		UpraviteljGresaka.getInstance().resetirajBrojacDatoteke();
		uspjesnoUcitaneStanice = ucitajStanice();
		uspjesnoUcitanePruge = ucitajPruge();

		UpraviteljGresaka.getInstance().resetirajBrojacDatoteke();
		uspjesnoUcitanaVozila = ucitajVozila();

		UpraviteljGresaka.getInstance().resetirajBrojacDatoteke();
		uspjesnoUcitaneKompozicije = ucitajKompozicije();

		UpraviteljGresaka.getInstance().resetirajBrojacDatoteke();
		uspjesnoUcitaneOznakeDana = ucitajOznakeDana();

		UpraviteljGresaka.getInstance().resetirajBrojacDatoteke();
		uspjesnoUcitaniVozniRed = ucitajVozniRed();

		System.out.println();

	}

	public boolean sveDatotekeUspjesnoUcitane() {
		return uspjesnoUcitaneStanice && uspjesnoUcitanePruge && uspjesnoUcitanaVozila && uspjesnoUcitaneKompozicije
				&& uspjesnoUcitaneOznakeDana && uspjesnoUcitaniVozniRed;
	}

	private boolean ucitajStanice() {
		System.out.println();
		System.out.println("Učitavam datoteku: " + datotekaStanice);
		CitacDatoteka datotekaStanica = new StaniceFactory().ucitajDatoteku(datotekaStanice);
		if (datotekaStanice != null) {
			return datotekaStanica.ucitajPodatke(datotekaStanice);
		} else {
			UpraviteljGresaka.getInstance()
					.greskaSDatotekama("Neuspješno učitavanje datoteke s podacima o željezničkim stanicama!");
			return false;
		}
	}

	private boolean ucitajPruge() {
		System.out.println();
		CitacDatoteka datotekaPruga = new PrugeFactory().ucitajDatoteku(datotekaStanice);
		if (datotekaStanice != null) {
			return datotekaPruga.ucitajPodatke(datotekaStanice);
		} else {
			UpraviteljGresaka.getInstance()
					.greskaSDatotekama("Neuspješno učitavanje datoteke s podacima o željezničkim prugama!");
			return false;
		}
	}

	private boolean ucitajVozila() {
		System.out.println();
		System.out.println("Učitavam datoteku: " + datotekaVozila);
		CitacDatoteka datotekaVozilo = new VozilaFactory().ucitajDatoteku(datotekaVozila);

		if (datotekaVozila != null) {
			return datotekaVozilo.ucitajPodatke(datotekaVozila);
		} else {
			UpraviteljGresaka.getInstance().greskaSDatotekama("Neuspješno učitavanje datoteke s podacima o vozilima!");
			return false;
		}
	}

	private boolean ucitajKompozicije() {
		System.out.println();
		System.out.println("Učitavam datoteku: " + datotekaKompozicije);
		CitacDatoteka datotekaKompozicija = new KompozicijeFactory().ucitajDatoteku(datotekaKompozicije);

		if (datotekaKompozicije != null) {
			return datotekaKompozicija.ucitajPodatke(datotekaKompozicije);
		} else {
			UpraviteljGresaka.getInstance()
					.greskaSDatotekama("Neuspješno učitavanje datoteke s podacima o kompozicijama!");
			return false;
		}
	}

	private boolean ucitajOznakeDana() {
		System.out.println();
		System.out.println("Učitavam datoteku: " + datotekaOznakeDana);
		CitacDatoteka datotekaOznakaDana = new OznakeDanaFactory().ucitajDatoteku(datotekaOznakeDana);

		if (datotekaOznakeDana != null) {
			return datotekaOznakaDana.ucitajPodatke(datotekaOznakeDana);
		} else {
			UpraviteljGresaka.getInstance()
					.greskaSDatotekama("Neuspješno učitavanje datoteke s podacima o oznakama dana!");
			return false;
		}
	}

	private boolean ucitajVozniRed() {
		System.out.println();
		System.out.println("Učitavam datoteku: " + datotekaVozniRedovi);
		CitacDatoteka datotekaVozniRed = new VozniRedFactory().ucitajDatoteku(datotekaVozniRedovi);

		if (datotekaVozniRedovi != null) {
			return datotekaVozniRed.ucitajPodatke(datotekaVozniRedovi);
		} else {
			UpraviteljGresaka.getInstance().greskaSDatotekama("Neuspješno učitavanje datoteke s voznim redom!");
			return false;
		}
	}

	// setteri za putanje do datoteka
	public void ucitajDatotekuStanica(String ucitanaDatotekaStanica) {
		datotekaStanice = ucitanaDatotekaStanica;
	}

	public void ucitajDatotekuVozila(String ucitanaDatotekaVozila) {
		datotekaVozila = ucitanaDatotekaVozila;
	}

	public void ucitajDatotekuKompozicija(String ucitanaDatotekaKompozicija) {
		datotekaKompozicije = ucitanaDatotekaKompozicija;
	}

	public void ucitajDatotekuOznakaDana(String ucitanaDatotekaOznakaDana) {
		datotekaOznakeDana = ucitanaDatotekaOznakaDana;
	}

	public void ucitajDatotekuVoznogReda(String ucitanaDatotekaVoznogReda) {
		datotekaVozniRedovi = ucitanaDatotekaVoznogReda;
	}

	public String dohvatiDatotekuStanica() {
		return datotekaStanice;
	}

	public String dohvatiDatotekuVozila() {
		return datotekaVozila;
	}

	public String dohvatiDatotekuKompozicija() {
		return datotekaKompozicije;
	}

	public String dohvatiDatotekuOznakaDana() {
		return datotekaOznakeDana;
	}

	public String dohvatiDatotekuVoznogReda() {
		return datotekaVozniRedovi;
	}

	public void dodajStanicu(Stanica stanica) {
		listaStanica.add(stanica);
	}

	public void dodajPrugu(Pruga pruga) {
		mapaPruga.put(pruga.dohvatiOznakuPruge(), pruga);
	}

	public void dodajVozilo(Vozilo vozilo) {
		listaVozila.add(vozilo);
	}

	public void dodajKompoziciju(Kompozicija kompozicija) {
		if (kompozicija != null) {
			listaKompozicija.add(kompozicija);
		}
	}

	public void dodajVozniRed(VozniRedComposite vozniRedComposite) {
		this.vozniRedComposite = vozniRedComposite;
	}

	public void dodajOznakuDana(OznakaDana oznakaDana) {
		mapaOznakaDana.put(oznakaDana.getOznaka(), oznakaDana);
	}

	public Pruga dohvatiPrugu(String oznakaPruge) {
		return mapaPruga.get(oznakaPruge);
	}

	public Vozilo dohvatiVoziloPoOznaci(String oznakaVozila) {
		for (Vozilo vozilo : listaVozila) {
			if (vozilo.dohvatiOznaku().equals(oznakaVozila)) {
				return vozilo;
			}
		}
		return null;
	}

	public VozniRedComposite dohvatiVozniRed() {
		return vozniRedComposite;
	}

	public OznakaDana dohvatiOznakuDana(String oznaka) {
		return mapaOznakaDana.get(oznaka);
	}

	public List<Kompozicija> dohvatiKompozicije() {
		return listaKompozicija;
	}

	public boolean postojiStanica(String nazivStanice) {
		for (Stanica stanica : listaStanica) {
			if (stanica.dohvatiNazivStanice().equalsIgnoreCase(nazivStanice)) {
				return true;
			}
		}
		return false;
	}

	public boolean postojiOznakaDana(String oznakaDana) {
		if (mapaOznakaDana != null && mapaOznakaDana.containsKey(oznakaDana)) {
			return true;
		}
		return false;
	}

	public boolean postojiVlak(String oznakaVlaka) {
		if (vozniRedComposite == null) {
			return false;
		}
		return vozniRedComposite.dohvatiVlak(oznakaVlaka) != null;
	}

	public void obradiKomanduIP() {
		System.out.println();
		System.out.println("Pregled pruga:");
		System.out.printf("%-10s %-30s %-30s %-10s\n", "Oznaka", "Početna stanica", "Završna stanica", "Ukupno km");

		for (Pruga pruga : mapaPruga.values()) {
			List<Stanica> stanice = pruga.dohvatiStaniceURedu();
			String pocetnaStanica = stanice.get(0).dohvatiNazivStanice();
			String zavrsnaStanica = stanice.get(stanice.size() - 1).dohvatiNazivStanice();

			System.out.printf("%-10s %-30s %-30s %-10.2f\n", pruga.dohvatiOznakuPruge(), pocetnaStanica, zavrsnaStanica,
					pruga.izracunajUkupnuDuljinu());
		}
		System.out.println();
	}

	public void obradiKomanduISP(String oznakaPruge, String redoslijed) {
		Pruga pruga = mapaPruga.get(oznakaPruge);

		if (pruga == null) {
			UpraviteljGresaka.getInstance().greskaURadu("Pruga s oznakom " + oznakaPruge + " nije pronađena.");
			return;
		}

		List<Stanica> stanice;
		if ("N".equalsIgnoreCase(redoslijed)) {
			stanice = pruga.dohvatiStaniceURedu();
		} else if ("O".equalsIgnoreCase(redoslijed)) {
			stanice = pruga.dohvatiStaniceUObrnutomRedu();
		} else {
			UpraviteljGresaka.getInstance()
					.greskaURadu("Neispravan redoslijed: " + redoslijed + ". Očekivano 'N' ili 'O'.");
			return;
		}

		System.out.println();
		System.out.println("Pregled stanica za prugu " + oznakaPruge + " ("
				+ (redoslijed.equalsIgnoreCase("N") ? "Normalan" : "Obrnuti") + " redoslijed):");
		System.out.printf("%-30s %-15s %-15s\n", "Naziv stanice", "Vrsta", "Km od početka");

		double udaljenost = 0.0;

		for (int i = 0; i < stanice.size(); i++) {
			Stanica trenutnaStanica = stanice.get(i);
			System.out.printf("%-30s %-15s %-15.2f\n", trenutnaStanica.dohvatiNazivStanice(),
					trenutnaStanica.dohvatiVrstuStanice(), udaljenost);

			if (i < stanice.size() - 1) {
				SegmentPruge segment = pronadiSegment(pruga, trenutnaStanica, stanice.get(i + 1));
				if (segment != null) {
					udaljenost += segment.dohvatiDuzinu();
				}
			}
		}

		System.out.println();
	}

	public void obradiKomanduISI2S(String polaznaStanica, String odredisnaStanica) {
		for (Pruga pruga : mapaPruga.values()) {
			List<Stanica> staniceNaPrugi = pruga.dohvatiStaniceURedu();

			int indeksPolazne = -1;
			int indeksOdredisne = -1;
			for (int i = 0; i < staniceNaPrugi.size(); i++) {
				Stanica stanica = staniceNaPrugi.get(i);
				if (stanica.dohvatiNazivStanice().equalsIgnoreCase(polaznaStanica)) {
					indeksPolazne = i;
				}
				if (stanica.dohvatiNazivStanice().equalsIgnoreCase(odredisnaStanica)) {
					indeksOdredisne = i;
				}
			}

			if (indeksPolazne != -1 && indeksOdredisne != -1) {
				ispisiStaniceIstaPruga(pruga, staniceNaPrugi, indeksPolazne, indeksOdredisne);
				return;
			}
		}

		Map<String, List<Pruga>> povezanostiStanica = izgradiMapuPovezanihPruga();

		List<Stanica> putanjaStanica = pronadiPutIzmedjuPruga(polaznaStanica, odredisnaStanica, povezanostiStanica);

		if (putanjaStanica != null && !putanjaStanica.isEmpty()) {
			ispisiPutanjuIzmedjuPruga(putanjaStanica);
		} else {
			System.out.println("Nije pronađen put između stanica na različitim prugama.");
		}
	}

	public void obradiKomanduIK(String oznaka) {
		Kompozicija trazenaKompozicija = null;
		for (Kompozicija kompozicija : listaKompozicija) {
			if (kompozicija.dohvatiOznakuKompozicije().equals(oznaka)) {
				trazenaKompozicija = kompozicija;
				break;
			}
		}

		if (trazenaKompozicija == null) {
			UpraviteljGresaka.getInstance().greskaURadu("Kompozicija s oznakom " + oznaka + " nije pronađena.");
			return;
		}

		System.out.println();
		System.out.println("Pregled kompozicije za oznaku: " + oznaka);
		System.out.printf("%-10s %-8s %-65s %-10s %-10s %-15s %-10s\n", "Oznaka", "Uloga", "Opis", "Godina", "Namjena",
				"Vrsta pogona", "Maks. brzina");

		List<Vozilo> prijevoznaSredstva = trazenaKompozicija.dohvatiPrijevoznaSredstva();
		List<String> uloge = trazenaKompozicija.dohvatiUloge();

		for (int i = 0; i < prijevoznaSredstva.size(); i++) {
			Vozilo vozilo = prijevoznaSredstva.get(i);
			String uloga = (i < uloge.size()) ? uloge.get(i) : "N/A"; // Not Available

			System.out.printf("%-10s %-8s %-65s %-10d %-10s %-15s %-10d\n", vozilo.dohvatiOznaku(), uloga,
					vozilo.dohvatiOpis(), vozilo.dohvatiGodinuProizvodnje(), vozilo.dohvatiNamjenu(),
					vozilo.dohvatiVrstuPogona(), vozilo.dohvatiMaxBrzinu());
		}

		System.out.println();
	}

	private SegmentPruge pronadiSegment(Pruga pruga, Stanica pocetna, Stanica zavrsna) {
		for (SegmentPruge segment : pruga.dohvatiSegmentePruge()) {
			if (segment.getPocetnaStanica().equals(pocetna) && segment.getZavrsnaStanica().equals(zavrsna)
					|| segment.getPocetnaStanica().equals(zavrsna) && segment.getZavrsnaStanica().equals(pocetna)) {
				return segment;
			}
		}
		return null;
	}

	private void ispisiStaniceIstaPruga(Pruga pruga, List<Stanica> stanice, int indeksPolazne, int indeksOdredisne) {
		System.out.println();
		System.out.printf("Pregled stanica između \"%s\" i \"%s\" na pruzi %s:\n",
				stanice.get(indeksPolazne).dohvatiNazivStanice(), stanice.get(indeksOdredisne).dohvatiNazivStanice(),
				pruga.dohvatiOznakuPruge());
		System.out.printf("%-30s %-15s %-15s\n", "Naziv stanice", "Vrsta", "Km od početne");

		double ukupnaUdaljenost = 0.0;
		if (indeksPolazne < indeksOdredisne) {
			for (int i = indeksPolazne; i <= indeksOdredisne; i++) {
				Stanica trenutnaStanica = stanice.get(i);
				System.out.printf("%-30s %-15s %-15.2f\n", trenutnaStanica.dohvatiNazivStanice(),
						trenutnaStanica.dohvatiVrstuStanice(), ukupnaUdaljenost);

				if (i < indeksOdredisne) {
					SegmentPruge segment = pronadiSegment(pruga, trenutnaStanica, stanice.get(i + 1));
					if (segment != null) {
						ukupnaUdaljenost += segment.dohvatiDuzinu();
					}
				}
			}
		} else {
			for (int i = indeksPolazne; i >= indeksOdredisne; i--) {
				Stanica trenutnaStanica = stanice.get(i);
				System.out.printf("%-30s %-15s %-15.2f\n", trenutnaStanica.dohvatiNazivStanice(),
						trenutnaStanica.dohvatiVrstuStanice(), ukupnaUdaljenost);

				if (i > indeksOdredisne) {
					SegmentPruge segment = pronadiSegment(pruga, trenutnaStanica, stanice.get(i - 1));
					if (segment != null) {
						ukupnaUdaljenost += segment.dohvatiDuzinu();
					}
				}
			}
		}

		System.out.println();
	}

	private List<Stanica> pronadiPutIzmedjuPruga(String polaznaStanica, String odredisnaStanica,
			Map<String, List<Pruga>> povezanostiStanica) {
		Map<Stanica, Stanica> prethodnici = new HashMap<>();
		Queue<Stanica> red = new LinkedList<>();
		Set<Stanica> posjeceneStanice = new HashSet<>();

		Stanica pocetnaStanica = null;
		for (Stanica stanica : listaStanica) {
			if (stanica.dohvatiNazivStanice().equalsIgnoreCase(polaznaStanica)) {
				pocetnaStanica = stanica;
				break;
			}
		}

		if (pocetnaStanica == null) {
			System.out.println("Polazna stanica '" + polaznaStanica + "' nije pronađena.");
			return null;
		}

		red.add(pocetnaStanica);
		posjeceneStanice.add(pocetnaStanica);

		while (!red.isEmpty()) {
			Stanica trenutnaStanica = red.poll();

			if (trenutnaStanica.dohvatiNazivStanice().equalsIgnoreCase(odredisnaStanica)) {
				return rekonstrurirajPutanju(prethodnici, pocetnaStanica, trenutnaStanica);
			}

			List<Pruga> prugeTrenutneStanice = new ArrayList<>();
			for (Pruga pruga : mapaPruga.values()) {
				if (pruga.dohvatiStaniceURedu().contains(trenutnaStanica)) {
					prugeTrenutneStanice.add(pruga);
				}
			}

			for (Pruga pruga : prugeTrenutneStanice) {
				List<Stanica> staniceNaPrugi = pruga.dohvatiStaniceURedu();
				int indeksTrenutne = staniceNaPrugi.indexOf(trenutnaStanica);

				if (indeksTrenutne > 0) {
					Stanica prethodnaStanica = staniceNaPrugi.get(indeksTrenutne - 1);
					if (!posjeceneStanice.contains(prethodnaStanica)) {
						posjeceneStanice.add(prethodnaStanica);
						red.add(prethodnaStanica);
						prethodnici.put(prethodnaStanica, trenutnaStanica);
					}
				}

				if (indeksTrenutne < staniceNaPrugi.size() - 1) {
					Stanica sljedecaStanica = staniceNaPrugi.get(indeksTrenutne + 1);
					if (!posjeceneStanice.contains(sljedecaStanica)) {
						posjeceneStanice.add(sljedecaStanica);
						red.add(sljedecaStanica);
						prethodnici.put(sljedecaStanica, trenutnaStanica);
					}
				}
			}

			if (povezanostiStanica.containsKey(trenutnaStanica.dohvatiNazivStanice())) {
				for (Pruga susjednaPruga : povezanostiStanica.get(trenutnaStanica.dohvatiNazivStanice())) {
					if (!prugeTrenutneStanice.contains(susjednaPruga)) {
						List<Stanica> staniceNaPrugi = susjednaPruga.dohvatiStaniceURedu();
						for (Stanica susjednaStanica : staniceNaPrugi) {
							if (!posjeceneStanice.contains(susjednaStanica)) {
								posjeceneStanice.add(susjednaStanica);
								red.add(susjednaStanica);
								prethodnici.put(susjednaStanica, trenutnaStanica);
							}
						}
					}
				}
			}
		}

		return null;
	}

	private List<Stanica> rekonstrurirajPutanju(Map<Stanica, Stanica> prethodnici, Stanica pocetnaStanica,
			Stanica odredisnaStanica) {
		List<Stanica> putanja = new ArrayList<>();
		for (Stanica stanica = odredisnaStanica; stanica != null; stanica = prethodnici.get(stanica)) {
			putanja.add(stanica);
		}
		Collections.reverse(putanja);
		return putanja;
	}

	private void ispisiPutanjuIzmedjuPruga(List<Stanica> putanjaStanica) {
		System.out.println();
		System.out.println("Pregled stanica između zadane dvije stanice na različitim prugama:");
		System.out.printf("%-30s %-15s %-15s\n", "Naziv stanice", "Vrsta", "Km od početka");

		double ukupnaUdaljenost = 0.0;

		for (int i = 0; i < putanjaStanica.size(); i++) {
			Stanica trenutnaStanica = putanjaStanica.get(i);
			System.out.printf("%-30s %-15s %-15.2f\n", trenutnaStanica.dohvatiNazivStanice(),
					trenutnaStanica.dohvatiVrstuStanice(), ukupnaUdaljenost);

			if (i < putanjaStanica.size() - 1) {
				SegmentPruge segment = pronadiSegmentRazlicitePruge(trenutnaStanica, putanjaStanica.get(i + 1));
				if (segment != null) {
					ukupnaUdaljenost += segment.dohvatiDuzinu();
				}
			}
		}

		System.out.println();
	}

	private SegmentPruge pronadiSegmentRazlicitePruge(Stanica pocetna, Stanica zavrsna) {
		for (Pruga pruga : mapaPruga.values()) {
			for (SegmentPruge segment : pruga.dohvatiSegmentePruge()) {
				if ((segment.getPocetnaStanica().equals(pocetna) && segment.getZavrsnaStanica().equals(zavrsna))
						|| (segment.getPocetnaStanica().equals(zavrsna)
								&& segment.getZavrsnaStanica().equals(pocetna))) {
					return segment;
				}
			}
		}
		return null;
	}

	private Map<String, List<Pruga>> izgradiMapuPovezanihPruga() {
		Map<String, List<Pruga>> povezanostiStanica = new HashMap<>();

		for (Pruga pruga : mapaPruga.values()) {
			for (Stanica stanica : pruga.dohvatiStaniceURedu()) {
				String nazivStanice = stanica.dohvatiNazivStanice();
				povezanostiStanica.computeIfAbsent(nazivStanice, k -> new ArrayList<>()).add(pruga);
			}
		}

		povezanostiStanica.entrySet().removeIf(entry -> entry.getValue().size() < 2);

		return povezanostiStanica;
	}
}
