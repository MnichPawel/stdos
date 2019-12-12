package stdos.Interface;

import java.util.Scanner;

class Input{
    public static void startInterface() throws UnsupportedOperationException {
        Scanner inputScanner = new Scanner(System.in);
            while (!SwitchInput.exitFlag) {
                String input = inputScanner.nextLine();
                try {
                    SwitchInput.inputSwitch(input);
                } catch (Exception e) {
                    System.out.println("[Shell]: Blad");
                }
            }
        }
    }


