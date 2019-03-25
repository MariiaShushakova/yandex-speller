package core;

import enums.Language;
import org.testng.annotations.DataProvider;

import java.util.Arrays;
import java.util.List;

import static enums.Language.*;
import static enums.Texts.*;

public class YandexSpellerDataProvider {

    @DataProvider
    public Object[][] correctTextsProvider() {
        return new Object[][]{
                {EN, EN_ANSWER.getValue()},
                {RU, RU_ANSWER.getValue()},
                {UK, UK_ANSWER.getValue()}
        };
    }

    @DataProvider
    public Object[][] misspelledTextsProvider() {
        return new Object[][]{
                {EN, EN_INCORRECT.getValue()},
                {RU, RU_INCORRECT.getValue()},
                {UK, UK_INCORRECT.getValue()}
        };
    }

    @DataProvider
    public Object[][] textsWithDigitsProvider() {
        return new Object[][]{
                {EN, EN_DIGITS.getValue()},
                {RU, RU_DIGITS.getValue()},
                {UK, UK_DIGITS.getValue()}
        };
    }

    @DataProvider
    public Object[][] textsWithLinksProvider() {
        return new Object[][]{
                {EN, EN_URL.getValue()},
                {RU, RU_URL.getValue()},
                {UK, UK_URL.getValue()}
        };
    }

    @DataProvider
    public Object[][] properCapitalizationProvider() {
        return new Object[][]{
                {EN, EN_INCORRECT_CAPS.getValue()},
                {RU, RU_INCORRECT_CAPS.getValue()},
                {UK, UK_INCORRECT_CAPS.getValue()}
        };
    }

    @DataProvider
    public Object[][] mixOfMisspelledTextsAndLanguagesProvider() {
        String[] texts = {
                RU_INCORRECT.getValue(),
                UK_INCORRECT.getValue(),
                EN_INCORRECT.getValue()
        };
        Language[] languages = {EN, RU, UK};
        return new Object[][]{
                {languages, texts}
        };
    }
}

