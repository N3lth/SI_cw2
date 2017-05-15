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

        String[][] systemYoutube = {
                {"1","2","3","4","1"},
                {"1","2","1","1","1"},
                {"1","2","3","4","1"},
                {"1","2","3","5","0"},
                {"1","0","1","0","0"},
                {"1","2","5","0","0"}
        };

        String[][] systemZPliku = readFile("SystemDecyzyjny.txt",8, 7);

        String[][] sys = systemYoutube;     // wybor systemu





        //      ALGORYTM 2

        System.out.print("\n\n");
        /*


        //     SPRAWDZENIE CZY DOBRZE TWORZY MACIERZ   --   OK


        int[][][] mn = tworzMacierzNieodroznialnosci(sys);
        for(int i=0; i<mn.length; i++){
            for(int j=0; j<mn[i].length; j++){
                System.out.println("ob"+(j+1)+"");
                if(mn[i][j].length != 0){
                    Arrays.stream(mn[i][j]).forEach(a -> System.out.print("a"+(a+1)+" "));
                }
                else System.out.print("    X");
                System.out.println("\n\n");
            }
            System.out.println("-----------------------\n");
        }

        */







//        List<Regula> regulyExhaustive = new ArrayList<>();
//        int[][][] mn = tworzMacierzNieodroznialnosci(sys);
//
//        Map<Integer, Integer> wyeliminowaneAtrybuty = new HashMap<>();
//
//
//        for(int rzad = 0; rzad < sys[0].length - 1; rzad++) {
//
//            for (int obiektNr = 0; obiektNr < mn.length; obiektNr++) {
//
//
//
//                List<Integer> listaObiektow = new ArrayList<>();    // tworzenie listy obiektow do kombinacji
//                for (int i = 0; i < sys[0].length - 1; i++) {
//                    listaObiektow.add(i);
//                }
//
//                // [[], [5], [], [5], [], [5], [], [4]]
//
//                if(rzad > 0){                                       // jesli rzad wiekszy od 1 to eliminuje z listy obiektow do kombinacji (uzytych w macierzy) odpowiednie atrybuty
//                    if(wyeliminowaneAtrybuty.get(obiektNr) != null){
//                        listaObiektow.remove(wyeliminowaneAtrybuty.get(obiektNr));
//                    }
//                }
//
//                List<int[]> kombinacjeWszystkie = kombinuj(listaObiektow, rzad+1);
//
//                for (int[] kombinacjeArr : kombinacjeWszystkie) {
//
//
//                    if (!czyKombinacjaZawieraSieWWierszu(kombinacjeArr, mn[obiektNr])) {    // jesli kombinacja zawiera sie w wierszu to tworze regule
//
//
//                        Regula reg = new Regula(sys[obiektNr], kombinacjeArr);    // tworzenie reguly w tym miejscu zeby nie tworzyc regul jesli kombinacja bedzie zawierac sie w wierszu macierzy, zwiekszam przez to wydajnosc programu
//
//
//                        if (!reg.czyRegulaZawieraJednaZRegul(regulyExhaustive) && !reg.powtarzajacaSieRegula(regulyExhaustive)) {   // sprawdzam czy regula zawiera regule z listy i wyrzucam reguly ktore sie powtarzaja na liscie
//
//                            List<Integer> supportObiekty = reg.obiektySupportu(sys);     // lista z obiektami ktore spelniaja regule ktora aktualnie leci w petli, lista jest zerowana przy kazdym przelocie petli
//
//                            reg.support = supportObiekty.size();    // licze support na podstawie ilosci obiektow ktore spelniaja dana regule
//                            regulyExhaustive.add(reg);   // dodaje regule do listy wynikowej
//
//                            if(rzad == 0){   // dla rzedu 1 eliminuje z rozwazan atrybuty na dla odpowiednich obiektow
//                                for(Integer obiekt : supportObiekty){ // kazdy obiekt z listy obiektow supportu
//                                    for(Integer atrybut : reg.deskryptor.keySet())  // kazdy atrybut z zestawu artybutow (kluczy) reguly
//                                        wyeliminowaneAtrybuty.put(obiekt, atrybut);
//                                }
//
//                            }
//
//                        }
//
//
//                    }
//                }
//            }
//        }




//        System.out.println("\n\nReguly algorytmu Exhaustive:\n");
//
//        int nrReguly = 0;
//        for(Regula r : regulyExhaustive){
//            System.out.print("R"+nrReguly+": ");
//            System.out.println(r.toString());
//            nrReguly++;
//        }

