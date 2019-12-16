package stdos;

import stdos.*;
import stdos.Filesystem.Katalogi;
import stdos.Interface.Interface;
import stdos.RAM.RAMModule;
import stdos.VM.VirtualMemory;
//TODO: import filesystem
import stdos.Processes.ProcessManager;
import stdos.CPU.CPU;
import stdos.Semaphore.semafor;
import stdos.Interpreter.Interpreter;

import java.io.*;

public class runSystem {
        private static final String PathToDiskWin = "Gotowy_system/STD_OS/src/stdos/Disk/";
        //private static final String[] INITIAL_PROGRAMS = new String[] {"dummy.txt", "prog1.txt"};
        private static final String PathForFilesFS = "C:";
        private static final String[] allowedExtension = new String[] {".txt", ".exe", "txt", "exe"};

        private static String getFileExtension(String filename) {
            String extension = "";
            int i = filename.lastIndexOf('.');
            if (i > 0) {
                extension = filename.substring(i+1);
            }
            return extension;
        }

        private static boolean checkFileExtensionFS(String extension) {
            for(String ext:allowedExtension) {
                if(extension.equalsIgnoreCase(ext)) {
                    return true;
                }
            }
            return false;
        }

        private static void loadProgramsToFilesystem() {
            Katalogi.getDir().KP_utwK(PathForFilesFS);
            Katalogi.setCurrentDir(PathForFilesFS);

            File diskWin = new File(PathToDiskWin);

            File[] diskWinFiles = diskWin.listFiles();
            for (File f : diskWinFiles) {
                if(!checkFileExtensionFS(getFileExtension(f.getName()))) {
                    continue;
                }
                String fileContent = "";
                try {
                    BufferedReader br = new BufferedReader(new FileReader(PathToDiskWin + f.getName()));
                    while (true) {
                        String currentLine = br.readLine();
                        if (currentLine != null)  {
                            fileContent = fileContent + currentLine + "\n";
                        }
                        else {
                            break;
                        }
                    }
                    br.close();
                } catch (FileNotFoundException e) {
                    e.getMessage();
                } catch (IOException e) {
                    e.getMessage();
                }
                Katalogi.getCurrentDir().getFiles().KP_utwP(f.getName());
                Katalogi.getCurrentDir().getFiles().KP_dopP(f.getName(), fileContent.getBytes());
            }
        }

        public static void main(String[] args){
            //Priority 1 - RAM and Filesystem
            RAMModule ram = new stdos.RAM.RAMModule();
            loadProgramsToFilesystem();
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

            //Load programs to memory



        }
    }
