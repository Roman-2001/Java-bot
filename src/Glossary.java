import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Glossary {
    public URL getUrl(String message) throws MalformedURLException {
        String begin = "https://docs.google.com/spreadsheets/d/e/2PACX-1vQ02zFoTmSVTeZD8SZ24ocWAVhKbTjn2qlVXyJsK5kFMns06nFzcd9d4yLnqcsKig/pub?gid=";
        String end = "&single=true&output=csv";
        String middle = "";
        switch (message){
            case "verb": middle = "1824750361"; break;
            case "adjectives": middle = "15517525"; break;
            case "adverb": middle = "1049149030"; break;
            case "pretext": middle = "1125540101"; break;
            case "conjuction": middle = "871500860"; break;
            default: middle = "893384538"; break;
        }
        return new URL(begin + middle + end);
    }

    public HashMap<String, ArrayList<String[]>> getThemes(URL doc) throws IOException {
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
                    tempGloss.add(new String[]{arr[2], arr[0]});
                }
            }
        }
        themes.put(tempTheme, tempGloss);
        return themes;
    }
}
