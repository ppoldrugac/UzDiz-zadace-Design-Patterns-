package org.foi.uzdiz.strategy;

public class VikendStrategija implements StrategijaCijene {

	private double popust;
	private double iznosPopusta;

	public VikendStrategija(double popust) {
		
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
	    return "Vikend popust: " + String.format("%.2f EUR", iznosPopusta);
	}
}