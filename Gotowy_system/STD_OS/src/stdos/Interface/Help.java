package stdos.Interface;

import java.util.Random;

class Help {
    static void help() {
        System.out.println("  _____________________________________HELP_______________________________________________________________________________________");
        /*interfejs*/
        System.out.println("  exit                                                        - konczy dzialanie systemu");
        System.out.println("  help                                                        - wyswietla pomoc");
        /*interfejs*/


        /* pamięc ram*/
        //nie korzysta z żadnego innego modułu*/
        System.out.println("  _____________________________________PAMIĘĆ RAM_________________________________________________________________________________");
        System.out.println("  ram                                                         - wypisuje zawartość ramu w zapisie heksadecymalnym");
        System.out.println("  ram_part                                                    - wypisuje podzial pamieci ram");
        /* pamięc ram*/

        /* pamięc wirtualna*/
        //korzysta z pamięci fizycznej i operacyjnej
        System.out.println("  _____________________________________PAMIĘĆ WIRTUALNA___________________________________________________________________________");
      //  System.out.println("  ret [nazwa_procesu]                                       - zwraca instrukcje dla procesu z pamięci operacyjnej");
        System.out.println("  erasevm                                                     - czysci segmenty pamieci wirtualnej");
        System.out.println("  dvm                                                         - wyswietla aktualna zawartosc pamieci wirtualnej");
        /* pamięc wirtualna*/


        System.out.println("  _____________________________________SEMAFOR_____________________________________________________________________________________");
        /* semafor: tablica procesów i procesor*/
        System.out.println("  semstate [nazwa_pliku]                                      - wyswietla obecny stan semafora");
        System.out.println("  queue    [nazwa_pliku]                                      - wyswietla kolejke semafora");

        /* semafor*/

        System.out.println("  _____________________________________PROCESOR____________________________________________________________________________________");
        /* zarządzanie procesami: semafor, pamiec ram, pamiec wirtualna*/
        System.out.println("  taskcreate [nazwa_pliku] [nazwa_procesu} {priorytet}        - tworzenie procesu");

        System.out.println("  kill [nazwa_procesu]                                        - zakonczenie dzialania danego procesu");
        System.out.println("  tasklist                                                    - wyswietla liste wszystkich procesow");
        System.out.println("  rtasklist                                                   - wyswietla liste gotowych procesow");

        /* zarządzanie procesami*/

        /* procesor : korzysta z zarzadzania procesami*/
        System.out.println("  task_exec                                                   - wyswietla wykonywany proces");
        System.out.println("  prior_tasklist                                              - wyswietla liste gotowych procesow i ich chwilowy priorytet");
        /* procesor*/

        System.out.println("  _____________________________________PLIKI I KATALOGI____________________________________________________________________________");
        /*zarządzanie plikami i katalogami*/
        System.out.println("  mkfile [nazwa_pliku]                                        - utworzenie pliku");
       // System.out.println("  opnfile [nazwa_pliku]                                       - pobranie zawartosci pliku");
        System.out.println("  dir                                                         - wyswietla wszystkie pliki");
        System.out.println("  erase [nazwa_pliku]                                         - usuniecie pliku");
        System.out.println("  mkdir [nazwa_katalogu]                                      - utworzenie katalogu");
        System.out.println("  rmdir [nazwa_katalogu]                                      - usuniecie katalogu ze wszystkimi plikami, ktore sie w nim znajduja");
       // System.out.println("  move [nazwa_pliku] [nazwa_katalogu]                         - przeniesienie pliku do katalogu");

        /*zarządzanie plikami i katalogami*/
        /*interpreter*/
        System.out.println(" _____________________________________ASSEMBLER____________________________________________________________________________________");

        System.out.println("  step                                                        - wykonaj 1 rozkaz assemblera");
        System.out.println("  step [liczba_krokow]                                        - wykonaj n rozkazow assemblera");
        System.out.println("  register                                                    - wyswietla stan rejestrow");

        /*interpreter*/

    }
    static void printLogo(){
        System.out.println("  _      _           _     _        ____   _____ \n" +
                "     | |    | |  _   _  | |   (_)      / __ \\ / ____|\n" +
                "  ___| |_ __| | (_) (_) | |__  _  __ _| |  | | (___  \n" +
                " / __| __/ _` |         | '_ \\| |/ _` | |  | |\\___ \\ \n" +
                " \\__ \\ || (_| |  _   _  | |_) | | (_| | |__| |____) |\n" +
                " |___/\\__\\__,_| (_) (_) |_.__/|_|\\__, |\\____/|_____/ \n" +
                "                                  __/ |              \n" +
                "                                 |___/            ");

        System.out.println(" Wpisz help, aby wyswietlic dostepne polecenia");

    }


}

