package stdos.Interface;
import java.util.Scanner;

class Input{
    public static void startInterface() throws UnsupportedOperationException {
        Help.printLogo();

        Scanner inputScanner = new Scanner(System.in);
            while (!SwitchInput.exitFlag) {
                System.out.print(">>");
                String input = inputScanner.nextLine();

                try {
                    SwitchInput.inputSwitch(input);
               } catch (Exception e) {
                    Sound.errorSound();
                   System.out.println("[Shell]: Blad modulu" + e.getMessage());
                }
            }

    }
    }


