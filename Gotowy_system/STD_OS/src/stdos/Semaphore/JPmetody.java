package stdos.Semaphore;

import stdos.Processes.PCB;
import stdos.Processes.ProcessState;

import stdos.Filesystem.Plik;

import static stdos.CPU.CPU.*;
import static stdos.CPU.CPU.MM_getRUNNING;
import static stdos.Processes.ProcessManager.KM_setProcessState;
import java.util.ArrayDeque;
import java.util.Deque;
public class JPmetody {
    private void wakeup(PCB p){
        KM_setProcessState (p, ProcessState.READY);//zmiana stanu procesu na ready
        MM_addReadyProcess(p);//dodanie procesu do listy kolejek priorytetowych
    }
    private void block(PCB p){
        KM_setProcessState (p, ProcessState.WAITING);//zmiana stanu procesu na waiting
        MM_unreadyProcess(p);//usuniecie procesu z listy kolejek priorytetowych
    }
   public void signal(semafor S){
        S.wartosc+=1;
        if(S.wartosc>0) {
            PCB pom = S.kolejka.poll();
            wakeup(pom);
        }
    }
  public  void wait(semafor S){
        S.wartosc-=1;
        if(S.wartosc<=0) {
            block(MM_getRUNNING());
             S.kolejka.offer(MM_getRUNNING());
        }
    }
    /*int JPwypisz(semafor S){ //wypisanie wartosci semafora
        return S.wartosc;
    }*/
   public void JPwypiszOgolne(semafor S){ //wypisanie wartosci semafora zakladajaca ze semafory sa nie tylko w plikach
       System.out.println(S.wartosc);
    }
  public  void JPwypisz(Plik P){ //wypisanie wartosci semafora
        System.out.println(P.sem.wartosc);
    }
   public void JPwypiszKolejke(Plik P){ //wypisanie wartosci semafora
        Deque<PCB> pom = P.sem.kolejka.clone(); //kopiowanie by zabezpieczyć się przed utratą zawartości oryginalnej kolejki
        PCB pompcb;
        for(int i=0; i<pom.size();i++){
            pompcb=pom.pollFirst();
            System.out.println(pompcb.getPid()+" "+pompcb.getPn());
        }
    }
   public void JPwypiszKolejkeOgolne(semafor S){ //wypisanie wartosci semafora, wersja zakladajaca ze semafory sa nie tylko w plikach
        Deque<PCB> pom = S.kolejka.clone(); //kopiowanie by zabezpieczyć się przed utratą zawartości oryginalnej kolejki
        PCB pompcb;
        for(int i=0; i<pom.size();i++){
            pompcb=pom.pollFirst();
            System.out.println(pompcb.getPid()+" "+pompcb.getPn());
        }
    }
    public static int main(){

        return 0;
    }
}
