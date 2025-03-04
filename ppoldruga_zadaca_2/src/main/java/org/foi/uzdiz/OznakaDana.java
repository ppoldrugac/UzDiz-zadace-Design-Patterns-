package org.foi.uzdiz;

import java.util.List;

public class OznakaDana {
    private String oznaka; 
    private List<String> daniVožnje; 

    public OznakaDana(String oznaka, List<String> daniVožnje) {
        this.oznaka = oznaka;
        this.daniVožnje = daniVožnje;
    }

    public String getOznaka() {
        return oznaka;
    }

    public List<String> getDaniVožnje() {
        return daniVožnje;
    }
    
    public String formatirajDane() {
        if (daniVožnje == null || daniVožnje.isEmpty()) {
            return "Po,U,Sr,Č,Pe,Su,N"; 
        }
        return String.join(",", daniVožnje);
    }

    @Override
    public String toString() {
        return "OznakaDana{" +
                "oznaka='" + oznaka + '\'' +
                ", daniVožnje=" + daniVožnje +
                '}';
    }
}
