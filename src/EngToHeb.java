import java.util.HashMap;
import java.util.Map;

public class EngToHeb implements Converter {


    private static final Map<Character, Character> dict = new HashMap<>();
    private static final char[] engAlphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] hebAlphabet = "שנבגקכעיןחלךצמםפ/רדאוה'סטז".toCharArray();
    private static final char[] capEngAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    //Initialize before use
    public static void Init() {

        for (int i = 0; i < engAlphabet.length; i++) {

            dict.put(engAlphabet[i], hebAlphabet[i]);
            dict.put(capEngAlphabet[i], hebAlphabet[i]);
        }

        dict.put(',', 'ת');
        dict.put('.', 'ץ');
        dict.put(';', 'ף');
        dict.put('/', '.');
        dict.put((char) 39, ',');
        dict.put('’', ',');
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

        char lastLetter = textArr[textArr.length - 1];
        //changing direction of last char in these cases
        if (lastLetter == 'q' || lastLetter == 'w' || lastLetter == (char) 39 || !Character.isLetter(lastLetter)) {
            String result = new String(res, 0, res.length - 1);
            return result + res[res.length - 1];
        }

        return new String(res);
    }
}
