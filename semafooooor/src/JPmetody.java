
public class JPmetody {
    void wakeup(PCB p){
        //tu tez to Kapcrowe
        KM_setProcessState (p, ProcessState.READY)
        MM_add_ready(p);
    }
    void block(PCB p){
        //kapcer tutaj sobie prawda ugulem cale te. NO zeby tam zmienic
        KM_setProcessState (p, ProcessState.WAITING)
        MM_unreadyProcess(p.pid);
    }
    void signal(semafor S){

        S.wartosc+=1;
        if(S.wartosc>0) {
            PCB pom = S.kolejka.poll();
            wakeup(pom);
        }
    }
    void wait(semafor S){
        PCB pro=new PCB ;
        S.wartosc-=1;
        if(S.wartosc<=0) {
            block(pro);
             S.kolejka.offer(pro);

        }
    }
    public static int main(){

        return 0;
    }
}
