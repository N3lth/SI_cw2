package pl.si.cw2;


import com.sun.org.apache.regexp.internal.RE;
import org.paukov.combinatorics3.Generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;


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

        String[][] systemZPliku = readFile("SystemDecyzyjny.txt",8, 7);




        String[][] sys = systemPDF;     // wybor systemu



        /*

        //     ALGORYTM 1   --   IMPLEMENTACJA


        Map<Regula,List<Integer>> obiektyReguly = new HashMap<>();

        // List<Regula> wszystkieReguly = new ArrayList<>();

        List<Integer> obiektyWyeliminowane = new ArrayList<>();

        List<Regula> utworzoneReguly = new ArrayList<>();


        for(int rzad = 1; rzad <= sys[0].length - 1; rzad++) {


            List<Integer> listaObiektow = new ArrayList<>();
            for (int i = 0; i < sys[0].length - 1; i++) {
                listaObiektow.add(i + 1);
            }
            List<int[]> kombinacjeWszystkie = kombinuj(listaObiektow, rzad);


            for (int obiektNr = 0; obiektNr < sys.length; obiektNr++) {

                if (!obiektyWyeliminowane.contains(obiektNr)) {     // jak obiekt nie jest w wyeliminowanych

                    for (int[] kombinacjeArr : kombinacjeWszystkie) {


                        Regula reg = tworzRegule(sys[obiektNr], kombinacjeArr);

                        // wszystkieReguly.add(reg);     //  lista pomocnicza ze wszystkimi regulami do sprawdzenia dzialania programu

                        if (czyNieSprzeczna(reg, sys)) {     // jesli regula jest niesprzeczna


                            List<Integer> supportObiekty = obiektySupportu(reg, sys);     // lista z obiektami ktore spelniaja regule ktora aktualnie leci w petli, lista jest zerowana przy kazdym przelocie petli
                            obiektyWyeliminowane.addAll(supportObiekty);     // lista ze wszystkimi obiektami wyeliminowanymi z rozwazan


                            reg.support = supportObiekty.size();      // ustawienie wartosci supportu jako wielkosc listy z obiektami supportu


                            utworzoneReguly.add(reg);     // dodanie do listy wynikowej reguly

                            obiektyReguly.put(reg, supportObiekty);    // lista z obiektami ktore spelniaja regule (czyli te z supportu)


                            break;    // przerwanie petli w momencie kiedy zostanie znaleziona regula niesprzeczna
                        }


                    }
                }
            }




        }





        //        WYSWIETLENIE WSZYSTKICH UTWORZONY REGUL -- DO TESTU

//        System.out.println("Wyswietlam wszystkie utworzone reguly: ");
//
//        for (Regula r : wszystkieReguly) {
//            System.out.println(r.deskryptor + " => " + r.decyzja);
//        }






        System.out.println("\nUtworzone reguly niesprzeczne: ");

        int licznikRegul = 1;
        for (Regula r : utworzoneReguly) {
            System.out.print("\nR" + licznikRegul + ": ");
            for(int key : r.deskryptor.keySet()){
                System.out.print("(a"+key+"="+r.deskryptor.get(key)+") ");
            }
            System.out.print("=> d=" + r.decyzja + " [" + r.support + "]    Obiekty: ");
            obiektyReguly.get(r).forEach(indeksObiektu -> System.out.print(indeksObiektu + 1 + " "));
            licznikRegul++;
        }


        */



        /*

        //     TEST TWORZENIA REGUL - SPRAWDZENIE CZY TWORZY DOBRZE


        List<Regula> regTest = new ArrayList<>();

        for(int i=0; i<sys.length; i++){
            for (Integer[] itr : kombInt) {
                int[] intArray2 = Arrays.stream(itr).mapToInt(Integer::intValue).toArray();

                Regula nowaRegula = tworzRegule(sys[i], intArray2);

                regTest.add(nowaRegula);
            }

        }

        for(Regula r : regTest){
            System.out.println(r.deskryptor+" => "+r.decyzja);
        }

        */









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


