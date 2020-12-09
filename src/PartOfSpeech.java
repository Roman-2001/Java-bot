import java.util.ArrayList;
import java.util.HashMap;

public class PartOfSpeech extends HashMap{
    HashMap<String, Vocabulary> partOfSpeechVocabulary;
    String name;

    PartOfSpeech() {
        this.name = "";
        this.partOfSpeechVocabulary = new HashMap<>();
    }
}
