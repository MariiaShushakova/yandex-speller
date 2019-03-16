package enums;

public enum Format {
    PLAIN("plain"),
    HTML("html"),
    INCORRECT_FORMAT("incorrect format");


    public final String value;

    Format(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
