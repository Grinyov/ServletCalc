package calc;

/**
 * Created by green on 04.10.2015.
 *
 * Перечесление вычислительных операций
 */
public enum OperationType {

    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    private String stringValue;

    private OperationType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
