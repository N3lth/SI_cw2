package pl.si.cw2;

/**
 * Created by Michal Stankiewicz on 09.05.2017.
 * Programisci.eu
 */
public class Deskryptor {
    public int nrAtrybutu;
    public String wartosc;
    public int czestosc;

    public Deskryptor() {
    }

    public Deskryptor(int nrAtrybutu, String wartosc, int czestosc) {
        this.nrAtrybutu = nrAtrybutu;
        this.wartosc = wartosc;
        this.czestosc = czestosc;
    }

    public String toString(){
        String out = "";
        out+="a" + (nrAtrybutu+1) + " = " + wartosc + ", czestosc: " + czestosc;
        return out;
    }
}
