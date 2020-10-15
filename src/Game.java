import java.util.Random;

public class Game {
    String[][] gloss = {
            {"стол", "table"},
            {"чай", "tea"},
            {"кофе", "coffee"},
            {"стул", "chair"},
            {"окно", "window"}
    };

    public String play (String message, Player player) {
        Random random = new Random();
        String result = "";
        int number = random.nextInt(5);
        if (!message.equals("")) {
            if (message.equals(player.expectedAnswer)) {
                player.point += 1;
                result = "Перевод верный\n";
            }
            else {
                result = "Перевод неверный\n";
                result += String.format("Верный перевод:\"%s\"%n", player.expectedAnswer);
            }
        }
        player.lastQuestion = gloss[number][0];
        player.expectedAnswer = gloss[number][1];
        return result +"счёт:" + player.point + "\n" + player.lastQuestion;
    }

}