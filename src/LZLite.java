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
        String bestMatch = "";
        int length = 1;

        while (pos + length <= input.length()) {
            String substring = input.substring(pos, pos + length);
            if (slidingWindow.contains(substring)) {
                bestMatch = substring;
                length++;
            } else {
                length = input.length() + 1;
            }
        }

        return bestMatch;
    }


    //TODO: TASK 5
    public String zip(String input) {
        String compressed = "";
        int pos = 0;

        while (pos < input.length()) {
            String bestMatch = maxMatchInWindow(input, pos);
            int matchLength = bestMatch.length();

            if (matchLength >= 2) {
                int matchIndex = slidingWindow.lastIndexOf(bestMatch);
                if (matchIndex != -1) {
                    int distance = slidingWindow.length() - matchIndex;
                    String token = tokenizer.toTokenString(distance, matchLength);

                    if (token.length() < matchLength) {
                        compressed = compressed + token;
                        appendToSlidingWindow(bestMatch);
                        pos += matchLength;
                        continue;
                    }
                }
            }

            char currentChar = input.charAt(pos);
            compressed = compressed + currentChar;
            appendToSlidingWindow(String.valueOf(currentChar));
            pos++;
        }

        return compressed;
    }


    //TODO: TASK 6
    public static String zipFileName(String fileName) {
        if (!fileName.endsWith(".txt")) {
            return null;
        }
        int txtIndex = fileName.lastIndexOf(".txt");
        return fileName.substring(0, txtIndex) + ".lz77.txt";
    }

    //TODO: TASK 6
    public static String unzipFileName(String fileName) {
        if (!fileName.endsWith(".lz77.txt")) {
            return null;
        }
        int lzIndex = fileName.lastIndexOf(".lz77.txt");
        return fileName.substring(0, lzIndex) + ".decompressed.txt";
    }

    //TODO: TASK 7
    public static String zipFile(String file, int windowSize, boolean readable) {
        String newFileName = zipFileName(file);
        if (newFileName == null)
            return null;

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
            char ch = input.charAt(i);

            if (ch == '^') {
                int[] ref = tokenizer.fromTokenString(input, i);
                int distance = ref[0];
                int length = ref[1];
                int tokenLength = ref[2];

                int start = slidingWindow.length() - distance;
                String match = slidingWindow.substring(start, start + length);

                output = output+ match;
                appendToSlidingWindow(match);
                i += tokenLength;
            } else {
                output = output + ch;
                appendToSlidingWindow(String.valueOf(ch));
                i++;
            }
        }

        return output;
    }

    //TODO: TASK 9
    public static String unzipFile(String file, int windowSize, boolean readable) {
        String uncompressedFileName = unzipFileName(file);
        if (uncompressedFileName == null) {
            return null;
        }

        String compressedText = FileUtils.readFile(file);
        LZLite lz = new LZLite(windowSize, readable);
        String uncompressedText = lz.unzip(compressedText);

        FileUtils.writeFile(uncompressedFileName, uncompressedText);
        return uncompressedFileName;
    }

    //TODO: TASK 9
    public static void main(String[] args) {
        String zipFileName = zipFile("test_files/genesis.txt", MAX_WINDOW_SIZE, true);
        String unzipFile = unzipFile(zipFileName, MAX_WINDOW_SIZE, true);
        System.out.println("Unzip to " + unzipFile + " completed!");
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
