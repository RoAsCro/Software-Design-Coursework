package sml;

/**
 * Represents an abstract instruction. An Instruction forms part of an SML program, executing an operation on a machine
 * and indicating which instruction in the program should be read next.
 * <p></p>
 * When implementing a concrete Instruction, it must only take integers, labels, and RegisterNames as its construction
 * parameters. In addition, it's opcode must not be null.
 * <p></>
 * An Instruction may have multiple constructors, but as they are typically instantiated using an input from an SML
 * file, i.e. using Strings, this may result in ambiguity if, say, an Instruction had a constructor that takes a label
 * and a RegisterName, and a second constructor that takes a label and a String. The second constructor may end up being
 * called used when the first constructor was intended.
 *
 * @author ...
 */
public abstract class Instruction {
	/**
	 * The optional label of the Instruction, used so that other Instructions in a program may refer to this
	 * instruction. May not be the same as the label of another Instruction in the program.
	 */
	protected final String label;
	/**
	 * The name of the operation performed by the Instruction.
	 */
	protected final String opcode;

	/**
	 * Constructor: an instruction with a label and an opcode
	 * (opcode must be an operation of the language)
	 *
	 * @param label optional label (can be null)
	 * @param opcode operation name
	 */
	public Instruction(String label, String opcode) {
		this.label = label;
		this.opcode = opcode;
	}

	/**
	 * Returns this Instruction's label.
	 *
	 * @return the label. May be null.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Returns this Instruction's opcode.
	 *
	 * @return the opcode.
	 */
	public String getOpcode() {
		return opcode;
	}

	/**
	 * The normal program counter update to be used when an Instruction needs to tell a machine to move onto the
	 * instruction with the next address.
	 */
	public static int NORMAL_PROGRAM_COUNTER_UPDATE = -1;

	/**
	 * Executes the instruction in the given machine.
	 *
	 * @param machine the machine the instruction runs on
	 * @return the new program counter (for jump instructions)
	 *          or NORMAL_PROGRAM_COUNTER_UPDATE to indicate that
	 *          the instruction with the next address is to be executed
	 */

	public abstract int execute(Machine machine);

	/**
	 * If this Instruction has a label, returns a String in the format "label: ".
	 * Otherwise, returns a blank String.
	 *
	 * @return a String representing the label
	 */
	protected String getLabelString() {
		return (getLabel() == null) ? "" : getLabel() + ": ";
	}

	// TODO: What does abstract in the declaration below mean?
	//       (Write a short explanation.)
	//Here, abstract means that the method has not been implemented by this class and must be implemented by any
	//non-abstract subclass of this this class.

	/**
	 * Returns a String representation of this Instruction.
	 *
	 * @return a String representation of this Instruction readable by humans.
	 */
	@Override
	public abstract String toString();

}
