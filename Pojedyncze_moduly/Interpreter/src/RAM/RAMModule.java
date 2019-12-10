package RAM;

import java.util.Arrays;
import java.util.Stack;

public class RAMModule {
    final int RAM_SIZE = 128;
    byte RAM[];
    BinaryTree binary_tree;

    public RAMModule(){
        RAM = new byte[RAM_SIZE];
        Arrays.fill(RAM, (byte)0);
        binary_tree = new BinaryTree(RAM_SIZE);
    }

    /* rezerwuje i zwraca adres w którym zaczyna się blok przydzielonej pamięci*/
    public int zarezerwuj_pamiec(int rozmiar){
        return binary_tree.reserve_place_for_memory(rozmiar);
    }

    /* rezerwuje pamiec zarezerwowana pod adresem */
    public void zwolnij_pamiec(int adres){
        binary_tree.free_place_from_memory(adres);
    }

    /* zapisuje bajt na podanym adresie */
    void zapisz_bajt(byte wartosc, int adres){
        RAM[adres] = wartosc;
    }
    void zapisz_bajt(byte wartosc, int adres_logiczny, int adres_fizyczny) { RAM[adres_logiczny + adres_fizyczny] = wartosc; }
    public void zapisz_bajty(byte[] wartosc, int adres){
        for(int i = adres; i < wartosc.length + adres; ++i) {
            RAM[i] = wartosc[i - adres];
        }
    }
    void zapisz_bajty(byte[] wartosc, int adres_logiczny, int adres_fizyczny) {
        for(int i = adres_fizyczny + adres_logiczny; i < wartosc.length + adres_fizyczny + adres_logiczny; ++i) {
            RAM[i] = wartosc[i - (adres_fizyczny + adres_logiczny)];
        }
    }


    /* zwraca wartość bajtu z podanego adresu */
    public byte odczytaj_bajt(int adres){
        return RAM[adres];
    }
    public byte odczytaj_bajt(int adres_logiczny, int adres_fizyczny) { return RAM[adres_logiczny + adres_fizyczny]; }
    byte[] odczytaj_bajty(int adres, int rozmiar){
        byte ret[] = new byte[rozmiar];

        for(int i = 0; i < rozmiar; ++i){
            ret[i] = RAM[adres + i];
        }

        return ret;
    }
    byte[] odczytaj_bajty(int adres_logiczny, int adres_fizyczny, int rozmiar) {
        byte ret[] = new byte[rozmiar];

        for(int i = 0; i < rozmiar; ++i){
            ret[i] = RAM[adres_logiczny + adres_fizyczny + i];
        }

        return ret;
    }

    /* wypisuje zawartość ramu */
    public void wypisz_pamiec(){
        for(int x = 0; x < RAM_SIZE; x += 16) {
            for (int i = 0; i < 16; ++i) {
                System.out.print(String.format("%02X", RAM[i + x]) + " ");
            }
            System.out.println();
        }
    }

    /* wypisuje podział */
    public void wypisz_podzial(){
        binary_tree.print_division();
    }
}
