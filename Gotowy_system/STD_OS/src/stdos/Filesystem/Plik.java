package stdos.Filesystem;
public class Plik {
    private String nazwa;
    private int rozmiar;
    private int indeks;

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
