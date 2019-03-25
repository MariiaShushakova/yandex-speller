package hw10;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static enums.Option.DEFAULT;
import static enums.Texts.*;
import static core.YandexSpellerApi.failResponse;
import static core.YandexSpellerApi.getAnswers;
import static core.YandexSpellerApi.getStringResult;
import static core.YandexSpellerApi.requestBuilder;
import static core.YandexSpellerApi.successResponse;
import static enums.Errors.ERROR_UNKNOWN_WORD;
import static enums.Format.INCORRECT_FORMAT;
import static enums.Language.EN;
import static enums.Language.INCORRECT_LANG;
import static enums.Language.RU;
import static enums.Option.IGNORE_DIGITS;
import static enums.Option.IGNORE_URLS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static utils.YaSpellerUtils.getExpectedAnswersForWrongInput;


import beans.YandexSpellerAnswer;
import core.YandexSpellerDataProvider;
import enums.Format;
import enums.Language;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import utils.YaSpellerUtils;

import java.util.List;

public class YaSpellerTests {
    //todo см. комменты про if/for ниже -
    //todo давай на этом кейсе напишем идеальный тест:
    //1. убери if
    //2. вместо проверки кода и сообщения об ошибке прямо в тесте, напиши класс асертов assertCorrectErrorCode(answers, expectedCode);
    //3. и второй вариант - сравнивай объекты! assertThat(answers, is(expectedAnswers));
    @Test
    public void checkErrorsForMisspelling() {
        String word = RU_INCORRECT.getValue();
        List<YandexSpellerAnswer> answers = getAnswers(requestBuilder()
                .language(RU)
                .text(word)
                .buildRequest()
                .sendGetRequest());
        List<YandexSpellerAnswer> expectedAnswers = getExpectedAnswersForWrongInput(word);
        assertThat(answers, Matchers.is(expectedAnswers));
        checkIncorrectTexts(RU, word);
    }

    @Test(dataProvider = "correctTextsProvider", dataProviderClass = YandexSpellerDataProvider.class)
    public void checkTexts(Language lang, String text) {
        List<String> result = getStringResult(requestBuilder()
                        .language(lang)
                        .text(text)
                        .buildRequest()
                        .sendGetRequest());
        assertThat("Errors in correct text:" + result, result.isEmpty());
    }

    //todo см. комменты про if/for ниже - fixed
    //+ этот и два следующих теста очень неплохо группируются в с DataProvider
    //BUG
    @Test(dataProvider = "misspelledTextsProvider",
            dataProviderClass = YandexSpellerDataProvider.class)
    public void checkIncorrectTexts(Language language, String text) {
        List<String> result = getStringResult(
                requestBuilder()
                        .language(language)
                        .text(text)
                        .buildRequest()
                        .sendGetRequest());
        assertThat("Fail to find spelling error in text: " + text,
                result.contains(text));
    }

    //BUG
    //todo см. комменты про if/for ниже - fixed
    @Test(dataProvider = "textsWithDigitsProvider",
            dataProviderClass = YandexSpellerDataProvider.class)
    public void checkTextsWithDigits(Language language, String text) {
        String[] texts = {EN_DIGITS.getValue(), RU_DIGITS.getValue(), UK_DIGITS.getValue()};
        List<String> result = getStringResult(
                requestBuilder()
                        .language(language)
                        .text(text)
                        .buildRequest()
                        .sendGetRequest());
                assertThat("Fail to find error in text with digits:" + text, result.contains(text));
    }

    //BUG
    //todo см. комменты про if/for ниже - fixed
    @Test(dataProvider = "textsWithLinksProvider",
            dataProviderClass = YandexSpellerDataProvider.class)
    public void checkTextsWithLinks(Language language, String text) {
        String[] texts = {EN_URL.getValue(), RU_URL.getValue(), UK_URL.getValue()};
        List<String> result = getStringResult(
                requestBuilder()
                        .language(language)
                        .text(text)
                        .buildRequest()
                        .sendGetRequest());
                assertThat("Fail to find error in text with URL:" + text, result.contains(text));
    }

    //BUG
    //todo вот всякие там циклы, ифы и лямбды не очень смотрятся в тестах. - fixed
    //лучше вынеси в отдельный класс с асертами (см. комменты в тестах ниже)
    @Test(dataProvider = "properCapitalizationProvider",
            dataProviderClass = YandexSpellerDataProvider.class)
    public void checkIncorrectCapitalization(Language language, String text) {
            List<String> result = getStringResult(
                    requestBuilder()
                            .language(language)
                            .text(text)
                            .buildRequest()
                            .sendGetRequest());
            assertThat("Fail to find error in proper name with lower case: " + text,
                    result.contains(text));
        }

    @Test//+
    public void checkIncorrectLangParameter() {
        requestBuilder()
                .language(INCORRECT_LANG)
                .text(EN_ANSWER.getValue())
                .buildRequest()
                .sendPostRequest()
                .then().assertThat()
                .specification(failResponse())
                .body(containsString("Invalid parameter 'lang'"));
    }

    //todo checkIgnoreDigitsUrlOption и checkIgnoreUrlOption можно объединить в DataProvider - fixed, unite both tests
    @Test(dataProvider = "textsWithDigitsProvider",
            dataProviderClass = YandexSpellerDataProvider.class)
    public void checkIgnoreDigitsUrlOption(Language lang, String text) {
        List<String> result = getStringResult(
                requestBuilder()
                        .language(lang)
                        .text(text)
                        .options(IGNORE_DIGITS, IGNORE_URLS)
                        .buildRequest()
                        .sendGetRequest());
        assertThat("Errors in text with digits despite 'ignore digits & url' options:" + result,
                result.isEmpty());
    }

    //todo  assertThat("Errors in text with URL despite 'ignore URLs'option:" + result, result.isEmpty());
    //вот этот асер дублируется, вместе с сообщением,  - можно создать отдельный класс с асертами, и там обертку c интерфейсом
    // assertResultIsEmpty(result). Или вынести в переменную сообщение об ошибке
//    @Test
//    public void checkIgnoreUrlOption(Language language, String text) {
//        List<String> result = getStringResult(
//                requestBuilder()
//                        .language(EN, RU, UK)
//                        .text(EN_URL.getValue(), RU_URL.getValue(), UK_URL.getValue())
//                        .options(IGNORE_URLS)
//                        .buildRequest()
//                        .sendGetRequest());
//        assertThat("Errors in text with URL despite 'ignore URLs'option:" + result, result.isEmpty());
//    }

    @Test//+
    public void checkIncorrectFormatOption() {
        requestBuilder()
                .language(EN)
                .text(EN_ANSWER.getValue())
                .format(INCORRECT_FORMAT)
                .buildRequest()
                .sendPostRequest()
                .then().assertThat()
                .specification(failResponse())
                .and()
                .body(containsString("Invalid parameter 'format'"));
    }

    @Test//+
    public void checkFormatOption() {
        requestBuilder()
                .language(EN)
                .text(EN_ANSWER.getValue())
                .format(Format.HTML)
                .buildRequest()
                .sendPostRequest()
                .then().assertThat()
                .specification(successResponse());
    }
}
