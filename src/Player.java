
import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    Integer point;
    String expectedAnswer;
    String lastQuestion;
    Integer indexPartOfSpeech;
    String theme;
    String lastProgramMessage;
    ArrayList<String[]> currentGloss;
    Integer lastNumber;

    Player(){
        this.point = 0;
        this.expectedAnswer = "";
        this.lastQuestion = "";
        this.theme = "";
        this.indexPartOfSpeech = -1;
        this.lastProgramMessage = "";
        this.currentGloss = new ArrayList<String[]>();
        this.lastNumber = -1;
    }
}