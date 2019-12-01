package VirtualMemory;

import RAM.*;
import Process.*;

import java.util.*;

public class VirtualMemoryModule {
    static RAMModule RAM_Module;

    //kolejka do kolejnego procesu - brak implementacji nie moglem wpasc na pomysl jak to zroibc

    //zawartosc pamieci wirtualnej
    static SegmentClass[] VM_CONTAINER = new SegmentClass[2];

    //tablica segmentow - kody
    static Map<Integer, Vector<Vector<Byte>>> Segments_Codes = new HashMap<>();

    //tablica segmentow - wszystkich ktore byly wykonywane w programie
    public static Map<Integer, SegmentClass> Segments_Table = new HashMap<>();

    //tablica sluzaca do okreslenia co znajduje sie aktualnie w pamieci RAM
    //32 indeksy poniewaz rozmiar RAM to 128 a minimalny rozmiar wpisu to 4, czyli mozemy wpisanc 32 elementy do ramu
    static RAMContainer[] RAM_Status = new RAMContainer[32];

    public VirtualMemoryModule(RAMModule RAM_Module) {
        VirtualMemoryModule.RAM_Module = RAM_Module;
        VM_CONTAINER[0] = new SegmentClass();
        VM_CONTAINER[1] = new SegmentClass();
    }

    public void createProcess(int pId, Vector<Vector<Byte>> code) {
        ProcessClass processClass = new ProcessClass(pId, code);
        SegmentClass segmentClass = putProcessInSegment(processClass);
        if(!Segments_Table.containsKey(pId))
            putSegmentInSegmentsTable(segmentClass);
        if(!Segments_Codes.containsKey(pId))
        putSegmentCodeInSegmentsCodesMap(pId, code);
        //rezerwacja w RAM
        //this.RAM_Module.zarezerwuj_pamiec(code.size());
        //zapisanie rezerwacji na odpowiednie indeksy do RAM_Status
        //konsultacja z Pawlem na spotkaniu

        //zaladowanie procesu do pamieci wirtualnej
        putSegmentInVirtualMemory(segmentClass);
    }

    private void putSegmentInVirtualMemory(SegmentClass segmentClass) {
        for (int i = 0; i < 2; i++) {
            if (!VM_CONTAINER[i].pVictim) {
                segmentClass.pInVM = true;
                segmentClass.pVictim = true;
                VM_CONTAINER[i] = segmentClass;
                break;
            } else {
                //dodaj do kolejki
                continue;
            }
        }
    }

    private void putSegmentCodeInSegmentsCodesMap(int pId, Vector<Vector<Byte>> code) {
        Segments_Codes.put(pId, code);
    }

    private void putSegmentInSegmentsTable(SegmentClass segmentClass) {
        Segments_Table.put(segmentClass.pId, segmentClass);
    }

    private SegmentClass putProcessInSegment(ProcessClass processClass) {
        SegmentClass segmentClass = new SegmentClass();
        segmentClass.pId = processClass.pId;
        return segmentClass;
    }

    //metoda usuwajaca proces ze wszystkich kontenerow
    public void removeProcess(int pId) {
        //usuniecie z kolejki - brak implementacji

        //zwolnienie z ramu - dogadanie z ramem

        if(VM_CONTAINER[0].pId == pId) {
            SegmentClass segmentClassTemp = VM_CONTAINER[0];
            VM_CONTAINER[0] = new SegmentClass();
            //lub dodanie kolejnego z kolejki
        }
        else if(VM_CONTAINER[1].pId == pId) {
            VM_CONTAINER[1] = new SegmentClass();
            //lub dodanie kolejnego z kolejki
        }
        Segments_Codes.remove(pId);
        Segments_Table.remove(pId);
    }

    public void displaySegmentsTableWithCodes() {
        System.out.println("SEGMENTS TABLE:");
        System.out.println("PID\tIN VM\tVICTIM\tCODE");

        for(Map.Entry<Integer, SegmentClass> entry : Segments_Table.entrySet()) {
            SegmentClass temp = entry.getValue();
            Vector<Vector<Byte>> tempVec = Segments_Codes.get(temp.pId);
            System.out.print(entry.getKey() + "\t" + temp.pInVM + "\t" + temp.pVictim + "\t");
            for(Vector<Byte> a:tempVec) {
                for(Byte b : a) {
                    System.out.print(b + " ");
                }
            }
            System.out.println();
        }
    }
}
