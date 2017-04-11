package pl.si.cw2;

import org.paukov.combinatorics3.Generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;


public class Main {


    public static void main(String[] args) {


        String[][] s1 = {
                                {"1","2","T","1","0"},
                                {"2","3","N","1","1"},
                                {"3","4","T","1","1"},
                                {"3","1","T","1","0"},
                                {"1","1","T","2","0"}
        };

        String[][] s2 = {
                            {"1","1","1","1","3","1","1"},
                            {"1","1","1","1","3","2","1"},
                            {"1","1","1","3","2","1","0"},
                            {"1","1","1","3","3","2","1"},
                            {"1","1","2","1","2","1","0"},
                            {"1","1","2","1","2","2","1"},
                            {"1","1","2","3","3","1","0"},
                            {"1","1","2","3","4","1","1"}

        };




        String[][] sys = s2;


        Map<Regula,List<Integer>> obiektyReguly = new HashMap<>();

        List<Regula> wszystkieReguly = new ArrayList<>();

        List<Integer> obiektyWyeliminowane = new ArrayList<>();

        List<Regula> listaRegul1 = new ArrayList<>();


        for(int rzad = 1; rzad <= sys[0].length - 1; rzad++) {


            List<Integer> listaObiektow = new ArrayList<>();
            for (int i = 0; i < sys[0].length - 1; i++) {
                listaObiektow.add(i + 1);
            }
            List<int[]> kombinacjeWszystkie = kombinuj(listaObiektow, rzad);


            for (int obiektNr = 0; obiektNr < sys.length; obiektNr++) {

                if (!obiektyWyeliminowane.contains(obiektNr)) {     // jak obiekt nie jest w wyeliminowanych

                    for (int[] kombinacjeArr : kombinacjeWszystkie) {


                        Regula nowaRegula = tworzRegule(sys[obiektNr], kombinacjeArr);

                        wszystkieReguly.add(nowaRegula);     //  lista pomocnicza ze wszystkimi regulami

                        if (czyNieSprzeczna(nowaRegula, sys)) {     // jesli regula jest niesprzeczna
                            List<Integer> supportObiekty = SupportObiekty(nowaRegula, sys);
                            obiektyWyeliminowane.addAll(supportObiekty);

//                        for (int indeksObiektu : supportObiekty) {
//                            obiektyWyeliminowane.add(indeksObiektu);
//                        }


                            nowaRegula.support = supportObiekty.size();
                            listaRegul1.add(nowaRegula);

                            obiektyReguly.put(nowaRegula, supportObiekty);    // lista z obiektami ktore spelniaja regule (czyli te z supportu)


                            break;
                        }


                    }
                }
            }




        }


//        System.out.println("Wyswietlam wszystkie utworzone reguly: ");
//
//        for (Regula r : wszystkieReguly) {
//            System.out.println(r.deskryptor + " => " + r.decyzja);
//        }


        System.out.println("\nUtworzone reguly niesprzeczne: ");

        int rcnt = 1;
        for (Regula r : listaRegul1) {
            System.out.print("R" + rcnt + ": " + r.deskryptor + " => " + r.decyzja + " [" + r.support + "]    Obiekty: ");
            obiektyReguly.get(r).forEach(indeksObiektu -> System.out.print(indeksObiektu + 1 + ", "));
            System.out.println(" ");
            rcnt++;
        }




        /*

        //     TEST TWORZENIA REGUL - TWORZY DOBRZE


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


    }



    static List<Integer> SupportObiekty(Regula r, String[][] obiekty)
    {
        List<Integer> indeksyOb = new ArrayList<>();
        for (int i = 0; i < obiekty.length; i++)
        {
            if (czyObiektSpelniaRegule(r, obiekty[i]))
                indeksyOb.add(i);
        }
        return indeksyOb;
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
            if(!deskr.getValue().equals(obiekt[deskr.getKey()-1])){    //   jak nie zgadzaja sie wartosci atrybutow to zwroc false
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
            String wartoscAtrybutu = obiekt[nrAtrybutu - 1];   // korekta -1 zeby zgadzaly sie indeksy
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
