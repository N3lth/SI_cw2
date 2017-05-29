package pl.si.cw2;

import javafx.util.Pair;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


public class Main {


    public static void main(String[] args) throws java.io.FileNotFoundException, java.lang.NoSuchMethodError{

        Integer[][] systemTst = {
                {2, 4, 2, 1, 4},
                {1, 2, 1, 1, 2},
                {9, 7, 10, 7, 4},
                {4, 4, 10, 10, 2}
        };

        Integer[][] systemTrn = {
                {1, 3, 1, 1, 2},
                {10, 3, 2, 1, 2},
                {2, 3, 1, 1, 2},
                {10, 9, 7, 1, 4},
                {3, 5, 2, 2, 4},
                {2, 3, 1, 1, 4}
        };


        Integer[][] Tst2 = {
                {6, 9, 5, 8, 4},
                {4, 4, 4, 8, 2},
                {10, 5, 5, 10, 1},
                {4, 1, 4, 1, 1}
        };

        Integer[][] Trn2 = {
                {2, 3, 10, 5, 2},
                {8, 9, 4, 1, 2},
                {3, 8, 10, 7, 4},
                {3, 7, 5, 4, 3},
                {10, 8, 4, 7, 1},
                {1, 4, 2, 4, 2}
        };


        Integer[][] Tst3 = {
                {3, 5, 10, 9, 3},
                {4, 3, 2, 3, 4},
                {5, 9, 2, 8, 1},
                {2, 1, 8, 8, 4}
        };

        Integer[][] Trn3 = {
                {10, 9, 5, 8, 7},
                {7, 6, 7, 1, 7},
                {5, 1, 9, 10, 4},
                {3, 4, 8, 8, 1},
                {5, 7, 5, 10, 4},
                {6, 7, 1, 5, 2}
        };



        String[][] systemZPliku = readFile("SystemDecyzyjny.txt",8, 7);



        //      KNN

        macierzPredykcji(systemTst, systemTrn, "euklidesa", 2);



    }

