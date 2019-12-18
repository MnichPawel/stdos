package stdos.Interpreter;

import stdos.Processes.*;
import stdos.Filesystem.*;
import stdos.Processes.ProcessManager;
import stdos.VM.VirtualMemory;

import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import static stdos.CPU.CPU.MM_getRUNNING;
import static stdos.VM.VirtualMemory.*;
import static stdos.Processes.ProcessManager.*;

public class Interpreter {
    /*==============================================STRUCTURES========================================================*/
    private static Map<String, Integer> arguments = new HashMap<>();
    private static Vector<String> Instruction = new Vector<>();

    private static PCB pcb;
    private static int address;
    private static Pliki file;


    public Interpreter() {
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
        arguments.put("CF", 1);  //CREATE FILE WITH CONTENT
        arguments.put("DF", 1);  //DOWNLOAD FILE
        arguments.put("AF", 2);  //ADD TO FILE
        arguments.put("OF", 1);  //OPEN FILE
        arguments.put("FC", 1);  //FILE CLOSE
        arguments.put("TF", 1);  //TERMINATE FILE

        //PROCESS OPERATION
        arguments.put("CP", 3);  //CREATE PROCESS //filename, processname and priority
        arguments.put("DP", 1);  //DELETE PROCESS //processname
    }

     private static void getORDER(){ //download order from VM
        Instruction.clear();
        int download;
        char temparse;
        StringBuilder temp = new StringBuilder();
        for(int i=0; i<2; i++)
        {
            download = get_value(address);
            temparse = (char)download;
            temp.append(temparse);
            address++;
        }
        Instruction.add(temp.toString());
    }

    private static void getARGUMENTS(int n){
        int download;
        char temparse = 32;
        String temp = "";
        String firstChar = "";
        for(int i=0; i<n; i++)
        {
            temp = "";
            while(true)
            {
                download = get_value(address);
                if(download!=32)
                {
                    if(firstChar.equalsIgnoreCase("")) {
                        firstChar = Character.toString(((char)download));
                    }
                    if(download==91||download==93)
                    {
                        temparse = (char)download;
                        Instruction.add(Character.toString(temparse).trim());
                        temp += temparse;
                        address++;
                    }
                    else
                    {

                        temparse = (char) download;
                        temp += temparse;
                        if(firstChar.equalsIgnoreCase("[")) {
                            Instruction.add(Character.toString(temparse).trim());
                        }
                        address++;
                    }
                }
                else
                {
                    temparse = (char) download;
                    temp += temparse;
                    address++;
                    break;
                }
            }
            if(!firstChar.equalsIgnoreCase("[")) {
                if(!temp.equalsIgnoreCase("")) {
                    if(!temp.equalsIgnoreCase(" ")) {
                        Instruction.add(temp.trim());
                    }
                }
            }
            if(temp.equalsIgnoreCase(" ")) {
                i--;
            }
            firstChar = "";
        }
    }

    private static void repairADDRESS() {
        Vector<String> InsTemp = new Vector<>();
        boolean inAddress = false;
        String temp = "";
        for(String s:Instruction) {
            if(inAddress==false) {
                InsTemp.add(s);
            } else {
                if(!s.equalsIgnoreCase("]")) {
                    temp += s;
                }
            }
            if(s.equalsIgnoreCase("[")) {
                inAddress = true;
            }
            if(s.equalsIgnoreCase("]")) {
                inAddress = false;
                InsTemp.add(temp);
                InsTemp.add("]");
            }
        }
        Instruction = InsTemp;
    }


