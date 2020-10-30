import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HebToEng implements Converter {

    private static final Map<Character, Character> dict = new HashMap<>();
    private static final char[] engAlphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] hebAlphabet = "שנבגקכעיןחלךצמםפ/רדאוה'סטז".toCharArray();

    public static void Init() {

        for (int i = 0; i < engAlphabet.length; i++) {

            dict.put(hebAlphabet[i], engAlphabet[i]);
        }

        dict.put('ת', ',');
        dict.put('ץ', '.');
        dict.put('ף', ';');
        dict.put('.', '/');
        dict.put(',', '’');
    }


    @Override
    public String convert(String text) {

        char[] textArr = text.toCharArray();
        char[] res = new char[textArr.length];
        int i = 0;

        for (char c : textArr) {

            if (dict.containsKey(c)) {
                res[i] = dict.get(c);
            } else {
                res[i] = textArr[i];
            }
            i++;
        }

        char lastLetter = textArr[0];
        String str = lastLetter + "";
        Pattern p = Pattern.compile("\\p{InHebrew}");
        Matcher m = p.matcher(str); // checking hebrew letter
        if (lastLetter == 'ץ' || lastLetter == 'ת' || !m.matches()) { //changing direction of last char in these cases
            String result = new String(res, 1, res.length);
            return res[0] + result;
        }

        return new String(res);
    }
}
