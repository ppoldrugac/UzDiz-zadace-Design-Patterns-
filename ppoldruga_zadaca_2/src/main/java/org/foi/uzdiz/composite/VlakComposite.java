package org.foi.uzdiz.composite;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.foi.uzdiz.singleton.UpraviteljGresaka;

public class VlakComposite implements VozniRedComponent {
	private String oznakaVlak;
	private String vrstaVlak;
	private List<VozniRedComponent> etape = new ArrayList<>();

	public VlakComposite(String oznakaVlak, String vrstaVlak) {
		this.oznakaVlak = oznakaVlak;
		this.vrstaVlak = vrstaVlak;
	}

	public void dodajKomponentu(VozniRedComponent komponenta) {
	    if (!(komponenta instanceof EtapaLeaf)) {
	        UpraviteljGresaka.getInstance().greskaURadu(
	            "Samo etape mogu biti dodane u vlak! Pokušano dodavanje: " 
	            + komponenta.getClass().getSimpleName());
	        return;
	    }

	    EtapaLeaf novaEtapa = (EtapaLeaf) komponenta;

	    if (etape.contains(novaEtapa)) {
	        System.out.println("Greška: Etapa je već dodana u listu.");
	        return;
	    }

	    List<EtapaLeaf> privremenaLista = new ArrayList<>();
	    for (VozniRedComponent komponenta2 : etape) {
	        if (komponenta2 instanceof EtapaLeaf) {
	            privremenaLista.add((EtapaLeaf) komponenta2);
	        }
	    }
	    privremenaLista.add(novaEtapa);

	    privremenaLista.sort(Comparator.comparingInt(EtapaLeaf::dohvatiVrijemePolaska));

	    for (int i = 1; i < privremenaLista.size(); i++) {
	        EtapaLeaf prethodna = privremenaLista.get(i - 1);
	        EtapaLeaf trenutna = privremenaLista.get(i);

	        if (!prethodna.dohvatiOdredisnuStanicu().equals(trenutna.dohvatiPolaznuStanicu())) {
	            UpraviteljGresaka.getInstance().greskaURadu(
	                "Pogrešno povezivanje etapa između: " 
	                + prethodna.dohvatiOdredisnuStanicu().dohvatiNazivStanice()
	                + " i " + trenutna.dohvatiPolaznuStanicu().dohvatiNazivStanice());
	            return; 
	        }
	    }
	    etape.add(novaEtapa);
	}
	
	@Override
	public void prikaziInformacije() {
		System.out.println("Vlak: " + oznakaVlak);
		for (VozniRedComponent etapa : etape) {
			etapa.prikaziInformacije();
		}
	}

	@Override
	public int izracunajUkupnoVrijeme(String vrstaVlak) {
	    int ukupnoVrijeme = 0;
	    for (VozniRedComponent etapa : etape) {
	        ukupnoVrijeme += etapa.izracunajUkupnoVrijeme(vrstaVlak); 
	    }
	    return ukupnoVrijeme;
	}
	
	@Override
	public double izracunajUkupnoKilometara() {
	    double ukupnoKm = 0;
	    for (VozniRedComponent etapa : etape) {
	        ukupnoKm += etapa.izracunajUkupnoKilometara();
	    }
	    return ukupnoKm;
	}

	public String dohvatiOznakuVlak() {
		return oznakaVlak;
	}
	
	public String dohvatiVrstaVlak() {
	    return this.vrstaVlak;
	}

	public List<VozniRedComponent> dohvatiEtape() {
		return etape;
	}

	public String dohvatiPolaznuStanicu() {
		if (!etape.isEmpty()) {
			EtapaLeaf prvaEtapa = (EtapaLeaf) etape.get(0);
			return prvaEtapa.dohvatiPolaznuStanicu().dohvatiNazivStanice();
		}
		return "Nepoznato";
	}
	
	public String dohvatiOdredisnuStanicu() {
	    if (!etape.isEmpty()) {
	        EtapaLeaf zadnjaEtapa = (EtapaLeaf) etape.get(etape.size() - 1);
	        return zadnjaEtapa.dohvatiOdredisnuStanicu().dohvatiNazivStanice();
	    }
	    return "Nepoznato";
	}

	public int dohvatiVrijemePolaska() {
	    if (!etape.isEmpty()) {
	        EtapaLeaf prvaEtapa = (EtapaLeaf) etape.get(0);
	        return prvaEtapa.dohvatiVrijemePolaska();
	    }
	    return 0;
	}

}
