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
        int start = tokenText.indexOf('^', index);
        int end = tokenText.indexOf('^', start + 1);
        if (start == -1 || end == -1 || start >= end) {
            throw new IllegalArgumentException("Invalid token format at index " + index);
        }

        String content = tokenText.substring(start + 1, end);
        String[] parts = content.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid token content: " + content);
        }

        int distance = Integer.parseInt(parts[0].trim());
        int length = Integer.parseInt(parts[1].trim());
        return new int[] { distance, length };
    }
}