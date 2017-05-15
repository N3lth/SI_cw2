package pl.si.cw2;
import java.util.*;


public class Regula {

    public Map<Integer, String> deskryptor = new HashMap<Integer, String>();
    public String decyzja;
    public int support = 0;

    public Regula() {
    }

    public String toString(){
        String out = "";
        for(int key : deskryptor.keySet()){
            out+="(a"+ (key+1) +"="+deskryptor.get(key)+") ";
        }
        if(support > 1) {
            out+="=> d=" + decyzja + " [" + support + "]";
        }
        else{
            out+="=> d=" + decyzja;
        }
        return out;
    }



    public Regula (String[] obiekt, int[] kombinacja){

        decyzja = obiekt[obiekt.length - 1];
        for(int nrAtrybutu : kombinacja){
            String wartoscAtrybutu = obiekt[nrAtrybutu];   // korekta -1 dla coveringa zeby zgadzaly sie indeksy
            deskryptor.put(nrAtrybutu, wartoscAtrybutu);
        }

    }



    public List<Integer> obiektySpelniajaceRegule(String[][] system)     //   obiektySupportu
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
            if(!deskr.getValue().equals(obiekt[deskr.getKey()])){    //   jak nie zgadzaja sie wartosci atrybutow to zwroc false    +  korekta -1 dla coveringu
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




    public Boolean powtarzajacaSieRegula(List<Regula> tymczasowaListaRegul){
        for(Regula r : tymczasowaListaRegul){
            if(r.deskryptor.equals(deskryptor) && r.decyzja.equals(decyzja)){   // jesli na liscie istnieje regula o takich samych deskryptorach i takiej samej decyzji to zwracam true
                return true;
            }
        }
        return false;
    }



    public Boolean czyRegulaZawieraRegule(Regula rWyzszegoRzedu, Regula rNizszegoRzedu){
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



    public Boolean czyRegulaZawieraJednaZRegul(List<Regula> listaRegulNizszegoRzedu){
        for(Regula rNizszegoRzedu : listaRegulNizszegoRzedu) {
            if (rNizszegoRzedu.deskryptor.size() > 1) {  // sprawdzenie czy rzad jest wiekszy od 1 (nie mozna brac pod uwage deskryptorow 1 rzedu)
                if (czyRegulaZawieraRegule(Regula.this, rNizszegoRzedu)) {
                    return true;
                }
            }
        }
        return false;
    }
}