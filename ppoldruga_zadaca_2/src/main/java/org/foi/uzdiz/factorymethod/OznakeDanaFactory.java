package org.foi.uzdiz.factorymethod;

public class OznakeDanaFactory extends DatotekaFactory {
	
	@Override
	public CitacDatoteka ucitajDatoteku(String nazivDatoteke) {
		CitacDatoteka product = new CitacOznakaDana();
		return product;
	}
}
