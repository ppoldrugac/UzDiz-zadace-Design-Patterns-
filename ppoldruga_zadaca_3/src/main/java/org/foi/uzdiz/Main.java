package org.foi.uzdiz;

import org.foi.uzdiz.facade.SustavFacade;

public class Main {

    public static void main(String[] args) {
        SustavFacade facade = new SustavFacade();

        if (!facade.inicijalizirajArgumente(args)) {
            System.out.println("Pogreška pri inicijalizaciji argumenata!");
            return;
        }

        if (!facade.ucitajPodatke()) {
            System.out.println("Pogreška pri učitavanju podataka!");
            return;
        }
        
        System.out.println("Učitavanje datoteka završeno.");
        
        facade.inicijalizirajKorisnickiRegistar();
        facade.inicijalizirajMediator();
        facade.inicijalizirajProdajaKarataOriginator();
        
        System.out.println("Program pokrenut.");
        
        System.out.println();

        facade.inicijalizirajLanacKomandi();
        facade.pokreniObraduKomandi();
   

    }
}
