import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // write your code here
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        int[][] arrSeedsAndMax = new int[b - a + 1][2];
        int j = 0;
        for (int i = a; i <= b; i++) {
            arrSeedsAndMax[j][0] = i;
            arrSeedsAndMax[j][1] = SeedMachine.maxRandom(i, n, k);
            j++;
        }
        Arrays.sort(arrSeedsAndMax, Comparator.comparingInt(c -> c[1]));
        System.out.println(arrSeedsAndMax[0][0]);
        System.out.println(arrSeedsAndMax[0][1]);
    }
}

class SeedMachine {
    public static int maxRandom(int seed, int n, int k) {
        Random rando = new Random(seed);
        int[] intArr = new int[n];
        for (int i = 0; i < intArr.length; i++) {
            intArr[i] = rando.nextInt(k);
        }
        Arrays.sort(intArr);
        return intArr[n - 1];
    }
}


