package stdos.Interface;

import java.util.Random;

class Help {
    static void help() {
        System.out.println("  -------------------------------------HELP------------------------------------------------");
        /*interfejs*/
        System.out.println("  exit                                  - konczy dzialanie systemu");
        System.out.println("  help                                  - wyswietla pomoc");
        /*interfejs*/

        /* pamięc ram*/
        //nie korzysta z żadnego innego modułu*/
        System.out.println("  -------------------------------------PAMIĘĆ RAM-------------------------------------------");
        System.out.println("  ram                                  - wypisuje zawartość ramu w zapisie heksadecymalnym");
        System.out.println("  ram_part                             - wypisuje podzial pamieci ram");
        /* pamięc ram*/

        /* pamięc wirtualna*/
        //korzysta z pamięci fizycznej i operacyjnej
        System.out.println("  -------------------------------------PAMIĘĆ WIRTUALNA--------------------------------------");
      //  System.out.println("  ret [nazwa_procesu]                   - zwraca instrukcje dla procesu z pamięci operacyjnej");
        System.out.println("  erasevm                               - czysci segmenty pamieci wirtualnej");
        System.out.println("  dvm                                   - wyswietla aktualna zawartosc pamieci wirtualnej");
        /* pamięc wirtualna*/
        System.out.println("  -------------------------------------SEMAFOR----------------------------------------------");
        /* semafor: tablica procesów i procesor*/
        System.out.println("  semstate [nazwa_pliku]                - wyswietla obecny stan semafora");
        System.out.println("  queue                                 - wyswietla kolejke semafora");

        /* semafor*/

        System.out.println("  -------------------------------------PROCESOR---------------------------------------------");
        /* zarządzanie procesami: semafor, pamiec ram, pamiec wirtualna*/
        System.out.println("  taskcreate [nazwa_pliku] [nazwa_procesu} {priorytet}  - tworzenie procesu");
        System.out.println("  kill [nazwa_procesu]                  - zakonczenie dzialania danego procesu");
        System.out.println("  tasklist                              - wyswietla liste wszystkich procesow");
        System.out.println("  rtasklist                             - wyswietla listę gotowych procesow");



        /* zarządzanie procesami*/


        /* procesor : korzysta z zarzadzania procesami*/
        System.out.println("  task_exec                             - wyswietla wykonywany proces");
        System.out.println("  prior_tasklist                        - wyswietla listę gotowych procesow i ich chwilowy priorytet");
        /* procesor*/

        System.out.println("  -------------------------------------PLIKI I KATALOGI--------------------------------------");
        /*zarządzanie plikami i katalogami*/
        System.out.println("  mkfile [nazwa_pliku]                  - utworzenie pliku");
        System.out.println("  opnfile [nazwa_pliku]                 - pobranie zawartosci pliku");
        System.out.println("  dir                                   - wyswietla wszystkie pliki");
        System.out.println("  erase [nazwa_pliku]                   - usuniecie pliku");
        System.out.println("  mkdir [nazwa_katalogu]                - utworzenie katalogu");
        System.out.println("  rmdir [nazwa_katalogu]                - usuniecie katalogu ze wszystkimi plikami, ktore sie w nim znajduja");
        System.out.println("  move [nazwa_pliku] [nazwa_katalogu]   - przeniesienie pliku do katalogu");

        /*zarządzanie plikami i katalogami*/
        /*interpreter*/
        System.out.println("  ------------------------------------ASSEMBLER----------------------------------------------");
        System.out.println("  step                                  - assembler praca krokowa");
        System.out.println("  register                              - wyswietla stan rejestrow");

        /*interpreter*/

    }

}

