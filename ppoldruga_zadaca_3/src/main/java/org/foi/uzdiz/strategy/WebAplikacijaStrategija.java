package org.foi.uzdiz.strategy;

public class WebAplikacijaStrategija implements StrategijaCijene {

	private double popust;
	private double iznosPopusta;

	public WebAplikacijaStrategija(double popust) {

		this.popust = popust;
	}

	@Override
	public double prilagodiCijenu(double osnovnaCijena, String vrstaVlaka) {

		double novaCijena = osnovnaCijena * (1 - popust / 100);
        iznosPopusta = osnovnaCijena - novaCijena; 
        return novaCijena;
	}
	
	@Override
	public String opisPromjeneCijene() {
	    return "Popust za kupovinu putem web/mobilne aplikacije: " + String.format("%.2f EUR", iznosPopusta);
	}
}
