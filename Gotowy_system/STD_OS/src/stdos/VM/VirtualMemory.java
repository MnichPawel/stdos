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
        reservation = RAMModule.zarezerwuj_pamiec(code.length());

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
        //int runningID = stdos.CPU.CPU.MM_getRUNNING().getPid();
        int runningID = 0;

        Pair pair = addressTable.get(runningID);
        if((boolean)pair.getKey()) {
            pair = (Pair) pair.getValue();
            if((int)pair.getKey() == address) {
                byte[] val = RAMModule.odczytaj_bajty((Integer) pair.getValue(), 2);
                short true_val = val[1];
                if(true_val < 0) true_val += 256;
                return true_val;
            }
        }
        else {
            pair = (Pair) pair.getValue();
            short val = (short) pair.getValue();
            return val;
        }
        return -1;
    }

    public static byte get_value(int address) {
        //int runningID = stdos.CPU.CPU.MM_getRUNNING().getPid();
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

    public static void set_value(int address, short value) {
        //int running = stdos.CPU.CPU.MM_getRUNNING().getPid();
        int running = 0;

        byte[] val = new byte[2];
        val[0] = (byte)(address);
        val[1] = (byte)(value & 0xff);

        int reservation = RAMModule.zarezerwuj_pamiec(2);
        if(reservation != -1) {
            RAMModule.zapisz_bajty(val, reservation);
            addressTable.put(running, new Pair<>(true, new Pair<>(address, reservation)));
        }
        else {
            SegmentFile.put(running*-1, val);
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
        if(!addressTable.isEmpty()) {
            System.out.println("\nPID\tADDR\tVALUE");
            Pair pair = null;
            int PID = -1;
            for(Map.Entry<Integer, Pair<Boolean, Pair<Integer, Integer>>> entry : addressTable.entrySet()) {
                System.out.print(entry.getKey()*-1 + "\t");
                pair = entry.getValue();
                if((boolean)pair.getKey()) {
                    pair = (Pair) pair.getValue();
                    System.out.print(((int)pair.getKey())+"\t");
                    System.out.println(get_value_from_addr_table((int)pair.getKey()));
                }
                else {
                    pair = (Pair) pair.getValue();
                    byte[] arr = SegmentFile.get(entry.getKey());
                    int addr = (arr[0] & 0xFF);
                    System.out.print(addr + "\t");
                    byte[] temp = new byte[2];
                    temp[0] = arr[1];
                    temp[1] = arr[2];
                    ByteBuffer byteBuffer = ByteBuffer.wrap(temp);
                    int val = byteBuffer.getInt();
                    System.out.println(val);
                }
            }
        }
    }

    public static void displayRAM() {
        RAMModule.wypisz_pamiec();
    }

    public static void displayBinaryTree() {
        RAMModule.wypisz_podzial();
    }
}