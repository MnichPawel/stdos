class SwitchInput {

        static boolean exitFlag = false;

        static void inputSwitch(String komunikat){
            String[] arguments =  komunikat.split("\\s+"); //one or more space space after (splits)

            switch (arguments[0]) {
//                /*pamięć RAM*/
//                case "ram_hex":
//                    PAMIEC_RAM.wypisz_pamiec();
//                    break;
//                case "ram_part":
//                    PAMIEC_RAM.wypisz_podzial();
//                    break;
//                /*pamięć RAM*/
//
//                /*pamięć wirtualna*/
//                case "ret":
//                    PAMIEC_WIRTUALNA.vm_find(Integer.parseInt(arguments[1]));
//                    break;
//                case "erasevm":
//                    PAMIEC_WIRTUALNA.vm_erase();
//                    break;
//                case "dvm":
//                    PAMIEC_WIRTUALNA.vm_display();
//                    break;
//                /*pamięć wirtualna*/
//
//                /*semafor*/
//                case "semstate":
//                    SEMAFOR.jp_display(String.valueOf(arguments[1]));
//                    break;
//                case "queue":
//                    SEMAFOR.jp_display_queue();
//                    break;
//                /*semafor*/
//
//                /*zarządzanie procesami*/
//                case "taskcreate":
//                    ZARZADZANIE_PROCESAMI.KM_CreateProcess(String.valueOf(arguments[1]), Integer.parseInt(arguments[2]);
//                    break;
//                case "kill":
//                    ZARZADZANIE_PROCESAMI.KM_TerminateProcess(String.valueOf(arguments[1]);
//                    break;
//                case "rdy_tasklist":
//                    ZARZADZANIE_PROCESAMI.KM_getReadyProcessListPrint();
//                    break;
//                case "tasklist":
//                    ZARZADZANIE_PROCESAMI.KM_getAllProcessListPrint();
//                    break;
//                /*zarządzanie procesami*/
//
//                /*procesor*/
//                case "task_exec":
//                    PROCESOR.mm_show_running();
//                    break;
//
//                case "rtasklist" :
//                   //? nie zarządzanie procesami? PROCESOR.mm_show_actual_priority();
//                    break;
//                /*procesor*/
//
//                /*interpreter*/
//                case "step":
//                    PROCESOR.MM_go();
//                    break;
//                case "register":
//                    INTERPRETER.KK_dispReg();
//                    break;
//                /*interpreter*/
//
//                /*zarządzanie plikami i katalogami*/
//                case "mkfile": //file create
//                    ZARZADZANIE_PLIKAMI_I_KATALOGAMI.KP_utwP(String.valueOf(arguments[1], Integer.parseInt(arguments[2]);
//                    break;
//                case "opnfile ":
//                    ZARZADZANIE_PLIKAMI_I_KATALOGAMI.KP_pobP(String.valueOf(arguments[1]);
//                    break;
//                case "dir":
//                    ZARZADZANIE_PLIKAMI_I_KATALOGAMI.KP_pokP();
//                    break;
//                case "erase":
//                    ZARZADZANIE_PLIKAMI_I_KATALOGAMI.KP_usunP(arguments[1]);
//                    break;
//                case "mkdir":
//                    ZARZADZANIE_PLIKAMI_I_KATALOGAMI.KP_utwK(String.valueOf(arguments[1]);
//                    break;
//                case "rmdir":
//                    ZARZADZANIE_PLIKAMI_I_KATALOGAMI.KP_usunK(String.valueOf(arguments[1]);
//                    break;
//                case "move":
//                    ZARZADZANIE_PLIKAMI_I_KATALOGAMI.KP_pP(String.valueOf(arguments[1], Integer.parseInt(arguments[2]);
//                    break;
//                /*zarządzanie plikami i katalogami*/
//
//                /*funkcje interfejsu*/
                case "exit":
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
