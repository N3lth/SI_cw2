package pl.si.cw2;


import com.sun.org.apache.regexp.internal.RE;
import org.paukov.combinatorics3.Generator;
import sun.security.krb5.internal.crypto.Des;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
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

        String[][] systemZPliku = readFile("SystemDecyzyjny.txt",8, 7);

        String[][] sys = systemLEMPDF;     // wybor systemu





        //      ALGORYTM 3

        List<Integer> listaAtrybutow = new ArrayList<>();
//        listaAtrybutow.add(0);
//        listaAtrybutow.add(1);
//        listaAtrybutow.add(2);
//        listaAtrybutow.add(3);

                String[][] testKoncept  = {
                {"1","2","1","1","1"}
        };

        Deskryptor d1 = najczestszyDeskryptorZListyAtrybutow(testKoncept, listaAtrybutow);
        System.out.println("a" + (d1.nrAtrybutu+1) + " = " + d1.wartosc + ", czestosc: " + d1.czestosc);

        List<String> decyzjeKonceptu = new ArrayList<>();
        decyzjeKonceptu.add("1");
        decyzjeKonceptu.add("0");

//        String[][] testKoncept  = {
//                {"1","0","1","0","0"},
//                {"1","2","5","0","0"}
//        };
//        for(String decyzjaKon : decyzjeKonceptu) {
        String decyzjaKon = "2";
        for (Regula r : regulyWynikowe(sys, decyzjaKon)) {
                System.out.println(r.toString());
            }
//        }


