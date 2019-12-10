package VirtualMemory;

import RAMModule.RAMModule;

import java.util.Random;
import java.util.Vector;

public class VirtualMemory {
    public static void main(String[] args) {
        VirtualMemoryModule vm = new VirtualMemoryModule(new RAMModule());
        Random random = null;
        Vector<Vector<Byte>> code = new Vector<>(new Vector<>());

        Vector<Byte> abc = new Vector<>();
        for(int i=0; i<3; i++) {
            abc.clear();
            abc.add((byte)12);
            code.add(abc);
        }
        vm.createProcess(0,code);
        vm.createProcess(1,code);
        vm.createProcess(2,code);
        vm.displaySegmentsTableWithCodes();
        vm.removeProcess(0);
        vm.removeProcess(2);
        vm.displaySegmentsTableWithCodes();
    }
}
