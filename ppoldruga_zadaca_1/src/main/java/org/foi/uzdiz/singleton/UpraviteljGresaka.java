package org.foi.uzdiz.singleton;

import java.util.HashSet;
import java.util.Set;

public class UpraviteljGresaka {

	private static volatile UpraviteljGresaka greskaInstance;

	private int brojacGresaka = 0; 
	private int brojacGresakaUDatoteci = 0; 

	private Set<String> zabiljezeneGreske;

	private UpraviteljGresaka() {
		this.zabiljezeneGreske = new HashSet<>();
	}

	public static UpraviteljGresaka getInstance() {
		if (greskaInstance == null) {
			synchronized (Tvrtka.class) {
				if (greskaInstance == null) {
					greskaInstance = new UpraviteljGresaka();
				}
			}
		}
		return greskaInstance;
	}

	public void resetirajBrojacDatoteke() {
		brojacGresakaUDatoteci = 0;
	}

	public void greskaURetku(String zapis, String opisGreske) {
		String kljucGreske = zapis + " || " + opisGreske;

		if (!zabiljezeneGreske.contains(kljucGreske)) {
			zabiljezeneGreske.add(kljucGreske);
			brojacGresaka++;
			brojacGresakaUDatoteci++;
			System.out.println("Ukupan redni broj greške: " + brojacGresaka + " || Redni broj greške unutar datoteke: "
					+ brojacGresakaUDatoteci + " || Sadržaj retka: " + zapis + " || Opis: " + opisGreske + "\n");
		}

	}

	public void greskaSDatotekama(String opisGreske) {
		brojacGresaka++;
		System.out.println("Ukupan redni broj greške: " + brojacGresaka + " || Opis greške s datotekom: " + opisGreske + "\n");
	}

	public void sustavskaGreska(Exception e) {
		brojacGresaka++;
		System.out
				.println("Ukupan redni broj greške: " + brojacGresaka + " || Opis sustavske greške: " + e.getMessage() + "\n");
	}

	public void greskaURadu(String opisGreske) {
		brojacGresaka++;
		System.out.println("Ukupan redni broj greške: " + brojacGresaka + " || Opis greške u radu: " + opisGreske + "\n");
	}

	public void greskaUlazneKomande(String opisGreske) {
		brojacGresaka++;
		System.out.println(
				"Ukupan redni broj greške: " + brojacGresaka + " || Opis greške u ulaznoj komandi: " + opisGreske + "\n");
	}

	public int dohvatiBrojGresaka() {
		return brojacGresaka;
	}

}
