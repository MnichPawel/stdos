import java.util.Scanner;

class Input{
    private static Scanner inputScanner= new Scanner(System.in);
    static void start() {

            while (!CheckInput.exitFlag) {
                System.out.print(">>");
                String input = inputScanner.nextLine();
                try {
                    CheckInput.inputData(input);
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    System.out.println("[Shell]: Podano za malo argumentow");
                } catch (Exception e) {
                    System.out.println("Blad");
                }
            }
        }

}
