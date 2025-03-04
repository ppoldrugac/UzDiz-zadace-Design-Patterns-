package org.foi.uzdiz.strategy;

public class OsnovnaStrategija implements StrategijaCijene {

	private double cijenaNormalni;
	private double cijenaUbrzani;
	private double cijenaBrzi;
	private double posljednjeIzracunataCijena;

	public OsnovnaStrategija(double cijenaNormalni, double cijenaUbrzani, double cijenaBrzi) {
		this.cijenaNormalni = cijenaNormalni;
		this.cijenaUbrzani = cijenaUbrzani;
		this.cijenaBrzi = cijenaBrzi;
	}

	@Override
	public double prilagodiCijenu(double kilometri, String vrstaVlaka) {
		switch (vrstaVlaka) {
		case "N":
			posljednjeIzracunataCijena = kilometri * cijenaNormalni;
			break;
		case "U":
			posljednjeIzracunataCijena = kilometri * cijenaUbrzani;
			break;
		case "B":
			posljednjeIzracunataCijena = kilometri * cijenaBrzi;
			break;
		default:
			posljednjeIzracunataCijena = 0;
			break;
		}
		return posljednjeIzracunataCijena;
	}

	@Override
	public String opisPromjeneCijene() {
		return "Izvorna cijena: " + String.format("%.2f EUR", posljednjeIzracunataCijena);
	}
}
