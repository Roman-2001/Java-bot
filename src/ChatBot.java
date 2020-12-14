import java.lang.reflect.Array;
import java.util.*;


public class ChatBot {
    Map<String, Player> players = new HashMap<String, Player>();
    String help = "Я бот, умею выдавать русское слово,\nполучать перевод слова на английском " +
            "и оценивать корректность перевода\nЧтобы начать игру, введите \"/play\"" +
            "\nЧтобы вывести справку, введите \"/help\"\n" +
            "Чтобы сразиться с другим игроком\nвведите \"/fight\"";
    ArrayList<PartOfSpeech> fullGlossary = new Glossary().fullGlossary;
    HashMap<String, Vocabulary> glossary = fullGlossary.get(0).partOfSpeechVocabulary;
    ArrayList<String> themes = new ArrayList<>();



    private String getChoosePartOfSpeech(){
        String res = "Выберите номер части речи:\n";
        for (int i=1; i<=fullGlossary.size(); i++) {
            res += i + ")" + fullGlossary.get(i-1).name + "\n";
        }
        return res;
    }
    private String getChooseTheme(String[] array) {
        String res = "Выберите номер темы:\n";
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
        switch (message.split("_")[0]) {
            case "/play":
                if (player.indexPartOfSpeech == -1) {
                    player.lastProgramMessage = Player.LastProgramMessage.WAITPARTOFSPEECH;
                    return getChoosePartOfSpeech();
                }
                if (player.theme.equals("")) {
                    player.lastProgramMessage = Player.LastProgramMessage.WAITPARTOFSPEECH;
                    return getMessage(index.toString(), id);
                }
                if (player.lastQuestion.equals("")) {
                    return player.playGame("");
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
                return getChoosePartOfSpeech();
            case "/changetheme":
                player.lastQuestion = "";
                player.theme = "";
                player.lastProgramMessage = Player.LastProgramMessage.WAITPARTOFSPEECH;
                fullGlossary = new Glossary().fullGlossary;
                return getMessage(index.toString(), id);
            case "/fight":
                player.fight = true;
                player.lastProgramMessage = Player.LastProgramMessage.WAITPARTOFSPEECH;
                return getChoosePartOfSpeech();
            case "/yes":
                if (!(players.get(message.split("_")[1]).opponentID.equals("")))
                    return "Пользователь уже играет\n" + help;
                player.opponentID = message.split("_")[1];
                player.wordsToShow = players.get(player.opponentID).wordsToShow;
                //players.get(player.opponentID).opponentID = id;
                player.fight = true;
                player.lastProgramMessage = Player.LastProgramMessage.WAITBATTLE;
        }
        switch (player.lastProgramMessage) {
            case WAITPARTOFSPEECH:
                player.indexPartOfSpeech = Integer.parseInt(message) - 1;
                glossary = fullGlossary.get(player.indexPartOfSpeech).partOfSpeechVocabulary;
                player.lastProgramMessage = Player.LastProgramMessage.WAITTHEME;
                themes = new ArrayList<String>(glossary.keySet());
                return getChooseTheme(themes.toArray(String[]::new));
            case WAITTHEME:
                int indexTheme = Integer.parseInt(message) - 1;
                player.theme = themes.get(indexTheme);
                player.wordsToShow = new ArrayList<>(glossary.get(player.theme).vocabularyTheme);
                if (player.fight){
                    player.lastProgramMessage = Player.LastProgramMessage.WAITBATTLE;
                    return "Ищем соперника. Ждите";
                }
                else {
                    player.lastProgramMessage = Player.LastProgramMessage.PLAYGAME;
                    return player.playGame("");
                }
            case PLAYGAME:
                return player.playGame(message);
            case WAITBATTLE:
                player.opponentID = message;
                if (!players.containsKey(player.opponentID))
                    return "Ожидайте";
                player.lastProgramMessage = Player.LastProgramMessage.FIGHTBATTLE;
                return "Соперник найден! Начинается игра!\n" + player.fight("");
            case FIGHTBATTLE:
                return "Счёт соперника:" + players.get(player.opponentID).pointForBattle + player.fight(message);
            default:
                return "Ой, что-то пошло не так";
        }
    }
}