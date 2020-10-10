import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Привет!");
        System.out.println(ChatBot.help);
        Integer id = 0;
        while (true) {
            Scanner a = new Scanner(System.in);
            String message = a.next();
            if (message.equals("играть")) {
                System.out.println("Введите числовой индентификатор");
                id = a.nextInt();
            }
            System.out.println(ChatBot.getMessage(message, id));
        }
    }
}