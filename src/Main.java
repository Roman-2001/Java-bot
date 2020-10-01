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
                System.out.println("Введите индентификатор");
                id = a.nextInt();
            }
            ChatBot.getMessage(message, id);
            ChatBot.players.get(id).lastPhrase = message;
        }
    }
} 