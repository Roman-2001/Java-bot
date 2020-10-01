import java.util.*;

public class ChatBot {
    static Map<Integer,Player> players = new HashMap<Integer, Player>();
    static String help = "Я бот, буду уметь выдавать английское слово,\nполучать перевод" +
            "и получать корректность перевода,\nно сейчас я выводить информацию о себе.";
    public static void getMessage(String message, Integer id) {
        switch (message) {
            case "играть":
                if (!players.containsKey((id))) {
                    players.put(id, new Player());
                }

                Game.play("", players.get(id));
                break;
            case "продолжить":
                System.out.println(message);
                break;
            case "help":
                System.out.println(help);
                break;
            default:
                Game.play(message, players.get(id));
                System.out.println(players.get(id).point);
        }
    }
}