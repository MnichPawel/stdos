package stdos.Filesystem;
import stdos.Semaphore.JPmetody;

import java.util.Vector;

public class Pliki extends Plik{

    public static Vector<Plik> Files = new Vector<>();
    public static Vector<Plik> Open = new Vector<>();
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

    public static boolean czyPotw(String nazwa) {
        for (Plik e : Open) {
            if (e.Nazwa().equals(nazwa)) {
                return true;
            }
        }
        return false;
    }

    public static Plik KP_dlaJP(String nazwa) {
        for (Plik e : Files) {
            if (e.Nazwa().equals(nazwa)) {
                return e;
            }
        }
        return null;
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
                    if (czyPotw(nazwa)) {
                        e.setIndexBlock(Dysk.addContent(content, indeks));
                        e.UstRozm(content.length);
                        return;

                    }
                   else{
                       System.out.println("Plik " + nazwa + " nie jest otwarty");
                        return;
                    }
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
                if (czyPotw(nazwa)) {
                    byte[] a =Dysk.getBlockByIndex(e.getIndexBlock());
                    return a;
                }
                else{
                    System.out.println("Plik " + nazwa + " nie jest otwarty");
                    return null;
                }
            }
        }
        System.out.println("Nie ma takiego pliku");
        return Dysk.invalid();
    }


    public void KP_pokP(){
        for(Plik e: Files){
            System.out.println("\t" + e.Rozm()+ "\t" + e.Nazwa() );
        }
    }


    public void KP_usunP(String nazwa){
        for (Plik e : Files){
            if (e.Nazwa().equals(nazwa)){
                //if (czyPotw(nazwa)) {
                    Dysk.remove(e.getIndexBlock());
                    Files.remove(e);
                    return;

               // }
                //else{
                    //System.out.println("Plik " + nazwa + " nie jest otwarty");
                    //return;
               // }

            }
        }
        System.out.println("Brak pliku o nazwie: " + nazwa);
    }
    public void KP_otwP(String nazwa) {
        for (Plik p : Files) {
            if (p.Nazwa().equals(nazwa)) {
                JPmetody.wait(p.sem);
                Open.add(p);
            }
        }
    }
    public void KP_zamkP(String nazwa) {
        for (Plik p : Files) {
            if (p.Nazwa().equals(nazwa)) {
                JPmetody.signal(p.sem);
                Open.remove(p);
            }
        }

    }
}