package employeepayroll;

public class invalidemployeerecordexception extends Exception {
	private final int lineNumber;

    public invalidemployeerecordexception(int lineNumber, String message) {
        super("Line " + lineNumber + ": " + message);
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
