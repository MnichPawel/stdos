package stdos.Interface;

import stdos.CPU.CPU;
import stdos.Filesystem.Dysk;
import stdos.Filesystem.Katalog;
import stdos.Filesystem.Katalogi;
import stdos.Filesystem.Pliki;
import stdos.Processes.ProcessManager;
import stdos.RAM.RAMModule;
import stdos.Semaphore.JPmetody;
import stdos.Semaphore.semafor;
import stdos.VM.VirtualMemory;

import static stdos.Processes.ProcessManager.KM_printRegisters;

class SwitchInput {
    /*flaga kończąca działanie*/
        static boolean exitFlag = false;

        static void inputSwitch(String komunikat) throws Exception {
            String[] arguments =  komunikat.split("\\s+"); //one or more space space after (splits)

            switch (arguments[0]) {
                /*pamięć RAM*/
                case "ram":
                    VirtualMemory.displayRAM();
                    break;

                case "ram_mem":
                    RAMModule.wypisz_pamiec_char();
                    break;

                case "ram_part":
                    VirtualMemory.displayBinaryTree();
                    break;
                /*pamięć RAM*/

                /*pamięć wirtualna*/

                case "dvm":
                    VirtualMemory.display();
                    break;
                /*pamięć wirtualna*/
                case "dvm_file":
                    VirtualMemory.displaySegmentFile();
                break;
                /*semafor*/
                case "semstate":
                    JPmetody.JPwypisz(String.valueOf(arguments[1]));
                    break;
                case "queue":
                    JPmetody.JPwypiszKolejke(String.valueOf(arguments[1]));
                    break;
                /*semafor*/

                    /*zarządzanie procesami*/
                case "tc":
                    try {
                        ProcessManager.KM_CreateProcess(String.valueOf(arguments[1]), String.valueOf(arguments[2]), Integer.parseInt(arguments[3]));
                        System.out.println("Utworzono proces: "+ arguments[1] +" proces: "+ arguments[2] + " priorytet: " +arguments[3]);
                    }
                    catch(Exception e){
                        Sound.errorSound();
                        System.out.println("Blad Process Manager: polecenie taskcreate");
                        System.out.println("Tresc bledu: " + e.getMessage());
                    }
                    break;

                case "kill":
                    try {
                        ProcessManager.KM_TerminateProcess(String.valueOf(arguments[1]));
                    }
                    catch(Exception e){
                        Sound.errorSound();
                        System.out.println("Blad Process Manager: polecenie kill");
                        System.out.println("Tresc bledu: "  + e.getMessage());
                    }
                    break;

                case "rtasklist":
                    ProcessManager.KM_getReadyProcessListPrint();
                    break;
                case "tasklist":
                    ProcessManager.KM_getAllProcessListPrint();
                    break;
                /*zarządzanie procesami*/

                /*procesor*/
                case "task_exec":
                    CPU.MM_show_running();
                    break;

                case "prior_tasklist" :
                    CPU.MM_show_actual_priority();
                    break;

                    /*procesor*/

                /*interpreter*/
                case "step":
                    if(arguments.length==1){
                        try {
                            CPU.MM_go();
                        } catch (Exception e) {
                            Sound.errorSound();
                            System.out.println("Blad: polecenie step");
                            System.out.println("Tresc bledu: " + e.getMessage());
                        }
                    }
                    else{
                        for (int i=0;i < Integer.parseInt(arguments[1]); i++) {
                            try {
                                CPU.MM_go();
                            } catch (Exception e) {
                                Sound.errorSound();
                                System.out.println("Blad: polecenie step");
                                System.out.println("Tresc bledu: " + e.getMessage());
                            }
                        }
                    }
                    break;
                    /* wyświetlaj krokowo*/
                case "stepr": //jeden krok asemblera i wyświetlenie rejestrów
                    try {
                        CPU.MM_go();
                        ProcessManager.KM_printRunningRegisters();
                    } catch (Exception e) {
                        Sound.errorSound();
                        System.out.println("Blad: polecenie step");
                        System.out.println("Tresc bledu: " + e.getMessage());
                    }

                    break;
                    /*wyświetlenie aktualnych procesów */
                case "register":
                    KM_printRegisters(CPU.prevRUNNING);
                    break;

                   /*zarządzanie plikami i katalogami*/
                case "mkfile": //utworzenie pliku
                    Katalogi.getCurrentDir().getFiles().KP_utwP(String.valueOf(arguments[1]));
                    System.out.println("Uworzono plik o nazwie: "  + arguments[1]);
                    break;

                case "opnfile":
                    /*otwarcie pliku*/
                    Katalogi.getCurrentDir().getFiles().KP_otwP(String.valueOf(arguments[1]));
                    /*otwarcie pliku*/

                    /*pobranie zawartości pliku*/
                    byte[] code = Katalogi.getCurrentDir().getFiles().KP_pobP(String.valueOf(arguments[1]));
                    /*pobranie zawartości pliku*/

                   // System.out.println("Pobrano zawartosc pliku o nazwie: "  + arguments[1]);
                    System.out.print(stdos.Processes.ProcessManager.getStringFromByteArray(code));

                    /*zamkniecie pliku*/
                    Katalogi.getCurrentDir().getFiles().KP_zamkP(String.valueOf(arguments[1]));
                    /*zamkniecie pliku*/
                    break;

                case "dir":
                    Katalogi.getCurrentDir().getFiles().KP_pokP();
                    break;

                case "disk":
                    Dysk.show();
                    break;

                case "erase":
                    Katalogi.getCurrentDir().getFiles().KP_usunP(arguments[1]);
                    break;
                /*zarządzanie plikami i katalogami*/

                /*funkcje interfejsu*/
                case "exit":
                    Sound.exitSystem();
                    exitFlag = true;
                    break;
                case "help":
                    Help.help();
                    break;
                /*funkcje interfejsu*/

              default:
                    System.out.println("[Shell]: Niepoprawna komenda");
                    break;
            }

            System.out.println();
        }


}
