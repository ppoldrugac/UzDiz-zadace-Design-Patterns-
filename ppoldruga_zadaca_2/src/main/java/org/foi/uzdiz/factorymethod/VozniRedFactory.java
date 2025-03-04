package org.foi.uzdiz.factorymethod;

public class VozniRedFactory extends DatotekaFactory{

	@Override
	public CitacDatoteka ucitajDatoteku(String nazivDatoteke) {
		CitacDatoteka product = new CitacVoznogReda();
		return product;
	}

}
