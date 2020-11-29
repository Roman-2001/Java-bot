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


public class GlossaryReader {
    PartOfSpeech nouns;
    PartOfSpeech verbs;
    PartOfSpeech adjectives;
    PartOfSpeech adverbs;
    PartOfSpeech pretexts;
    PartOfSpeech conjuctions;
    GlossaryReader() {
        try {
            nouns = getThemes(getUrl("nouns"));
        } catch (IOException e) {
            System.out.println("Exception in nouns");
            e.printStackTrace();
        }
        try {
            verbs = getThemes(getUrl("verbs"));
        } catch (IOException e) {
            System.out.println("Exception in verbs");
            e.printStackTrace();
        }
        try {
            adjectives = getThemes(getUrl("adjectives"));
        } catch (IOException e) {
            System.out.println("Exception in adj.");
            e.printStackTrace();
        }
        try {
            adverbs = getThemes(getUrl("adverbs"));
        } catch (IOException e) {
            System.out.println("Exception in adverbs");
            e.printStackTrace();
        }
        try {
            pretexts = getThemes(getUrl("pretexts"));
        } catch (IOException e) {
            System.out.println("Exception in pretexts");
            e.printStackTrace();
        }
        try {
            conjuctions = getThemes(getUrl("conjunctions"));
        } catch (IOException e) {
            System.out.println("Exception in conj.");
            e.printStackTrace();
        }
        //fullGlossary = new Glossary[]{nouns, verbs, adjectives, adverbs, pretexts, conjuctions};
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
                    themes.partOfSpeech.put(theme, tempGloss);
                    tempGloss = new Vocabulary();
                }
                tempTheme = str.substring(0, matcher.end()-2);
            }
            else {
                arr = str.split(",");
                if (arr.length == 3) {
                    wordAndTanslate.enWord = arr[0].split(" \\(")[0];
                    wordAndTanslate.ruWord = arr[2];
                    tempGloss.vocabulary.add(wordAndTanslate);
                }
            }
        }
        themes.partOfSpeech.put(tempTheme, tempGloss);
        return themes;
    }
}
