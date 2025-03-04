package org.foi.uzdiz.decorator;

public class OsnovnoVrijeme implements Vrijeme {
    private final int minute;

    public OsnovnoVrijeme(int minute) {
        this.minute = minute;
    }

    @Override
    public int dohvatiVrijeme() {
        return minute;
    }

    @Override
    public String formatirajVrijeme() {
        int sati = minute / 60;
        int min = minute % 60;
        return String.format("%02d:%02d", sati, min);
    }

    public static int parsirajVrijeme(String vrijeme) {
        String[] dijelovi = vrijeme.split(":");
        int sati = Integer.parseInt(dijelovi[0]);
        int minute = Integer.parseInt(dijelovi[1]);
        return sati * 60 + minute;
    }
}
