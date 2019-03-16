package hw10;

import beans.YandexSpellerAnswer;
import enums.Format;
import org.junit.Test;

import java.util.List;

import static constants.Texts.*;
import static core.YandexSpellerApi.*;
import static enums.Errors.*;
import static enums.Format.*;
import static enums.Language.*;
import static enums.Option.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class YaSpellerTests {
    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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
