import java.util.Random;

class Help {
    static void help() {

        /*interfejs*/
        System.out.println("  exit                                - konczy dzialanie systemu");
        System.out.println("  help                                - wyswietla pomoc");
        /*interfejs*/

        /* pamięc ram*/
        //nie korzysta z żadnego innego modułu*/

        System.out.println("  ram_disp_hex                        - wypisuje zawartość ramu w zapisie heksadecymalnym");
        System.out.println("  ram_disp_part                       - wypisuje podzial pamieci ram");
        /* pamięc ram*/

        /* pamięc wirtualna*/
        //korzysta z pamięci fizycznej i operacyjnej

        System.out.println("  vm_ret nazwa_procesu                - zwraca instrukcje dla procesu z pamięci operacyjnej lub fizycznej");
        System.out.println("  vm_erase                            - czysci segmenty pamieci wirtualnej");
        System.out.println("  vm_disp                             - wyswietla aktualna zawartosc pamieci wirtualnej");
        /* pamięc wirtualna*/

        /* semafor: tablica procesów i procesor*/
        System.out.println("  semafor_disp nazwa_pliku            - wyswietla obecny stan semafora");
        System.out.println("  semafor_disp_q                      - wyswietla kolejke semafora");

        /* semafor*/

        /* zarządzanie procesami: semafor, pamiec ram, pamiec wirtualna*/
        System.out.println("  cp nazwa_pliku priorytet            - tworzenie procesu");
        System.out.println("  kill nazwa procesu                  - zakonczenie dzialania danego procesu");
        System.out.println("  pr_disp                             - wyswietla liste wszystkich procesow");

        /* zarządzanie procesami*/


        /* procesor : korzysta z zarzadzania procesami*/
        System.out.println("  pr_disp_executing                   - wyswietla wykonywany proces");
        System.out.println("  pr_disp_rdy                         - wyswietla listę gotowych procesow i ich chwilowy priorytet");
        /* procesor*/

        /*zarządzanie plikami i katalogami*/
        System.out.println("  fc nazwa rozmiar(w bajtach)         - utworzenie pliku");
        System.out.println("  f_disp nazwa                        - pobranie zawartosci pliku");
        System.out.println("  f_disp_all                          - wyswietla wszystkie pliki");
        System.out.println("  f_rem nazwa                         - usuniecie pliku");
        System.out.println("  cc nazwa                            - utworzenie katalogu");
        System.out.println("  c_rem nazwa                         - usuniecie katalogu ze wszystkimi plikami, ktore sie w nim znajduja");
        System.out.println("  move nazwa_pliku nazwa_katalogu     - przeniesienie pliku do katalogu");

        /*zarządzanie plikami i katalogami*/
        /*interpreter*/
        System.out.println("  inter_read                          - odczyt bajtow z kodem rozkazu");
        System.out.println("  inter_disp                          - wyswietla stan rejestrow");
        /*interpreter*/

    }

}

