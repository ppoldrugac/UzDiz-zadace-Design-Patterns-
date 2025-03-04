package org.foi.uzdiz.factorymethod;

public class KompozicijeFactory extends DatotekaFactory{

	@Override
	public CitacDatoteka ucitajDatoteku(String nazivDatoteke) {
		CitacDatoteka product = new CitacKompozicija();
		return product;
	}
	

}
