package org.foi.uzdiz.strategy;

public class VlakStrategija implements StrategijaCijene {

	private double uvecanje;
	private double iznosUvecanja;

	public VlakStrategija(double uvecanje) {

		this.uvecanje = uvecanje;
	}

	@Override
	public double prilagodiCijenu(double osnovnaCijena, String vrstaVlaka) {

		double novaCijena = osnovnaCijena * (1 + uvecanje / 100);
        iznosUvecanja = novaCijena - osnovnaCijena; 
        return novaCijena;
		

	}
	
	@Override
	public String opisPromjeneCijene() {
	    return "Uvećanje cijene za kupovinu u vlaku: " + String.format("%.2f EUR", iznosUvecanja);
	}
}
