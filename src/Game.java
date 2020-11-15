import java.util.ArrayList;
import java.util.Random;

public class Game {
    public String play (String message, Player player) {
        Random random = new Random();
        String result = "";
        if (!message.equals("")) {
            if (message.equals(player.expectedAnswer)) {
                player.point += 1;
                result = "Перевод верный\n";
                player.currentGloss.remove(player.currentGloss.get(player.lastNumber));
            }
            else {
                result = "Перевод неверный\n";
                result += String.format("Верный перевод:\"%s\"%n", player.expectedAnswer);
            }
        }
        if (player.currentGloss.size()==0) {
            player.lastNumber = -1;
            return "Тема усвоена! Смените тему!";
        }
        ArrayList gloss = player.currentGloss;
        player.lastNumber = random.nextInt(gloss.size());
        String[] arrStr = (String[]) gloss.get(player.lastNumber);
        player.lastQuestion = arrStr[0];
        player.expectedAnswer = arrStr[1].toLowerCase();
        return result +"счёт:" + player.point + "\n\n" + player.lastQuestion;
    }

}