package org.foi.uzdiz.factorymethod;

public class PrugeFactory extends DatotekaFactory {

	@Override
	public CitacDatoteka ucitajDatoteku(String nazivDatoteke) {
		CitacDatoteka product = new CitacPruga();
		return product;
	}
	

}
