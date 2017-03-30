package pl.si.cw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;



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
        r2.deskryptor.put(1,"1");    // regula zalozona, leci do sprawdzenia
        r2.decyzja = "1";


        String[][] system = {
                                {"1","2","T","1","1"},
                                {"2","3","N","1","1"},
                                {"3","4","T","1","1"},
                                {"3","1","T","1","0"},
                                {"1","1","T","2","0"}
        };

        String[][] s2 = {
                            {"2","5","4","1","4","4","1"},
                            {"3","4","2","2","4","5","1"},
                            {"3","5","2","2","1","5","1"},
                            {"2","4","2","5","4","5","0"},
                            {"4","4","3","5","3","3","1"},
                            {"4","4","3","2","3","3","0"},
                            {"2","4","5","3","1","3","0"},
                            {"5","2","1","3","2","3","0"}

        };

        //int nrObiektu = 0;
        //loop:
//        for(String[] obiekt : system){
//            nrObiektu++;
//            System.out.print("\nObiekt "+nrObiektu+": ");
//
//
////            if(czyObiektSpelniaRegule(r2,obiekt)){
////                System.out.println("");
////                //break loop;
//
//            czyRegulaJestSprzeczna(r2,obiekt);
//
//        }


        if(czySprzeczna(r2,system)) System.out.println("True"); else System.out.println("False");

        //if() System.out.println("Tak, sprzeczna"); else System.out.println("Niesprzeczna");


    //   jak znajde obiekt ktory ma taka sama wartosc deskryptora (ten sam deskryotpr) ale inna klase decyzyjna
        //   bez sensu, totalnie niepotrzebne, mozna zrobic prosciej





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
            System.out.println("\nObiekt spelnia wszystkie reguly\n\n");
            return true;
        }
        else{
            System.out.println("\nnie spełnia reguł\n\n");
            return false;
        }

    }


//
//
//    public static void czyRegulaJestSprzeczna(Regula reg, String[] obiekt){
//
//        for(Map.Entry<Integer, String> deskr : reg.deskryptor.entrySet()){     // foreach dla kazdego deskryptora
//
//            //System.out.print("\n"+deskr.getKey()+" => "+deskr.getValue()+", D: "+reg.decyzja);
//
//            if(deskr.getValue().equals(obiekt[deskr.getKey()-1])){
//                System.out.println("Obiekt spelnia deskryptor");
//                if(!reg.decyzja.equals(obiekt[obiekt.length - 1])){
//                    System.out.println("Obiekt ma różną decyzję");
//                }
//                else{
//                    System.out.println("Obiekt ma identyczną decyzję");
//                }
//                //System.out.print("   spełnia");
//
//            }
//
//            else{ System.out.print("Obiekt nie spelnia deskryptora"); }
//
//        }
//
//    }



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
                    System.out.println();
                }
            }
        }
        return true;
    }


    public static Regula tworzRegule(String[] obiekt, int[] kombinacja){
        Regula r = new Regula();
        r.decyzja = obiekt[obiekt.length - 1];
        for(int nrAtrybutu : kombinacja){
            String wartoscAtrybutu = obiekt[nrAtrybutu];
            r.deskryptor.put(nrAtrybutu, wartoscAtrybutu);
        }
        return r;
    }



    // napisac metode czy Regula jest sprzeczna na tej samej konstrukcji

    //napisac metode ktora bedzie dzialac w przeciwny sposob


    static void permute(List<Integer> arr, int k){
        for(int i = k; i < arr.size(); i++){
            Collections.swap(arr, i, k);
            permute(arr, k+1);
            Collections.swap(arr, k, i);
        }
        if (k == arr.size() -1){
            System.out.println(Arrays.toString(arr.toArray()));
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
