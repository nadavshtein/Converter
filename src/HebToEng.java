import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HebToEng implements Converter {

    private final Map<Character, Character> mapByKeyboard = new HashMap<>();
    private final char[] engAlphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private final char[] hebAlphabet = "שנבגקכעיןחלךצמםפ/רדאוה'סטז".toCharArray();

    public HebToEng() {
        initKeyboardMap();
    }

    public void initKeyboardMap() {

        for (int i = 0; i < engAlphabet.length; i++) {
            mapByKeyboard.put(hebAlphabet[i], engAlphabet[i]);
        }

        mapByKeyboard.put('ת', ',');
        mapByKeyboard.put('ץ', '.');
        mapByKeyboard.put('ף', ';');
        mapByKeyboard.put('.', '/');
        mapByKeyboard.put(',', '’');
    }

    @Override
    public String convert(String text) {
        char[] textArr = text.toCharArray();
        char[] res = new char[textArr.length];
        int i = 0;

        for (char c : textArr) {

            if (mapByKeyboard.containsKey(c)) {
                res[i] = mapByKeyboard.get(c);
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
