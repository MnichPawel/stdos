package stdos.Interface;

import java.util.Scanner;

class Input{
    public static void startInterface() throws UnsupportedOperationException {
        Scanner inputScanner = new Scanner(System.in);
            while (!SwitchInput.exitFlag) {
                System.out.print(">>");
                String input = inputScanner.nextLine();
                ///#TODO: linia 107 running CPU jest nullem exception
                try {
                    SwitchInput.inputSwitch(input);
               } catch (Exception e) {
                   System.out.println("[Shell]: Blad modulu" + e.getMessage());
                }
            }
        }
    }


