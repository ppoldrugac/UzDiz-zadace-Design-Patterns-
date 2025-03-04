package org.foi.uzdiz;

import org.foi.uzdiz.singleton.Tvrtka;
import org.foi.uzdiz.singleton.UpraviteljGresaka;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	private static final Tvrtka tvrtka = Tvrtka.getInstance();

	public static void main(String[] args) {

		if (provjeriArgumente(args)) {
			Scanner scanner = new Scanner(System.in);
			String komanda;

			System.out.println("Unesite komandu (ili 'Q' za izlaz):");

			while (true) {
				komanda = scanner.nextLine().trim();

				if (komanda.equalsIgnoreCase("Q")) {
					System.out.println("Program završen.");
					break;
				}

				obradiKomandu(komanda);
			}

			scanner.close();
		}
	}

	private static boolean provjeriArgumente(String[] args) {
		String spojenaKomanda = String.join(" ", args);
		String regex = "(?=.*--zs (?<zs>[\\w_\\-]+\\.csv))(?=.*--zps (?<zps>[\\w_\\-]+\\.csv))(?=.*--zk (?<zk>[\\w_\\-]+\\.csv)).*";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(spojenaKomanda);

		if (matcher.matches()) {
			tvrtka.ucitajDatotekuStanica(matcher.group("zs"));
			tvrtka.ucitajDatotekuVozila(matcher.group("zps"));
			tvrtka.ucitajDatotekuKompozicija(matcher.group("zk"));

			tvrtka.ucitajDatoteke();
			return true;
		} else {
			UpraviteljGresaka.getInstance().greskaUlazneKomande("Argumenti nisu ispravni!");

		}
		return false;
	}

	private static void obradiKomandu(String komanda) {
		String[] dijeloviKomande = komanda.split(" ");

		switch (dijeloviKomande[0].toUpperCase()) {
		case "IP":
			if (dijeloviKomande.length == 1) {
				tvrtka.obradiKomanduIP();
			} else {
				UpraviteljGresaka.getInstance().greskaURadu("Neispravna komanda IP!");
			}
			break;

		case "ISP":
			if (dijeloviKomande.length == 3) {
				String oznakaPruge = dijeloviKomande[1];
				String redoslijed = dijeloviKomande[2];
				tvrtka.obradiKomanduISP(oznakaPruge, redoslijed);
			} else {
				UpraviteljGresaka.getInstance().greskaURadu("Neispravna komanda ISP!");
			}
			break;

		case "ISI2S":
			if (dijeloviKomande.length >= 2) {
		        String spojenaKomanda = String.join(" ", dijeloviKomande).substring(6);

		        int separatorIndex = spojenaKomanda.indexOf(" - ");
		        if (separatorIndex != -1) {
		            String polaznaStanica = spojenaKomanda.substring(0, separatorIndex).trim();
		            String odredisnaStanica = spojenaKomanda.substring(separatorIndex + 3).trim();

		            if (!tvrtka.postojiStanica(polaznaStanica)) {
		                UpraviteljGresaka.getInstance().greskaURadu("Polazna stanica '" + polaznaStanica + "' ne postoji.");
		                break;
		            }
		            if (!tvrtka.postojiStanica(odredisnaStanica)) {
		                UpraviteljGresaka.getInstance().greskaURadu("Odredišna stanica '" + odredisnaStanica + "' ne postoji.");
		                break;
		            }

		            tvrtka.obradiKomanduISI2S(polaznaStanica, odredisnaStanica);
		        } else {
		            UpraviteljGresaka.getInstance().greskaURadu("Neispravna komanda ISI2S! Očekivani format je: 'ISI2S pocetnaStanica - odredisnaStanica'");
		        }
		    } else {
		        UpraviteljGresaka.getInstance().greskaURadu("Neispravna komanda ISI2S! Očekivani format je: 'ISI2S pocetnaStanica - odredisnaStanica'");
		    }
		    break;

		case "IK":
			if (dijeloviKomande.length == 2) {
				String oznaka = dijeloviKomande[1];
				tvrtka.obradiKomanduIK(oznaka);
			} else {
				UpraviteljGresaka.getInstance().greskaURadu("Neispravna komanda IK!");
			}
			break;

		default:
			UpraviteljGresaka.getInstance().greskaURadu("Nepoznata komanda: " + komanda);
			break;
		}
		System.out.println("Unesite komandu (ili 'Q' za izlaz):");
	}

}