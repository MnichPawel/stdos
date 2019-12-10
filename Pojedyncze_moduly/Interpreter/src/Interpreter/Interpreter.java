package Interpreter;

import CPU.*;
import ProcessManager.*;
import VirtualMemory.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import static CPU.CPU.MM_getRUNNING;
import static VirtualMemory.VirtualMemory.get_value;
//TODO Byte Receiver from Virtual Memory Method //DONE
//TODO Assembler Instruction Map //DONE
//TODO getRegisters Method
public class Interpreter {
    /*==============================================STRUCTURES========================================================*/
    private static Map<String, Integer> arguments = new HashMap<>();
    private static Vector<String> Instruction = new Vector<>();

    private static PCB pcb;
    private static int address;

    public Interpreter() { // Constructor
        PCB pcb = MM_getRUNNING();
        int address = pcb.getPC();
        /*=============================================ADD KEYS AND VALUES=========================================== */
        //ARITHMETIC
        arguments.put("AD", 2);  //ADDITION
        arguments.put("SU", 2);  //SUBTRACTION
        arguments.put("MU", 2);  //MULTIPLICATION
        arguments.put("IN", 1);  //INCREMENT
        arguments.put("DE", 1);  //DECREMENT
        //JUMPS
        arguments.put("JU", 1);  //UNCONDITIONAL JUMP
        arguments.put("JZ", 1);  //JUMP IF CX = 0
        arguments.put("JN", 1);  //JUMP IF CX != 0
        arguments.put("SP", 0);  //END OF THE PROGRAM
        //MOVING
        arguments.put("MO", 2);  //MOVING TO FROM
        //WORK ON FILES
            //...IN PROGRESS
        //PROCESS OPERATION
        arguments.put("CP", 2);  //CREATE PROCESS
        arguments.put("DP", 1);  //DELETE PROCESS
    }

     private static void getORDER(){  //
        int download;
        char temparse;
        //String temp = "";
        StringBuilder temp = new StringBuilder();
        for(int i=0; i<2; i++)
        {
            download = get_value(address);
            temparse = (char)download;
            //temp+=temparse;
            temp.append(temparse);
            address++;
        }
        //Instruction.add(temp);
        Instruction.add(temp.toString());
    }

