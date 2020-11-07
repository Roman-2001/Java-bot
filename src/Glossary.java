import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
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

    public static HashMap<String, HashMap<String, String>> getThemes(URL doc) throws IOException {
        HashMap<String, HashMap<String,String>> themes = new HashMap<>();
        URLConnection b = doc.openConnection();
        BufferedReader c = new BufferedReader(new InputStreamReader(b.getInputStream()));
        Pattern tm = Pattern.compile(".*,,");
        Pattern rus = Pattern.compile(" ,+");
        Pattern eng = Pattern.compile(",\\[+");
        String str = "";
        String theme = "";
        String temp = "";
        HashMap<String, String> tempGloss = new HashMap<>();
        String r = "";
        String e = "";
        while ((str = c.readLine()) != null){
            Matcher matcher = tm.matcher(str);
            if (matcher.matches()){
                theme = temp;
                if (!(theme.equals(""))){
                    themes.put(theme, tempGloss);
                    tempGloss = new HashMap<>();
                }
                temp = str.substring(0, matcher.end()-2);
            }
            else {
                Matcher mrus = rus.matcher(str);
                Matcher meng = eng.matcher(str);
                if (mrus.matches()) r = str.substring(mrus.end()+2, str.length());
                if (meng.matches()) e = str.substring(0, meng.start());
                tempGloss.put(r, e);
            }
        }
        themes.put(temp, tempGloss);
        return themes;
    }
}
