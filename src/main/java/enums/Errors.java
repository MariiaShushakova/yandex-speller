package enums;

public enum Errors {
    ERROR_UNKNOWN_WORD(1),
    ERROR_REPEAT_WORD(2),
    ERROR_CAPITALIZATION(3),
    ERROR_TOO_MANY_ERRORS(4);

    private final int value;

    Errors(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
