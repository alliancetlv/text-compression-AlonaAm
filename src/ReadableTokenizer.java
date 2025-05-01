/**
 * A tokenizer for encoding and decoding LZ77-style compression tokens in a readable format.
 * Tokens are represented as: "^distance,length^".
 */
public class ReadableTokenizer implements Tokenizer {

    //TODO: TASK 4
    public String toTokenString(int distance, int length) {
        return "^" + distance + "," + length + "^";

    }

    // TODO TASK 4
    public int[] fromTokenString(String tokenText, int index) {
        int end = tokenText.indexOf('^', index + 1);
        int comma = tokenText.indexOf(',', index + 1);

        String distanceStr = tokenText.substring(index + 1, comma);
        String lengthStr = tokenText.substring(comma + 1, end);

        int distance = 0;
        for (int i = 0; i < distanceStr.length(); i++) {
            distance = distance * 10 + (distanceStr.charAt(i) - '0');
        }

        int length = 0;
        for (int i = 0; i < lengthStr.length(); i++) {
            length = length * 10 + (lengthStr.charAt(i) - '0');
        }

        int tokenLength = end - index + 1;

        return new int[] { distance, length, tokenLength };
    }
}