//        for(String[] ob : zwrocKoncept(sys, "1")){
//            Arrays.stream(ob).forEach(el -> System.out.print(el+" "));
//            System.out.println("");
//        }

        // sprawdzenie ile razy wystepuje ta sama wartosc dla kolejnego atrybutu

        List<List<Deskryptor>> listaObiektowDeskryptorow = new ArrayList<>();


        String[][] koncept = zwrocKoncept(sys, "0");
        //Map<Integer, Deskryptor> listaDeskrptorow = new HashMap<>();


        //Deskryptor d = new Deskryptor();
        for(int i=0; i<koncept.length; i++){ // przelatuje wszystkie obiekty
            String[] obiekt = koncept[i];



            List<Deskryptor> listaAtrybutow = new ArrayList<>();
            for(int j = 0; j<obiekt.length; j++){   // przelatuje dla atrybutow
                Deskryptor d = new Deskryptor();
                d.nrAtrybutu = j;
                d.wartosc = obiekt[j];
//                d.czestosc++;
                listaAtrybutow.add(d);
            }


            //listaAtrybutow.stream().forEach(at -> System.out.print(at.nrAtrybutu+"="+at.wartosc+"     "+at.czestosc+"\n"));
            //System.out.println("\n\n");


            listaObiektowDeskryptorow.add(listaAtrybutow);


        }


        //   biore sobie jeden caly obiekt i musze wzgledem niego sprawdzic sobie w petli czly system i policzyc ile razy wystepuje dany atrubyt


//        List<Integer> obiektyIdx = new ArrayList<>();
//        int nrObiektu = 0;
////        for (int nrObiektu = 0; nrObiektu < koncept.length; nrObiektu++)
////        {
//        int nrObiektuWew = 0;
//            for(String[] calySystem : sys){
//                if (koncept[nrObiektu][3].equals(calySystem[3])) {
//                    System.out.println("obiekt " +(nrObiektuWew+1));
//                    obiektyIdx.add(nrObiektuWew);
//                }
//                nrObiektuWew++;
//            }



//        for(int i=0; i<koncept.length; i++) { // przelatuje wszystkie obiekty
//            String[] obiekt = koncept[i];
//
//            List<Deskryptor> listaAtr = new ArrayList<>();
//            listaAtr.addAll(listaObiektowDeskryptorow.get(i));
//
//            for(int j = 0; j<obiekt.length; j++) {   // przelatuje dla atrybutow
//                if(listaAtr.get(j).nrAtrybutu == j && listaAtr.get(i).wartosc == obiekt[j]){
//                    listaAtr.get(j).czestosc++;
//                }
//            }
//        }
//
//        int it = 0;
//        for(List<Deskryptor> deskryptory: listaObiektowDeskryptorow){
//            System.out.println(it);
//            for(Deskryptor d : deskryptory){
//                System.out.println(d.nrAtrybutu+"="+d.wartosc+"     "+d.czestosc);
//            }
//            it++;
//        }

        //zwrocKoncept(sys,"0");


//        for(Deskryptor d : liczCzestoscAtrybutu(zwrocKoncept(sys, "1"), 2)){
////            Deskryptor d = new Deskryptor();
////            for(Map.Entry<Integer, String> atrybut : desk.getKey().entrySet()){
////                d.nrAtrybutu = atrybut.getKey();
////                d.wartosc = atrybut.getValue();
////                d.czestosc = desk.getValue();
////            }
//            System.out.println("nrAtrybutu: " + (d.nrAtrybutu+1) + ", Wartosc: " + d.wartosc + ", czestosc: " + d.czestosc);
//        }

        List<Integer> listaAtrybutow = new ArrayList<>();
        listaAtrybutow.add(0);
        listaAtrybutow.add(1);
        listaAtrybutow.add(2);
        listaAtrybutow.add(3);

//        Deskryptor d1 = najczestszyDeskryptorAtrybutu(zwrocKoncept(sys, "1"), 3);
//        System.out.println("a" + (d1.nrAtrybutu+1) + " = " + d1.wartosc + ", czestosc: " + d1.czestosc);

        List<String> decyzjeKonceptu = new ArrayList<>();
        decyzjeKonceptu.add("1");
        decyzjeKonceptu.add("0");

        String[][] testKoncept  = {
                {"1","0","1","0","0"},
                {"1","2","5","0","0"}
        };
        //for(String decyzjaKon : decyzjeKonceptu) {
        String decyzjaKon = "1";
        for (Regula r : regulyWynikowe(sys, decyzjaKon)) {
                System.out.println(r.toString());
            }
        //}




