package stdos;

import stdos.*;
import stdos.RAM.RAMModule;
import stdos.VM.VirtualMemory;
//TODO: import filesystem
import stdos.Processes.ProcessManager;
import stdos.CPU.CPU;
import stdos.Semaphore.semafor;
import stdos.Interpreter.Interpreter;
//TODO: import interface

public class main {
    //Priority 1 - RAM and Filesystem
    public RAMModule ram = new stdos.RAM.RAMModule();
    //TODO: Here will be constructor of the filesystem
    //Priority 2 - VM
    public VirtualMemory vm = new stdos.VM.VirtualMemory();
    //Priority 3 - Processes
    public ProcessManager pm = new stdos.Processes.ProcessManager();
    //Priority 4 - CPU
    public CPU cpu = new stdos.CPU.CPU();
    //Priority 5 - Semaphore
    //TODO: public semafor sem = new stdos.Semaphore.semafor();
    //Priority 6 - Interpreter
    public Interpreter inter =  new stdos.Interpreter.Interpreter();
    //Priority 7 - Interface
    //TODO: constructor Interface
}
