package stdos;

import stdos.Filesystem.LoadFiles;
import stdos.Interface.Interface;
import stdos.RAM.RAMModule;
import stdos.VM.VirtualMemory;
//TODO: import filesystem
import stdos.Processes.ProcessManager;
import stdos.CPU.CPU;
import stdos.Interpreter.Interpreter;

public class runSystem {

        public static void main(String[] args){
            //Priority 1 - RAM and Filesystem
            RAMModule ram = new stdos.RAM.RAMModule();
            /*load files to system*/
            LoadFiles.loadProgramsToFilesystem();
            //TODO: Here will be constructor of the filesystem
            //Priority 2 - VM
            VirtualMemory vm = new stdos.VM.VirtualMemory();
            //Priority 3 - Processes
            ProcessManager pm = new stdos.Processes.ProcessManager();
            //Priority 4 - CPU
            CPU cpu = new stdos.CPU.CPU();
            //Priority 5 - Semaphore
            //TODO: public semafor sem = new stdos.Semaphore.semafor();
            //Priority 6 - Interpreter
            Interpreter inter = new stdos.Interpreter.Interpreter();
            //Priority 7 - Interface
            Interface _interface = new Interface();

        }
    }
