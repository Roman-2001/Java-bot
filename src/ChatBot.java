import java.util.*;

public class ChatBot {
    static Map<Integer,Player> players = new HashMap<Integer, Player>();
    static String help = "Я бот, умею выдавать русское слово,\nполучать перевод слова на английском " +
            "и оценивать корректность перевода\nЧтобы начать игру, введите \"играть\"" +
            "\nЧтобы вывести справку, введите \"help\"";
    public static String getMessage(String message, Integer id) {
        switch (message) {
            case "играть":
                if (!players.containsKey((id))) {
                    players.put(id, new Player());
                    return Game.play("", players.get(id));
                }
                else return players.get(id).lastQuestion;
            case "help":
                return help;
            default:
                if (id == 0)
                    return "Чтобы начать игру, введите \"играть\"";
                else {
                    return Game.play(message, players.get(id));
                }
        }
    }
}