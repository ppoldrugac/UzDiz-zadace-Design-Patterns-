package org.foi.uzdiz.factorymethod;

public class StaniceFactory extends DatotekaFactory {

	@Override
	public CitacDatoteka ucitajDatoteku(String nazivDatoteke) {
		CitacDatoteka product = new CitacStanica();
		return product;
	}
	

}
