//#TODO: DISPLAY ADDRESS_TABLE IN DISPLAY() METHOD

package stdos.VM;

import stdos.RAM.RAMModule;
import javafx.util.Pair;

import java.nio.ByteBuffer;
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

    //mapa logiczna adresow
    static Map<Integer, Pair<Boolean, Pair<Integer, Integer>>> addressTable = new HashMap<>(); //pid flaga 1-ram 0-file adr_log adr_fiz

    /*===============================PROCESY=================================*/
    public static void load_to_virtualmemory(int PID, String code) {
        //przerobienie kodu na bajty
        byte[] bytes_code = new byte[code.length()];

        boolean flag_space = true;
        boolean flag = false; //true - in RAM, false - in SegmentFile

        int reservation = -1;
        reservation = RAMModule.zarezerwuj_pamiec(code.length() - 1);

        /*for(int i=0; i<code.length(); i++) {
            if(code.charAt(i) == ' ') {
                if(flag_space) {
                    flag_space = false;
                    i++;
                }
            }
            if(flag_space)
                bytes_code[i] = (byte) code.charAt(i);
            else
                bytes_code[i-1] = (byte) code.charAt(i);
        }*/
        //Comment by KM
        for(int i=0; i<code.length(); i++) {
            bytes_code[i] = (byte) code.charAt(i);
        }
        //For loop by KM

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
            if(addressTable.containsKey(PID)) {
                Pair pair = addressTable.get(PID);
                boolean place = (boolean) pair.getKey();
                if (place) {
                    Pair temp = (Pair) pair.getValue();
                    RAMModule.zwolnij_pamiec((Integer) temp.getValue());
                }
                addressTable.remove(PID);
            }
            VM.remove(PID);
        }
    }

    /*===============================/PROCESY=================================*/

    /*===============================ASEMBLER=================================*/
    public static short get_value_from_addr_table(int address) {
        int runningID = stdos.CPU.CPU.MM_getRUNNING().getPid();

        Pair pair = addressTable.get(runningID);
        if((boolean)pair.getKey()) {
            pair = (Pair) pair.getValue();
            if((int)pair.getKey() == address) {
                short val = RAMModule.odczytaj_bajt(0,(Integer) pair.getValue());
                if(val < 0) val += 256;
                return val;
            }
        }
        else {
            pair = (Pair) pair.getValue();
            short val = (short) pair.getValue();
            return val;
        }
        return Short.parseShort(null);
    }

    public static byte get_value(int address) {
        int runningID = stdos.CPU.CPU.MM_getRUNNING().getPid();

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

    public static void set_value(int address, short value) {
        int running = stdos.CPU.CPU.MM_getRUNNING().getPid();

        byte[] val = new byte[2];
        val[0] = (byte)(value & 0xff);
        val[1] = (byte)((value >> 8) & 0xff);

        int reservation = RAMModule.zarezerwuj_pamiec(1);
        if(reservation != -1) {
            RAMModule.zapisz_bajty(val, reservation);
            addressTable.put(running, new Pair<>(true, new Pair<>(address, reservation)));
        }
        else {
            SegmentFile.put(running, val);
            int val_int = Short.toUnsignedInt(value);
            addressTable.put(running, new Pair<>(false, new Pair<>(null, val_int)));
        }
    }
    /*===============================/ASEMBLER=================================*/

    public static void erase() {
        for(Map.Entry<Integer, Vector<Segment>> entry : segmentTable.entrySet()) {
            for(Segment segment : entry.getValue()) {
                RAMModule.zwolnij_pamiec(segment.beginAddress);
            }
        }
        for(Map.Entry<Integer, Pair<Boolean, Pair<Integer, Integer>>> entry : addressTable.entrySet()) {
            Pair pair = entry.getValue();
            boolean place = (boolean) pair.getKey();
            if(place) {
                Pair temp = (Pair) pair.getValue();
                RAMModule.zwolnij_pamiec((Integer) temp.getValue());
            }
        }
        addressTable.clear();
        SegmentFile.clear();
        segmentTable.clear();
        VM.clear();
        System.out.println("Wyczyszczono pamięć wirtualną");
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
        System.out.println("\nPID\tADDR\tVALUE");
    }

    public static void displayRAM() {
        RAMModule.wypisz_pamiec();
    }

    public static void displayBinaryTree() {
        RAMModule.wypisz_podzial();
    }
}