    public static void macierzPredykcji(Integer[][] sysTst, Integer[][] sysTrn, String metryka, Integer k){

        List<Integer> listaDecyzjiTST = new ArrayList<>();
        List<Integer> listaDecyzjiKNN = new ArrayList<>();

        Arrays.stream(sysTst).forEach(obiektTst -> listaDecyzjiKNN.add(klasyfikacja(obiektTst, sysTrn, k, metryka)));
        Arrays.stream(sysTst).forEach(obiektTst -> listaDecyzjiTST.add(obiektTst[obiektTst.length-1]));

        List<Integer> klasyDecyzyjne = unikalneDecyzje(sysTst);




        //   UTWORZENIE MAPY Z LISTĄ OBIEKTÓW DLA KONKRETNEJ KLASY DECYZYJNEJ

        Map<Integer, List<Integer>> klasaZNumeramiObiektow = new HashMap<>();

        for(Integer klasaDec : klasyDecyzyjne) {
            List<Integer> numeryObiektowDlaKlasyDecyzyjnej = new ArrayList<>();
            for (int i = 0; i < sysTst.length; i++) {
                Integer[] obiektTst = sysTst[i];
                if (obiektTst[obiektTst.length - 1].equals(klasaDec)) {
                    numeryObiektowDlaKlasyDecyzyjnej.add(i);
                }
            }
            klasaZNumeramiObiektow.put(klasaDec, numeryObiektowDlaKlasyDecyzyjnej);
        }




        Map<Integer, Map<Integer, Integer>> wynik = new HashMap<>();

        for(Map.Entry<Integer, List<Integer>> obiektyDlaKlasy : klasaZNumeramiObiektow.entrySet()) {
            Map<Integer, Integer> iloscWystapienWKlasie = new HashMap<>();
//              klasa  wystapienia

            for (Integer klasa : klasyDecyzyjne) {
                int licznikWystapien = 0;
                for (Integer nrObiektu : obiektyDlaKlasy.getValue()) {
                    if (listaDecyzjiKNN.get(nrObiektu).equals(klasa)) {
                        licznikWystapien++;
                    }
                }
                iloscWystapienWKlasie.put(klasa, licznikWystapien);
            }
            wynik.put(obiektyDlaKlasy.getKey(), iloscWystapienWKlasie);
        }




        // UTWORZENIE MAPY Z LICZBĄ OBIEKTOW ZE ZGODNYMI DECYZJANI W DANEJ KLASIE

        Map<Integer, Integer> liczbaObiektowKlasy = new HashMap<>();
        for(Integer klasa : klasyDecyzyjne){
            int iloscObiektow = 0;
            for(Map.Entry<Integer, List<Integer>> obiektyDlaKlasy : klasaZNumeramiObiektow.entrySet()){
                if(obiektyDlaKlasy.getKey().equals(klasa)) {
                    iloscObiektow = obiektyDlaKlasy.getValue().size();
                }
            }
            liczbaObiektowKlasy.put(klasa, iloscObiektow);
        }


        Map<Integer, Integer> obiektySklasyfikowane = new HashMap<>();
        for(Integer klasa : klasyDecyzyjne) {
            int liczbaObiektowSklasyfikowanych = 0;
            for(Integer nrObiektu : klasaZNumeramiObiektow.get(klasa)){
                if(listaDecyzjiKNN.get(nrObiektu) >= 0){
                    liczbaObiektowSklasyfikowanych++;
                }
            }
            obiektySklasyfikowane.put(klasa, liczbaObiektowSklasyfikowanych);
        }


        Map<Integer, Integer> obiektyPoprawnieSklasyfikowane = new HashMap<>();
        for(Integer klasa : klasyDecyzyjne) {
            int liczbaObiektowPoprawnieSklasyfikowanych = 0;
            for(Integer nrObiektu : klasaZNumeramiObiektow.get(klasa)){
                if(listaDecyzjiKNN.get(nrObiektu).equals(listaDecyzjiTST.get(nrObiektu))){
                    liczbaObiektowPoprawnieSklasyfikowanych++;
                }
            }
            obiektyPoprawnieSklasyfikowane.put(klasa, liczbaObiektowPoprawnieSklasyfikowanych);
        }



        //  PRZEROBIENIE MAPY WYNIKU NA TABLICE TPR

        Integer[][] wynikArr = new Integer[wynik.size()][];

        int licznikKlas = 0;
        for(Map.Entry<Integer, Map<Integer, Integer>> jednaKlasaWyniku : wynik.entrySet()){
            Integer[] wierszArr = new Integer[wynik.size()];
            int licznikWierszy = 0;
            for(Map.Entry<Integer, Integer> kolumnyDlaKlasy : jednaKlasaWyniku.getValue().entrySet()){
                wierszArr[licznikWierszy] = kolumnyDlaKlasy.getValue();
                licznikWierszy++;
            }
            wynikArr[licznikKlas] = wierszArr;
            licznikKlas++;
        }


        Map<Integer, Integer[]> wynikTpr = new HashMap<>();

        int licznikDlaArr = 0;
        for(Integer klasa : klasyDecyzyjne){
            wynikTpr.put(klasa, colToRowInt(wynikArr, licznikDlaArr));
            licznikDlaArr++;
        }





        Map<Integer, Double> accuracyC = new HashMap<>();
        for(Integer klasa : klasyDecyzyjne){
            double acc = (double) obiektyPoprawnieSklasyfikowane.get(klasa)/obiektySklasyfikowane.get(klasa);
            accuracyC.put(klasa, acc);
        }


        Map<Integer, Double> coverageC = new HashMap<>();
        for(Integer klasa : klasyDecyzyjne){
            double cov = (double) obiektySklasyfikowane.get(klasa)/liczbaObiektowKlasy.get(klasa);
            coverageC.put(klasa, cov);
        }


        Map<Integer, Double> TprC = new HashMap<>();
        for(Integer klasa : klasyDecyzyjne){
            double tpr = (double) obiektyPoprawnieSklasyfikowane.get(klasa) / Arrays.stream(wynikTpr.get(klasa)).mapToInt(e -> e).sum();
            TprC.put(klasa, tpr);
        }






        //  OBIECZENIE ACCURACY I COVERAGE DLA SYSTEMU TST

        int iloscObiektowTST = 0;
        for(Integer klasa : klasyDecyzyjne){
            for(Map.Entry<Integer, List<Integer>> obiektyDlaKlasy : klasaZNumeramiObiektow.entrySet()){
                if(obiektyDlaKlasy.getKey().equals(klasa)) {
                    iloscObiektowTST += obiektyDlaKlasy.getValue().size();
                }
            }
        }

        int liczbaObiektowChwyconychTST = 0;
        for(Integer klasa : klasyDecyzyjne) {
            for(Integer nrObiektu : klasaZNumeramiObiektow.get(klasa)){
                if(listaDecyzjiKNN.get(nrObiektu) >= 0){
                    liczbaObiektowChwyconychTST++;
                }
            }
        }


        int liczbaObiektowPoprawnieSklasyfikowanychTST = 0;
        for(Integer klasa : klasyDecyzyjne) {
            for(Integer nrObiektu : klasaZNumeramiObiektow.get(klasa)){
                if(listaDecyzjiKNN.get(nrObiektu).equals(listaDecyzjiTST.get(nrObiektu))){
                    liczbaObiektowPoprawnieSklasyfikowanychTST++;
                }
            }
        }


        wyswietlMacierzPredykcji(klasyDecyzyjne, wynik, liczbaObiektowKlasy, accuracyC, coverageC, TprC);

        System.out.println();
        System.out.println("Accuracy TST: "+(double) liczbaObiektowPoprawnieSklasyfikowanychTST/liczbaObiektowChwyconychTST);
        System.out.println("Coverage TST: "+(double) liczbaObiektowChwyconychTST/iloscObiektowTST);


    }



