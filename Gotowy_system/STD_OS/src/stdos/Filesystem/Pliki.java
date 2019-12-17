package stdos.Filesystem;
import stdos.Semaphore.JPmetody;

import java.util.Arrays;
import java.util.Vector;

public class Pliki extends Plik{

    public static Vector<Plik> Files = new Vector<>();

    public Pliki(String nazwa) {
        super(nazwa);
    }

    public Pliki() {

    }

    public static boolean czyPjest(String nazwa) {
        for (Plik e : Files) {
            if (e.Nazwa().equals(nazwa)) {
                return true;
            }
        }
        return false;
    }



    public void KP_utwP(String nazwa) {
        if (czyPjest(nazwa)) {
            System.out.println("Plik " + nazwa + " już istnieje");
            return;
        }
        if (nazwa.equals("")) {
            System.out.println("Podaj nazwe pliku");
            return;
        }
        Plik newP = new Plik(nazwa);
        byte[]content={};
        newP.setIndexBlock(Dysk.addContent(content, indeks));
        newP.UstRozm(content.length);
        Files.add(newP);
    }

    public void KP_dopP(String nazwa, byte[] content) {
        if (czyPjest(nazwa)) {
            for (Plik e : Files) {
                if (e.Nazwa().equals(nazwa)) {
                    otwP(e);
                    e.setIndexBlock(Dysk.addContent(content, indeks));
                    e.UstRozm(content.length);
                    zamkP(e);
                    return;
                }
            }
        }
        if (nazwa.equals("")) {
            System.out.println("Podaj nazwe pliku");
            return;
        }
    }

    public byte[] KP_pobP(String nazwa) {
        for (Plik e : Files) {
            if (e.Nazwa().equals(nazwa)) {
                otwP(e);
                byte[] a =Dysk.getBlockByIndex(e.getIndexBlock());
                zamkP(e);
                return a;
            }
        }
        System.out.println("Nie ma takiego pliku");
        return Dysk.invalid();
    }


    public void KP_pokP(){
        for(Plik e: Files){
            otwP(e);
            System.out.println("\t" + e.Rozm()+ "\t" + e.Nazwa() );
            zamkP(e);
        }
    }


    public void KP_usunP(String nazwa){
        for (Plik e : Files){
            if (e.Nazwa().equals(nazwa)){
                otwP(e);
                Dysk.remove(e.getIndexBlock());
                Files.remove(e);
                zamkP(e);
                return;
            }
        }
        System.out.println("Brak pliku o nazwie: " + nazwa);
    }
}