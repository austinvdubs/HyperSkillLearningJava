import java.io.BufferedReader;
import java.io.InputStreamReader;

class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        getNextChar(reader);
        reader.close();
        StringBuilder megan = new StringBuilder("pizza");
        megan.reverse();
    }


    private static void getNextChar(BufferedReader reader) throws Exception {
        int nextChar = reader.read();
        if (nextChar != -1) {
            getNextChar(reader);
            System.out.print((char) nextChar);
        }
    }
}