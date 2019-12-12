package KP;
import java.util.Arrays;
import java.util.Vector;

public class Pliki extends Plik{

    private Vector<Plik> Files = new Vector<>();

    public Pliki(String nazwa) {
        super(nazwa);
    }

    public Pliki() {

    }

    public boolean czyPjest(String nazwa) {
        for (Plik e : Files) {
            if (e.Nazwa().equals(nazwa)) {
                return true;
            }
        }
        return false;
    }



    public void KP_utwP(String nazwa, byte[] content) {
        if (czyPjest(nazwa)) {
            System.out.println("Plik " + nazwa + " już istnieje");
            return;
        }
        if (nazwa.equals("")) {
            System.out.println("Podaj nazwe pliku");
            return;
        }
        Plik newP = new Plik(nazwa);
        newP.setIndexBlock(Dysk.addContent(content, 10));
        newP.UstRozm(content.length);
        Files.add(newP);
    }

    public byte[] KP_pobP(String nazwa) {
        for (Plik e : Files) {
            if (e.Nazwa().equals(nazwa)) {
                return Dysk.getBlockByIndex(e.getIndexBlock());
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
                Dysk.remove(e.getIndexBlock());
                Files.remove(e);
                return;
            }
        }
        System.out.println("Brak pliku o nazwie: " + nazwa);
    }

}