package stdos.Semaphore;

import stdos.Processes.PCB;
import stdos.Processes.ProcessState;
import stdos.Filesystem.Pliki;
import stdos.Filesystem.Plik;

import static stdos.CPU.CPU.*;
import static stdos.CPU.CPU.MM_getRUNNING;
import static stdos.Processes.ProcessManager.KM_setProcessState;
import java.util.ArrayDeque;
import java.util.Deque;
public class JPmetody {
    private static void wakeup(PCB p){
        KM_setProcessState (p, ProcessState.READY);//zmiana stanu procesu na ready
        //^ oraz dodanie procesu do listy kolejek priorytetowych
    }
    private static void block(PCB p){
        KM_setProcessState (p, ProcessState.WAITING);//zmiana stanu procesu na waiting
        //^ oraz usuniecie procesu z listy kolejek priorytetowych
    }
    public static void signal(semafor S){
        S.wartosc+=1;
        if(S.wartosc<=0) {
            PCB pom = S.kolejka.poll();
            wakeup(pom);
        }
    }
    public static void wait(semafor S){
        S.wartosc-=1;
        if(S.wartosc<0) {
            block(MM_getRUNNING());
            S.kolejka.offer(MM_getRUNNING());
        }
    }
    //================================================wypisywanie semafora na ekran=====================================

    public static void JPwypisz(String nazwa){ //wypisanie wartosci semafora

        Plik P=KP_dlaJP(nazwa);
        System.out.println(P.sem.wartosc);
    }
    public static void JPwypiszKolejke(String nazwa){ //wypisanie wartosci semafora
        Plik P=KP_dlaJP(nazwa);
        Deque<PCB> pom = P.sem.kolejka.clone(); //kopiowanie by zabezpieczyć się przed utratą zawartości oryginalnej kolejki
        PCB pompcb;
        for(int i=0; i<pom.size();i++){
            pompcb=pom.pollFirst();
            System.out.println(pompcb.getPid()+" "+pompcb.getPn());
        }
    }

    //================================================== wyswietlanie; funkcje ogolne, raczej nie beda uzywane=======================
    public static void JPwypiszOgolne(semafor S){ //wypisanie wartosci semafora zakladajaca ze semafory sa nie tylko w plikach
        System.out.println(S.wartosc);
    }
    public static void JPwypiszKolejkeOgolne(semafor S){ //wypisanie wartosci semafora, wersja zakladajaca ze semafory sa nie tylko w plikach
        Deque<PCB> pom = S.kolejka.clone(); //kopiowanie by zabezpieczyć się przed utratą zawartości oryginalnej kolejki
        PCB pompcb;
        for(int i=0; i<pom.size();i++){
            pompcb=pom.pollFirst();
            System.out.println(pompcb.getPid()+" "+pompcb.getPn());
        }
    }
}