    public static void wyswietlMacierzPredykcji(List<Integer> klasyDecyzyjne, Map<Integer, Map<Integer, Integer>> wynik, Map<Integer, Integer> liczbaObiektowKlasy, Map<Integer, Double> accuracyC, Map<Integer, Double> coverageC, Map<Integer, Double> TprC){

        //  TWORZE MAPE DLA KONKRETNEJ KLASY Z LISTA KOLEJNYCH WARTOSCI ODPOWIADAJACYCH KOLUMNOM W MACIERZY

        Map<Integer, List<Double>> macierzPredykcji = new HashMap<>();
        for(Integer klasa : klasyDecyzyjne){
            List<Double> wierszMacierzy = new ArrayList<>();
            for(Map.Entry<Integer, Map<Integer, Integer>> wynikDlaKlasy : wynik.entrySet()){
                if(wynikDlaKlasy.getKey().equals(klasa)) {
                    for (Map.Entry<Integer, Integer> iloscWystapienDlaKlasy : wynikDlaKlasy.getValue().entrySet()) {
                        wierszMacierzy.add((double) iloscWystapienDlaKlasy.getValue());
                    }
                }
            }
            wierszMacierzy.add((double) liczbaObiektowKlasy.get(klasa));
            wierszMacierzy.add(accuracyC.get(klasa));
            wierszMacierzy.add(coverageC.get(klasa));
            macierzPredykcji.put(klasa, wierszMacierzy);
        }




        //   WYSWIETLANIE MACIERZY

        System.out.print("      ");
        for(Integer klasa : klasyDecyzyjne){
            if(klasyDecyzyjne.get(klasyDecyzyjne.size()-1).equals(klasa)) System.out.print(klasa+"   ");
            else System.out.print(klasa+"    ");

        }
        System.out.println("n.o  acc  cov");
        for(Integer klasa : klasyDecyzyjne) {
            System.out.print(klasa+"    ");
            macierzPredykcji.get(klasa).forEach(e -> System.out.print(e+"  "));
            System.out.println();
        }
        System.out.print("TPR  ");
        for(Integer klasa : klasyDecyzyjne){
            if(!TprC.get(klasa).isNaN()) System.out.print(TprC.get(klasa)+"  ");
            else System.out.print("XXX  ");
        }
        System.out.println();

    }




    public static Integer klasyfikacja(Integer[] obiekt, Integer[][] sysTrn, Integer k, String metryka){

        Set<Integer> klasyDecyzyjne = new HashSet<>();

        //   PROBA WYWOLANIA METODY PO PODANIU JEJ NAZWY JAKO STRING - PROBLEM: INVOKE NIE PRZYJMUJE 2 PARAMETROW ARRAY

        for(Integer[] obiektTrn : sysTrn) {
            try {
                Object o = Metryki.class.getDeclaredMethod(metryka, Integer[].class, Integer[].class).invoke(null, new Object[]{obiekt, obiektTrn});
                Pair<Double, Integer> p = (Pair) o;
                klasyDecyzyjne.add(p.getValue());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                System.out.println(e);
            }
        }


//        Arrays.stream(sysTrn).forEach(obiektTrn -> klasyDecyzyjne.add(Metryki.euklidesa(obiekt, obiektTrn).getValue()));

        //  TWORZE MAPE Z KLASA DECYZYJNA I LISTA ODLEGLOSCI OD OBIEKTOW

        Map<Integer, List<Double>> odleglosciOdObiektu = new HashMap<>();
        // ZESTAW DANYCH - KLASA DECYZYJNA, ODLEGLOSCI

        for(Integer klasa : klasyDecyzyjne){
            List<Double> odleglosci = new ArrayList<>();
            for(Integer[] obiektTrn : sysTrn){
                try {
                    Object o = Metryki.class.getDeclaredMethod(metryka, Integer[].class, Integer[].class).invoke(null, new Object[]{obiekt, obiektTrn});
                    Pair<Double, Integer> p = (Pair) o;
                    if(klasa.equals(p.getValue())){
                        odleglosci.add(p.getKey());
                    }
                }
                catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    System.out.println(e);
                }


            }
            odleglosciOdObiektu.put(klasa, odleglosci);
        }


