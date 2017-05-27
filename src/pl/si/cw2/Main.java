package pl.si.cw2;

import javafx.collections.transformation.SortedList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class Main {


    public static void main(String[] args) throws java.io.FileNotFoundException{


        String[][] systemWyklad = {
                                {"1","2","T","1","0"},
                                {"2","3","N","1","1"},
                                {"3","4","T","1","1"},
                                {"3","1","T","1","0"},
                                {"1","1","T","2","0"}
        };

        String[][] systemPDF = {
                            {"1","1","1","1","3","1","1"},
                            {"1","1","1","1","3","2","1"},
                            {"1","1","1","3","2","1","0"},
                            {"1","1","1","3","3","2","1"},
                            {"1","1","2","1","2","1","0"},
                            {"1","1","2","1","2","2","1"},
                            {"1","1","2","2","3","1","0"},
                            {"1","1","2","2","4","1","1"}

        };

        String[][] systemLEMYoutube = {
                {"1","2","3","4","1"},
                {"1","2","1","1","1"},
                {"1","2","3","4","1"},
                {"1","2","3","5","0"},
                {"1","0","1","0","0"},
                {"1","2","5","0","0"}
        };

        String[][] systemLEMPDF = {
                {"2","6","1","2","3","1"},
                {"1","1","1","3","2","1"},
                {"2","1","1","2","3","1"},
                {"4","1","3","1","2","1"},
                {"3","5","2","1","3","2"},
                {"3","1","3","1","1","2"},
                {"1","1","1","3","1","2"}

        };
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

        String[][] systemZPliku = readFile("SystemDecyzyjny.txt",8, 7);

        String[][] sys = systemLEMPDF;     // wybor systemu




        //      KNN

//        System.out.println(Metryki.euklidesa(systemTst[0], systemTrn[1]).getKey()+" "+Metryki.euklidesa(systemTst[0], systemTrn[1]).getValue());

        for(Integer[] obiektTst : systemTst) {
            System.out.println(klasyfikacja(obiektTst, systemTrn, 2));
        }

    }


    public static String klasyfikacja(Integer[] obiekt, Integer[][] sysTrn, Integer k){

        Map<Integer, List<Double>> odleglosciOdObiektu = new HashMap<Integer, List<Double>>();
        // zestaw danych - klasa decyzyjna, odleglosc

        Set<Integer> klasyDecyzyjne = new HashSet<>();

//        Map<Integer, Double> odleglosci = new HashMap<Integer, Double>();



        for(Integer[] obiektTrn : sysTrn){
            klasyDecyzyjne.add(Metryki.euklidesa(obiekt,obiektTrn).getValue());
        }


        for(Integer klasa : klasyDecyzyjne){
            List<Double> odleglosci = new ArrayList<Double>();
            for(Integer[] obiektTrn : sysTrn){
                if(klasa.equals(Metryki.euklidesa(obiekt,obiektTrn).getValue())){
                    odleglosci.add(Metryki.euklidesa(obiekt,obiektTrn).getKey());
                }
            }
            odleglosciOdObiektu.put(klasa, odleglosci);
        }

//        System.out.println(odleglosciOdObiektu);


        // licze odleglosci miedzy obiektami i mocy

        Map<Integer, Double> moceDlaKlasDecyzyjnych = new HashMap<>();

        for(Integer klasaDec : odleglosciOdObiektu.keySet()) {
            List<Double> listaOdleglosciDlaKlasy = new ArrayList<>(odleglosciOdObiektu.get(klasaDec));
            Collections.sort(listaOdleglosciDlaKlasy);
            Double moc = 0.0;
            for (int i = 0; i < k; i++) {
                moc += listaOdleglosciDlaKlasy.get(i);
            }
            moceDlaKlasDecyzyjnych.put(klasaDec, moc);
//            System.out.println("Klasa decyzyjna: "+klasaDec+", Moc: "+moc);
        }



        // teraz musze porownac wartosci i wybrac najmniejsza odleglosc (moc) z calego zestawu oraz przypisac dla instancji ObiektKNN nr obiektu i decyzje
        // moge wywalic sama klase dec, a numer obiektu mam wyzej, przed uzyciem metody klasyfikacja


        // tutaj musze dodac tez sprawdzenie czy moce rowne zeby wywalic blad

        if(new HashSet<Double>(moceDlaKlasDecyzyjnych.values()).size() == 1){
//            System.out.println("Wszystkie moce takie same");
            return "nieklasyfikowany";
        }

        Double najwiekszaMoc = Collections.max(moceDlaKlasDecyzyjnych.values());

//        System.out.println("\nWynik:");
        List<Double> listaMocy = new ArrayList<>(moceDlaKlasDecyzyjnych.values());
        Double najmniejszaMoc = Collections.min(listaMocy);
        for(Map.Entry<Integer, Double> mocDlaKlasy : moceDlaKlasDecyzyjnych.entrySet()){
            if(mocDlaKlasy.getValue().equals(najmniejszaMoc)){
//                System.out.println("Klasa decyzyjna: "+mocDlaKlasy.getKey()+", Moc: "+mocDlaKlasy.getValue());
                return mocDlaKlasy.getKey().toString();
            }
        }

        return "err";

    }



    public static List<Regula> regulyWynikowe(String[][] sysDec, String decyzjaDlaKonceptu){

        String[][] koncept = zwrocKoncept(sysDec, decyzjaDlaKonceptu);

        List<String[]> konceptLista = new ArrayList<>();
        for(String[] obiekt : koncept){
            konceptLista.add(obiekt);
        }

        List<Regula> regulyWynikoweWszystkie = new ArrayList<>();

        List<Integer> listaAtrybutow = new ArrayList<>();
        for(int i=0; i<koncept[0].length-1; i++){
            listaAtrybutow.add(i);
        }

        while(konceptLista.size() > 0){  // petla while dopoki koncept sie nie wyzeruje

            Boolean tworzenieReguly = true;

            List<Integer> pokryteAtrybuty = new ArrayList<>();

            Regula rWynikowa = new Regula();
            rWynikowa.decyzja = decyzjaDlaKonceptu;

            List<String[]> obiektySpelniajaceRegule = new ArrayList<>(konceptLista);

            while(tworzenieReguly) {

                //   szukamy najczestszego deskryptora z konceptu (koncept moze sie zmniejszyc bo beda z niego usuwane obiekty)

                // tutaj dostaje liste obiektow, ale nie moze tutaj dostac obiektow ktore nie spelniaja poprzedniej reguly
                // konwersja z listy na tablice, bo metoda najczestszyDeskryptorZListyAtrybutow dostaje na wejsciu tablice
                String[][] konceptDoNajczestszyDeskryptorZListyAtrybutow = new String[obiektySpelniajaceRegule.size()][];
                for (int i = 0; i < obiektySpelniajaceRegule.size(); i++) {
                    konceptDoNajczestszyDeskryptorZListyAtrybutow[i] = obiektySpelniajaceRegule.get(i);
                }
                //  tworzymy i dodajemy najczestszyDeskryptorZListyAtrybutow do reguly
                Deskryptor d = najczestszyDeskryptorZListyAtrybutow(konceptDoNajczestszyDeskryptorZListyAtrybutow, pokryteAtrybuty);
                rWynikowa.deskryptor.put(d.nrAtrybutu, d.wartosc);

                // sprawdzamy czy utworzona regula jest sprzeczna
                if (!rWynikowa.czyNieSprzeczna(sysDec)) {

                    //  dodaje do listy pokrytych atrybutow numer atrybutu z ktorego zostala utworzona aktualna regula
                    pokryteAtrybuty.add(d.nrAtrybutu);

                    // zawezamy zbior przerabianych obiektow (konceptLista) usuwajac te obiekty, ktore nie spelniaja reguly
                    // (maja zostac tylko obiekty ktore spelniaja regule)

                    // zrobiona kopia listy, java nie pozwala na modyfikacje aktualnego stosu (listy) - nie sprawdza elementow usunietych
                    List<String[]> obiektySpelniajaceReguleKopia = new ArrayList<>(obiektySpelniajaceRegule);
                    for(String[] obiekt : obiektySpelniajaceReguleKopia){
                        if(!rWynikowa.czyObiektSpelniaRegule(obiekt)){
                            obiektySpelniajaceRegule.remove(obiekt);
                        }
                    }

                    //  zabezpieczenie - jesli petla pokrytych atrybutow dojdzie do konca to wtedy konczymy tworzenie reguly
                    if (pokryteAtrybuty.size() == koncept[0].length - 1) {
                        List<String[]> konceptListaKopia = new ArrayList<>(konceptLista);
                        for (String[] obiekt : konceptListaKopia) {
                            if(rWynikowa.czyObiektSpelniaRegule(obiekt)) {
                                konceptLista.remove(obiekt);
                            }
                        }
                        rWynikowa.support = rWynikowa.obiektySpelniajaceRegule(sysDec).size();
                        regulyWynikoweWszystkie.add(rWynikowa);
                        tworzenieReguly = false;
                    }
                }

                else {
                    // z rozwazanego konceptu wykreslamy obiekty spelniajace regule
                    List<String[]> konceptListaPom = new ArrayList<>(konceptLista);
                    for (String[] obiekt : konceptListaPom) {
                        if(rWynikowa.czyObiektSpelniaRegule(obiekt)) {
                            konceptLista.remove(obiekt);
                        }
                    }
                    rWynikowa.support = rWynikowa.obiektySpelniajaceRegule(sysDec).size();
                    regulyWynikoweWszystkie.add(rWynikowa);
                    tworzenieReguly = false;
                }
            }
        }

        return regulyWynikoweWszystkie;

    }



    public static String[][] zwrocKoncept(String[][] systemDec, String decyzja){

        List<String[]> konceptList = new ArrayList<>();

        for(int i = 0; i<systemDec.length; i++){
                if(systemDec[i][systemDec[i].length-1].equals(decyzja)){    // sprawdzenie czy wartosc atrybutu decyzyjnego zgadza sie z podana decyzja
                    konceptList.add(systemDec[i]);
                }
        }

        String[][] koncept = new String[konceptList.size()][];
        for(int i=0; i<konceptList.size(); i++){
            koncept[i] = konceptList.get(i);
        }

        return koncept;
    }


    public static Deskryptor najczestszyDeskryptorAtrybutu(String[][] sysk, Integer nrAtrybutu){//}, Integer nrObiektu){   // Map<Integer, Deskryptor>

        String[][] kopiaSys = sysk;
        String[][] koncept = sysk;
        Map<Integer, Deskryptor> deskryptoryZObiektami = new HashMap<>();

        for(int nrObiektu = 0; nrObiektu < koncept.length; nrObiektu++) {
            int czestosc = 0;
            String wartoscAtrybutu = koncept[nrObiektu][nrAtrybutu];
            for (String[] kopiaSysObiekt : kopiaSys) {
                if (wartoscAtrybutu.equals(kopiaSysObiekt[nrAtrybutu])) {
                    czestosc++;
                }
            }
            Deskryptor d = new Deskryptor(nrAtrybutu, wartoscAtrybutu, czestosc);
            deskryptoryZObiektami.put(nrObiektu, d);
        }

        List<Integer> czestosci = new ArrayList<>();

        for(Map.Entry<Integer, Deskryptor> deskryptorObiektu : deskryptoryZObiektami.entrySet()){
            czestosci.add(deskryptorObiektu.getValue().czestosc);
        }

        int czestoscMax = Collections.max(czestosci);

        Deskryptor wynikowyDeskryptor = new Deskryptor();

        for(Map.Entry<Integer, Deskryptor> deskryptorObiektu : deskryptoryZObiektami.entrySet()){
            if(czestoscMax == deskryptorObiektu.getValue().czestosc){
                wynikowyDeskryptor = deskryptorObiektu.getValue();
                break;
            }
        }

        return wynikowyDeskryptor;
    }


    public static Deskryptor najczestszyDeskryptorZListyAtrybutow(String[][] sysk, List<Integer> listaNumerowAtrybutow){

        Deskryptor out = new Deskryptor();

        List<Integer> listaAtrybutowZSystemu = new ArrayList<>();
        for(int nrAtrybutu=0; nrAtrybutu<sysk[0].length-1; nrAtrybutu++){
            listaAtrybutowZSystemu.add(nrAtrybutu);
        }

        if(!listaNumerowAtrybutow.isEmpty()) {
            for (Integer nrAtrybutu : listaNumerowAtrybutow) {
                if (listaAtrybutowZSystemu.contains(nrAtrybutu)) {
                    listaAtrybutowZSystemu.remove((Object) nrAtrybutu);
                }
            }
        }

        List<Deskryptor> listaWszystkichDeskryptorow = new ArrayList<>();

        for(Integer nrAtrybutu : listaAtrybutowZSystemu){
            Deskryptor d = najczestszyDeskryptorAtrybutu(sysk, nrAtrybutu);
            listaWszystkichDeskryptorow.add(d);
        }

        // mam liste wszystkich deskryptorow dla poszczegolnych atrybutow, musze teraz porownac sobie ich czestosci i
        // wybrac ten, ktory ma najwiskza czestosc w kolejnosci 1..n

        int najwiekszaCzestosc = 0;
        for(Deskryptor desk : listaWszystkichDeskryptorow){
            if(desk.czestosc > najwiekszaCzestosc){
                najwiekszaCzestosc = desk.czestosc;
            }
        }

        for(Deskryptor desk : listaWszystkichDeskryptorow){
            if(desk.czestosc == najwiekszaCzestosc){
                out = desk;
                break;
            }
        }
        return out;
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


    public static int rowsCount(String[][] fileTab){
        int count = 0;
        for(int i = 0; i<fileTab.length; i++){
            if(fileTab[i][0] != null){
                count = i;
            }
        }
        return count+1;
    }


    public static int colsCount(String[][] fileTab){
        int count = 0;
        for(int i = 0; i<fileTab[0].length; i++){
            if(fileTab[0][i] != null){
                count = i;
            }
        }
        return count+1;
    }


    public static String[] uniqueValues(String[] tab){

        List<String> Unique = new ArrayList<String>();
        Unique.add(tab[0]);
        for(int i=1; i<tab.length; i++){
            if(!Unique.contains(tab[i])){
                Unique.add(tab[i]);
            }
        }
        return Unique.toArray(new String[0]);
    }


    public static String[] colToRow(String[][] fileTab, int col){
        int rows = rowsCount(fileTab);
        int cols = colsCount(fileTab);
        String[] resCol = new String[rows];

        for(int i = 0; i<rows; i++){
            for (int j = 0; j<cols; j++){
                resCol[i] = fileTab[i][col - 1];
            }
        }
        return resCol;
    }
}