//        Deskryptor d2 = najczestszyDeskryptorAtrybutu(zwrocKoncept(systemLEMYoutube,"1"), 2);
//        System.out.println("a" + (d2.nrAtrybutu+1) + " = " + d2.wartosc + ", czestosc: " + d2.czestosc);







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

        // mam liste wszystkich deskryptorow dla poszczegolnych atrybutow, musze teraz porownac sobie ich czestosci i wybrac ten, ktory ma najwiskza czestosc w kolejnosci 1..n

        // musze porownac czestosci atrybutow ale tych, ktorych nie ma na liscie
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



    public static List<Regula> regulyWynikowe(String[][] sysDec, String decyzjaDlaKonceptu){

        String[][] koncept = zwrocKoncept(sysDec, decyzjaDlaKonceptu);

        List<String[]> konceptLista = new ArrayList<>();
//        konceptLista.addAll(Arrays.stream(koncept).collect(Collectors.toList()));
        for(String[] obiekt : koncept){
            konceptLista.add(obiekt);
        }

        List<Regula> regulyWynikoweWszystkie = new ArrayList<>();

        List<Integer> listaAtrybutow = new ArrayList<>();
        for(int i=0; i<koncept[0].length-1; i++){
            listaAtrybutow.add(i);
        }


//        listaAtrybutow.add(0);
//        listaAtrybutow.add(1);
//        listaAtrybutow.add(2);
//        listaAtrybutow.add(3);

        // petla while dopoki koncept sie nie wyzeruje


        // ta metoda ma mi znalezc z lisyt podanych deskryptorow obiekty, ktore maja takie deskryptory z dana decyzja


        // dajemy foreach zeby przepisac wszystkie deskryptory do regule, potem sprawdzamy czy obiekt spelnia regule i dostaniemy liste obiektow ktore ja spelniaja


        while(konceptLista.size() > 0) {

            Boolean flaga = true;

            // nie utworzyl na nowo listy atrybutow
            List<Integer> robioneAtrybuty = new ArrayList<>();

            Regula rWynikowa = new Regula();
            rWynikowa.decyzja = decyzjaDlaKonceptu;//   koncept[0][koncept.length -1]

            List<String[]> obiektySpelniajaceRegule = new ArrayList<>(konceptLista);

            while(flaga) {

                //   szukamy najczestszego deskryptora z konceptu (koncept moze sie zmniejszyc bo beda z niego usuwane obiekty)

                // tutaj dostaje liste obiektow, ale nie moze tutaj dostac obiektow ktore nie spelniaja poprzedniej reguly
                // tu sie wywala, rozmiar obiektyspelniajaceregule sie nie zgadza
                String[][] konceptDoNajczestszyDeskryptorZListyAtrybutow = new String[obiektySpelniajaceRegule.size()][];
//                int itr = 0;
                for (int i = 0; i < obiektySpelniajaceRegule.size(); i++) {
                    //if(rWynikowa.czyObiektSpelniaRegule(konceptLista.get(i)))
                    konceptDoNajczestszyDeskryptorZListyAtrybutow[i] = obiektySpelniajaceRegule.get(i);
//                    itr++;
                }
                Deskryptor d = najczestszyDeskryptorZListyAtrybutow(konceptDoNajczestszyDeskryptorZListyAtrybutow, robioneAtrybuty);


                //   dodajemy najczestszyDeskryptorZListyAtrybutow do reguly
                rWynikowa.deskryptor.put(d.nrAtrybutu, d.wartosc);


                // sprawdzamy czy utworzona regula jest sprzeczna
                if (!rWynikowa.czyNieSprzeczna(sysDec)) {

                    //  dodaje do listy zrobionych atrybutow numer atrybutu z ktorego zostala utworzona aktualna regula
                    robioneAtrybuty.add(d.nrAtrybutu);

                    // zrobiona kopia listy, java nie pozwala na modyfikacje aktualnego stosu (listy) - nie sprawdza elementow usunietych
                    List<String[]> obiektySpelniajaceReguleKopia = new ArrayList<>(obiektySpelniajaceRegule);

                    for(String[] obiekt : obiektySpelniajaceReguleKopia){
                        if(!rWynikowa.czyObiektSpelniaRegule(obiekt)){
                            obiektySpelniajaceRegule.remove(obiekt);
//                            obiektySpelniajaceReguleKopia.add(obiekt);
                        }
                    }

//                    obiektySpelniajaceRegule.removeAll(obiektySpelniajaceReguleKopia);
//                    obiektySpelniajaceRegule = obiektySpelniajaceReguleKopia;

                    // zawezamy zbior przerabianych obiektow (konceptLista) usuwajac te obiekty, ktore nie spelniaja reguly (maja zostac tylko obiekty ktore spelniaja regule)
                    if (robioneAtrybuty.size() == koncept[0].length - 1) {
//                        konceptLista = rWynikowa.obiektySpelniajaceRegule(koncept);

                        // z rozwazanego konceptu wykreslamy obiekty spelniajace regule
                        //List<String[]> obiektySpelniajaceRegule = rWynikowa.obiektySpelniajaceRegule(koncept);
                        List<String[]> konceptListaKopia = new ArrayList<>(konceptLista);
                        for (String[] obiekt : konceptListaKopia) {
                            if(rWynikowa.czyObiektSpelniaRegule(obiekt)) {
                                konceptLista.remove(obiekt);
                            }
                        }

                        rWynikowa.support = rWynikowa.obiektySpelniajaceRegule(sysDec).size();

                        regulyWynikoweWszystkie.add(rWynikowa);

                        flaga = false;
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
//                    konceptLista = new ArrayList<>(konceptListaPom);

//                    rWynikowa.support = obiektySpelniajaceRegule.size();
                    rWynikowa.support = rWynikowa.obiektySpelniajaceRegule(sysDec).size();
                    regulyWynikoweWszystkie.add(rWynikowa);
                    flaga = false;
                }

            }












            /*


            // trzeba uwzglednic wyeliminowane obiekty po utworeniu reguly

            // sprawdzenie czy obiekt zawiera sie na konceptlista

            List<Integer> atrybutydoNajczestszegoDeskryptora = new ArrayList<>();
            List<Integer> atrybutyWykluczone = new ArrayList<>();
            int atrybutWykluczony = 0;
            atrybutydoNajczestszegoDeskryptora.addAll(listaAtrybutow);

            for (Integer nrAtrybutu : listaAtrybutow) {
                if(nrAtrybutu >= atrybutWykluczony && atrybutWykluczony != koncept[0].length-2) {
                    Regula rTymczasowa = new Regula();
                    rTymczasowa.decyzja = koncept[0][koncept.length + 1];

                    // moment kiedy musimy wywalic z listy obiekty ktore spelniaja regule


                    // konwertuje liste konceptu na tablice - do poprawki zeby metoda najczestszyDeskryptorAtrybutu przyjmowala liste

                    // przy obiekcie 2 dla konceptu z decyzja "1" przy atrybucie dostaje obiekt o indeksie 1 (ob 2) a powinien dostac indeks obiektu 0 bo rozmiar tablicy koncept z listy jest 1 (tylko indeks 0 ma a powinna miec indeks 1)
                    List<Integer> obiektySpelniajaceReg = rWynikowa.obiektySpelniajaceRegule(koncept);
                    System.out.println("Przed: "+obiektySpelniajaceReg);
                    int iteratorOdZera = 0;
                    int korekcja = rWynikowa.obiektySpelniajaceRegule(koncept).get(0) - iteratorOdZera;
                    if(obiektySpelniajaceReg.size() > 0) {
                        for (Integer obiektSpelniajacyRegule : obiektySpelniajaceReg) {
                            if (obiektSpelniajacyRegule > iteratorOdZera) {
                                // robie swapa zeby numer obiektu byl iterowany od zera, poprawic zeby szlo w odpowiedniej kolejnosci bo sie wysypie dla innej kolejnosci obiektow z niewlasciwymi indeksami
//                                obiektySpelniajaceReg.remove(obiektSpelniajacyRegule);
//                                obiektySpelniajaceReg.add(iteratorOdZera);
//                                Collections.rotate(obiektySpelniajaceReg, -korekcja);
                                obiektySpelniajaceReg.set(iteratorOdZera, obiektySpelniajaceReg.get(iteratorOdZera)-korekcja);
                            }
                            iteratorOdZera++;
                        }
                    }
                    System.out.println("Po: "+obiektySpelniajaceReg);
                    // sprawdzenie rozmiaryw list, jesli wielkosc obiektySpelniajaceReg < konceptLista to ustaw wielkosc konceptZListy na obiektyspelniajaceRegule.size()
                    int konceptZListySize = 0;
                    if (obiektySpelniajaceReg.size() < konceptLista.size()) {
                        konceptZListySize = obiektySpelniajaceReg.size();
                    } else {
                        konceptZListySize = konceptLista.size();
                    }
//                    if(konceptZListySize == 1) konceptZListySize++;
                    //   sprawdzic czemu dostaje konceptZListy jako null przy obiekcie 2 atrybut 3(4) dla caleko konceptu z decjzja 1

                    // tutaj lepiej oerowac na liscie a potem ja przekowertowac na tablice, bez kombinacji w tablicy tylko obliczenia robic na liscie
                    String[][] konceptZListy = new String[konceptZListySize][];
                    int iterator = 0;
                    for (int obIdx = 0; obIdx < konceptLista.size(); obIdx++) {
                        // obiekty spelniajace regule ma wartosc 1 zamiast 0
                        if (obiektySpelniajaceReg.contains(obIdx)) {
                            konceptZListy[iterator] = konceptLista.get(obIdx);    // robi sie dziura, trzeba wypelniac tablice po kolei a nie numerami indeksow
                            iterator++;
                        }
                    }


                    //  dorobic sprawdzenie ktore obiekty spelniaja caly deskryptor i dopiero wtedy dla tych obiektow sprawdzac najczestszy


                    // tutaj sprawdzic najczestszy deskryptor ale Z LISTY ATRYBUTOW

                    // sprawdzic czemu dostaje konceptZListy jako null przy obiekcie 2 atrybut 3(4) dla caleko konceptu z decjzja 1
                    Deskryptor d = najczestszyDeskryptorZListyAtrybutow(konceptZListy, atrybutydoNajczestszegoDeskryptora);
                    rTymczasowa.deskryptor.put(d.nrAtrybutu, d.wartosc);
                    atrybutWykluczony = d.nrAtrybutu;


                    //rWynikowa.deskryptor.put(d.nrAtrybutu, d.wartosc);

                    // jak regula niesprzeczn to przerywam algorytm i zapisuje wynikowa regule

                    // jak mam utworzona regule to sprawdzamjej obiekty i wywalam z konceptu     to jest po petli while

                    if (!rWynikowa.czyNieSprzeczna(sysDec)) {     // jesli regula jest sprzeczna to przerywam petle while
                        rWynikowa.deskryptor.put(d.nrAtrybutu, d.wartosc);
                    }

//                    listaAtrybutow.remove(atrybutWykluczony);

                    // trzeba sprawdzic ktory numer atrybutu jest wywalany, podac mu numer d.nratrybutu - 1 i wywalic wszystkie ktore sa mniejsze od jego wartosci

                    // inaczej: trzeba wywalic numery atrybutow z listy ktore sa mniejsze od wartosci d.nratrybutu

//                for(int nrAtrybutuDowywalenia : listaAtrybutow){
//                    if(d.nrAtrybutu > nrAtrybutuDowywalenia) {
//                        atrybutydoNajczestszegoDeskryptora.remove(nrAtrybutuDowywalenia);
//                    }
//                }

                    atrybutydoNajczestszegoDeskryptora.remove(nrAtrybutu);

//                atrybutyWykluczone.add(d.nrAtrybutu);
                }
            }






            // w tym momencie dostaje regule ze wszystkimi deskryptorami z listy atrybutow

            // teraz musze pobrac obiekty ( [] ) ktore spelniaja ta regule

            // robie petle dla wszystkich numerow obiektow z konceptu i uzywam funkcji czyObiektSpelniaRegule i te obiekty ktore spelniaja dodaje je do listy

            List<Integer> obiektySpelniajaceRegule = new ArrayList<>();      /////      mzna zastapic metoda obiektySupportu

            String[][] konceptZListy2 = new String[konceptLista.size()][];   // tez do poprawki, ale upelnic sie
            for (int obIdx = 0; obIdx < konceptLista.size(); obIdx++) {
                konceptZListy2[obIdx] = konceptLista.get(obIdx);
            }


            int nrObiektu = 0;
            for (String[] obiekt : konceptZListy2) {   // tutaj musze podac obiekty tylko z konceptu
                if (rWynikowa.czyObiektSpelniaRegule(obiekt)) {
                    obiektySpelniajaceRegule.add(nrObiektu);
                    konceptLista.remove(obiekt);   // wywala index ot of bounds, poprawic
                }
                nrObiektu++;
            }


            // teraz jako wynik chcialbym miec regule i obiekty ktore ja spelniaja        albo       regule z policzonym supportem

            rWynikowa.support = obiektySpelniajaceRegule.size();

            regulyWynikoweWszystkie.add(rWynikowa);

        }


        */
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




    public static List<int[]> kombinuj(List<Integer> obiekt, int rzad){

        List<List<Integer>> kombList = Generator.combination(obiekt).simple(rzad).stream().collect(Collectors.toList());     // deklaracja listy do przechowania wynikow z dodatkowej funkcji generujacej kombinacje,  funkcja generowania kombinacji bez powtorzen z dodatkowej biblioteki wrzucajaca wynik do listy

        List<int[]> outList = new ArrayList<>();    // deklaracja listy (tablic) wynikowej

        for(List<Integer> kombItem : kombList){         // przelot dla kazdego elementu listy kombinacji
            int[] kombArr = new int[rzad];      // deklaracja tablicy do przechowywania kombinacji
            for(int j=0;j<kombItem.size();j++){
                kombArr[j] = kombItem.get(j);           // przepisywanie kombinacji z listy do tablicy
            }
            outList.add(kombArr);                       // dodawanie tablicy do wynikowej listy tablic
        }
        return outList;
    }



    public static int[] tworzKomorke(String[] ob1, String[] ob2){
        List<Integer> komorka = new ArrayList<>();
        if(ob1[ob1.length-1].equals(ob2[ob2.length-1])){         //   sprawdzam, czy decyzje sa identyczne
            return komorka.stream().mapToInt(i -> i).toArray();
        }

        for(int i=0;i<ob1.length;i++){          //    jak decyzje sa rozne
            if(ob1[i] == ob2[i]){
                komorka.add(i);
            }
        }

        return komorka.stream().mapToInt(i -> i).toArray();
    }



    public static int[][][] tworzMacierzNieodroznialnosci(String[][] system){
        int[][][] wynik = new int[system.length][][];
        for(int i=0; i<system.length; i++){
            wynik[i] = new int[system.length][];
            for(int j = 0; j<system.length; j++){
                wynik[i][j] = tworzKomorke(system[i], system[j]);
            }
        }
        return wynik;
    }



    public static Boolean czyKombinacjaZawieraSieWKomorce(int[] kombinacja, int[] komorka){
        List<Integer> komorkaList = new ArrayList<>();
        for (int index = 0; index < komorka.length; index++) komorkaList.add(komorka[index]);        //  konwercja na liste bo java nie zawiera wbudowanej metody contains dla tablicy

        for(int elementKombinacji : kombinacja){
            if(!komorkaList.contains(elementKombinacji)){
                return false;
            }
        }
        return true;
    }



    public static Boolean czyKombinacjaZawieraSieWWierszu(int[] kombinacja, int[][] wiersz){
        for(int komorka[] : wiersz){
            if(czyKombinacjaZawieraSieWKomorce(kombinacja, komorka)){
                return true;
            }
        }
        return false;
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
