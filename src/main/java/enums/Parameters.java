package enums;

public enum Parameters {
    //todo в качестве пожелания - лучше переделать в enum - fixed
     TEXT("text"),
     OPTIONS("options"),
     LANGUAGE("lang"),
     FORMAT("format");

     private String value;

     Parameters(String value) {
         this.value = value;
     }

     public String getValue() {
         return value;
     }

}
