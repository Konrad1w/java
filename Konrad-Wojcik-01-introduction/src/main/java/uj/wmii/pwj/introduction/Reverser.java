package uj.wmii.pwj.introduction;

public class Reverser {
    public static String reverse(String input) {
        if (input == null)
            return null;
        else {
            String withoutSpacesString = input.trim();
            String reverseString = "";
            for (int i = withoutSpacesString.length() - 1; i >= 0; i--) {
                reverseString += withoutSpacesString.charAt(i);
            }
            return reverseString;
        }
    }


    public static String reverseWords(String input) {
        String withoutSpacesString = input.trim();
        String[] arrWords;
        arrWords = withoutSpacesString.split(" ");
        String result = "";
        for (int i = arrWords.length - 1; i >= 0; i--) {
            if (i == 0)
                result += arrWords[i];
            else
                result += (arrWords[i] + ' ');
        }
        return result;

    }
}
