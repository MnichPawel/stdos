package stdos.Filesystem;
import stdos.Semaphore.JPmetody;
import stdos.Semaphore.*;

public class Plik {
    private String nazwa;
    private int rozmiar;
    public int indeks;
    public semafor sem = new semafor(1);
    public Plik(String nazwa){
        this.nazwa = nazwa;

    }

    public Plik() {

    }

    public String Nazwa() {

        return nazwa;
    }

    public int Rozm() {
        return rozmiar;
    }

    public void UstRozm(int rozmiar) {
        this.rozmiar = rozmiar;
    }

    public int getIndexBlock() {
        return indeks;
    }

    public void setIndexBlock(int indeks) {
        this.indeks = indeks;
    }

}

