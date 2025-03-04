package org.foi.uzdiz;

public class Cjenik {
	
    private double cijenaNormalni, cijenaUbrzani, cijenaBrzi;
    
    private double popustSuN, popustWebMob, uvecanjeVlak;

    public Cjenik() {}

    public void postaviCijene(double cijenaNormalni, double cijenaUbrzani, double cijenaBrzi) {
        this.cijenaNormalni = cijenaNormalni;
        this.cijenaUbrzani = cijenaUbrzani;
        this.cijenaBrzi = cijenaBrzi;
    }

    public void postaviPopuste(double popustSuN, double popustWebMob, double uvecanjeVlak) {
        this.popustSuN = popustSuN;
        this.popustWebMob = popustWebMob;
        this.uvecanjeVlak = uvecanjeVlak;
    }

    public double dohvatiCijenaNormalni() { return cijenaNormalni; }
    public double dohvatiCijenaUbrzani() { return cijenaUbrzani; }
    public double dohvatiCijenaBrzi() { return cijenaBrzi; }
    public double dohvatiPopustSuN() { return popustSuN; }
    public double dohvatiPopustWebMob() { return popustWebMob; }
    public double dohvatiUvecanjeVlak() { return uvecanjeVlak; }
}