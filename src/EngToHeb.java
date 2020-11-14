import java.util.HashMap;
import java.util.Map;

public class EngToHeb implements Converter {

    private final Map<Character, Character> mapByKeyboard = new HashMap<>();
    private final char[] engAlphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private final char[] hebAlphabet = "שנבגקכעיןחלךצמםפ/רדאוה'סטז".toCharArray();
    private final char[] capEngAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public EngToHeb() {
        initKeyboardMap();
    }

    //Initialize before use
    public void initKeyboardMap() {

        for (int i = 0; i < engAlphabet.length; i++) {

            mapByKeyboard.put(engAlphabet[i], hebAlphabet[i]);
            mapByKeyboard.put(capEngAlphabet[i], hebAlphabet[i]);
        }

        mapByKeyboard.put(',', 'ת');
        mapByKeyboard.put('.', 'ץ');
        mapByKeyboard.put(';', 'ף');
        mapByKeyboard.put('/', '.');
        mapByKeyboard.put((char) 39, ',');
        mapByKeyboard.put('’', ',');
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

        char lastLetter = textArr[textArr.length - 1];
        //changing direction of last char in these cases
        if (lastLetter == 'q' || lastLetter == 'w' || lastLetter == (char) 39 || !Character.isLetter(lastLetter)) {
            String result = new String(res, 0, res.length - 1);
            return result + res[res.length - 1];
        }

        return new String(res);
    }


}
