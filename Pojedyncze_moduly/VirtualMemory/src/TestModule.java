import VirtualMemory.VirtualMemory;

public class TestModule {
    public static void main(String[] args) {
        VirtualMemory virtualMemory = new VirtualMemory();

        VirtualMemory.load_to_virtualmemory(1, "AD AX [1001]");
        System.out.println("ADD TO MEMORY: AD AX [1001]");
        VirtualMemory.display();
        VirtualMemory.displayRAM();
        VirtualMemory.displayBinaryTree();
        VirtualMemory.load_to_virtualmemory(2, "AD BX 100 AD AX 101 AD AX BX");
        System.out.println("AD BX 100 AD AX 101 AD AX BX");
        VirtualMemory.display();
        VirtualMemory.displayRAM();
        VirtualMemory.displayBinaryTree();
        int value = (int)VirtualMemory.get_value(1004) & 0xFF;
        String output = String.format("%X", value);
        System.out.println(output);
        System.out.println((char)value + "\n\n\n\n\n");

        VirtualMemory.remove_from_virtualmemory(1);
        VirtualMemory.display();
        VirtualMemory.displayRAM();
        VirtualMemory.displayBinaryTree();

        VirtualMemory.erase();
        VirtualMemory.display();
        VirtualMemory.displayRAM();
        VirtualMemory.displayBinaryTree();
    }
}
