package enums;

public enum Language {
    RU("ru"),
    UK("uk"),
    EN("en"),
    INCORRECT_LANG("tosh");

    public final String value;

    Language(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
