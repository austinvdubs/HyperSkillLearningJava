package correcter;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
import java.lang.Character;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.print("Write a mode: ");
        Scanner scany = new Scanner(System.in);
        String mode = scany.next();


        switch (mode) {
            case "encode":
                File sendFile = new File("send.txt");
                FileOutputStream encodeOutputStream = new FileOutputStream("encoded.txt");
                encodeOutputStream.write(Encoder.parity(Encoder.encode(sendFile)));
                encodeOutputStream.close();
                break;
            case "send":
                File encodedFile = new File("encoded.txt");
                FileOutputStream sendOutputStream = new FileOutputStream("received.txt");
                sendOutputStream.write(ByteNoiseMachine.makeSomeNoise(encodedFile));
                sendOutputStream.close();
                break;
            case "decode":
                File decodeFile = new File("received.txt");
                FileOutputStream decodeOutputStream = new FileOutputStream("decoded.txt");
                decodeOutputStream.write(Decoder.decode(Decoder.repair(decodeFile)));
                decodeOutputStream.close();
                break;
            default:
                System.out.println("Choose a mode: encode, send, or decode.");
                break;
        }
    }
}

class Encoder {
    public static byte[] encode(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        byte[] inputBytes = inputStream.readAllBytes(); //read all the bytes
        inputStream.close();

        byte[] encodedBytes = new byte[inputBytes.length * 2]; //For every input byte, there are 2 output bytes

        byte byteConsolidator = 0;
        double shwifter = 10.0; //shifts the bits. (Get swhifty)
        int chosenBit = 0, encodedBytesCounter = 0, byteHolderCounter = 0, outputByteCounter = 0;

        for (int i = 0; i < inputBytes.length; i++) { //cycles through each input byte in the array
            for (int j = 7; j >= 0; j--) { //cycles through each bit of the byte
                chosenBit = (inputBytes[i] & (1 << j)); //plucks the desired chosenBit from the input byte
                chosenBit = chosenBit >>> j; //moves the bit to the first position (0000 000x)
                shwifter = Math.ceil(shwifter / 2);
                chosenBit = chosenBit << (int) shwifter;//have to move bit from first position to significant bit locations by shifting 5,3,2, or 1. This can be done by shifting by 5, then Math.ceil(x/2) to get 3,2,&1;
                byteConsolidator = (byte) (byteConsolidator | chosenBit);

                if (shwifter == 1.0) { //resets the shwifter (shit on the floor)
                    shwifter = 10.0;
                }
                if (j == 4 || j == 0) {
                    encodedBytes[outputByteCounter] = byteConsolidator;
                    outputByteCounter++;
                    byteConsolidator = 0;
                }


            }
        }
        return encodedBytes;
    }

    public static byte[] parity(byte[] inputBytes) {
        byte[] parityedBytes = Arrays.copyOf(inputBytes, inputBytes.length);

        int parity1 = 0, parity2 = 0, parity3 = 0; // p000 0000 , 0p00 0000 , 000p 0000 respectively

        for (int j = 0; j < inputBytes.length; j++) {
            if ((inputBytes[j] & (1 << 5)) != 0) {
                parity1++;
                parity2++;
            }
            if ((inputBytes[j] & (1 << 3)) != 0) {
                parity1++;
                parity3++;
            }
            if ((inputBytes[j] & (1 << 2)) != 0) {
                parity2++;
                parity3++;
            }
            if ((inputBytes[j] & (1 << 1)) != 0) {
                parity1++;
                parity2++;
                parity3++;
            }
            parity1 = parity1 % 2;
            parity2 = parity2 % 2;
            parity3 = parity3 % 2;
            parityedBytes[j] = (byte) (parityedBytes[j] | (parity1 << 7) | (parity2 << 6) | (parity3 << 4));
            parity1 = 0;
            parity2 = 0;
            parity3 = 0;
        }
        return parityedBytes;
    }

}

class ByteNoiseMachine {
    public static byte[] makeSomeNoise(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        byte[] inputBytes = inputStream.readAllBytes();
        inputStream.close();

        byte[] noisyBytes = new byte[inputBytes.length];
        Random rando = new Random();

        byte bitToFlip;
        for (int i = 0; i < inputBytes.length; i++) {
            bitToFlip = (byte) (1 << rando.nextInt(8)); //bit shifts 1 (0000 0001) by random amount
            noisyBytes[i] = (byte) (inputBytes[i] ^ bitToFlip); //flips whatever bit the bitToFlip was shifted to
        }
        return noisyBytes;
    }
}

