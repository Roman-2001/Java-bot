import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

public class ChatBot {
    Map<String,Player> players = new HashMap<String, Player>();
    String help = "Я бот, умею выдавать русское слово,\nполучать перевод слова на английском " +
            "и оценивать корректность перевода\nЧтобы начать игру, введите \"/play\"" +
            "\nЧтобы вывести справку, введите \"/help\"";
    String[] partsOfSpeech = {
            "nouns (существительные)",
            "verbs (глаголы)",
            "adjectives (прилагательные)",
            "adverbs (наречия)",
            "pretexts (предлоги)",
            "conjuctions (союзы)"
    };
    HashMap<String, ArrayList<String[]>>[] glossary = new Glossary().fullGlossary;

    public String getChoose(String[] array) {
        String res = "";
        for (int i = 1; i <= array.length; i++) {
            res += i + ")" + array[i-1] + "\n";
        }
        return res;
    }

    public String getMessage(String message, String id) throws IOException {
        Game game = new Game();
        if (message.equals("/start")) {
            if (!players.containsKey((id))) {
                players.put(id, new Player());
            }
            return "Чтобы начать игру, введите \"/play\"" +
                    "\nЧтобы вывести справку, введите \"/help\"";
        }
        message = message.toLowerCase();
        Player player = players.get(id);
        Integer index = player.indexPartOfSpeech + 1;
        switch (player.lastProgramMessage) {
            case "partOfSpeech":
                player.indexPartOfSpeech = Integer.parseInt(message) - 1;
                player.lastProgramMessage = "theme";
                String[] themes = glossary[player.indexPartOfSpeech].keySet().toArray(String[]::new);
                return "Выберите номер темы:\n" + getChoose(themes);
            case "theme":
                HashMap<String, ArrayList<String[]>> dictionaryWithThemes = glossary[player.indexPartOfSpeech];
                player.lastProgramMessage = "";
                Integer indexTheme = Integer.parseInt(message) - 1;
                player.theme = (String) dictionaryWithThemes.keySet().toArray()[indexTheme];
                player.currentGloss = dictionaryWithThemes.get(player.theme);
                return game.play("", player);
            default: break;
        }
        switch (message) {
            case "/play":
                if (player.indexPartOfSpeech == -1) {
                    player.lastProgramMessage = "partOfSpeech";
                    return "Выберите номер части речи:\n" + getChoose(partsOfSpeech);
                }
                if (player.theme.equals("")) {
                    player.lastProgramMessage = "partOfSpeech";
                    return getMessage(index.toString(), id);
                }
                if (player.lastQuestion.equals("")) {
                    return game.play("", player);
                }
                else
                    return player.lastQuestion;
            case "/help":
                return help;
            case "/changespeech":
                player.lastQuestion = "";
                player.theme = "";
                player.indexPartOfSpeech = -1;
                player.lastProgramMessage = "partOfSpeech";
                return "Выберите номер части речи:\n" + getChoose(partsOfSpeech);
            case "/changetheme":
                player.lastQuestion = "";
                player.theme = "";
                player.lastProgramMessage = "partOfSpeech";
                return getMessage(index.toString(), id);
            default:
                if (id.equals(""))
                    return "Чтобы начать игру, введите \"/play\"";
                else {
                    return game.play(message, player);
                }
        }
    }
}