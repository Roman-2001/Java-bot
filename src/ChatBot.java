import java.util.*;

public class ChatBot {
    Map<String,Player> players = new HashMap<String, Player>();
    String help = "Я бот, умею выдавать русское слово,\nполучать перевод слова на английском " +
            "и оценивать корректность перевода\nЧтобы начать игру, введите \"/play\"" +
            "\nЧтобы вывести справку, введите \"help\"";
    public String getMessage(String message, String id) {
        Game game = new Game();
        switch (message) {
            case "/play":
                if (!players.containsKey((id))) {
                    players.put(id, new Player());
                    return game.play("", players.get(id));
                }
                else return players.get(id).lastQuestion;
            case "/help":
                return help;
            default:
                if (id.equals(""))
                    return "Чтобы начать игру, введите \"/play\"";
                else {
                    return game.play(message, players.get(id));
                }
        }
    }
}