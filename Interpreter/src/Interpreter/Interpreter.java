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
    static Map<String, Integer> arguments = new HashMap<String, Integer>();
    static Vector<String> Instruction = new Vector<String>();

    static PCB pcb;
    static int address;

    public void Interpreter() { // Constructor
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
        arguments.put("DP", 1)   //DELETE PROCESS
    }

     static void getORDER(){  //
        int download = 0;
        char temparse;
        String temp = "";
        for(int i=0; i<2; i++)
        {
            download = get_value(address);
            temparse = (char)download;
            temp += temparse;
            address++;
        }
        Instruction.add(temp);
    }

    static void getARGUMENTS(int n){
        int download = 0;
        char temparse;
        String temp = "";
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
    /*JEŚLI TO CZYTASZ, POMÓŻ MI WYMYŚLEĆ JAK ZAPROGRAMOWAĆ TO INNACZEJ, PONIEWAŻ WZIĘCIE POD UWAGĘ WSZYSTKICH OPCJI TO
    * BĘDZIE MASAKRYCZNA ILOŚĆ KODU DO NAPISANIA*/
    static void makeINSTRUCTION(){ //rejestr rejestr, rejestr adres, rejestr liczba
        if(Instruction.get(0)=="AD"){
            if (Instruction.get(1)=="AX"){
                if (Instruction.get(2)=="["){

                }
                else if (Instruction.get(2)=="AX"){
                    int ax = pcb.getAx(), aax = pcb.getAx();
                    pcb.setAx(ax+aax);
                }
                else if (Instruction.get(2)=="BX"){
                    int ax = pcb.getAx(), bx = pcb.getBx();
                    pcb.setAx(ax+bx);
                }
                else if (Instruction.get(2)=="CX"){
                    int ax = pcb.getAx(), cx = pcb.getCx();
                    pcb.setAx(ax+cx);
                }
                else if (Instruction.get(2)=="DX"){
                    int ax = pcb.getAx(), dx = pcb.getDx();
                    pcb.setAx(ax+dx);
                }
                else{
                    int ax = pcb.getAx();
                    pcb.setAx(ax+Integer.parseInt(Instruction.get(2)));
                }
            }
            else if (Instruction.get(1)=="BX"){
                if (Instruction.get(2)=="["){

                }
                else if (Instruction.get(2)=="AX"){

                }
                else if (Instruction.get(2)=="BX"){

                }
                else if (Instruction.get(2)=="CX"){

                }
                else if (Instruction.get(2)=="DX"){

                }
                else{
                    //zostaje opcja że to zwykła liczba
                }
            }
            else if (Instruction.get(1)=="CX"){
                if (Instruction.get(2)=="["){

                }
                else if (Instruction.get(2)=="AX"){

                }
                else if (Instruction.get(2)=="BX"){

                }
                else if (Instruction.get(2)=="CX"){

                }
                else if (Instruction.get(2)=="DX"){

                }
                else{
                    //zostaje opcja że to zwykła liczba
                }
            }
            else if (Instruction.get(1)=="DX"){
                if (Instruction.get(2)=="["){

                }
                else if (Instruction.get(2)=="AX"){

                }
                else if (Instruction.get(2)=="BX"){

                }
                else if (Instruction.get(2)=="CX"){

                }
                else if (Instruction.get(2)=="DX"){

                }
                else{
                    //zostaje opcja że to zwykła liczba
                }
            }
        }
    }

    public static void KK_Interpret(){
        getORDER();
        getARGUMENTS(arguments.get(Instruction.firstElement());
        pcb.setPC(address);

    }
}
