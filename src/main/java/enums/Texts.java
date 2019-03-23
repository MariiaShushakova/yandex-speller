package enums;

public enum Texts {
    //todo это есть тестовые данные, а их лучше не хранить внутри описание, а тем более создавать на них глобальные константы - fixed
    //1 - давай вынесем на уровень тестов, можно в Dataprovider, если можно переиспользовать в сценарии
    //2 - при желании создать именно пару (Pair<String, String>)(хотя просто достаточно двух входных параметров, если говорить про методы - incorrectWord, correctWord)
    RU_DIGITS("капи9бара"),
    RU_URL("капибара https://www.youtube.com"),
    RU_INCORRECT_CAPS("маШа"),
    RU_ANSWER("капибара"),
    RU_INCORRECT("капиббара"),

    EN_DIGITS("He11o"),
    EN_URL("Hello https://www.ya.ru/"),
    EN_INCORRECT_CAPS("maRia"),
    EN_ANSWER("Hello"),
    EN_INCORRECT("caw"),

    UK_DIGITS("Укра9на"),
    UK_URL("Украiна https://www.google.ru/"),
    UK_INCORRECT_CAPS("укРаiна"),
    UK_ANSWER("Привіт"),
    UK_INCORRECT("Прибіт");

    private String value;

    Texts(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
