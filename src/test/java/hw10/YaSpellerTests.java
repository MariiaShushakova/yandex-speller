package hw10;

import static constants.Texts.*;
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
import static enums.Language.UK;
import static enums.Option.IGNORE_DIGITS;
import static enums.Option.IGNORE_URLS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import beans.YandexSpellerAnswer;
import enums.Format;
import org.junit.Test;

import java.util.List;

public class YaSpellerTests {
    @Test
    //todo см. комменты про if/for ниже
    public void checkErrorsForMisspelling() {
        List<YandexSpellerAnswer> answers = getAnswers(requestBuilder()
                .language(RU)
                .text(RU_INCORRECT)
                .buildRequest()
                .sendGetRequest());
        if (!answers.isEmpty()) {
            assertThat("Expected code is wrong:" + answers.get(0).code + "instead of:"
                    + ERROR_UNKNOWN_WORD.value, answers.get(0).code == ERROR_UNKNOWN_WORD.value);
        } else checkIncorrectTexts();
    }

    @Test//+
    public void checkTexts() {
        List<String> result = getStringResult(
                requestBuilder()
                        .language(EN, RU, UK)
                        .text(RU_ANSWER, EN_ANSWER, UK_ANSWER)
                        .buildRequest()
                        .sendGetRequest());
        assertThat("Errors in correct text:" + result, result.isEmpty());
    }

    @Test //BUG
    //todo см. комменты про if/for ниже
    //+ этот и два следующих теста очень неплохо группируются в с DataProvider
    public void checkIncorrectTexts() {
        String[] texts = {EN_INCORRECT, RU_INCORRECT, UK_INCORRECT};
        List<String> result = getStringResult(
                requestBuilder()
                        .language(EN, RU, UK)
                        .text(texts)
                        .buildRequest()
                        .sendGetRequest());
        if (result.size() != texts.length) {
            for (String text : texts) {
                assertThat("Fail to find spelling error in text:" + text, result.contains(text));
            }
        }
    }

    @Test //BUG
    //todo см. комменты про if/for ниже
    public void checkTextsWithDigits() {
        String[] texts = {EN_DIGITS, RU_DIGITS, UK_DIGITS};
        List<String> result = getStringResult(
                requestBuilder()
                        .language(EN, RU, UK)
                        .text(texts)
                        .buildRequest()
                        .sendGetRequest());
        if (result.size() != texts.length) {
            for (String text : texts) {
                assertThat("Fail to find error in text with digits:" + text, result.contains(text));
            }
        }
    }

    @Test //BUG
    //todo см. комменты про if/for ниже
    public void checkTextsWithLinks() {
        String[] texts = {EN_URL, RU_URL, UK_URL};
        List<String> result = getStringResult(
                requestBuilder()
                        .language(EN, RU, UK)
                        .text(texts)
                        .buildRequest()
                        .sendGetRequest());
        if (result.size() != texts.length) {
            for (String text : texts) {
                assertThat("Fail to find error in text with URL:" + text, result.contains(text));
            }
        }
    }

    @Test //BUG
    //todo вот всякие там циклы, ифы и лямбды не очень смотрятся в тестах.
    //лучше вынеси в отдельный класс с асертами (см. комменты в тестах ниже)
    public void checkIncorrectCapitalization() {
        String[] texts = {EN_INCORRECT_CAPS, RU_INCORRECT_CAPS, UK_INCORRECT_CAPS};
        List<String> result = getStringResult(requestBuilder()
                .language(EN, RU, UK)
                .text(texts)
                .buildRequest()
                .sendGetRequest());
        if (result.size() != texts.length) {
            for (String text : texts) {
                assertThat("Fail to find error in names with incorrect capitalization:" + text, result.contains(text));
            }
        }
    }

    @Test//+
    public void checkIncorrectLangParameter() {
        requestBuilder()
                .language(INCORRECT_LANG)
                .text(EN_ANSWER)
                .buildRequest()
                .sendPostRequest()
                .then().assertThat()
                .specification(failResponse())
                .body(containsString("Invalid parameter 'lang'"));
    }

    //todo checkIgnoreDigitsOption и checkIgnoreUrlOption можно объединить в DataProvider
    @Test
    public void checkIgnoreDigitsOption() {
        List<String> result = getStringResult(
                requestBuilder()
                        .language(RU, EN, UK)
                        .text(RU_DIGITS, EN_ANSWER, UK_ANSWER)
                        .options(IGNORE_DIGITS)
                        .buildRequest()
                        .sendGetRequest());
        assertThat("Errors in text with digits despite 'ignore digits' option:" + result,
                result.isEmpty());
    }

    //todo  assertThat("Errors in text with URL despite 'ignore URLs'option:" + result, result.isEmpty());
    //вот этот асер дублируется, вместе с сообщением,  - можно создать отдельный класс с асертами, и там обертку c интерфейсом
    // assertResultIsEmpty(result). Или вынести в переменную сообщение об ошибке
    @Test
    public void checkIgnoreUrlOption() {
        List<String> result = getStringResult(
                requestBuilder()
                        .language(EN, RU, UK)
                        .text(EN_URL, RU_URL, UK_URL)
                        .options(IGNORE_URLS)
                        .buildRequest()
                        .sendGetRequest());
        assertThat("Errors in text with URL despite 'ignore URLs'option:" + result, result.isEmpty());
    }

    @Test//+
    public void checkIncorrectFormatOption() {
        requestBuilder()
                .language(EN)
                .text(EN_ANSWER)
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
                .text(EN_ANSWER)
                .format(Format.HTML)
                .buildRequest()
                .sendPostRequest()
                .then().assertThat()
                .specification(successResponse());
    }
}
