package pl.si.cw2;


import com.sun.org.apache.regexp.internal.RE;
import org.paukov.combinatorics3.Generator;

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

        String[][] systemZPliku = readFile("SystemDecyzyjny.txt",8, 7);

        String[][] sys = systemPDF;     // wybor systemu





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

        for(String[] ob : zwrocKoncept(sys, "1")){
            Arrays.stream(ob).forEach(el -> System.out.print(el+" "));
            System.out.println("");
        }

        //zwrocKoncept(sys,"0");




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
