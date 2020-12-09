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
        FillingGlossary(fullGlossary, "nouns", 0);
        FillingGlossary(fullGlossary, "verbs", 1);
        FillingGlossary(fullGlossary, "adjectives", 2);
        FillingGlossary(fullGlossary, "adverbs", 3);
        FillingGlossary(fullGlossary, "pretexts", 4);
        FillingGlossary(fullGlossary, "conjunctions", 5);
        if (fullGlossary.size() == 0){
            throw new NullPointerException("Glossary is empty");
        }
    }
    private void FillingGlossary(ArrayList<PartOfSpeech> fullGlossary, String name, int index){
        try {
            fullGlossary.add(getThemes(getUrl(name)));
            fullGlossary.get(index).name = name;
        } catch (IOException e) {
            fullGlossary.remove(fullGlossary.get(index));
            e.printStackTrace();
        }
    }
    private URL getUrl(String partOfSpeech) throws MalformedURLException {
        String begin = "https://docs.google.com/spreadsheets/d/e/2PACX-1vQ02zFoTmSVTeZD8SZ24ocWAVhKbTjn2qlVXyJsK5kFMns06nFzcd9d4yLnqcsKig/pub?gid=";
        String end = "&single=true&output=csv";
        String middle = "";
        switch (partOfSpeech){
            case "nouns": middle = "893384538"; break;
            case "verbs": middle = "1824750361"; break;
            case "adjectives": middle = "15517525"; break;
            case "adverbs": middle = "1049149030"; break;
            case "pretexts": middle = "1125540101"; break;
            case "conjunctions": middle = "871500860"; break;
        }
        return new URL(begin + middle + end);
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
