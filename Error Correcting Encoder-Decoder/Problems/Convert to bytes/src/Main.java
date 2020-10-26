import java.io.InputStream;

class Main {
    public static void main(String[] args) throws Exception {
        InputStream inputStream = System.in;

        int b = inputStream.read();
        while (b != -1) {
            byte bite = (byte) b;
            System.out.print(bite);
            b = inputStream.read();
        }

    }
}