package stdos.VM;

import stdos.RAM.RAMModule;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class VirtualMemory {
    //obiekt pamięci RAM
    //static RAMModule RAM = new RAMModule();

    //mapa pamieci wirtualnej Integer to PID, wektor oznacza identyfikatory segmentow w tablicy segmentow
    static Map<Integer, Vector<Integer>> VM = new HashMap<>();

    //tablica segmentow
    static Vector<Segment> segmentTable = new Vector<>();

    /*===============================PROCESY=================================*/
    public static void load_to_virtualmemory(int PID, String code) {
        //przerobienie kodu na bajty
        byte[] bytes_code = new byte[2];
        byte[] bytes_value = new byte[code.length() - 2];

        Vector<Integer> keys = new Vector<>();

        int reservation = 0;

        for(int i=0; i<code.length(); i++) {
            if(i<2)
                bytes_code[i] = (byte)code.charAt(i);
            if(i>=2) {
                if(i==2)
                    reservation = RAM.zarezerwuj_pamiec(2);

                bytes_value[i-2] = (byte)code.charAt(i);
                if(i+1 == code.length())
                    reservation = RAM.zarezerwuj_pamiec(code.length()-2);

            }
            if(i == 2 || i+1 == code.length()) {
                if(i==2)
                    RAM.zapisz_bajty(bytes_code, reservation);
                if(i+1 == code.length())
                    RAM.zapisz_bajty(bytes_value, reservation);
                Segment segment = new Segment();
                segment.beginAddress = reservation;

                if(i==2)
                    segment.limit = 2;
                if(i+1 == code.length())
                    segment.limit = code.length()-2;

                segmentTable.add(segment);
                int key = segmentTable.indexOf(segment);
                keys.add(key);
            }
        }

        VM.put(PID, keys);
    }

    public static void remove_from_virtualmemory(int PID) {
        Vector<Integer> keys = VM.get(PID);
        VM.remove(PID);
        for(int i = 0; i<keys.size(); i++) {
            RAM.zwolnij_pamiec(segmentTable.get(keys.get((i))).beginAddress);
            segmentTable.remove(keys.get(i));
        }
    }

    /*===============================/PROCESY=================================*/

    /*===============================ASEMBLER=================================*/
    public static byte get_value(int address) {
        int nr_seg = address / 1000;
        int offset = address % 1000;
        Segment temp = segmentTable.get(nr_seg);
        if(temp.limit >= offset)
            return RAM.odczytaj_bajt(temp.beginAddress, offset);
        return -1;
    }
    /*===============================/ASEMBLER=================================*/

    public static void erase() {
        Vector<Integer> keys;
        for(Map.Entry<Integer, Vector<Integer>> entry : VM.entrySet()) {
            keys = entry.getValue();
            for(int j=0; j<keys.size(); j++) {
                RAM.zwolnij_pamiec(segmentTable.get(keys.get(j)).beginAddress);
            }
        }
        VM.clear();
        segmentTable.clear();
    }

    public static void display() {
        System.out.println("PID\tADDR_BEG\tLIMIT");
        for(Map.Entry<Integer, Vector<Integer>> entry : VM.entrySet()) {
            System.out.println(entry.getKey()+"\t");
            for(Integer SID : entry.getValue()) {
                System.out.println(segmentTable.get(SID).beginAddress + "\t" + segmentTable.get(SID).limit);
            }
            System.out.println();
        }
    }

    public static void displayRAM() {
        RAM.wypisz_pamiec();
    }

    public static void displayBinaryTree() {
        RAM.wypisz_podzial();
    }
}
