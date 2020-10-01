import java.util.*;
import java.util.Random;

public class Game {
    //static Map<String, String> gloss = new HashMap<String, String>();
    public static void play (String message, Player player) {
        //Random random = new Random();
        //int number = random.nextInt(51380) * 2 + 1;
        //
        //System.out.println(message.equals("tea"));
        System.out.println("чай");
        player.point += message.equals("tea") ? 1 : 0;
    }

}