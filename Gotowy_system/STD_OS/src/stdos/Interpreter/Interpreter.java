package stdos.Interpreter;

import stdos.Processes.*;
import stdos.Filesystem.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import static stdos.CPU.CPU.MM_getRUNNING;
import static stdos.VM.VirtualMemory.*;
//TODO Byte Receiver from Virtual Memory Method //DONE
//TODO Assembler Instruction Map //DONE
//TODO getRegisters Method
public class Interpreter {
    /*==============================================STRUCTURES========================================================*/
    private static Map<String, Integer> arguments = new HashMap<>();
    private static Vector<String> Instruction = new Vector<>();

    private static PCB pcb;
    private static int address;
    private static Pliki plik;

    public Interpreter() { // Constructor
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
        arguments.put("CF", 2);  //CREATE FILE WITH CONTENT
        arguments.put("DF", 1);  //DOWNLOAD FILE
        arguments.put("AF", 2);  //ADD TO FILE
        arguments.put("SF", 0);  //SHOW FILES TODO::I think this is Interface function
        arguments.put("TF", 1);  //TERMINATE FILE
        arguments.put("CC", 1);  //CREATE CATALOG
        arguments.put("MF", 2);  //MOVING FILE TO CATALOG
        arguments.put("TC", 1);  //TERMINATE CATALOG WITH ALL FILES
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
                    if(download!=32)
                    {
                        if(download==91||download==93)
                        {
                            temparse = (char)download;
                            Instruction.add(Character.toString(temparse));
                            address++;
                        }
                        else
                        {
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
    private static void makeINSTRUCTION() {
        /*==============ARITHMETIC=======================*/
        if (Instruction.get(0).equals("AD")) {
            switch (Instruction.get(1)) {
                case "AX":
                    switch (Instruction.get(2)) {
                        case "[": {
                            int ax = pcb.getAx(), value = get_value(Integer.parseInt(Instruction.get(3)));
                            pcb.setAx(ax + value);
                            break;
                        }
                        case "AX": {
                            int ax = pcb.getAx();
                            pcb.setAx(ax + ax);
                            break;
                        }
                        case "BX": {
                            int ax = pcb.getAx(), bx = pcb.getBx();                 //ELSEIF VERSION

                            pcb.setAx(ax + bx);
                            break;
                        }
                        case "CX": {
                            int ax = pcb.getAx(), cx = pcb.getCx();
                            pcb.setAx(ax + cx);
                            break;
                        }
                        case "DX": {
                            int ax = pcb.getAx(), dx = pcb.getDx();
                            pcb.setAx(ax + dx);
                            break;
                        }
                        default: {
                            int ax = pcb.getAx();
                            pcb.setAx(ax + Integer.parseInt(Instruction.get(2)));
                            break;
                        }
                    }
                    break;
                case "BX":
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
                    break;
                case "CX":
                    switch (Instruction.get(2)) {
                        case "[": {
                            int cx = pcb.getCx(), value = get_value(Integer.parseInt(Instruction.get(3)));
                            pcb.setCx(cx + value);
                            break;
                        }
                        case "AX": {
                            int cx = pcb.getCx(), ax = pcb.getAx();
                            pcb.setCx(cx + ax);
                            break;
                        }
                        case "BX": {
                            int cx = pcb.getCx(), bx = pcb.getBx();
                            pcb.setCx(cx + bx);
                            break;
                        }
                        case "CX": {
                            int cx = pcb.getCx();
                            pcb.setCx(cx + cx);
                            break;
                        }
                        case "DX": {
                            int cx = pcb.getCx(), dx = pcb.getDx();
                            pcb.setCx(cx + dx);
                            break;
                        }
                        default: {
                            int cx = pcb.getCx();
                            pcb.setCx(cx + Integer.parseInt(Instruction.get(2)));
                            break;
                        }
                    }
                    break;
                case "DX":
                    switch (Instruction.get(2)) {
                        case "[": {
                            int dx = pcb.getDx(), value = get_value(Integer.parseInt(Instruction.get(3)));
                            pcb.setDx(dx + value);
                            break;
                        }
                        case "AX": {
                            int dx = pcb.getDx(), ax = pcb.getAx();
                            pcb.setDx(dx + ax);
                            break;
                        }
                        case "BX": {
                            int dx = pcb.getDx(), bx = pcb.getBx();
                            pcb.setDx(dx + bx);
                            break;
                        }
                        case "CX": {
                            int dx = pcb.getDx(), cx = pcb.getCx();
                            pcb.setDx(dx + cx);
                            break;
                        }
                        case "DX": {
                            int dx = pcb.getDx();
                            pcb.setDx(dx + dx);
                            break;
                        }
                        default: {
                            int dx = pcb.getDx();
                            pcb.setCx(dx + Integer.parseInt(Instruction.get(2)));
                            break;
                        }
                    }
                    break;
            }
        }
        else if (Instruction.get(0).equals("SU")) {
            switch (Instruction.get(1)) {
                case "AX":
                    switch (Instruction.get(2)) {
                        case "[": {
                            int ax = pcb.getAx(), value = get_value(Integer.parseInt(Instruction.get(3)));
                            pcb.setAx(ax - value);
                            break;
                        }
                        case "AX": {
                            int ax = pcb.getAx();
                            pcb.setAx(ax-ax);
                            break;
                        }
                        case "BX": {
                            int ax = pcb.getAx(), bx = pcb.getBx();                 //ELSEIF VERSION

                            pcb.setAx(ax - bx);
                            break;
                        }
                        case "CX": {
                            int ax = pcb.getAx(), cx = pcb.getCx();
                            pcb.setAx(ax - cx);
                            break;
                        }
                        case "DX": {
                            int ax = pcb.getAx(), dx = pcb.getDx();
                            pcb.setAx(ax - dx);
                            break;
                        }
                        default: {
                            int ax = pcb.getAx();
                            pcb.setAx(ax - Integer.parseInt(Instruction.get(2)));
                            break;
                        }
                    }
                    break;
                case "BX":
                    switch (Instruction.get(2)) {
                        case "[": {
                            int bx = pcb.getBx(), value = get_value(Integer.parseInt(Instruction.get(3)));
                            pcb.setBx(bx - value);
                            break;
                        }
                        case "AX": {
                            int bx = pcb.getBx(), ax = pcb.getAx();
                            pcb.setBx(bx - ax);
                            break;
                        }
                        case "BX": {
                            int bx = pcb.getBx();
                            pcb.setBx(bx - bx);
                            break;
                        }                                      //SWITCH CASE VERSION
                        case "CX": {
                            int bx = pcb.getBx(), cx = pcb.getCx();
                            pcb.setBx(bx - cx);
                            break;
                        }
                        case "DX": {
                            int bx = pcb.getBx(), dx = pcb.getDx();
                            pcb.setBx(bx - dx);
                            break;
                        }
                        default: {
                            int bx = pcb.getBx();
                            pcb.setBx(bx - Integer.parseInt(Instruction.get(2)));
                            break;
                        }
                    }
                    break;
                case "CX":
                    switch (Instruction.get(2)) {
                        case "[": {
                            int cx = pcb.getCx(), value = get_value(Integer.parseInt(Instruction.get(3)));
                            pcb.setCx(cx - value);
                            break;
                        }
                        case "AX": {
                            int cx = pcb.getCx(), ax = pcb.getAx();
                            pcb.setCx(cx - ax);
                            break;
                        }
                        case "BX": {
                            int cx = pcb.getCx(), bx = pcb.getBx();
                            pcb.setCx(cx - bx);
                            break;
                        }
                        case "CX": {
                            int cx = pcb.getCx();
                            pcb.setCx(cx - cx);
                            break;
                        }
                        case "DX": {
                            int cx = pcb.getCx(), dx = pcb.getDx();
                            pcb.setCx(cx - dx);
                            break;
                        }
                        default: {
                            int cx = pcb.getCx();
                            pcb.setCx(cx - Integer.parseInt(Instruction.get(2)));
                            break;
                        }
                    }
                    break;
                case "DX":
                    switch (Instruction.get(2)) {
                        case "[": {
                            int dx = pcb.getDx(), value = get_value(Integer.parseInt(Instruction.get(3)));
                            pcb.setDx(dx - value);
                            break;
                        }
                        case "AX": {
                            int dx = pcb.getDx(), ax = pcb.getAx();
                            pcb.setDx(dx - ax);
                            break;
                        }
                        case "BX": {
                            int dx = pcb.getDx(), bx = pcb.getBx();
                            pcb.setDx(dx - bx);
                            break;
                        }
                        case "CX": {
                            int dx = pcb.getDx(), cx = pcb.getCx();
                            pcb.setDx(dx - cx);
                            break;
                        }
                        case "DX": {
                            int dx = pcb.getDx();
                            pcb.setDx(dx - dx);
                            break;
                        }
                        default: {
                            int dx = pcb.getDx();
                            pcb.setCx(dx - Integer.parseInt(Instruction.get(2)));
                            break;
                        }
                    }
                    break;
            }
        }
        else if (Instruction.get(0).equals("MU")) {
            switch (Instruction.get(1)) {
                case "AX":
                    switch (Instruction.get(2)) {
                        case "[": {
                            int ax = pcb.getAx(), value = get_value(Integer.parseInt(Instruction.get(3)));
                            pcb.setAx(ax * value);
                            break;
                        }
                        case "AX": {
                            int ax = pcb.getAx();
                            pcb.setAx(ax * ax);
                            break;
                        }
                        case "BX": {
                            int ax = pcb.getAx(), bx = pcb.getBx();
                            pcb.setAx(ax * bx);
                            break;
                        }
                        case "CX": {
                            int ax = pcb.getAx(), cx = pcb.getCx();
                            pcb.setAx(ax - cx);
                            break;
                        }
                        case "DX": {
                            int ax = pcb.getAx(), dx = pcb.getDx();
                            pcb.setAx(ax * dx);
                            break;
                        }
                        default: {
                            int ax = pcb.getAx();
                            pcb.setAx(ax * Integer.parseInt(Instruction.get(2)));
                            break;
                        }
                    }
                    break;
                case "BX":
                    switch (Instruction.get(2)) {
                        case "[": {
                            int bx = pcb.getBx(), value = get_value(Integer.parseInt(Instruction.get(3)));
                            pcb.setBx(bx * value);
                            break;
                        }
                        case "AX": {
                            int bx = pcb.getBx(), ax = pcb.getAx();
                            pcb.setBx(bx * ax);
                            break;
                        }
                        case "BX": {
                            int bx = pcb.getBx();
                            pcb.setBx(bx * bx);
                            break;
                        }                                      //SWITCH CASE VERSION
                        case "CX": {
                            int bx = pcb.getBx(), cx = pcb.getCx();
                            pcb.setBx(bx * cx);
                            break;
                        }
                        case "DX": {
                            int bx = pcb.getBx(), dx = pcb.getDx();
                            pcb.setBx(bx * dx);
                            break;
                        }
                        default: {
                            int bx = pcb.getBx();
                            pcb.setBx(bx * Integer.parseInt(Instruction.get(2)));
                            break;
                        }
                    }
                    break;
                case "CX":
                    switch (Instruction.get(2)) {
                        case "[": {
                            int cx = pcb.getCx(), value = get_value(Integer.parseInt(Instruction.get(3)));
                            pcb.setCx(cx * value);
                            break;
                        }
                        case "AX": {
                            int cx = pcb.getCx(), ax = pcb.getAx();
                            pcb.setCx(cx * ax);
                            break;
                        }
                        case "BX": {
                            int cx = pcb.getCx(), bx = pcb.getBx();
                            pcb.setCx(cx * bx);
                            break;
                        }
                        case "CX": {
                            int cx = pcb.getCx();
                            pcb.setCx(cx * cx);
                            break;
                        }
                        case "DX": {
                            int cx = pcb.getCx(), dx = pcb.getDx();
                            pcb.setCx(cx * dx);
                            break;
                        }
                        default: {
                            int cx = pcb.getCx();
                            pcb.setCx(cx * Integer.parseInt(Instruction.get(2)));
                            break;
                        }
                    }
                    break;
                case "DX":
                    switch (Instruction.get(2)) {
                        case "[": {
                            int dx = pcb.getDx(), value = get_value(Integer.parseInt(Instruction.get(3)));
                            pcb.setDx(dx * value);
                            break;
                        }
                        case "AX": {
                            int dx = pcb.getDx(), ax = pcb.getAx();
                            pcb.setDx(dx * ax);
                            break;
                        }
                        case "BX": {
                            int dx = pcb.getDx(), bx = pcb.getBx();
                            pcb.setDx(dx * bx);
                            break;
                        }
                        case "CX": {
                            int dx = pcb.getDx(), cx = pcb.getCx();
                            pcb.setDx(dx * cx);
                            break;
                        }
                        case "DX": {
                            int dx = pcb.getDx();
                            pcb.setDx(dx * dx);
                            break;
                        }
                        default: {
                            int dx = pcb.getDx();
                            pcb.setCx(dx * Integer.parseInt(Instruction.get(2)));
                            break;
                        }
                    }
                    break;
            }
        }
        else if (Instruction.get(0).equals("IN")) {
            if(Instruction.get(1).equals("AX"))
            {
                pcb.setAx(pcb.getAx()+1);
            }
            else if (Instruction.get(1).equals("BX"))
            {
                pcb.setBx(pcb.getBx()+1);
            }
            else if (Instruction.get(1).equals("CX"))
            {
                pcb.setCx(pcb.getCx()+1);
            }
            else if (Instruction.get(1).equals("DX"))
            {
                pcb.setDx(pcb.getDx()+1);
            }
            else if (Instruction.get(1).equals("["))
            {
                //TODO::I need function to set value in VM
            }

        }
        else if (Instruction.get(0).equals("DE")) {
            if(Instruction.get(1).equals("AX"))
            {
                pcb.setAx(pcb.getAx()-1);
            }
            else if (Instruction.get(1).equals("BX"))
            {
                pcb.setBx(pcb.getBx()-1);
            }
            else if (Instruction.get(1).equals("CX"))
            {
                pcb.setCx(pcb.getCx()-1);
            }
            else if (Instruction.get(1).equals("DX"))
            {
                pcb.setDx(pcb.getDx()-1);
            }
            else if (Instruction.get(1).equals("["))
            {
                //TODO::I need function to set value in VM
            }

        }
        /*====================JUMPS======================*/
        else if (Instruction.get(0).equals("JU")){
            if(Instruction.get(1).equals("["))
            {
                address = Integer.parseInt(Instruction.get(2));
            }
        }
        else if (Instruction.get(0).equals("JZ")){
            if(pcb.getCx()==0) {
                if (Instruction.get(1).equals("[")) {
                    address = Integer.parseInt(Instruction.get(2));
                }
            }
        }
        else if (Instruction.get(0).equals("JN")){
            if(pcb.getCx()!=0) {
                if (Instruction.get(1).equals("[")) {
                    address = Integer.parseInt(Instruction.get(2));
                }
            }
        }
        /*================MOVING=========================*/
        else if (Instruction.get(0).equals("MO")){
            switch (Instruction.get(1)) {
                case "AX":
                    switch (Instruction.get(2)) {
                        case "[": {
                            pcb.setAx(get_value(Integer.parseInt(Instruction.get(3))));
                            break;
                        }
                        case "BX": {
                            pcb.setAx(pcb.getBx());
                            break;
                        }
                        case "CX": {
                            pcb.setAx(pcb.getCx());
                            break;
                        }
                        case "DX": {
                            pcb.setAx(pcb.getDx());
                            break;
                        }
                        default: {
                            pcb.setAx(Integer.parseInt(Instruction.get(2)));
                            break;
                        }
                    }
                    break;
                case "BX":
                    switch (Instruction.get(2)) {
                        case "[": {
                            pcb.setBx(get_value(Integer.parseInt(Instruction.get(3))));
                            break;
                        }
                        case "AX": {
                            pcb.setBx(pcb.getAx());
                            break;
                        }
                        case "CX": {
                            pcb.setBx(pcb.getCx());
                            break;
                        }
                        case "DX": {
                            pcb.setBx(pcb.getDx());
                            break;
                        }
                        default: {
                            pcb.setBx(Integer.parseInt(Instruction.get(2)));
                            break;
                        }
                    }
                    break;
                case "CX":
                    switch (Instruction.get(2)) {
                        case "[": {
                            pcb.setCx(get_value(Integer.parseInt(Instruction.get(3))));
                            break;
                        }
                        case "AX": {
                            pcb.setCx(pcb.getAx());
                            break;
                        }
                        case "BX": {
                            pcb.setCx(pcb.getBx());
                            break;
                        }
                        case "DX": {
                            pcb.setCx(pcb.getDx());
                            break;
                        }
                        default: {
                            pcb.setCx(Integer.parseInt(Instruction.get(2)));
                            break;
                        }
                    }
                    break;
                case "DX":
                    switch (Instruction.get(2)) {
                        case "[": {
                            pcb.setDx(get_value(Integer.parseInt(Instruction.get(3))));
                            break;
                        }
                        case "AX": {
                            pcb.setDx(pcb.getAx());
                            break;
                        }
                        case "BX": {
                            pcb.setDx(pcb.getBx());
                            break;
                        }
                        case "CX": {
                            pcb.setDx(pcb.getCx());
                            break;
                        }
                        default: {
                            pcb.setCx(Integer.parseInt(Instruction.get(2)));
                            break;
                        }
                    }
                    break;
                case "[": //TODO::MOVING TO ADRESS
                    switch (Instruction.get(2)){

                    }
            }
        } //TODO:MOVING TO ADRESS
        /*================WORK_ON_FILES==================*/
        else if (Instruction.get(0).equals("CF")){
            plik = new Pliki(Instruction.get(1));

        }
        else if (Instruction.get(0).equals("DF")){}
        else if (Instruction.get(0).equals("AF")){}
        else if (Instruction.get(0).equals("SF")){}
        else if (Instruction.get(0).equals("TF")){}
        else if (Instruction.get(0).equals("CC")){}
        else if (Instruction.get(0).equals("MF")){}
        else if (Instruction.get(0).equals("TC")){}
        /*==============PROCESS OPERATION================*/
        else if (Instruction.get(0).equals("CP")){}
        else if (Instruction.get(0).equals("DP")){}

    }


    public static boolean KK_Interpret(){
        PCB pcb = MM_getRUNNING();
        int address = pcb.getPC();


        getORDER();
        if(Instruction.firstElement().equals("SP"))
        {
            return false;                      // Instruction if order is SP - End of Program
        }
        else
        {
            getARGUMENTS(arguments.get(Instruction.firstElement()));
            makeINSTRUCTION();
            pcb.setPC(address); // Instruction if order is other than SP
            return true;
        }
    }
}