class Decoder {
    public static byte[] repair(File file) throws IOException { //this class repairs the file back to the encoded state
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] inputBytes = fileInputStream.readAllBytes();
        fileInputStream.close();
        byte[] repairedBytes = Arrays.copyOf(inputBytes, inputBytes.length);

        int parity1 = 0, parity2 = 0, parity3 = 0;
        boolean parity1Error = false, parity2Error = false, parity3Error = false;

        for (int j = 0; j < inputBytes.length; j++) {
            if ((inputBytes[j] & (1 << 5)) != 0) {
                parity1++;
                parity2++;
            }
            if ((inputBytes[j] & (1 << 3)) != 0) {
                parity1++;
                parity3++;
            }
            if ((inputBytes[j] & (1 << 2)) != 0) {
                parity2++;
                parity3++;
            }
            if ((inputBytes[j] & (1 << 1)) != 0) {
                parity1++;
                parity2++;
                parity3++;
            }
            parity1 = parity1 % 2;
            parity2 = parity2 % 2;
            parity3 = parity3 % 2;

            if (((inputBytes[j] & (1 << 7)) >>> 7) != parity1) {
                parity1Error = true;
            }
            if (((inputBytes[j] & (1 << 6)) >>> 6) != parity2) {
                parity2Error = true;
            }
            if (((inputBytes[j] & (1 << 4)) >>> 4) != parity3) {
                parity3Error = true;
            }

            System.out.printf("Parity1Error: %b, parity2Error: %b, parity3Error: %b \r\n", parity1Error, parity2Error, parity3Error);

            if (parity1Error && parity2Error && parity3Error) { //flip bit 1
                repairedBytes[j] = (byte) (inputBytes[j] ^ (1 << 1));
            } else if (parity1Error && parity2Error) { //flip bit 5
                repairedBytes[j] = (byte) (inputBytes[j] ^ (1 << 5));
            } else if (parity1Error && parity3Error) { //flip bit 3
                repairedBytes[j] = (byte) (inputBytes[j] ^ (1 << 3));
            } else if (parity2Error && parity3Error) { //flip bit 2
                repairedBytes[j] = (byte) (inputBytes[j] ^ (1 << 2));
            } else {
                repairedBytes[j] = inputBytes[j];
            }

            parity1 = 0;
            parity2 = 0;
            parity3 = 0;
            parity1Error = false;
            parity2Error = false;
            parity3Error = false;

        }
        return repairedBytes;
    }


    public static byte[] decode(byte[] inputBytes) { //this class decodes received.txt after it has been repair
        byte[] outputBytes = new byte[inputBytes.length / 2]; //For every 8 received bytes there are 3 decoded bytes
        int inputByteCounter = 0, outputByteCounter = 0;
        double schwifter = 5.0; //It's time to get schwifty in here.
        int chosenBit = 0;
        int byteConsolidator = 0;

        for (int i = 0; i < outputBytes.length; i++) { //cycles through output bytes
            for (int j = 0; j < 8; j++) {
                chosenBit = (inputBytes[inputByteCounter] & (1 << (int) schwifter)); //plucks the desired chosenBit from the input byte
                chosenBit = chosenBit >>> (int) schwifter; //moves the bit to the first position (0000 000x)
                schwifter = Math.ceil(schwifter / 2);

                chosenBit = chosenBit << 7 - j;//have to move bit from first position to significant bit locations by shifting 5,3,2, or 1. This can be done by shifting by 5, then Math.ceil(x/2) to get 3,2,&1;
                byteConsolidator = (byte) (byteConsolidator | chosenBit);

                if (j == 3 || j == 7) {
                    inputByteCounter++;
                    schwifter = 5.0; //resets the shifter. (Take off your pants and your panties.)
                }
            }
            outputBytes[i] = (byte) byteConsolidator;
            byteConsolidator = 0;
        }


//        if (outputBytes[outputBytes.length - 1] == 0) { //Clips off the last byte if it's all zeros. This is important because you can end up in a situation where the last decoded bit in the received.txt is a zero and it can be put into a whole new decoded byte by itself
//            return Arrays.copyOfRange(outputBytes, 0, outputBytes.length - 1);
//        } else {
//            return outputBytes;
//        }
        return outputBytes;
    }
}
