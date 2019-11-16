import java.util.Scanner;

class UserInputLoop {
    static class StartLooping{

        private static Scanner reader = new Scanner(System.in);

        static void start() {

            while (!CheckIfInputCorrect.exitFlag) {
                System.out.print(">>");
                String input = reader.nextLine();
                try {
                    CheckIfInputCorrect.inputData(input);
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    System.out.println("[Shell]: Podano za malo argumentow");
                } catch (Exception e) {
                    System.out.println("Blad");
                }
            }
        }
    }
}
