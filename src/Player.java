import java.util.ArrayList;
import java.util.Random;

public class Player {
    Integer point;
    String expectedAnswer;
    String lastQuestion;
    Integer indexPartOfSpeech;
    String theme;
    enum LastProgramMessage {
        PARTOFSPEECH,
        THEME,
        GAME
    }
    LastProgramMessage lastProgramMessage;
    ArrayList<Words> wordsToShow;
    Integer lastNumber;

    Player() {
        this.point = 0;
        this.expectedAnswer = "";
        this.lastQuestion = "";
        this.theme = "";
        this.indexPartOfSpeech = -1;
        this.wordsToShow = new Vocabulary().vocabulary;
        this.lastNumber = -1;
    }

    public void DeleteWord(String message) {
        if (message.equals(expectedAnswer))
            wordsToShow.remove(wordsToShow.get(lastNumber));
    }

    public String PlayGame(String message){
        Random random = new Random();
        String result = "";
        if (!message.equals("")) {
            if (message.equals(expectedAnswer)) {
                point += 1;
                result = "Перевод верный\n" + "счёт:" + point + "\n\n";
                DeleteWord(message);
            }
            else {
                result = "Перевод неверный\n";
                result += String.format("Верный перевод:\"%s\"%n", expectedAnswer);
                result +=  "счёт:" + point + "\n\n";
            }
        }
        if (wordsToShow.size()==0) {
            lastNumber = -1;
            return result + "Тема усвоена! Смените тему!";
        }
        lastNumber = random.nextInt(wordsToShow.size());
        Words words = (Words) wordsToShow.get(lastNumber);
        lastQuestion = words.ruWord;
        expectedAnswer = words.enWord.toLowerCase();
        return result + lastQuestion;
    }
}