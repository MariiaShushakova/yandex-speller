package hw10;

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
import static org.hamcrest.Matchers.containsString;

import beans.YandexSpellerAnswer;
import core.YandexSpellerDataProvider;
import enums.Format;
import enums.Language;
import org.testng.annotations.Test;

import java.util.List;

public class YaSpellerTests {
    @Test
    //todo см. комменты про if/for ниже -
    public void checkErrorsForMisspelling() {
        List<YandexSpellerAnswer> answers = getAnswers(requestBuilder()
                .language(RU)
                .text(RU_INCORRECT.getValue())
                .buildRequest()
                .sendGetRequest());
        if (!answers.isEmpty()) {
            assertThat("Expected code is wrong:" + answers.get(0).code + "instead of:"
                    + ERROR_UNKNOWN_WORD.getValue(), answers.get(0).code == ERROR_UNKNOWN_WORD.getValue());
        } else checkIncorrectTexts(RU, RU_INCORRECT.getValue());
    }

    @Test(dataProvider = "correctTextsProvider", dataProviderClass = YandexSpellerDataProvider.class)
    public void checkTexts(Language lang, String text) {
        List<String> result = getStringResult(
                requestBuilder()
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