//        Deskryptor d2 = najczestszyDeskryptorZListyAtrybutow(testKoncept, listaAtrybutow);
//        System.out.println("a" + (d2.nrAtrybutu+1) + " = " + d2.wartosc + ", czestosc: " + d2.czestosc);


//        for(Map.Entry<Integer, Deskryptor> deskryptorObiektu : liczCzestoscAtrybutu(zwrocKoncept(sys, "1"), 2).entrySet()){
//            Deskryptor d1 = deskryptorObiektu.getValue();
//            int nrOb = deskryptorObiektu.getKey();
//            System.out.println("o" + (nrOb+1) + ": a" + (d1.nrAtrybutu+1) + " = " + d1.wartosc + ", czestosc: " + d1.czestosc);
//
//        }





    }


    public static Deskryptor najczestszyDeskryptorAtrybutu(String[][] sysk, Integer nrAtrybutu){//}, Integer nrObiektu){   // Map<Integer, Deskryptor>

        String[][] kopiaSys = sysk;
        String[][] koncept = sysk;
        Map<Integer, Deskryptor> deskryptoryZObiektami = new HashMap<>();

        for(int nrObiektu = 0; nrObiektu < koncept.length; nrObiektu++) {
            int czestosc = 0;
            String wartoscAtrybutu = koncept[nrObiektu][nrAtrybutu];
            for (String[] kopia : kopiaSys) {
                if (wartoscAtrybutu.equals(kopia[nrAtrybutu])) {
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

        List<Deskryptor> listaWszystkichDeskryptorow = new ArrayList<>();

        for(Integer nrAtrybutu : listaNumerowAtrybutow){
            Deskryptor d = najczestszyDeskryptorAtrybutu(sysk, nrAtrybutu);
            listaWszystkichDeskryptorow.add(d);
        }

        // mam liste wszystkich deskryptorow dla poszczegolnych atrybutow, musze teraz porownac sobie ich czestosci i wybrac ten, ktory ma najwiskza czestosc w kolejnosci 1..n
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

//        String[][] koncept = {
//                {"1", "0", "1", "0", "0"},
//                {"1", "2", "5", "0", "0"}
//        };


//                String[][] koncept = {
//                {"1", "2", "1", "1", "0"}
//        };

        // zamieniam koncept na List<String[]>

        List<String[]> konceptLista = new ArrayList<>();

        for(String[] obiekt : koncept){
            konceptLista.add(obiekt);
        }

        List<Regula> regulyWynikoweWszystkie = new ArrayList<>();

        List<Integer> listaAtrybutow = new ArrayList<>();
        listaAtrybutow.add(0);
        listaAtrybutow.add(1);
        listaAtrybutow.add(2);
        listaAtrybutow.add(3);

        // petla while dopoki koncept sie nie wyzeruje


        // ta metoda ma mi znalezc z lisyt podanych deskryptorow obiekty, ktore maja takie deskryptory z dana decyzja


        // dajemy foreach zeby przepisac wszystkie deskryptory do regule, potem sprawdzamy czy obiekt spelnia regule i dostaniemy liste obiektow ktore ja spelniaja


        while(konceptLista.size() > 0) {
            Regula rWynikowa = new Regula();

            rWynikowa.decyzja = koncept[0][koncept.length + 1];


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
                    List<Integer> obiektySpelniajaceReg = rWynikowa.obiektySpelniajaceRegule(koncept);
                    // sprawdzenie rozmiaryw list, jesli wielkosc obiektySpelniajaceReg < konceptLista to ustaw wielkosc konceptZListy na obiektyspelniajaceRegule.size()
                    int konceptZListySize = 0;
                    if (obiektySpelniajaceReg.size() < konceptLista.size()) {
                        konceptZListySize = obiektySpelniajaceReg.size();
                    } else {
                        konceptZListySize = konceptLista.size();
                    }
//                    if(konceptZListySize == 1) konceptZListySize++;
                    //   sprawdzic czemu dostaje konceptZListy jako null przy obiekcie 2 atrybut 3(4) dla caleko konceptu z decjzja 1
                    String[][] konceptZListy = new String[konceptZListySize][];
                    int iterator = 0;
                    for (int obIdx = 0; obIdx < konceptLista.size(); obIdx++) {
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
