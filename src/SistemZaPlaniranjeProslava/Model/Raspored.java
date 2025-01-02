package SistemZaPlaniranjeProslava.Model;

import java.util.ArrayList;
import java.util.Arrays;

public class Raspored {
    private Sto sto;
    private Proslava proslava;
    private ArrayList<String> gosti = new ArrayList<>();

    public Raspored(Sto sto, Proslava proslava, String gosti) {
        this.sto = sto;
        this.proslava = proslava;
        if (!gosti.isEmpty())
            this.gosti.addAll(Arrays.stream(gosti.split(",")).toList());
    }

    public Raspored(Sto sto, Proslava proslava, ArrayList<String> gosti) {
        this.sto = sto;
        this.proslava = proslava;
        this.gosti.addAll(gosti);
    }

    public Sto getSto() {
        return sto;
    }

    public void setSto(Sto sto) {
        this.sto = sto;
    }

    public Proslava getProslava() {
        return proslava;
    }

    public void setProslava(Proslava proslava) {
        this.proslava = proslava;
    }

    public ArrayList<String> getGosti() {
        return gosti;
    }

    @Override
    public String toString() {
        return "Raspored{" +
                "sto=" + sto +
                ", proslava=" + proslava +
                ", gosti='" + gosti + '\'' +
                '}';
    }
}