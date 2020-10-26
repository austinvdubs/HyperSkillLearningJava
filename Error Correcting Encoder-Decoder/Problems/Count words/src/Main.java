import java.io.BufferedReader;
import java.io.InputStreamReader;

class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // start coding here
        char[] charleton = reader.readLine().toCharArray();
        int wordCount = 0;
        boolean wordComplete = true;
        if (charleton.length > 0) {
            for (char charle : charleton) {
                if (charle != ' ' && wordComplete == true) {
                    wordCount++;
                    wordComplete = false;
                } else if (charle == ' ') {
                    wordComplete = true;
                }
            }
        }

        reader.close();
        System.out.println(wordCount);
    }
}