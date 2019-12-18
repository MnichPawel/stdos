package stdos.RAM;

import java.util.Arrays;
import java.util.Stack;

class Node{
    private static int minimal_size = 2;

    private boolean reserved = false;
    private boolean divided = false;

    public int size;
    public int start;

    public Node left_node;
    public Node right_node;

    public Node(int _size, int _start){
        size = _size;
        start = _start;
    }

    public boolean is_divided(){
        return divided;
    }

    public void divide(){
        left_node = new Node(size / 2, start);
        right_node = new Node(size / 2, start + size / 2);
        divided = true;
    }

    public void merge(){
        left_node = null;
        right_node = null;
        divided = false;
    }

    public void reserve(){ reserved = true; }
    public void free(){ reserved = false; }
    public boolean is_reserved(){ return reserved; }

    int find_free_place(int searched_size){
        if(is_reserved()){
            return -1;
        }

        if(size < searched_size){
            return -1;
        }

        if(size < searched_size * 2 || size == minimal_size){
            if(divided) {
                return -1;
            }

            reserve();
            return start;
        } else {
            if(!divided){
                divide();
            }

            int found_left_node = left_node.find_free_place(searched_size);
            if(found_left_node != -1){
                return found_left_node;
            }

            int found_right_node = right_node.find_free_place(searched_size);
            if(found_right_node != -1){
                return found_right_node;
            }
        }

        return -1;
    }

    void free_place(int address){
        if(divided){
            if(address >= right_node.start){
                right_node.free_place(address);
            } else {
                left_node.free_place(address);
            }
        } else {
            if (address == start) {
                free();
                return;
            }
        }


        if(divided){
            if(!right_node.is_reserved() && !left_node.is_reserved() && !right_node.divided && !left_node.divided){
                merge();
            }
        }
    }

    void print_division(){
        if(!divided){
            String status;
            if(reserved){
                status = "Zarezerwowany";
            }else{
                status = "Wolny";
            }
            System.out.println("Od: " + start + ", rozmiar: " + size + ", " + status);
        }else{
            left_node.print_division();
            right_node.print_division();
        }
    }
}

class BinaryTree {
    private Node main_node;

    public BinaryTree(int size){
        main_node = new Node(size, 0);
    }

    public int reserve_place_for_memory(int size){
        return main_node.find_free_place(size);
    }

    public void free_place_from_memory(int address){
        main_node.free_place(address);
    }

    public void print_division(){
        main_node.print_division();
    }
}

public class RAMModule {
    static final int RAM_SIZE = 256;
    static byte[] RAM;
    static BinaryTree binary_tree;

    public RAMModule(){
        RAM = new byte[RAM_SIZE];
        Arrays.fill(RAM, (byte)0);
        binary_tree = new BinaryTree(RAM_SIZE);
    }

    /* rezerwuje i zwraca adres w którym zaczyna się blok przydzielonej pamięci*/
    public static int zarezerwuj_pamiec(int rozmiar){
        return binary_tree.reserve_place_for_memory(rozmiar);
    }

    /* rezerwuje pamiec zarezerwowana pod adresem */
    public static void zwolnij_pamiec(int adres){
        binary_tree.free_place_from_memory(adres);
    }

    /* zapisuje bajt na podanym adresie */
    void zapisz_bajt(byte wartosc, int adres){
        RAM[adres] = wartosc;
    }
    void zapisz_bajt(byte wartosc, int adres_logiczny, int adres_fizyczny) { RAM[adres_logiczny + adres_fizyczny] = wartosc; }
    public static void zapisz_bajty(byte[] wartosc, int adres){
        for(int i = adres; i < wartosc.length + adres; ++i) {
            RAM[i] = wartosc[i - adres];
        }
    }
    public static void zapisz_bajty(byte[] wartosc, int adres_logiczny, int adres_fizyczny) {
        for(int i = adres_fizyczny + adres_logiczny; i < wartosc.length + adres_fizyczny + adres_logiczny; ++i) {
            RAM[i] = wartosc[i - (adres_fizyczny + adres_logiczny)];
        }
    }


    /* zwraca wartość bajtu z podanego adresu */
    byte odczytaj_bajt(int adres){
        return RAM[adres];
    }
    public static byte odczytaj_bajt(int adres_logiczny, int adres_fizyczny) { return RAM[adres_logiczny + adres_fizyczny]; }
    public static byte[] odczytaj_bajty(int adres, int rozmiar){
        byte ret[] = new byte[rozmiar];

        for(int i = 0; i < rozmiar; ++i){
            ret[i] = RAM[adres + i];
        }

        return ret;
    }
    public static byte[] odczytaj_bajty(int adres_logiczny, int adres_fizyczny, int rozmiar) {
        byte ret[] = new byte[rozmiar];

        for(int i = 0; i < rozmiar; ++i){
            ret[i] = RAM[adres_logiczny + adres_fizyczny + i];
        }

        return ret;
    }

    /* wypisuje zawartość ramu */
    public static void wypisz_pamiec(){
        for(int x = 0; x < RAM_SIZE; x += 16) {
            for (int i = 0; i < 16; ++i) {
                System.out.print(String.format("%02X", RAM[i + x]) + " ");
            }
            System.out.println();
        }
    }

    public static void wypisz_pamiec_char(){
        for(int x = 0; x <RAM_SIZE; x += 16){
            for(int i = 0; i < 16; ++i){
                System.out.print((char)RAM[i + x]);
            }
            System.out.println();
        }

    }

    /* wypisuje podział */
    public static void wypisz_podzial(){
        binary_tree.print_division();
    }
}
