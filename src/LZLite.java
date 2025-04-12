/**
 * Class for performing LZ77 compression/decompression.
 */


/**
 * Class for performing compression/decompression loosely based on LZ77.
 */
public class LZLite {
    public static int MAX_WINDOW_SIZE = 65535;
    private int windowSize;
    private String slidingWindow;
    private Tokenizer tokenizer;

    //TODO: TASK 1
    public LZLite(int windowSize, boolean readable) {
        this.slidingWindow = "";
        this.windowSize = windowSize;
        if (readable) {
            this.tokenizer = new ReadableTokenizer();
        } else {
            this.tokenizer = new LeanTokenizer();
        }
    }

    //TODO: TASK 2
    public void appendToSlidingWindow(String st) {
        slidingWindow += st;
        if (slidingWindow.length() > windowSize) {
            slidingWindow = slidingWindow.substring(slidingWindow.length() - windowSize);
        }
    }

    //TODO: TASK 3
    public String maxMatchInWindow(String input, int pos) {
        int bestLength = 0;
        int bestStart = -1;

        int windowStart = Math.max(0, pos - MAX_WINDOW_SIZE);

        for (int i = windowStart; i < pos; i++) {
            int length = 0;

            while (pos + length < input.length() &&
                    input.charAt(i + length) == input.charAt(pos + length)) {
                length++;
                if (i + length >= pos) break;
            }

            if (length > bestLength) {
                bestLength = length;
                bestStart = i;
            }
        }

        if (bestLength == 0) {
            return "";
        }

        return input.substring(bestStart, bestStart + bestLength);
    }

    private String findMatch(String input, int pos, String window, int offset) {
        int maxLength = 0;
        String match = "";
        for (int i = 0; i < input.length() - pos; i++) {
            if (pos + i < input.length() && input.charAt(pos + i) == window.charAt(offset + i)) {
                maxLength++;
                match = input.substring(pos, pos + maxLength);
            } else {
                break;
            }
        }
        return match;
    }

    //TODO: TASK 5
    public String zip(String input) {
        StringBuilder st = new StringBuilder();
        int pos = 0;

        while (pos < input.length()) {
            String match = maxMatchInWindow(input, pos);
            if (match.length() > 1 && match.length() < input.substring(pos).length()) {
                int distance = slidingWindow.indexOf(match);
                st.append(tokenizer.toTokenString(distance, match.length()));
                pos += match.length();
            } else {
                st.append(input.charAt(pos));
                pos++;
            }
            appendToSlidingWindow(input.substring(pos - 1, pos));
        }
        return st.toString();
    }


    //TODO: TASK 6
    public static String zipFileName(String fileName) {
        if (!fileName.endsWith(".txt"))
            return null;
        return fileName.substring(0, fileName.length() - 4) + ".lz77.txt";
    }

    //TODO: TASK 6
    public static String unzipFileName(String fileName) {
        if (!fileName.endsWith(".lz77.txt"))
            return null;
        return fileName.substring(0, fileName.length() - 9) + ".decompressed.txt";
    }

    //TODO: TASK 7
    public static String zipFile(String file, int windowSize, boolean readable) {
        String newFileName = zipFileName(file);
        if (newFileName == null) return null;

        String content = FileUtils.readFile(file);
        LZLite lzLite = new LZLite(windowSize, readable);
        String compressedContent = lzLite.zip(content);
        FileUtils.writeFile(newFileName, compressedContent);
        return newFileName;
    }

    //TODO: TASK 8
    public String unzip(String input) {
        String output = "";

        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);

            if (c == '^') {
                int[] ref = tokenizer.fromTokenString(input, i);
                int distance = ref[0];
                int length = ref[1];

                int start = output.length() - distance;
                String toCopy = output.substring(start, start + length);

                output += toCopy;

                i += 3;
            } else {
                output += c;
                i++;
            }
        }

        return output;
    }

    //TODO: TASK 9
    public static String unzipFile(String file, int windowSize, boolean readable) {
        String newFileName = unzipFileName(file);
        if (newFileName == null) return null;

        String content = FileUtils.readFile(file);
        LZLite lzLite = new LZLite(windowSize, readable);
        String decompressedContent = lzLite.unzip(content);
        FileUtils.writeFile(newFileName, decompressedContent);
        return newFileName;
    }

    //TODO: TASK 9
    public static void main(String[] args) {

    }


    // DON'T DELETE THE GETTERS! THEY ARE REQUIRED FOR TESTING
    public int getWindowSize() {
        return windowSize;
    }

    public String getSlidingWindow() {
        return slidingWindow;
    }

    public Tokenizer getTokenizer() {
        return tokenizer;
    }
}
