import java.io.CharArrayWriter;
import java.io.IOException;

class Converter {
    public static char[] convert(String[] words) throws IOException {
        // implement the method
        CharArrayWriter charWriter = new CharArrayWriter();
        for (String word : words) {
            charWriter.write(word);
        }

        char[] outputChars = charWriter.toCharArray();
        charWriter.close();
        return outputChars;
    }
}