/*




        List<Regula> rwgulyExhaustive = new ArrayList<>();
        int[][][] mn = tworzMacierzNieodroznialnosci(sys);

        Map<Integer, Integer> wyeliminowaneAtrybuty = new HashMap<>();


        for(int rzad = 0; rzad < sys[0].length - 1; rzad++) {

            for (int obiektNr = 0; obiektNr < mn.length; obiektNr++) {


                List<Integer> listaObiektow = new ArrayList<>();    // tworzenie listy obiektow do kombinacji
                for (int i = 0; i < sys[0].length - 1; i++) {
                    listaObiektow.add(i);
                }


                // [[], [5], [], [5], [], [5], [], [4]]

                if(rzad > 0){                                       // jesli rzad wiekszy od 1 to eliminuje z listy obiektow do kombinacji (uzytych w macierzy) odpowiednie atrybuty
                    if(wyeliminowaneAtrybuty.get(obiektNr) != null){
                        listaObiektow.remove(wyeliminowaneAtrybuty.get(obiektNr));
                    }
                }

                List<int[]> kombinacjeWszystkie = kombinuj(listaObiektow, rzad+1);

                for (int[] kombinacjeArr : kombinacjeWszystkie) {


                    if (!czyKombinacjaZawieraSieWWierszu(kombinacjeArr, mn[obiektNr])) {    // jesli kombinacja zawiera sie w wierszu to tworze regule


                        Regula reg = tworzRegule(sys[obiektNr], kombinacjeArr);


                        if (!czyRegulaZawieraJednaZRegul(reg, rwgulyExhaustive) && !powtarzajacaSieRegula(rwgulyExhaustive, reg)) {   // wyrzucam reguly ktore sie powtarzaja na liscie

                            List<Integer> supportObiekty = obiektySupportu(reg, sys);     // lista z obiektami ktore spelniaja regule ktora aktualnie leci w petli, lista jest zerowana przy kazdym przelocie petli

                            reg.support = supportObiekty.size();
                            rwgulyExhaustive.add(reg);

                            if(rzad == 0){   // dla rzedu 1 eliminuje z rozwazan atrybuty na dla odpowiednich obiektow
                                for(Integer obiekt : supportObiekty){
                                    for(Integer atrybut : reg.deskryptor.keySet())
                                    wyeliminowaneAtrybuty.put(obiekt, atrybut);
                                }

                            }

                        }


                    }
                }
            }
        }




        System.out.println("\n\nReguly algorytmu Exhaustive:\n");
        for(Regula r : rwgulyExhaustive){
            for(Map.Entry<Integer, String> desk : r.deskryptor.entrySet()) {
                System.out.print("(a"+(desk.getKey()+1)+"="+desk.getValue()+") ");
            }
            System.out.println("=> " + "d="+r.decyzja + " [" + r.support + "]");
        }




*/





