package CPU;
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
            block(RUNNING);
             S.kolejka.offer(RUNNING);
        }
    }
    /*int JPwypisz(semafor S){ //wypisanie wartosci semafora
        return S.wartosc;
    }*/
   public void JPwypiszOgolne(semafor S){ //wypisanie wartosci semafora zakladajaca ze semafory sa nie tylko w plikach
       System.out.println(S.wartosc);
    }
  public  void JPwypisz(plik P){ //wypisanie wartosci semafora
        System.out.println(P.semafor.wartosc);
    }
   public void JPwypiszKolejke(plik P){ //wypisanie wartosci semafora
        Deque<PCB> pom = P.semafor.kolejka.clone(); //kopiowanie by zabezpieczyć się przed utratą zawartości oryginalnej kolejki
        PCB pompcb;
        for(int i=0; i<pom.size();i++){
            pompcb=pom.pollFirst();
            System.out.println(pompcb.pid,pompcb.pn);
        }
    }
   public void JPwypiszKolejkeOgolne(semafor S){ //wypisanie wartosci semafora, wersja zakladajaca ze semafory sa nie tylko w plikach
        Deque<PCB> pom = S.kolejka.clone(); //kopiowanie by zabezpieczyć się przed utratą zawartości oryginalnej kolejki
        PCB pompcb;
        for(int i=0; i<pom.size();i++){
            pompcb=pom.pollFirst();
            System.out.println(pompcb.pid,pompcb.pn);
        }
    }
    public static int main(){

        return 0;
    }
}
