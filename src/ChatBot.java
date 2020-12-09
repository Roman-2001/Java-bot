import java.lang.reflect.Array;
import java.util.*;


public class ChatBot {
    Map<String, Player> players = new HashMap<String, Player>();
    String help = "Я бот, умею выдавать русское слово,\nполучать перевод слова на английском " +
            "и оценивать корректность перевода\nЧтобы начать игру, введите \"/play\"" +
            "\nЧтобы вывести справку, введите \"/help\"";
    ArrayList<PartOfSpeech> fullGlossary = new Glossary().fullGlossary;
    HashMap<String, Vocabulary> glossary = fullGlossary.get(0).partOfSpeechVocabulary;


    private String getChoosePartOfSpeech(ArrayList<PartOfSpeech> fullGlossary){
        String res = "";
        for (int i=1; i<=fullGlossary.size(); i++) {
            res += i + ")" + fullGlossary.get(i-1).name + "\n";
        }
        return res;
    }
    private String getChooseTheme(String[] array) {
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
                    player.lastProgramMessage = Player.LastProgramMessage.WAITPARTOFSPEECH;
                    return "Выберите номер части речи:\n" + getChoosePartOfSpeech(fullGlossary);
                }
                if (player.theme.equals("")) {
                    player.lastProgramMessage = Player.LastProgramMessage.WAITPARTOFSPEECH;
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
                player.lastProgramMessage = Player.LastProgramMessage.WAITPARTOFSPEECH;
                fullGlossary = new Glossary().fullGlossary;
                return "Выберите номер части речи:\n" + getChoosePartOfSpeech(fullGlossary);
            case "/changetheme":
                player.lastQuestion = "";
                player.theme = "";
                player.lastProgramMessage = Player.LastProgramMessage.WAITPARTOFSPEECH;
                fullGlossary = new Glossary().fullGlossary;
                return getMessage(index.toString(), id);
        }
        switch (player.lastProgramMessage) {
            case WAITPARTOFSPEECH:
                player.indexPartOfSpeech = Integer.parseInt(message) - 1;
                glossary = fullGlossary.get(player.indexPartOfSpeech).partOfSpeechVocabulary;
                player.lastProgramMessage = Player.LastProgramMessage.WAITTHEME;
                return "Выберите номер темы:\n" + getChooseTheme(glossary.keySet().toArray(String[]::new));
            case WAITTHEME:
                player.lastProgramMessage = Player.LastProgramMessage.PLAYGAME;
                int indexTheme = Integer.parseInt(message) - 1;
                player.theme = (String) glossary.keySet().toArray()[indexTheme];
                player.wordsToShow = new ArrayList<>(glossary.get(player.theme).vocabularyTheme);
                return player.PlayGame("");
            case PLAYGAME:
                    return player.PlayGame(message);
            default:
                return "Ой, что-то пошло не так";
        }
    }
}