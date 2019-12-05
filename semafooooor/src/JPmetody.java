package CPU;
public class JPmetody {
    void wakeup(PCB p){
        KM_setProcessState (p, ProcessState.READY);//zmiana stanu procesu na ready
        MM_addReadyProcess(p);//dodanie procesu do listy kolejek priorytetowych
    }
    void block(PCB p){
        KM_setProcessState (p, ProcessState.WAITING);//zmiana stanu procesu na waiting
        MM_unreadyProcess(p);//usuniecie procesu z listy kolejek priorytetowych
    }
    void signal(semafor S){
        S.wartosc+=1;
        if(S.wartosc>0) {
            PCB pom = S.kolejka.poll();
            wakeup(pom);
        }
    }
    void wait(semafor S){
        S.wartosc-=1;
        if(S.wartosc<=0) {
            block(RUNNING);
             S.kolejka.offer(RUNNING);
        }
    }
    int JPwypisz(semafor S){ //wypisanie wartosci semafora
        return S.wartosc;
    }
    public static int main(){

        return 0;
    }
}
