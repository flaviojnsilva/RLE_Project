import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.nio.file.*;

public class Main {

    public static void main(String[] args) throws Exception {

        //        encode("AMANHECEU. ERA A PRIMEIRA MANHÃ DO AMANHÃ E O AMANHECER DEU LUGAR À VERDADEIRA MANHÃ " +
//                "QUE RAPIDAMENTE AMANHECIA PARA AMANHECER E SE TORNAR NO AMANHÃ AMANHECIDO.");
//        decode("t!e5!s3te");

        String data = readFileAsString("/Users/anogueira/Desktop/Multimedia/RLE/input/encode/CorpusSilesia/dickens");

        encode(data);
    }

    public static void saveFiles(String str) {

        try {
            File newTextFile = new File("/Users/anogueira/Desktop/Multimedia/RLE/thetextfile.txt");

            FileWriter fw = new FileWriter(newTextFile);
            fw.write(str);
            fw.close();

        } catch (IOException iox) {
            iox.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método responsável por interpretar os ficheiros de input como uma string de caracteres (independentemente do formato do ficheiro)
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    private static String readFileAsString(String fileName) throws Exception {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }

    /**
     * Método responsável pelo processo de encoding
     *
     * @param input
     */
    public static void encode(String input) {

        long nano1 = System.nanoTime();
        int lengthOfInput = input.length();
        char lastCharacter = input.charAt(0);
        int lastCharacterCount = 1;

        StringBuilder result = new StringBuilder();

        for (int index = 1; index <= lengthOfInput; index++) {

            if (index == lengthOfInput) {
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
                } else {
                    for (int i = 0; i < lastCharacterCount; i++) {
                        result.append(lastCharacter);
                    }
                }
                lastCharacter = currentCharacter;
                lastCharacterCount = 1;
            }
        }
        String teste = result.toString();
        saveFiles(teste);

        long nano2 = System.nanoTime();
        long result1 = TimeUnit.NANOSECONDS.toMicros(nano2 - nano1);

        System.out.println("Tempo de duração da compressão (ET - encoding time): " + result1 + " ns\n");
        //System.out.println("Output depois da compressão usando RLE:\n" + result);
        System.out.println("\nRácio de compressão resultante (CR - compression ratio): " + (float) (input.length()) / result.length() + ":1");
    }

    /**
     * Método responsável pelo processo de decoding
     *
     * @param encoded
     * @return
     */
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

