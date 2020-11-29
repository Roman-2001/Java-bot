import java.util.*;


public class ChatBot {
    Map<String, Player> players = new HashMap<String, Player>();
    String help = "Я бот, умею выдавать русское слово,\nполучать перевод слова на английском " +
            "и оценивать корректность перевода\nЧтобы начать игру, введите \"/play\"" +
            "\nЧтобы вывести справку, введите \"/help\"";
    String[] partsOfSpeech = new String[]{
            "nouns (существительные)",
            "verbs (глаголы)",
            "adjectives (прилагательные)",
            "adverbs (наречия)",
            "pretexts (предлоги)",
            "conjunctions (союзы)"
    };
    GlossaryReader fullGlossary = new GlossaryReader();
    HashMap<String, Vocabulary> glossary = fullGlossary.nouns.partOfSpeech;

    private String getChoose(String[] array) {
        String res = "";
        for (int i = 1; i <= array.length; i++) {
            res += i + ")" + array[i - 1] + "\n";
        }
        return res;
    }

    public String getMessage(String message, String id) {
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
        switch (message) {
            case "/play":
                if (player.indexPartOfSpeech == -1) {
                    player.lastProgramMessage = Player.LastProgramMessage.PARTOFSPEECH;
                    return "Выберите номер части речи:\n" + getChoose(partsOfSpeech);
                }
                if (player.theme.equals("")) {
                    player.lastProgramMessage = Player.LastProgramMessage.PARTOFSPEECH;
                    return getMessage(index.toString(), id);
                }
                if (player.lastQuestion.equals("")) {
                    return player.PlayGame("");
                } else
                    return player.lastQuestion;
            case "/help":
                return help;
            case "/changespeech":
                player.lastQuestion = "";
                player.theme = "";
                player.indexPartOfSpeech = -1;
                player.lastProgramMessage = Player.LastProgramMessage.PARTOFSPEECH;
                fullGlossary = new GlossaryReader();
                return "Выберите номер части речи:\n" + getChoose(partsOfSpeech);
            case "/changetheme":
                player.lastQuestion = "";
                player.theme = "";
                player.lastProgramMessage = Player.LastProgramMessage.PARTOFSPEECH;
                fullGlossary = new GlossaryReader();
                return getMessage(index.toString(), id);
            default:
                break;
        }
        switch (player.lastProgramMessage) {
            case PARTOFSPEECH:
                player.indexPartOfSpeech = Integer.parseInt(message) - 1;
                String partOfSpeech = partsOfSpeech[player.indexPartOfSpeech].split(" ")[0];
                player.lastProgramMessage = Player.LastProgramMessage.THEME;
                switch (partOfSpeech){
                    case "verbs":
                        glossary = fullGlossary.verbs.partOfSpeech;
                        break;
                    case "adjectives":
                        glossary = fullGlossary.adjectives.partOfSpeech;
                        break;
                    case "adverbs":
                        glossary = fullGlossary.adverbs.partOfSpeech;
                        break;
                    case "pretexts":
                        glossary = fullGlossary.pretexts.partOfSpeech;
                        break;
                    case "conjunctions":
                        glossary = fullGlossary.conjuctions.partOfSpeech;
                        break;
                    case "nouns":
                        glossary = fullGlossary.nouns.partOfSpeech;
                        break;
                    default: break;
                }
                return "Выберите номер темы:\n" + getChoose(glossary.keySet().toArray(String[]::new));
            case THEME:
                player.lastProgramMessage = Player.LastProgramMessage.GAME;
                int indexTheme = Integer.parseInt(message) - 1;
                player.theme = (String) glossary.keySet().toArray()[indexTheme];
                player.wordsToShow = new ArrayList<>(glossary.get(player.theme).vocabulary);
                return player.PlayGame("");
            case GAME:
                if (id.equals(""))
                    return "Чтобы начать игру, введите \"/start\"";
                else {
                    return player.PlayGame(message);
                }
            default:
                return "Ой, что-то пошло не так";
        }
    }
}