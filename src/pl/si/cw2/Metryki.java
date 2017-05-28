package pl.si.cw2;

import javafx.util.Pair;

import java.util.*;

/**
 * Created by Michal Stankiewicz on 27.05.2017.
 * Programisci.eu
 */
public class Metryki {

    public static Pair<Double, Integer> manhattan(Integer[] tst, Integer[] trn){
        Double wynik = 0.0;
        for(int i=0; i<(tst.length)-1; i++){
            wynik+=Math.abs((tst[i]-trn[i]));
        }
        Pair<Double, Integer> out = new Pair<>(wynik,trn[trn.length-1]);
        return out;
    }

    public static Pair<Double, Integer> euklidesa(Integer[] tst, Integer[] trn){
        Double wynik = 0.0;
        // od atrybutu testowego odejmuje atrybut treningowego
        for(int i=0; i<(tst.length)-1; i++){
            wynik+=(Math.pow((tst[i]-trn[i]),2));
        }
        Pair<Double, Integer> out = new Pair<>(Math.sqrt(wynik),trn[trn.length-1]);
        return out;
    }

    public static Pair<Double, Integer> canberra(Integer[] tst, Integer[] trn){
        Double wynik = 0.0;
        for(int i=0; i<(tst.length)-1; i++){
            wynik+=Math.abs((double)(tst[i]-trn[i])/(tst[i]+trn[i]));
        }
        Pair<Double, Integer> out = new Pair<>(wynik,trn[trn.length-1]);
        return out;
    }

    public static Pair<Double, Integer> czebyszewa(Integer[] tst, Integer[] trn){
        List<Integer> listaWynikow = new ArrayList<>();
        for(int i=0; i<(tst.length)-1; i++){
            listaWynikow.add(Math.abs((tst[i]-trn[i])));
        }
        double wynik = Collections.max(listaWynikow);
        Pair<Double, Integer> out = new Pair<>(wynik,trn[trn.length-1]);
        return out;
    }

    public static Pair<Double, Integer> pearsona(Integer[] tst, Integer[] trn){
        Double sredniaX = 0.0;
        Double sredniaY = 0.0;
        for(int i=0; i<(tst.length)-1; i++){
            sredniaX+=tst[i];
            sredniaY+=trn[i];
        }
        sredniaX /= (tst.length)-1;
        sredniaY /= (tst.length)-1;


        Pair<Double, Integer> out = new Pair<>(0.0,trn[trn.length-1]);
        return out;
    }
}
