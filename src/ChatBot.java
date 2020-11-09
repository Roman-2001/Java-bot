import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

public class ChatBot {
    Map<String,Player> players = new HashMap<String, Player>();
    String help = "Я бот, умею выдавать русское слово,\nполучать перевод слова на английском " +
            "и оценивать корректность перевода\nЧтобы начать игру, введите \"/play\"" +
            "\nЧтобы вывести справку, введите \"/help\"";
    String[] partsOfSpeech = {
            "noun (существительное)",
            "verb (глагол)",
            "adjectives (прилагательное)",
            "adverb (наречие)",
            "pretext (предлоги)",
            "conjuction (союзы)"
    };

    public String getChoose(String[] array) {
        String res = "";
        for (int i = 1; i <= array.length; i++) {
            res += i + ")" + array[i-1] + "\n";
        }
        return res;
    }

    public String getMessage(String message, String id) throws IOException {
        Game game = new Game();
        Glossary glossary = new Glossary();
        message = message.toLowerCase();
        if (players.containsKey(id))
            switch (players.get(id).lastProgramMessage) {
                case "partOfSpeech":
                    players.get(id).partOfSpeech = partsOfSpeech[Integer.parseInt(message) - 1];
                    players.get(id).lastProgramMessage = "theme";
                    players.get(id).themes = (HashMap<String, ArrayList<String[]>>) glossary.getThemes(glossary.getUrl(players.get(id).partOfSpeech));
                    return "Выберите тему:\n" + getChoose(players.get(id).themes.keySet().toArray(String[]::new));
                case "theme":
                    players.get(id).theme = (String) glossary.getThemes(glossary.getUrl(players.get(id).partOfSpeech)).keySet().toArray()[Integer.parseInt(message) - 1];
                    players.get(id).lastProgramMessage = "";
                    players.get(id).currentGloss = glossary.getThemes(glossary.getUrl(players.get(id).partOfSpeech)).get(players.get(id).theme);
                    return game.play("", players.get(id));
                default: break;
            }
        switch (message) {
            case "/start":
                if (!players.containsKey((id))) {
                    players.put(id, new Player());
                }
                return "Чтобы начать игру, введите \"/play\"" +
                        "\nЧтобы вывести справку, введите \"/help\"";
            case "/play":
                if (players.get(id).partOfSpeech.equals("")) {
                    players.get(id).lastProgramMessage = "partOfSpeech";
                    return "Выберите часть речи:\n" + getChoose(partsOfSpeech);
                }
                if (players.get(id).theme.equals("")) {
                    players.get(id).lastProgramMessage = "theme";
                    return "Выберите тему:\n" + getChoose(players.get(id).themes.keySet().toArray(String[]::new));
                }
                if (players.get(id).lastQuestion.equals("")) {
                    return game.play("", players.get(id));
                }
                else
                    return players.get(id).lastQuestion;
            case "/help":
                return help;
            case "/changespeech":
                players.get(id).lastQuestion = "";
                players.get(id).theme = "";
                players.get(id).partOfSpeech = "";
                players.get(id).lastProgramMessage = "partOfSpeech";
                return "Выберите часть речи:\n" + getChoose(partsOfSpeech);
            case "/changetheme":
                players.get(id).lastQuestion = "";
                players.get(id).theme = "";
                players.get(id).lastProgramMessage = "theme";
                return "Выберите тему:\n" + getChoose(players.get(id).themes.keySet().toArray(String[]::new));
            default:
                if (id.equals(""))
                    return "Чтобы начать игру, введите \"/play\"";
                else {
                    return game.play(message, players.get(id));
                }
        }
    }
}