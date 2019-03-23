package Lesson;

import core.YandexSpellerSOAP;
import enums.Language;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Arrays;

import static core.YandexSpellerConstants.*;

/**
 * Created by yulia_atlasova@epam.com.
 * try to test SOAP via RestAssured
 */
public class TestYandexSpellerSOAP {

    @Test
    public void simpleCall(){
                YandexSpellerSOAP
                        .with().text(SimpleWord.BROTHER.wrongVer())
                        .callSOAP()
                        .then()
                        .body(Matchers.stringContainsInOrder
                                (Arrays.asList(SimpleWord.BROTHER.wrongVer(), SimpleWord.BROTHER.corrVer())));
    }

    @Test
    public void useRequestBuilderToChangeParams(){
        YandexSpellerSOAP.with()
                .language(Language.EN)
                .text(SimpleWord.BROTHER.wrongVer())
                .options("6")
                .action(SoapAction.CHECK_TEXTS)
                .callSOAP()
                .then()
                .body(Matchers.stringContainsInOrder
                        (Arrays.asList(SimpleWord.BROTHER.wrongVer(), SimpleWord.BROTHER.corrVer())));
    }
}
