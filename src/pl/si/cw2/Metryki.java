package pl.si.cw2;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michal Stankiewicz on 27.05.2017.
 * Programisci.eu
 */
public class Metryki {

    public static Pair<Double, Integer> euklidesa(Integer[] tst, Integer[] trn){
        Double wynik = 0.0;
        // od atrybutu testowego odejmuje atrybut treningowego
        for(int i=0; i<(tst.length)-1; i++){
            wynik+=(Math.pow((tst[i]-trn[i]),2));
        }
        Pair<Double, Integer> out = new Pair<>(Math.sqrt(wynik),trn[trn.length-1]);
        return out;
    }
}
