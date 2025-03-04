package org.foi.uzdiz.strategy;

import java.util.ArrayList;
import java.util.List;

public class KompozitnaStrategija implements StrategijaCijene {
    private List<StrategijaCijene> strategije = new ArrayList<>();

    public void dodajStrategiju(StrategijaCijene strategija) {
        strategije.add(strategija);
    }

    @Override
    public double prilagodiCijenu(double osnovnaCijena, String vrstaVlaka) {
        for (StrategijaCijene strategija : strategije) {
            osnovnaCijena = strategija.prilagodiCijenu(osnovnaCijena, vrstaVlaka);
        }
        return osnovnaCijena;
    }
    
    @Override
    public String opisPromjeneCijene() {
        StringBuilder sb = new StringBuilder();
        for (StrategijaCijene strategija : strategije) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(strategija.opisPromjeneCijene());
        }
        return sb.toString();
    }
    
    public List<String> dobaviPromjeneCijena() {
        List<String> promjeneCijena = new ArrayList<>();
        for (StrategijaCijene strategija : strategije) {
            String opis = strategija.opisPromjeneCijene();
            if (!opis.isEmpty()) {
                promjeneCijena.add(opis);
            }
        }
        return promjeneCijena;
    }
}
