package constants;

import org.apache.commons.lang3.tuple.Pair;

public class Texts {
    //todo это есть тестовые данные, а их лучше не хранить внутри описание, а тем более создавать на них глобальные константы
    //1 - давай вынесем на уровень тестов, можно в Dataprovider, если можно переиспользовать в сценарии
    //2 - при желании создать именно пару (Pair<String, String>)(хотя просто достаточно двух входных параметров, если говорить про методы - incorrectWord, correctWord)
    public static final String RU_DIGITS = "капи9бара";
    public static final String RU_URL = "капибара https://www.youtube.com";
    public static final String RU_INCORRECT_CAPS = "маШа";
    public static final String RU_ANSWER = "капибара";
    public static final String RU_INCORRECT="капиббара";

    public static final String EN_DIGITS = "He11o";
    public static final String EN_URL = "Hello https://www.ya.ru/";
    public static final String EN_INCORRECT_CAPS = "maRia";
    public static final String EN_ANSWER = "Hello";
    public static final String EN_INCORRECT = "buug";

    public static final String UK_DIGITS = "Укра9на";
    public static final String UK_URL = "Украiна https://www.google.ru/";
    public static final String UK_INCORRECT_CAPS = "укРаiна";
    public static final String UK_ANSWER = "Украiна";
    public static final String UK_INCORRECT = "Украина";
    public static final Pair<String, String> f = null;
}
