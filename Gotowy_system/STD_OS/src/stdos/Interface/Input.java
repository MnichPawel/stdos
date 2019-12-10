import java.util.Scanner;

class Input{
    private static Scanner inputScanner= new Scanner(System.in);
    static void start() throws UnsupportedOperationException {
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
