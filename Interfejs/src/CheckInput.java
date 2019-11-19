class CheckInput {

        static boolean exitFlag = false;

        static void inputSwitch(String data){
            String[] arguments = data.split("\\s+"); //one or more space space after (splits)

            switch (arguments[0]) {
                /*pamięć RAM*/
                case "ram_disp_hex":
                    PAMIEC_RAM .wypisz_pamiec();
                    break;
                case "ram_disp_part":
                    PAMIEC_RAM.wypisz_podzial();
                    break;
                /*pamięć RAM*/

                /*pamięć wirtualna*/
                case "vm_ret":
                    PAMIEC_WIRTUALNA.vm_find(Integer.parseInt(arguments[1]));
                    break;
                case "vm_erase":
                    PAMIEC_WIRTUALNA.vm_erase();
                    break;
                case "vm_disp":
                    PAMIEC_WIRTUALNA.vm_display();
                    break;
                /*pamięć wirtualna*/

                /*semafor*/
                case "semafor_disp":
                    SEMAFOR.jp_display(String.valueOf(arguments[1]));
                    break;
                case "semafor_disp_q":
                    SEMAFOR.jp_display_queue();
                    break;
                /*semafor*/

                /*zarządzanie procesami*/
                case "cp":
                    ZARZADZANIE_PROCESAMI.KM_CreateProcess(String.valueOf(arguments[1]), Integer.parseInt(arguments[2]);
                    break;
                case "kill":
                    ZARZADZANIE_PROCESAMI.KM_TerminateProcess(String.valueOf(arguments[1]);
                    break;
                case "pr_disp":
                    ZARZADZANIE_PROCESAMI.KM_getAllProcessList();
                    break;
                /*zarządzanie procesami*/

                /*procesor*/
                case "pr_disp_executing":
                    PROCESOR.mm_show_running();
                    break;

                case "pr_disp_rdy" :
                    PROCESOR.mm_show_actual_priority();
                    break;
                /*procesor*/

                /*interpreter*/
                case "interpreter": // wywołanie asemblera Kamil Korbik
                    INTERPRETER.KK_exec_instruction();
                    break;
                /*interpreter*/

                /*zarządzanie plikami i katalogami*/
                case "fc": //file create
                    ZARZADZANIE_PLIKAMI_I_KATALOGAMI.KP_utwP(String.valueOf(arguments[1], Integer.parseInt(arguments[2]);
                    break;
                case "f_disp ":
                    ZARZADZANIE_PLIKAMI_I_KATALOGAMI.KP_pobP(String.valueOf(arguments[1]);
                    break;
                case "f_disp_all":
                    ZARZADZANIE_PLIKAMI_I_KATALOGAMI.KP_pokP();
                    break;
                case "f_rem ":
                    ZARZADZANIE_PLIKAMI_I_KATALOGAMI.KP_usunP(arguments[1]);
                    break;
                case "cc ":
                    ZARZADZANIE_PLIKAMI_I_KATALOGAMI.KP_utwK(String.valueOf(arguments[1]);
                    break;
                case "c_rem ":
                    ZARZADZANIE_PLIKAMI_I_KATALOGAMI.KP_usunK(String.valueOf(arguments[1]);
                    break;
                case "move":
                    ZARZADZANIE_PLIKAMI_I_KATALOGAMI.KP_pP(String.valueOf(arguments[1], Integer.parseInt(arguments[2]);
                    break;
                /*zarządzanie plikami i katalogami*/

                /*funkcje interfejsu*/
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
