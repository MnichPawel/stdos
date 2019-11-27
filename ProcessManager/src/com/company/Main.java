package com.company;

public class Main {

    public static void main(String[] args) {
        ProcessManager pm = new ProcessManager();
        ProcessManager.KM_getAllProcessListPrint();
        try {
            ProcessManager.KM_CreateProcess("SOC", 3);
            ProcessManager.KM_CreateProcess("SOCCOS", 1);

        } catch (Exception e) {
            System.out.println(e);
        }
        ProcessManager.KM_getAllProcessListPrint();
        try {
            ProcessManager.KM_TerminateProcess(ProcessManager.KM_getPCBbyPID(1));
            ProcessManager.KM_TerminateProcess(ProcessManager.KM_getPCBbyPID(2));

        } catch (Exception e) {
            System.out.println(e);
        }
        ProcessManager.KM_getAllProcessListPrint();
    }
}