    private static void getARGUMENTS(int n){
        int download;
        char temparse;
        String temp;
        for(int i=0; i<n; i++)
        {
            temp = "";
            while(true)
            {
                download = get_value(address);
                    if(download!=32) {
                        if(download==91||download==93){
                        temparse = (char)download;
                        Instruction.add(Character.toString(temparse));
                        address++;
                        }
                        else {
                            temparse = (char) download;
                            temp += temparse;
                            address++;
                        }
                    }
                    else
                    {
                        address++;
                        break;
                    }
            }
            Instruction.add(temp);
        }
    }
    /*JEŚLI TO CZYTASZ, POMÓŻ MI WYMYŚLiĆ JAK ZAPROGRAMOWAĆ TO INNACZEJ, PONIEWAŻ WZIĘCIE POD UWAGĘ WSZYSTKICH OPCJI TO
    * BĘDZIE MASAKRYCZNA ILOŚĆ KODU DO NAPISANIA*/
    private static void makeINSTRUCTION(){ //rejestr rejestr, rejestr adres, rejestr liczba
        if(Instruction.get(0).equals("AD")){
            if (Instruction.get(1).equals("AX")){
                if (Instruction.get(2).equals("[")){
                    int ax = pcb.getAx(), value = get_value(Integer.parseInt(Instruction.get(3)));
                    pcb.setAx(ax+value);
                }
                else if (Instruction.get(2).equals("AX")){
                    int ax = pcb.getAx();
                    pcb.setAx(ax+ax);
                }
                else if (Instruction.get(2).equals("BX")){
                    int ax = pcb.getAx(), bx = pcb.getBx();                 //ELSEIF VERSION
                    pcb.setAx(ax+bx);
                }
                else if (Instruction.get(2).equals("CX")){
                    int ax = pcb.getAx(), cx = pcb.getCx();
                    pcb.setAx(ax+cx);
                }
                else if (Instruction.get(2).equals("DX")){
                    int ax = pcb.getAx(), dx = pcb.getDx();
                    pcb.setAx(ax+dx);
                }
                else{
                    int ax = pcb.getAx();
                    pcb.setAx(ax+Integer.parseInt(Instruction.get(2)));
                }
            }
            else if (Instruction.get(1).equals("BX")){
                switch (Instruction.get(2)) {
                    case "[": {
                        int bx = pcb.getBx(), value = get_value(Integer.parseInt(Instruction.get(3)));
                        pcb.setBx(bx + value);
                        break;
                    }
                    case "AX": {
                        int bx = pcb.getBx(), ax = pcb.getAx();
                        pcb.setBx(bx + ax);
                        break;
                    }
                    case "BX": {
                        int bx = pcb.getBx();
                        pcb.setBx(bx + bx);
                        break;
                    }                                      //SWITCH CASE VERSION
                    case "CX": {
                        int bx = pcb.getBx(), cx = pcb.getCx();
                        pcb.setBx(bx + cx);
                        break;
                    }
                    case "DX": {
                        int bx = pcb.getBx(), dx = pcb.getDx();
                        pcb.setBx(bx + dx);
                        break;
                    }
                    default: {
                        int bx = pcb.getBx();
                        pcb.setBx(bx + Integer.parseInt(Instruction.get(2)));
                        break;
                    }
                }
            }
            else if (Instruction.get(1).equals("CX")){
                if (Instruction.get(2).equals("[")){
                    int cx = pcb.getCx(), value = get_value(Integer.parseInt(Instruction.get(3)));
                    pcb.setCx(cx+value);
                }
                else if (Instruction.get(2).equals("AX")){
                    int cx = pcb.getCx(), ax = pcb.getAx();
                    pcb.setCx(cx + ax);
                }
                else if (Instruction.get(2)=="BX"){
                    int cx = pcb.getCx(), bx = pcb.getBx();
                    pcb.setCx(cx + bx);
                }
                else if (Instruction.get(2)=="CX"){
                    int cx = pcb.getCx();
                    pcb.setCx(cx + cx);
                }
                else if (Instruction.get(2)=="DX"){
                    int cx = pcb.getCx(), dx = pcb.getDx();
                    pcb.setCx(cx + dx);
                }
                else{
                    int cx = pcb.getCx();
                    pcb.setCx(cx + Integer.parseInt(Instruction.get(2)));
                }
            }
            else if (Instruction.get(1)=="DX"){
                if (Instruction.get(2)=="["){
                    int dx = pcb.getDx(), value = get_value(Integer.parseInt(Instruction.get(3)));
                    pcb.setDx(dx+value);
                }
                else if (Instruction.get(2)=="AX"){
                    int dx = pcb.getDx(), ax = pcb.getAx();
                    pcb.setDx(dx + ax);
                }
                else if (Instruction.get(2)=="BX"){
                    int dx = pcb.getDx(), bx = pcb.getBx();
                    pcb.setDx(dx + bx);
                }
                else if (Instruction.get(2)=="CX"){
                    int dx = pcb.getDx(), cx = pcb.getCx();
                    pcb.setDx(dx + cx);
                }
                else if (Instruction.get(2)=="DX"){
                    int dx = pcb.getDx();
                    pcb.setDx(dx + dx);
                }
                else{
                    int dx = pcb.getDx();
                    pcb.setCx(dx + Integer.parseInt(Instruction.get(2)));
                }
            }
        }
    }

    public static void KK_Interpret(){
        getORDER();
        getARGUMENTS(arguments.get(Instruction.firstElement()));
        makeINSTRUCTION();
        pcb.setPC(address);

    }
}