        // LICZE MOCE DLA KONKRETNYCH KLAS DECYZYJNYCH

        Map<Integer, Double> moceDlaKlasDecyzyjnych = new HashMap<>();

        for(Integer klasaDec : odleglosciOdObiektu.keySet()) {
            List<Double> listaOdleglosciDlaKlasy = new ArrayList<>(odleglosciOdObiektu.get(klasaDec));
            Collections.sort(listaOdleglosciDlaKlasy);
            Double moc = 0.0;
            for (int i = 0; i < k; i++) {
                moc += listaOdleglosciDlaKlasy.get(i);
            }
            moceDlaKlasDecyzyjnych.put(klasaDec, moc);
        }

        //  SPRAWDZAM, CZY MOCE SA ROWNE

        if(new HashSet<>(moceDlaKlasDecyzyjnych.values()).size() == 1) return -1;   // KOD ZWROTKI DLA NIEKLASYFIKOWANEGO OBIEKTU


        //  WYBIERAM NAJMNIEJSZA MOC ZE WSZYSTKICH MOCY DLA WSZYSTKICH KLAS DECYZYJNYCH

        List<Double> listaMocy = new ArrayList<>(moceDlaKlasDecyzyjnych.values());
        Double najmniejszaMoc = Collections.min(listaMocy);
        for(Map.Entry<Integer, Double> mocDlaKlasy : moceDlaKlasDecyzyjnych.entrySet()){
            if(mocDlaKlasy.getValue().equals(najmniejszaMoc)){   // JESLI MOC Z LISTY MOCY JEST TAKA ZAMA JAK NAJMNIEJSZA MOC ZWRACAM JEJ KLASE DECYZYJNA
                return mocDlaKlasy.getKey();
            }
        }

        return -2;    // kod zwrotki dla innego bledu

    }


    public static List<Integer> unikalneDecyzje(Integer[][] sys){
        List<Integer> klasyDecyzyjne = new ArrayList<>();
        Arrays.stream(sys).forEach(obiekt -> klasyDecyzyjne.add(obiekt[sys.length]));
        return new ArrayList<>(new HashSet<>(klasyDecyzyjne));
    }






    //       METODY UNIWERSALNE

    public static String[][] readFile(String src, int rows, int cols) throws FileNotFoundException {
        File plik1 = new File(src);
        Scanner p1 = new Scanner(plik1);

        int lineCounter = 0;
        int colCounter = 0;
        int cnt = 0;
        String[][] line = new String[rows][cols];

        while (p1.hasNextLine()) {
            String l = p1.nextLine();
            for(colCounter = 0; colCounter < line[lineCounter].length; colCounter++){
                String[] col = l.split(" ");
                cnt = 0;
                while(cnt < col.length){
                    line[lineCounter][cnt] = col[cnt];
                    cnt++;
                }
            }
            lineCounter++;
        }
        p1.close();
        return line;
    }



    public static int rowsCountInt(Integer[][] fileTab){
        int count = 0;
        for(int i = 0; i<fileTab.length; i++){
            if(fileTab[i][0] != null){
                count = i;
            }
        }
        return count+1;
    }


    public static int colsCountInt(Integer[][] fileTab){
        int count = 0;
        for(int i = 0; i<fileTab[0].length; i++){
            if(fileTab[0][i] != null){
                count = i;
            }
        }
        return count+1;
    }


    public static Integer[] colToRowInt(Integer[][] fileTab, int col){
        int rows = rowsCountInt(fileTab);
        int cols = colsCountInt(fileTab);
        Integer[] resCol = new Integer[rows];

        for(int i = 0; i<rows; i++){
            for (int j = 0; j<cols; j++){
                resCol[i] = fileTab[i][col];
            }
        }
        return resCol;
    }
}
