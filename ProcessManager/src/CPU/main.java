package CPU;

import java.util.LinkedList;
import java.util.Queue;
import ProcessManager.*;

public class main {
    public static void main(String[] args){
        Queue<PCB> q1 = new LinkedList<>();
        PCB p1 = new PCB(0,"p1",0,15,10);
        PCB p2 = new PCB(1,"p2",0,15,10);
        PCB p3 = new PCB(2,"p3",0,15,10);
        PCB tmp;
        q1.add(p1);
        q1.add(p2);
        q1.add(p3);
        for(int j = 0; j < q1.size(); j++){
            tmp = q1.poll();
            System.out.print("[ " + tmp.getPid() + tmp.getPn() + " ] ");
            q1.add(tmp);
        }
        System.out.print("\n");
        for(int j = 0; j < q1.size(); j++){
            tmp = q1.poll();
            System.out.print("[ " + tmp.getPid() + tmp.getPn() + " ] ");
            q1.add(tmp);
        }
    }
}
