package stdos.Processes;

public class PCB {
    private int pid; //PID - processID
    private String pn; //Process name
    private String filename; //Filename
    private int priD; //Dynamic priority
    private int priS; //Static priority
    private ProcessState ps; //ProcessState
    private int wt; //Waiting time
    private int PC; //Program Counter ?
    private int ax,bx,cx,dx; //Registry


    public PCB() {
        this.pid = 0;
        this.filename = "";
        this.pn = "";
        this.priD = 0;
        this.priS = 0;
        this.ps = ProcessState.NEW;
        this.wt = 0;
        this.PC = 0;
        this.ax = 0;
        this.bx = 0;
        this.cx = 0;
        this.dx = 0;
    }

    public PCB(int pid, String filename, String pn, int priS) {
        this.pid = pid;
        this.filename = filename;
        this.pn = pn;
        this.priD = priS; //At start -> priorityDynamic = priorityStatic
        this.priS = priS;
        this.ps = ProcessState.NEW;
        this.wt = 0;
        this.PC = 0;
        this.ax = 0;
        this.bx = 0;
        this.cx = 0;
        this.dx = 0;
    }

    public String getPn() { return pn; }

    public int getWt() { return wt; }

    public void setWt(int wt) { this.wt = wt; }

    public int getPid() {
        return pid;
    }

    public String getFilename() {
        return filename;
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public int getAx() {
        return ax;
    }

    public void setAx(int ax) {
        this.ax = ax;
    }

    public int getBx() {
        return bx;
    }

    public void setBx(int bx) {
        this.bx = bx;
    }

    public int getCx() {
        return cx;
    }

    public void setCx(int cx) {
        this.cx = cx;
    }

    public int getDx() { return dx; }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getPriD() {
        return priD;
    }

    public void setPriD(int priD) {
        this.priD = priD;
    }

    public int getPriS() {
        return priS;
    }

    //Useless, noone change static priority
    /*
    public void setPriS(int priS) {
        this.priS = priS;
    }
    */

    public ProcessState getPs() {
        return ps;
    }

    public void setPs(ProcessState ps) {
        this.ps = ps;
    }
}
