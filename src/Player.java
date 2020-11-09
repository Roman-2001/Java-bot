
import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    Integer point;
    String expectedAnswer;
    String lastQuestion;
    String partOfSpeech;
    String theme;
    String lastProgramMessage;
    ArrayList<String[]> currentGloss;
    Integer lastNumber;
    HashMap<String, ArrayList<String[]>> themes;

    Player(){
        this.point = 0;
        this.expectedAnswer = "";
        this.lastQuestion = "";
        this.theme = "";
        this.partOfSpeech = "";
        this.lastProgramMessage = "";
        this.currentGloss = new ArrayList<String[]>();
        this.lastNumber = -1;
        this.themes = new HashMap<>();
    }
}