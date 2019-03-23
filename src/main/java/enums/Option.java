package enums;

public enum Option {
    DEFAULT(0),
    IGNORE_DIGITS(2),
    IGNORE_URLS(4),
    FIND_REPEAT_WORDS(8),
    IGNORE_CAPITALIZATION(512);

    private final int value;

    Option(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

}