/*

        //      TEST ZAWIERANIA REGUL

        Regula r1 = new Regula();
        Regula r2 = new Regula();
        Regula r1a = new Regula();
        Regula r2a = new Regula();
        Regula r1b = new Regula();
        Regula r2b = new Regula();
        Regula r1c = new Regula();
        Regula r2c = new Regula();
        Regula r1d = new Regula();
        Regula r2d = new Regula();
        Regula r1e = new Regula();
        Regula r2e = new Regula();



        //    (a3 = 1) & (a4 = 1) & (a5 = 3) => (d = 1)
        r1.deskryptor.put(3,"1");
        r1.deskryptor.put(4,"1");
        r1.deskryptor.put(5,"1");
        r1.decyzja = "1";

        // [1=1, 2=1, 4=2]

        r2.deskryptor.put(1,"1");
        r2.deskryptor.put(2,"1"); //
        r2.deskryptor.put(4,"2"); //
        r2.decyzja = "0";

        r2a.deskryptor.put(2,"1");
        r2a.deskryptor.put(4,"2");
        //r2a.deskryptor.put(5,"0");
        r2a.decyzja = "0";


        r1a.deskryptor.put(4,"3");
        r1a.deskryptor.put(6,"1");
        r1a.decyzja = "0";

        r1b.deskryptor.put(3,"1");
        r1b.deskryptor.put(4,"1");
        r1b.decyzja = "1";                // decyzja jest tez wazna

        List<Regula> tst = new ArrayList<>();
        tst.add(r1a);
        tst.add(r2a);

        System.out.println(czyRegulaZawieraJednaZRegul(r2,tst));






        //     SPRAWDZANIE CZY KOMBINACJA ZAWIERA SIE W KOMORCE

        int[][] wiersz =   {
                                {1,2,3,6},
                                {1,2,3},
                                {1,2,3,4},
                                {1,2,5},
                                {1,2,6}
                            };

        int[] kombinacja = {1,2};

        //System.out.println(czyKombinacjaZawieraSieWWierszu(kombinacja, wiersz));

*/










    //   ALGORYTM 3

















    }





















    //      METODY DO 1 ALGORYTMU


    public static List<Integer> obiektySupportu(Regula r, String[][] system)
    {
        List<Integer> obiektyIdx = new ArrayList<>();
        for (int i = 0; i < system.length; i++)
        {
            if (czyObiektSpelniaRegule(r, system[i]))
                obiektyIdx.add(i);
        }
        return obiektyIdx;
    }



    public static List<int[]> kombinuj(List<Integer> obiekt, int rzad){
        List<List<Integer>> kombList = new ArrayList<>();   // deklaracja listy do przechowania wynikow z dodatkowej funkcji generujacej kombinacje

        List<int[]> outList = new ArrayList<>();    // deklaracja listy (tablic) wynikowej

        Generator.combination(obiekt).simple(rzad).stream().forEach(kItem -> kombList.add(kItem));     // funkcja generowania kombinacji bez powtorzen z dodatkowej biblioteki


        for(List<Integer> kombItem : kombList){         // przelot dla kazdego elementu listy kombinacji
            int[] kombArr = new int[rzad];      // deklaracja tablicy do przechowywania kombinacji
            for(int j=0;j<kombItem.size();j++){
                kombArr[j] = kombItem.get(j);           // przepisywanie kombinacji z listy do tablicy
            }
            outList.add(kombArr);                       // dodawanie tablicy do wynikowej listy tablic
        }
        return outList;
    }



    static List<String[]> kombinujS(List<String> obiekt, int rzad){
        List<List<String>> kombList = new ArrayList<>();   // deklaracja listy do przechowania wynikow z dodatkowej funkcji generujacej kombinacje

        List<String[]> outList = new ArrayList<>();    // deklaracja listy (tablic) wynikowej

        Generator.combination(obiekt).simple(rzad).stream().forEach(kItem -> kombList.add(kItem));     // funkcja generowania kombinacji bez powtorzen z dodatkowej biblioteki


        for(List<String> kombItem : kombList){         // przelot dla kazdego elementu listy kombinacji
            String[] kombArr = new String[rzad];      // deklaracja tablicy do przechowywania kombinacji
            for(int j=0;j<kombItem.size();j++){
                kombArr[j] = kombItem.get(j);           // przepisywanie kombinacji z listy do tablicy
            }
            outList.add(kombArr);                       // dodawanie tablicy do wynikowej listy tablic
        }
        return outList;
    }



    public static boolean czyObiektSpelniaRegule(Regula reg, String[] obiekt){

        for(Map.Entry<Integer, String> deskr : reg.deskryptor.entrySet()){     // foreach dla kazdego deskryptora
            if(!deskr.getValue().equals(obiekt[deskr.getKey()])){    //   jak nie zgadzaja sie wartosci atrybutow to zwroc false   // korekcja
                return false;
            }
        }
        return true;
    }


    public static boolean czyNieSprzeczna(Regula reg, String[][] systemDec) {
        for (String[] obiekt : systemDec) {
            if (czyObiektSpelniaRegule(reg, obiekt) && !reg.decyzja.equals(obiekt[obiekt.length - 1]) ) {
                return false;
            }
        }
        return true;
    }



    public static Regula tworzRegule(String[] obiekt, int[] kombinacja){
        Regula r = new Regula();
        r.decyzja = obiekt[obiekt.length - 1];
        for(int nrAtrybutu : kombinacja){
            String wartoscAtrybutu = obiekt[nrAtrybutu];   // korekta -1 zeby zgadzaly sie indeksy
            r.deskryptor.put(nrAtrybutu, wartoscAtrybutu);
        }
        return r;
    }





    public static List<List<Integer>> Kombinacja(List<Integer> strings) {
        if (strings.size() > 1) {
            List<List<Integer>> result = new ArrayList<>();

            for (Integer str : strings) {
                List<Integer> subStrings = new ArrayList<>(strings);
                subStrings.remove(str);

                result.add(new ArrayList<Integer>(Arrays.asList(str)));

                for (List<Integer> combinations : Kombinacja(subStrings)) {
                    combinations.add(str);
                    result.add(combinations);
                }
            }

            return result;
        } else {
            List<List<Integer>> result = new ArrayList<>();
            result.add(new ArrayList<Integer>(strings));
            return result;
        }
    }




    public static List<List<String>> KombinacjaString(List<String> strings) {
        if (strings.size() > 1) {
            List<List<String>> result = new ArrayList<List<String>>();

            for (String str : strings) {
                List<String> subStrings = new ArrayList<String>(strings);
                subStrings.remove(str);

                result.add(new ArrayList<String>(Arrays.asList(str)));

                for (List<String> combinations : KombinacjaString(subStrings)) {
                    combinations.add(str);
                    result.add(combinations);
                }
            }

            return result;
        } else {
            List<List<String>> result = new ArrayList<List<String>>();
            result.add(new ArrayList<String>(strings));
            return result;
        }
    }


























    //      METODY DO 2 ALGORYTMU

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

    public static Boolean czyRegulaZawieraRegule(Regula rWyzszegoRzedu, Regula rNizszegoRzedu){
        int licznik = 0;

        for(Map.Entry<Integer, String> deskr : rNizszegoRzedu.deskryptor.entrySet()){     // foreach kazdego deskryptora dla rNizszegoRzedu
            if(rNizszegoRzedu.deskryptor.size() < rWyzszegoRzedu.deskryptor.size()) {
                if (rWyzszegoRzedu.deskryptor.entrySet().contains(deskr) && rWyzszegoRzedu.decyzja.equals(rNizszegoRzedu.decyzja)) {
                    licznik++;   // liczenie ile deskryptorow sie zgadza
                }
            }
        }

        if(licznik>=rNizszegoRzedu.deskryptor.size()) return true;     // jesli wszystkie reguly z zestawu deskryptorow sie zgadzaja
        else return false;
    }

    public static Boolean czyRegulaZawieraJednaZRegul(Regula rWyzszegoRzedu, List<Regula> listaRegulNizszegoRzedu){
        for(Regula rNizszegoRzedu : listaRegulNizszegoRzedu) {
            if (rNizszegoRzedu.deskryptor.size() > 1) {  // sprawdzenie czy rzad jest wiekszy od 1 (nie mozna brac pod uwage deskryptorow 1 rzedu)
                if (czyRegulaZawieraRegule(rWyzszegoRzedu, rNizszegoRzedu)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static Boolean powtarzajacaSieRegula(List<Regula> tymczasowaListaRegul, Regula regula){
        for(Regula r : tymczasowaListaRegul){
            if(r.deskryptor.equals(regula.deskryptor) && r.decyzja.equals(regula.decyzja)){
                return true;
            }
        }
        return false;
    }


    public static int liczSupport(Regula r, String[][] system){
        r.support = 0;
        for(int i = 0; i<system.length; i++){
            if(czyObiektSpelniaRegule(r,system[i])){
                r.support++;
            }
        }
        return r.support;
    }

    //       METODT UNIWERSALNE

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
