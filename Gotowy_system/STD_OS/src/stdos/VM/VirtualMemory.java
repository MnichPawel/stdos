package stdos.VM;

import stdos.RAM.RAMModule;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class VirtualMemory {
    //mapa pamieci wirtualnej Integer to PID, wektor oznacza identyfikatory segmentow w tablicy segmentow
    static Map<Integer, byte[]> SegmentFile = new HashMap<>();

    //tablica segmentow w RAM
    static Map<Integer, Vector<Segment>> segmentTable = new HashMap<>();

    //zawartosc pamieci wirtualnej
    static Map<Integer, Boolean> VM = new HashMap<>();

    //do utworzenia set_value albo mapa logiczna np że adres 2000 to adres 7
    //albo konsultacja z Pawlem dot. funkcji rezerwujaca podany adres

    /*===============================PROCESY=================================*/
    public static void load_to_virtualmemory(int PID, String code) {
        //przerobienie kodu na bajty
        byte[] bytes_code = new byte[code.length()];

        Boolean flag = false; //true - in RAM, false - in SegmentFile

        int reservation = -1;
        reservation = RAMModule.zarezerwuj_pamiec(code.length());

        for(int i=0; i<code.length(); i++) {
            bytes_code[i] = (byte) code.charAt(i);
        }
        //KONSULTACJA Z PAWŁEM
        if(reservation == -1) {
            SegmentFile.put(PID, bytes_code);
        }
        else {
            flag = true;
            RAMModule.zapisz_bajty(bytes_code, reservation);
            Segment segment = new Segment();
            segment.beginAddress = reservation;
            segment.limit = code.length();

            Vector<Segment> tempSegTable = new Vector<>();
            tempSegTable.add(segment);
            segmentTable.put(PID, tempSegTable);
        }
        VM.put(PID, flag);
    }

    public static void remove_from_virtualmemory(int PID) {
        if(VM.containsKey(PID)) {
            if(VM.get(PID)) {
                //usuwanie z RAM
                Vector<Segment> segmentRemoveVec = new Vector<>();
                segmentRemoveVec = segmentTable.get(PID);
                for(Segment seg : segmentRemoveVec) {
                    RAMModule.zwolnij_pamiec(seg.beginAddress);
                }
                segmentTable.remove(PID);
            }
            else {
                //usuwanie z pliku
                SegmentFile.remove(PID);
            }
            VM.remove(PID);
        }
    }

    /*===============================/PROCESY=================================*/

    /*===============================ASEMBLER=================================*/
    public static byte get_value(int address) {
        //int runningID = CPU.RUNNING.getPid();
        int runningID = 0;

        if(VM.get(runningID)) {
            Vector<Segment> tempGVVector = new Vector<>();
            tempGVVector = segmentTable.get(runningID);
            if(tempGVVector.get(0).limit >= address) {
                return RAMModule.odczytaj_bajt(tempGVVector.get(0).beginAddress, address);
            }
        }
        else {
            return SegmentFile.get(runningID)[address];
        }
        return -1;
    }

    public static void set_value(int address, int value) {
        //dodanie wektora wartosci do segmntu procesu itp
    }
    /*===============================/ASEMBLER=================================*/

    public static void erase() {
        for(Map.Entry<Integer, Vector<Segment>> entry : segmentTable.entrySet()) {
            for(Segment segment : entry.getValue()) {
                RAMModule.zwolnij_pamiec(segment.beginAddress);
            }
        }

        SegmentFile.clear();
        segmentTable.clear();
        VM.clear();

        System.out.println("Wyczyszczono segmenty pamieci wirtualnej");
    }

    public static void display() {
        System.out.println("PID\tCODE");
        for(Map.Entry<Integer, Boolean> entry : VM.entrySet()) {
            System.out.print(entry.getKey()+"\t");
            if(entry.getValue()) {
                int beg, limit;
                beg = segmentTable.get(entry.getKey()).get(0).beginAddress;
                limit = segmentTable.get(entry.getKey()).get(0).limit;
                byte[] ram = RAMModule.odczytaj_bajty(beg, limit);
                for (byte b : ram) {
                    System.out.print(b + " ");
                }
            }
            else {
                byte[] file = SegmentFile.get(entry.getKey());

                for(byte b : file) {
                    System.out.print(b + " ");
                }
            }
            System.out.println();
        }
    }

    public static void displayRAM() {
        RAMModule.wypisz_pamiec();
    }

    public static void displayBinaryTree() {
        RAMModule.wypisz_podzial();
    }
}