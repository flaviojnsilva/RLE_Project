import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.nio.file.*;


public class Main {

    public static void main(String[] args) throws Exception {

        /*encode("AMANHECEU. ERA A PRIMEIRA MANHÃ DO AMANHÃ E O AMANHECER DEU LUGAR À VERDADEIRA MANHÃ " +
                "QUE RAPIDAMENTE AMANHECIA PARA AMANHECER E SE TORNAR NO AMANHÃ AMANHECIDO.");
*/
        encode("teste.txt");
        //decode("ENC_teste.txt");
    }

    public static void saveFiles(String str, String name) {

        try {
            String path = "/Users/anogueira/Desktop/RLE_Project/output/" + name;
            File newTextFile = new File(path);

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
    public static void encode(String input) throws Exception {

        String path = "/Users/anogueira/Desktop/RLE_Project/input/CorpusSilesia/" + input;
        String data = readFileAsString(path);
        int lengthOfInput = data.length();
        System.out.println(data.length());

        long InitialTime = System.nanoTime();

        char lastCharacter = data.charAt(0);
        int lastCharacterCount = 1;
        StringBuilder result = new StringBuilder();

        for (int index = 1; index <= lengthOfInput; index++) {

            if (index == lengthOfInput) {
                if (lastCharacterCount > 3) {

                    result.append('!').append(lastCharacterCount).append(lastCharacter);
                    break;
                } else if (lastCharacterCount < 4 && lastCharacterCount > 1) {
                    for (int i = 0; i < lastCharacterCount; i++) {
                        result.append(lastCharacter);
                    }
                    break;
                }

                result.append(lastCharacter);
                break;
            }

            char currentCharacter = data.charAt(index);

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

        long FinalTime = System.nanoTime();

        long ElapsedTime = TimeUnit.NANOSECONDS.toMicros(FinalTime - InitialTime);

        String FinalFile = result.toString();
        String FileName = "ENC_" + input;

        saveFiles(FinalFile, FileName);
        //Desktop.getDesktop().open(new File("C:\\Users\\fjns\\Documents\\UFP\\2o_Semestre\\MULT_II\\Projeto\\output\\encoded_files" + FileName));
        System.out.println("Tempo de duração da compressão (ET - encoding time): " + ElapsedTime + " ns\n");
        System.out.println("Rácio de compressão resultante (CR - compression ratio): " + (float) (data.length()) / result.length() + ":1");
        //Desktop.getDesktop().open(new File("C:\\Users\\fjns\\Documents\\UFP\\2o_Semestre\\MULT_II\\Projeto\\output\\encoded_files" + FileName));

    }

    /**
     * Método responsável pelo processo de decoding
     *
     * @param
     * @return
     */
    private static void decode(String input) throws Exception {

        int flag = 0;

        String path = "/Users/anogueira/Desktop/RLE_Project/output/" + input;
        String encoded = readFileAsString(path);

        long InitialTime = System.nanoTime();

        StringBuilder result = new StringBuilder();
        StringBuilder empty = new StringBuilder("");

        int lengthOfEncodedString = encoded.length();
        StringBuilder timesToRepeatLastCharacter = new StringBuilder("");

        for (int index = 0; index < lengthOfEncodedString; index++) {
            char currentCharacter = encoded.charAt(index);

            if (index == lengthOfEncodedString) {

                result.append(currentCharacter);

                break;
            } else if (currentCharacter == '!') {
                char next = encoded.charAt(index + 1);
                if (next == currentCharacter) {
                    result.append(currentCharacter);
                    flag = 0;
                } else if (Character.isDigit(next)) {
                    flag = 1;
                } else {
                    result.append(currentCharacter);
                }

            } else if (Character.isDigit(currentCharacter) && flag == 1) {

                timesToRepeatLastCharacter.append(currentCharacter);

            } else if (Character.isDigit(currentCharacter)) {
                result.append(currentCharacter);
                timesToRepeatLastCharacter = new StringBuilder();


            } else {

                if (!timesToRepeatLastCharacter.toString().isEmpty()) {
                    for (int i = 0; i < Long.parseLong(timesToRepeatLastCharacter.toString()); i++) {
                        result.append(currentCharacter);
                    }
                    flag = 0;
                    timesToRepeatLastCharacter = new StringBuilder();
                } else {
                    result.append(currentCharacter);
                    timesToRepeatLastCharacter = new StringBuilder();
                }
            }
        }

        long FinalTime = System.nanoTime();
        long ElapsedTime = TimeUnit.NANOSECONDS.toMicros(FinalTime - InitialTime);
        System.out.println("Decoding total time taken : nano seconds -> " + ElapsedTime);

        String FinalFile = result.toString();
        String FileName = "DEC_" + input;

        System.out.println(result.length());
        //System.out.println("New File Created on output path.\nFile Name:" + FileName);
        saveFiles(FinalFile, FileName);
        // Desktop.getDesktop().open(new File("C:\\Users\\fjns\\Documents\\UFP\\2o_Semestre\\MULT_II\\Projeto\\output\\" + FileName));
    }
}

