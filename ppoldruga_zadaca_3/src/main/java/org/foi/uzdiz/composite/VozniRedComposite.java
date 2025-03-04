package org.foi.uzdiz.composite;

import org.foi.uzdiz.singleton.UpraviteljGresaka;

import java.util.ArrayList;
import java.util.List;

public class VozniRedComposite implements VozniRedComponent {
	private List<VozniRedComponent> vlakovi = new ArrayList<>();

	public void dodajVlak(VozniRedComponent vlak) {
		vlakovi.add(vlak);
	}

	public void dodajKomponentu(VozniRedComponent komponenta) {
		if (komponenta instanceof VlakComposite) {
			vlakovi.add(komponenta);
		} else {
			UpraviteljGresaka.getInstance()
					.greskaURadu("Samo vlakovi mogu biti dodani u vozni red! Pokušano dodavanje: "
							+ komponenta.getClass().getSimpleName());
		}
	}

	@Override
	public void prikaziInformacije() {
		System.out.println("Vozni Red:");
		for (VozniRedComponent vlak : vlakovi) {
			vlak.prikaziInformacije();
		}
	}

	@Override
	public int izracunajUkupnoVrijeme(String vrstaVlak) {
	    int ukupnoVrijeme = 0;
	    for (VozniRedComponent vlak : vlakovi) {
	        ukupnoVrijeme += vlak.izracunajUkupnoVrijeme(vrstaVlak); 
	    }
	    return ukupnoVrijeme;
	}

	@Override
	public double izracunajUkupnoKilometara() {
		double ukupnoKm = 0;
		for (VozniRedComponent vlak : vlakovi) {
			ukupnoKm += vlak.izracunajUkupnoKilometara(); 
		}
		return ukupnoKm;
	}

	public VlakComposite dohvatiVlak(String oznakaVlaka) {
		for (VozniRedComponent component : vlakovi) {
			if (component instanceof VlakComposite) {
				VlakComposite vlak = (VlakComposite) component;
				if (vlak.dohvatiOznakuVlak().equals(oznakaVlaka)) {
					return vlak;
				}
			}
		}
		return null;
	}

	public List<VlakComposite> dohvatiSveVlakove() {
	    List<VlakComposite> listaVlakova = new ArrayList<>();
	    for (VozniRedComponent component : vlakovi) {
	        if (component instanceof VlakComposite) {
	            listaVlakova.add((VlakComposite) component);
	        }
	    }
	    return listaVlakova;
	}
	
}
