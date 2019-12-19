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
    static Map<Pair<Integer, Integer>, Pair<Boolean, Integer>> addressTableFinal = new HashMap<>(); //Pair<PID, ADDR_LOG>, Pair<RAM TAK/NIE, ADDR_PHY>

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
            if(!addressTableFinal.isEmpty()) {
                Vector<Pair<Integer, Integer>> to_delete = new Vector<>();
                for (Map.Entry<Pair<Integer, Integer>, Pair<Boolean, Integer>> entry : addressTableFinal.entrySet()) {
                    Pair pair = entry.getKey();

                    if ((Integer) pair.getKey() == PID) {
                        Pair pair_data = entry.getValue();
                        if ((boolean) pair_data.getKey()) {
                            RAMModule.zwolnij_pamiec((Integer) pair_data.getValue());
                        }

                        to_delete.add(entry.getKey());
                    }
                }
                for(Pair<Integer, Integer> pair : to_delete) {
                    addressTableFinal.remove(pair);
                }
            }
            VM.remove(PID);
        }
    }

    /*===============================/PROCESY=================================*/

    /*===============================ASEMBLER=================================*/
    public static short get_value_from_addr_table(int address) {
        int runningID = stdos.CPU.CPU.MM_getRUNNING().getPid();

        Pair pair;
        for(Map.Entry<Pair<Integer, Integer>, Pair<Boolean, Integer>> entry : addressTableFinal.entrySet()) {
            pair = entry.getKey();
            if((int)pair.getKey() == runningID && (int)pair.getValue() == address) {
                pair = addressTableFinal.get(pair);
                if((boolean)pair.getKey()) {
                    byte[] val = RAMModule.odczytaj_bajty((Integer) pair.getValue(), 2);

                    short true_val = val[1];
                    if(true_val < 0) true_val += 256;
                    return true_val;
                }
                else {
                    short val = (short) pair.getValue();
                    return val;
                }
            }
        }
        return -1;
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
        val[0] = (byte)(address);
        val[1] = (byte)(value & 0xff);

        int reservation = RAMModule.zarezerwuj_pamiec(2);
        if(reservation != -1) {
            RAMModule.zapisz_bajty(val, reservation);
            addressTableFinal.put(new Pair<>(running, address), new Pair<>(true, reservation));
        }
        else {
            SegmentFile.put(running*-1, val);
            addressTableFinal.put(new Pair<>(running, address), new Pair<>(false, (int)value));
        }
    }
    /*===============================/ASEMBLER=================================*/
    

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
        if(!addressTableFinal.isEmpty()) {
            System.out.println("\nPID\t\tADDR\t\tVALUE");
            Pair pair = null;
            int PID = -1;
            for(Map.Entry<Pair<Integer, Integer>, Pair<Boolean, Integer>> entry : addressTableFinal.entrySet()) {
                pair = entry.getKey();
                System.out.print(pair.getKey() + "\t\t" + pair.getValue() + "\t\t");
                pair = entry.getValue();
                if((boolean)pair.getKey()) {
                    Pair pair_addr = entry.getKey();
                    System.out.println(get_value_from_addr_table((int)pair_addr.getValue()));
                }
                else {
                    System.out.println(pair.getValue());
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

    public static void displaySegmentFile() {
        for(Map.Entry<Integer, byte[]> entry : SegmentFile.entrySet()) {
            for(byte b : entry.getValue()) {
                System.out.print(b+" ");
            }
            System.out.println();
        }
    }
}