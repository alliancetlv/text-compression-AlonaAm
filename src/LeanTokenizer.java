/**
 * A lightweight tokenizer for encoding and decoding.
 * Uses a compact three-character format to represent references in the compressed text.
 */
public class LeanTokenizer implements Tokenizer {
    public LeanTokenizer() {

    }

    //TODO: TASK 10
    public int[] fromTokenString(String tokenText, int index) {
        if (index < 0 || index + 2 >= tokenText.length()) {
            throw new IllegalArgumentException("Invalid index or tokenText");
        }

        char distanceChar = tokenText.charAt(index + 1);

        char lengthChar = tokenText.charAt(index + 2);

        int distance = (int) distanceChar;
        int length = (int) lengthChar;

        return new int[] {distance, length};
    }

    //TODO: TASK 10
    public String toTokenString(int distance, int length) {
        return "^" + (char) distance + "," + (char) length + "^";
    }
}