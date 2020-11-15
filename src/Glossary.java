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
    HashMap<String, ArrayList<String[]>> fullGlossary[];
    HashMap<String, ArrayList<String[]>> nouns;
    HashMap<String, ArrayList<String[]>> verbs;
    HashMap<String, ArrayList<String[]>> adjectives;
    HashMap<String, ArrayList<String[]>> adverbs;
    HashMap<String, ArrayList<String[]>> pretexts;
    HashMap<String, ArrayList<String[]>> conjuctions;
    Glossary() {
        try {
            nouns = getThemes(getUrl("nouns"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            verbs = getThemes(getUrl("verbs"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            adjectives = getThemes(getUrl("adjectives"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            adverbs = getThemes(getUrl("adverbs"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pretexts = getThemes(getUrl("pretexts"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            conjuctions = getThemes(getUrl("conjunctions"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        fullGlossary = new HashMap[]{nouns, verbs, adjectives, adverbs, pretexts, conjuctions};
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

    private HashMap<String, ArrayList<String[]>> getThemes(URL doc) throws IOException {
        HashMap<String, ArrayList<String[]>> themes = new HashMap<>();
        URLConnection b = doc.openConnection();
        BufferedReader c = new BufferedReader(new InputStreamReader(b.getInputStream()));
        Pattern tm = Pattern.compile(".*,,");
        String str = "";
        String[] arr = null;
        String theme = "";
        String tempTheme = "";
        ArrayList<String[]> tempGloss = new ArrayList<>();
        while ((str = c.readLine()) != null){
            Matcher matcher = tm.matcher(str);
            if (matcher.matches()){
                theme = tempTheme;
                if (!(theme.equals(""))){
                    themes.put(theme, tempGloss);
                    tempGloss = new ArrayList<>();
                }
                tempTheme = str.substring(0, matcher.end()-2);
            }
            else {
                arr = str.split(",");
                if (arr.length == 3){
                    if (arr[0].split("\\(")[0].equals(arr[0]))
                        tempGloss.add(new String[]{arr[2], arr[0]});
                    else tempGloss.add(new String[]{arr[2], arr[0].split(" ")[0]});
                }
            }
        }
        themes.put(tempTheme, tempGloss);
        return themes;
    }
}
