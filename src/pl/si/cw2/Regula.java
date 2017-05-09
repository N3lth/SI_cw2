package pl.si.cw2;

import java.util.*;


public class Regula {
    public Map<Integer, String> deskryptor = new HashMap<Integer, String>();
    public String decyzja;
    public int support = 0;


    public Regula (String[] obiekt, int[] kombinacja){

        decyzja = obiekt[obiekt.length - 1];
        for(int nrAtrybutu : kombinacja){
            String wartoscAtrybutu = obiekt[nrAtrybutu - 1];   // korekta -1 zeby zgadzaly sie indeksy
            deskryptor.put(nrAtrybutu, wartoscAtrybutu);
        }

    }


    public List<Integer> obiektySupportu(String[][] system)
    {
        List<Integer> obiektyIdx = new ArrayList<>();
        for (int i = 0; i < system.length; i++)
        {
            if (czyObiektSpelniaRegule(system[i]))
                obiektyIdx.add(i);
        }
        return obiektyIdx;
    }

    public boolean czyObiektSpelniaRegule(String[] obiekt){

        for(Map.Entry<Integer, String> deskr : deskryptor.entrySet()){     // foreach dla kazdego deskryptora
            if(!deskr.getValue().equals(obiekt[deskr.getKey()-1])){    //   jak nie zgadzaja sie wartosci atrybutow to zwroc false
                return false;
            }
        }
        return true;
    }


    public boolean czyNieSprzeczna(String[][] systemDec) {
        for (String[] obiekt : systemDec) {
            if (czyObiektSpelniaRegule(obiekt) && !decyzja.equals(obiekt[obiekt.length - 1]) ) {
                return false;
            }
        }
        return true;
    }

    public String toString(){
        //System.out.print("\nR" + licznikRegul + ": ");
        String out = "";
        for(int key : deskryptor.keySet()){
            //System.out.print("(a"+key+"="+deskryptor.get(key)+") ");
            out+="(a"+key+"="+deskryptor.get(key)+") ";
        }
        if(support > 1) {
            //System.out.print("=> d=" + decyzja + " [" + support + "]    Obiekty: ");
            out+="=> d=" + decyzja + " [" + support + "]    Obiekty: ";
        }
        else{
            //System.out.print("=> d=" + decyzja + "    Obiekty: ");
            out+="=> d=" + decyzja + "    Obiekty: ";
        }
        return out;
    }
}
