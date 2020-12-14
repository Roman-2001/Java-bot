import java.util.ArrayList;
import java.util.Random;

public class Player {
    Integer point;
    Integer pointForBattle;
    String expectedAnswer;
    String lastQuestion;
    Integer health;
    Integer indexPartOfSpeech;
    String theme;
    enum LastProgramMessage {
        WAITPARTOFSPEECH,
        WAITTHEME,
        PLAYGAME,
        WAITBATTLE,
        FIGHTBATTLE
    }
    Boolean fight;
    LastProgramMessage lastProgramMessage;
    ArrayList<Words> wordsToShow;
    Integer lastNumber;
    String opponentID;

    Player() {
        this.point = 0;
        this.expectedAnswer = "";
        this.lastQuestion = "";
        this.theme = "";
        this.indexPartOfSpeech = -1;
        this.wordsToShow = new Vocabulary().vocabularyTheme;
        this.lastNumber = -1;
        this.health = 3;
        this.pointForBattle = 0;
        this.fight = false;
        this.opponentID = "";
    }

    public void deleteWord() {
        wordsToShow.remove(wordsToShow.get(lastNumber));
    }

    public String playGame(String message) {
        Random random = new Random();
        String result = "";
        if (!message.equals("")) {
            if (message.equals(expectedAnswer)) {
                point += 1;
                result = "Перевод верный\n" + "счёт:" + point + "\n\n";
                deleteWord();
            } else {
                result = "Перевод неверный\n";
                result += String.format("Верный перевод:\"%s\"%n", expectedAnswer);
                result += "счёт:" + point + "\n\n";
            }
        }
        if (wordsToShow.size() == 0) {
            lastNumber = -1;
            return result + "Тема усвоена! Смените тему!";
        }
        lastNumber = random.nextInt(wordsToShow.size());
        Words words = wordsToShow.get(lastNumber);
        lastQuestion = words.ruWord;
        expectedAnswer = words.enWord.toLowerCase();
        return result + lastQuestion;
    }


    public String fight(String message) {
        String result = "";
        if (!message.equals("")) {
            if (message.equals(expectedAnswer)) {
                pointForBattle += 1;
                result = "Верно!\n" + "счёт:" + point + "\n\n";
            } else {
                result = "Неверно\n" + "счёт:" + point + "\n\n";
                health -= 1;
            }
            deleteWord();
        }
        if (wordsToShow.size() == 0) {
            lastNumber = -1;
            return result;
        }
        lastNumber = wordsToShow.size()-1;
        Words words = wordsToShow.get(lastNumber);
        lastQuestion = words.ruWord;
        expectedAnswer = words.enWord.toLowerCase();
        return result + lastQuestion;
    }
}