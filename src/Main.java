import java.util.concurrent.TimeUnit;
import java.nio.file.*;

public class Main {

    public static void main(String[] args) throws Exception {
        //encode("teessssttte");
//        encode("AMANHECEU. ERA A PRIMEIRA MANHÃ DO AMANHÃ E O AMANHECER DEU LUGAR À VERDADEIRA MANHÃ " +
//                "QUE RAPIDAMENTE AMANHECIA PARA AMANHECER E SE TORNAR NO AMANHÃ AMANHECIDO.");
//        decode("t!e5!s3te");

        String data = readFileAsString("C://Users//fjns//Documents//UFP//2o_Semestre//MULT_II//Projeto//input//CorpusSilesia//teste_dickens");
//        String data2 = readFileAsString("C://Users//fjns//Documents//UFP//2o_Semestre//MULT_II//Projeto//input//CorpusSilesia//nci");

        System.out.println(data.length() + "\n");
        encode(data);


    }


    private static String readFileAsString(String fileName) throws Exception {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }


    public static void encode(String input) {

        long nano1 = System.nanoTime();

        StringBuilder result = new StringBuilder();
        int lengthOfInput = input.length();
        char lastCharacter = input.charAt(0);
        int lastCharacterCount = 1;

        // we will go until equal to the length of input and we will do the final flush inside the loop
        // in this way we will have roughly 90% - 95% performance improvent over nested iteration
        // but this might take a little more mory as compared to the nested loop iteration
        for (int index = 1; index <= lengthOfInput; index++) {

            if (index == lengthOfInput) {
                // we have already completed everything let us append the final value of index - 1 iteration
                if (lastCharacterCount == 1) {

                    result.append(lastCharacter);
                    break;
                } else
                    result.append(lastCharacter).append(lastCharacterCount);
                break;
            }

            char currentCharacter = input.charAt(index);

            if (lastCharacter == currentCharacter) {

                lastCharacterCount++;

            } else if (lastCharacterCount == 1) {
                result.append(lastCharacter);
                lastCharacter = currentCharacter;
                lastCharacterCount = 1;
            } else {
                if (lastCharacterCount >= 4) {
                    result.append('!').append(lastCharacterCount).append(lastCharacter);
                    lastCharacter = currentCharacter;
                    lastCharacterCount = 1;
                } else {
                    for (int i = 0; i < lastCharacterCount; i++) {
                        result.append(lastCharacter);
                    }
                    lastCharacter = currentCharacter;
                    lastCharacterCount = 1;
                }
            }
        }

        long nano2 = System.nanoTime();

        long result1 = TimeUnit.NANOSECONDS.toMicros(nano2 - nano1);


        System.out.println("Tempo de duração da compressão (ET - encoding time): " + result1 + " ns\n");
        System.out.println("Output depois da compressão usando RLE:\n" + result);

        System.out.println("Rácio: " + (float) (input.length()) / result.length() + ":1");
    }


    public static String decode(String encoded) {

        long nano1 = System.nanoTime();

        StringBuilder result = new StringBuilder();
        int lengthOfEncodedString = encoded.length();

        StringBuilder timesToRepeatLastCharacter = new StringBuilder("");
        char lastCharacter = encoded.charAt(0);

        for (int index = 1; index <= lengthOfEncodedString; index++) {

            if (index == lengthOfEncodedString) {
                // we have reached to the end of encoding ; do the final round
                // this code looks repeated
                if (timesToRepeatLastCharacter.length() != 0) {
                    for (int i = 0; i < Integer.parseInt(timesToRepeatLastCharacter.toString()); i++) {
                        result.append(lastCharacter);
                    }

                } else {
                    result.append(lastCharacter);

                }
                break;
            }

            char currentCharacter = encoded.charAt(index);

            if (currentCharacter == '!') {

                System.out.println("flag");
                currentCharacter = encoded.charAt(index + 1);
                index++;

                if (Character.isDigit(currentCharacter)) {

                    timesToRepeatLastCharacter.append(currentCharacter);
                } else {

                    if (timesToRepeatLastCharacter.length() != 0) {

                        // try parsing the timesToRepeatLastCharacter and get the number of times the character should be repeated
                        for (int i = 0; i < Integer.parseInt(timesToRepeatLastCharacter.toString()); i++) {
                            result.append(lastCharacter);
                        }

                        lastCharacter = currentCharacter;
                        timesToRepeatLastCharacter = new StringBuilder();
                    } else {
                        result.append(lastCharacter);
                    }
                }
            }
        }

        long nano2 = System.nanoTime();

        long result1 = TimeUnit.NANOSECONDS.toMicros(nano2 - nano1);

        System.out.println(result);

        System.out.println("decoding total time taken : nano seconds -> " + result1);

        return result.toString();
    }


}

