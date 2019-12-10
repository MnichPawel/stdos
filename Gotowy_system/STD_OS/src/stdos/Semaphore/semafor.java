package stdos.Semaphore;

import stdos.Processes.*;

import java.util.ArrayDeque;

public class semafor {

   public int wartosc=1;
   public ArrayDeque<PCB> kolejka= new ArrayDeque<PCB>();
   public semafor(int war){
        wartosc = war;
    }


}
