import org.checkerframework.checker.units.qual.A;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Glossary {
    ArrayList<PartOfSpeech> fullGlossary = new ArrayList<>();
    Glossary() {
        FillGlossary(fullGlossary, "nouns", "893384538");
        FillGlossary(fullGlossary, "verbs", "1824750361");
        FillGlossary(fullGlossary, "adjectives", "15517525");
        FillGlossary(fullGlossary, "adverbs", "1049149030");
        FillGlossary(fullGlossary, "pretexts", "1125540101");
        FillGlossary(fullGlossary, "conjunctions", "871500860");
        if (fullGlossary.size() == 0){
            throw new NullPointerException("Glossary is empty");
        }
    }
    private void FillGlossary(ArrayList<PartOfSpeech> fullGlossary, String name, String pageInDocument){
        try {
            fullGlossary.add(getThemes(getUrl(pageInDocument)));
            fullGlossary.get(fullGlossary.size()-1).name = name;
        } catch (IOException e) {
            fullGlossary.remove(fullGlossary.get(fullGlossary.size()-1));
            e.printStackTrace();
        }
    }
    private URL getUrl(String partOfSpeech) throws MalformedURLException {
        String begin = "https://docs.google.com/spreadsheets/d/e/2PACX-1vQ02zFoTmSVTeZD8SZ24ocWAVhKbTjn2qlVXyJsK5kFMns06nFzcd9d4yLnqcsKig/pub?gid=";
        String end = "&single=true&output=csv";
        return new URL(begin + partOfSpeech + end);
    }

    private PartOfSpeech getThemes(URL doc) throws IOException {
        PartOfSpeech themes = new PartOfSpeech();
        Vocabulary tempGloss = new Vocabulary();

        URLConnection b = doc.openConnection();
        BufferedReader c = new BufferedReader(new InputStreamReader(b.getInputStream()));
        Pattern tm = Pattern.compile(".*,,");
        String str = "";
        String[] arr = new String[2];
        String theme = "";
        String tempTheme = "";
        while ((str = c.readLine()) != null){
            Words wordAndTanslate = new Words();
            Matcher matcher = tm.matcher(str);
            if (matcher.matches()){
                theme = tempTheme;
                if (!(theme.equals(""))){
                    themes.partOfSpeechVocabulary.put(theme, tempGloss);
                    tempGloss = new Vocabulary();
                }
                tempTheme = str.substring(0, matcher.end()-2);
            }
            else {
                arr = str.split(",");
                if (arr.length == 3) {
                    wordAndTanslate.enWord = arr[0].split(" \\(")[0];
                    wordAndTanslate.ruWord = arr[2];
                    tempGloss.vocabularyTheme.add(wordAndTanslate);
                }
            }
        }
        themes.partOfSpeechVocabulary.put(tempTheme, tempGloss);
        return themes;
    }
}
