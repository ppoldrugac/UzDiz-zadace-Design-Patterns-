package org.foi.uzdiz.observer;

public interface Subject {
	
    void dodajPromatraca(Observer observer);
    
    void ukloniPromatraca(Observer observer);
    
    void obavijestiPromatrace(String stanica, String vlak, String message);
}