    private static void makeINSTRUCTION() throws Exception {
        /*==============ARITHMETIC=======================*/
        if (Instruction.get(0).equals("AD")) {
            switch (Instruction.get(1)) {
                case "AX":
                    switch (Instruction.get(2)) {
                        case "[": {
                            int ax = pcb.getAx(), value = get_value_from_addr_table(Integer.parseInt(Instruction.get(3)));
                            pcb.setAx(ax + value);
                            break;
                        }
                        case "AX": {
                            int ax = pcb.getAx();
                            pcb.setAx(ax + ax);
                            break;
                        }
                        case "BX": {
                            int ax = pcb.getAx(), bx = pcb.getBx();

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
                            int bx = pcb.getBx(), value = get_value_from_addr_table(Integer.parseInt(Instruction.get(3)));
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
                        }
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
                            int cx = pcb.getCx(), value = get_value_from_addr_table(Integer.parseInt(Instruction.get(3)));
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
                            int dx = pcb.getDx(), value = get_value_from_addr_table(Integer.parseInt(Instruction.get(3)));
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
                            pcb.setDx(dx + Integer.parseInt(Instruction.get(2)));
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
                            int ax = pcb.getAx(), value = get_value_from_addr_table(Integer.parseInt(Instruction.get(3)));
                            pcb.setAx(ax - value);
                            break;
                        }
                        case "AX": {
                            int ax = pcb.getAx();
                            pcb.setAx(ax-ax);
                            break;
                        }
                        case "BX": {
                            int ax = pcb.getAx(), bx = pcb.getBx();

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
                            int bx = pcb.getBx(), value = get_value_from_addr_table(Integer.parseInt(Instruction.get(3)));
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
                        }
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
                            int cx = pcb.getCx(), value = get_value_from_addr_table(Integer.parseInt(Instruction.get(3)));
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
                            int dx = pcb.getDx(), value = get_value_from_addr_table(Integer.parseInt(Instruction.get(3)));
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
                            int ax = pcb.getAx(), value = get_value_from_addr_table(Integer.parseInt(Instruction.get(3)));
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
                            int bx = pcb.getBx(), value = get_value_from_addr_table(Integer.parseInt(Instruction.get(3)));
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
                        }
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
                            int cx = pcb.getCx(), value = get_value_from_addr_table(Integer.parseInt(Instruction.get(3)));
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
                            int dx = pcb.getDx(), value = get_value_from_addr_table(Integer.parseInt(Instruction.get(3)));
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
                set_value(Integer.parseInt(Instruction.get(2)),get_value_from_addr_table(Integer.getInteger(Instruction.get(2))+1));
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
                set_value(Integer.parseInt(Instruction.get(2)),get_value_from_addr_table(Integer.getInteger(Instruction.get(2))-1));
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
                            pcb.setAx(get_value_from_addr_table(Integer.parseInt(Instruction.get(3))));
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
                            pcb.setBx(get_value_from_addr_table(Integer.parseInt(Instruction.get(3))));
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
                            pcb.setCx(get_value_from_addr_table(Integer.parseInt(Instruction.get(3))));
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
                            pcb.setDx(get_value_from_addr_table(Integer.parseInt(Instruction.get(3))));
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
                case "[": 
                    switch (Instruction.get(4)){
                        case "[":{
                            set_value(Integer.parseInt(Instruction.get(2)),Short.parseShort(Instruction.get(5)));
                        }
                        case "AX":{
                            set_value(Integer.parseInt(Instruction.get(2)),(short)pcb.getAx());
                        }
                        case "BX":{
                            set_value(Integer.parseInt(Instruction.get(2)),(short)pcb.getBx());
                        }
                        case "CX":{
                            set_value(Integer.parseInt(Instruction.get(2)),(short)pcb.getCx());
                        }
                        case "DX":{
                            set_value(Integer.parseInt(Instruction.get(2)),(short)pcb.getDx());
                        }
                        default:{
                            set_value(Integer.parseInt(Instruction.get(2)), Short.parseShort(Instruction.get(4)));
                        }
                    }
            }
        }
        /*================WORK_ON_FILES==================*/
        else if (Instruction.get(0).equals("CF")){
            file.KP_utwP(Instruction.get(1));
        }
        else if (Instruction.get(0).equals("OF")){
           file.KP_otwP(Instruction.get(1));
        }
        else if (Instruction.get(0).equals("FC")){
            file.KP_zamkP(Instruction.get(1));
        }
        else if (Instruction.get(0).equals("DF")){
            file.KP_pobP(Instruction.get(1));
        }
        else if (Instruction.get(0).equals("AF")){
            switch (Instruction.get(2)) {
                case "[": {
                    file.KP_dopP(Instruction.get(1),Short.valueOf(get_value_from_addr_table(Integer.parseInt(Instruction.get(3)))).toString().getBytes());
                    break;
                }
                case "AX": {

                    file.KP_dopP(Instruction.get(1),Integer.valueOf(pcb.getAx()).toString().getBytes());
                    break;
                }
                case "BX": {
                    file.KP_dopP(Instruction.get(1),Integer.valueOf(pcb.getBx()).toString().getBytes());
                    break;
                }
                case "CX": {
                    file.KP_dopP(Instruction.get(1),Integer.valueOf(pcb.getCx()).toString().getBytes());
                    break;
                }
                case "DX": {
                    file.KP_dopP(Instruction.get(1),Integer.valueOf(pcb.getDx()).toString().getBytes());
                    break;
                }
                default: {
                    file.KP_dopP(Instruction.get(1),Instruction.get(2).getBytes());
                    break;
                }
            }
        }
        else if (Instruction.get(0).equals("TF")){
            file.KP_usunP(Instruction.get(1));
        }
        /*==============PROCESS OPERATION================*/
        else if (Instruction.get(0).equals("CP")){
            KM_CreateProcess(Instruction.get(1),Instruction.get(2),Integer.parseInt(Instruction.get(3)));
        }
        else if (Instruction.get(0).equals("DP")){
            KM_TerminateProcess(Instruction.get(1));
        }

    }

    private static void printCODE() { //Written by KM
        StringBuilder temp = new StringBuilder();
        boolean inAddress = false;
        for(String s : Instruction) {
            if(s.equalsIgnoreCase("]")) {
                inAddress = false;
                temp.append(s).append(" ");
            } else if(s.equalsIgnoreCase("[")) {
                inAddress = true;
                temp.append(s);
            } else if(inAddress){
                temp.append(s);
            } else {
                temp.append(s).append(" ");
            }
        }
        System.out.println(temp);
    }


    public static boolean KK_Interpret() throws Exception {
        pcb = MM_getRUNNING(); 
        address = pcb.getPC(); 
        file = new Pliki();

        getORDER();
        if(Instruction.firstElement().equals("SP"))
        {
            printCODE();
            return false;                      
        }
        else
        {
            getARGUMENTS(arguments.get(Instruction.firstElement()));
            repairADDRESS();
            printCODE();
            makeINSTRUCTION();
            pcb.setPC(address);
            return true;
        }
    }
}
