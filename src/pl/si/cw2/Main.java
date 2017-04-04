package pl.si.cw2;

import org.paukov.combinatorics3.Generator;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;


public class Main {

    public static void main(String[] args) {
//        permute(Arrays.asList(1,2,3), 0);          // permutacja (kombinacja bez powtorzen)

//        ArrayList<String> lst=new ArrayList<String>();
//        lst.add(0,"xd");
//        lst.add(1,"df");

//        for(int i=0, i<lst.size(), ){
//            System.out.print(i);
//        }

        Regula r1 = new Regula();

        r1.deskryptor.put(2,"1");
        r1.deskryptor.put(3,"N");

        r1.decyzja = "0";

//        System.out.println(r1.deskryptory.keySet().toArray()[0]+" => "+r1.deskryptory.get(r1.deskryptory.keySet().toArray()[0]));

        Regula r2 = new Regula();
        r2.deskryptor.put(5,"4");    // regula zalozona, leci do sprawdzenia
        r2.decyzja = "1";


        String[][] system = {
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




        /*

        // generowanie kombinacji dla stringow, przyklad ze dziala

        List<String> lstStr = new ArrayList<>();
        lstStr.add("1");
        lstStr.add("2");
        lstStr.add("3");
        lstStr.add("4");


        List<String[]> kombStr = kombinujIChujString(lstStr,2);

        for(int i=0;i<kombStr.size();i++){      // przelatuje dla kazdej kombinacji (al)
            for(String itg : kombStr.get(i))
            {
                System.out.print(itg+",");
            }
            System.out.println();
        }


        */



        // tworzenie regul dla kombinacji dla obiektu obiektNr

        int obiektNr = 2;
        int rzad = 1;

        List<Integer> lst = new ArrayList<>();
        for(int i=0;i<s2[obiektNr+1].length-1;i++){
            lst.add(i+1);
        }
        List<Integer[]> kombInt = kombinujIChuj(lst,rzad);

        ArrayList<Regula> reguly = new ArrayList<>();

        for(Integer[] it : kombInt){
            int[] intArray = Arrays.stream(it).mapToInt(Integer::intValue).toArray();
            reguly.add(tworzRegule(s2[obiektNr+1],intArray));      // czy regyly moge zrobic na podstawie jednego obiektu a potem je sprawdzac, czy moze mam robic dla kazdego obiektu?
        }

        System.out.println("Rząd "+rzad+", sprawdzam stworzone reguły dla obiektu "+obiektNr);

        for(Regula r : reguly){
            System.out.print("Utworzona regula: "+r.deskryptor+" => d="+r.decyzja);
            if(!czySprzeczna(r,s2)) System.out.println("  X - Regula jest sprzeczna"); else System.out.println("  OK - Regula jest niesprzeczna, support: "+r.support);
        }








        //if() System.out.println("Tak, sprzeczna"); else System.out.println("Niesprzeczna");


    //   jak znajde obiekt ktory ma taka sama wartosc deskryptora (ten sam deskryotpr) ale inna klase decyzyjna
        //   bez sensu, totalnie niepotrzebne, mozna zrobic prosciej





    }


    static List<Integer[]> kombinujIChuj(List<Integer> obiekt, int rzad){
        List<List<Integer>> kombList = new ArrayList<>();   // deklaracja listy do przechowania wynikow z dodatkowej funkcji generujacej kombinacje

        List<Integer[]> outList = new ArrayList<>();    // deklaracja listy (tablic) wynikowej

        Generator.combination(obiekt).simple(rzad).stream().forEach(kItem -> kombList.add(kItem));     // funkcja generowania kombinacji bez powtorzen z dodatkowej biblioteki


        for(List<Integer> kombItem : kombList){         // przelot dla kazdego elementu listy kombinacji
            Integer[] kombArr = new Integer[rzad];      // deklaracja tablicy do przechowywania kombinacji
            for(int j=0;j<kombItem.size();j++){
                kombArr[j] = kombItem.get(j);           // przepisywanie kombinacji z listy do tablicy
            }
            outList.add(kombArr);                       // dodawanie tablicy do wynikowej listy tablic
        }
        return outList;
    }



    static List<String[]> kombinujIChujString(List<String> obiekt, int rzad){
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
        int ileSpelnia = 0;
        int iloscRegul = reg.deskryptor.size();

        for(Map.Entry<Integer, String> deskr : reg.deskryptor.entrySet()){     // foreach dla kazdego deskryptora

            //System.out.print("\n"+deskr.getKey()+" => "+deskr.getValue()+", D: "+reg.decyzja);

            if(deskr.getValue().equals(obiekt[deskr.getKey()-1])){    //   && reg.decyzja.equals(obiekt[obiekt.length - 1])){
                //System.out.print("   spełnia");
                ileSpelnia++;
            }

            //else{ System.out.print("   niespełnia"); }

        }

        if(ileSpelnia == iloscRegul){
            //System.out.println("\nObiekt spelnia wszystkie reguly\n\n");
            return true;
        }
        else{
            //System.out.println("\nnie spełnia reguł\n\n");
            return false;
        }

    }




    public static boolean czySprzeczna(Regula reg, String[][] systemDec){

        ArrayList<String> obiektyTakieSame = new ArrayList<>();
        ArrayList<String> obiektyNiesprzeczne = new ArrayList<>();

        for(Map.Entry<Integer, String> deskr : reg.deskryptor.entrySet()){     // foreach dla kazdego deskryptora

            //System.out.print("\n"+deskr.getKey()+" => "+deskr.getValue()+", D: "+reg.decyzja);

            // musze dostac liste obiektow ktore maja deskryptory takie same jak zadany
            for(String[] obiekt : systemDec){
                if(czyObiektSpelniaRegule(reg, obiekt) && reg.decyzja != obiekt[obiekt.length - 1]){
                    return false;
                }
                if(deskr.getValue().equals(obiekt[deskr.getKey()-1])){       // jak sie zgadza z wartoscia atrybutu
                    obiektyTakieSame.add(obiekt[deskr.getKey()-1]);
                    reg.support++;
                    //System.out.println();
                }
            }
        }
        return true;
    }


    public static Regula tworzRegule(String[] obiekt, int[] kombinacja){
        Regula r = new Regula();
        r.decyzja = obiekt[obiekt.length - 1];
        for(int nrAtrybutu : kombinacja){
            String wartoscAtrybutu = obiekt[nrAtrybutu -1];   // korekta -1 zeby zgadzaly sie indeksy
            r.deskryptor.put(nrAtrybutu, wartoscAtrybutu);
        }
        return r;
    }



    // napisac metode czy Regula jest sprzeczna na tej samej konstrukcji

    //napisac metode ktora bedzie dzialac w przeciwny sposob




    public static List<List<String>> Kombinacja(List<String> strings) {
        if (strings.size() > 1) {
            List<List<String>> result = new ArrayList<List<String>>();

            for (String str : strings) {
                List<String> subStrings = new ArrayList<String>(strings);
                subStrings.remove(str);

                result.add(new ArrayList<String>(Arrays.asList(str)));

                for (List<String> combinations : Kombinacja(subStrings)) {
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
