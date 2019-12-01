package Process;

import java.util.Vector;
//przyklaodwa klasa Procesu na potrzebe zadania
public class ProcessClass {
    public int pId;
    public Vector<Vector<Byte>> code;
    public ProcessClass(int pId, Vector<Vector<Byte>> code) {
        this.pId = pId;
        this.code = code;
    }
}
