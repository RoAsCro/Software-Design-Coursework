package sml;

import java.io.File;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

/**
 * The translator of a <b>S</b><b>M</b>al<b>L</b> program. Takes a text file on construction containing an SML program
 * to be translated, converting it into Instructions executable by a Machine.
 *
 * @author ...
 * @author Roland Crompton
 */
public final class Translator {

    /**
     * The source file of the SML code
     */
    private final String fileName;

    /**
     * line contains the characters in the current line that's not been processed yet
     */
    private String line = "";

    /**
     * Constructs a Translator taking the String fileName as the location of the source file of the SML program.
     * @param fileName the source file of the SML program.
     */
    public Translator(String fileName) {
        this.fileName =  fileName;
    }


//    translate the small program in the file into lab (the labels) and
//     prog (the program)
//     return "no errors were detected"

    /**
     * Translates the SML program in the file into a series of Instructions executable by a Machine. Instructions are
     * stored in program parameter, and Labels in the labels parameter.
     *
     * @param labels an instance of Labels where the labels of the instructions in the program will be stored
     * @param program a list of Instructions where the translated Instructions will be stored
     * @throws IOException if something goes wrong in the process of reading the file, including finding errors in the
     * formatting of the SML file
     */
    public void readAndTranslate(Labels labels, List<Instruction> program) throws IOException {
        try (var sc = new Scanner(new File(fileName), StandardCharsets.UTF_8)) {
            labels.reset();
            program.clear();

            // Each iteration processes line and reads the next input line into "line"
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                String label = getLabel();

                Instruction instruction = getInstruction(label);
                if (instruction != null) {
                    if (label != null)
                        if (!labels.addLabel(label, program.size()))
                            throw new IOException();
                    program.add(instruction);
                } else
                    throw new IOException();
            }
        }
    }

    /**
     * Translates the current line into an instruction with the given label
     *
     * @param label the instruction label
     * @return the new instruction. Null if the instruction isn't valid for any  reason.
     * <p>
     * The input line should consist of a single SML instruction,
     * with its label already removed.
     */
    private Instruction getInstruction(String label) {
        if (line.isEmpty())
            return null;
        String opcode = scan();
        int[] l = {line.length()};

        //Create a list of parameters
        List<String> params = Stream.generate(this::scan).takeWhile(x -> {
            boolean y = line.length() != l[0];
            l[0] = line.length();
            return y;})
                .toList();

        return InstructionFactory.getInstance().getInstruction(label, opcode, params);
    }

    /**
     * Checks if the current line of the file contains a label.
     * @return the label if there is one, null if there is not.
     */
    private String getLabel() {
        String word = scan();
        if (word.endsWith(":"))
            return word.substring(0, word.length() - 1);

        // undo scanning the word
        line = word + ((!line.equals(word))
                ? " " + line
                :"");
        return null;
    }


    /**
     * Return the first word of the current line and remove it from the line.
     *
     * @return the first word of the line if there is one, otherwise the line.
     */
    private String scan() {
        line = line.trim();

        for (int i = 0; i < line.length(); i++)
            if (Character.isWhitespace(line.charAt(i))) {
                String word = line.substring(0, i);
                line = line.substring(i);
                return word;
            }

        return line;
    }
}