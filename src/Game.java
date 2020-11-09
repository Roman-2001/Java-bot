import java.util.ArrayList;
import java.util.Random;

public class Game {
    public String play (String message, Player player) {
        ArrayList gloss = player.currentGloss;
        Random random = new Random();
        String result = "";
        if (!message.equals("")) {
            if (message.equals(player.expectedAnswer)) {
                player.point += 1;
                result = "Перевод верный\n";
                gloss.remove(player.lastNumber);
            }
            else {
                result = "Перевод неверный\n";
                result += String.format("Верный перевод:\"%s\"%n", player.expectedAnswer);
            }
        }
        if (gloss.size()==0) {
            player.lastNumber = -1;
            return "Тема усвоена!";
        }
        player.lastNumber = random.nextInt(gloss.size());
        String[] arrStr = (String[]) gloss.get(player.lastNumber);
        player.lastQuestion = arrStr[0];
        player.expectedAnswer = arrStr[1];
        return result +"счёт:" + player.point + "\n" + player.lastQuestion;

        //getClass().getResource() - чтение данных из файла
        //подключить через googledoc.
    }

}