package core;

import static enums.Parameters.FORMAT;
import static enums.Parameters.LANGUAGE;
import static enums.Parameters.OPTIONS;
import static enums.Parameters.TEXT;
import static core.YandexSpellerConstants.YANDEX_SPELLER_API_URI;
import static org.hamcrest.Matchers.lessThan;

import beans.YandexSpellerAnswer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import enums.Format;
import enums.Language;
import enums.Option;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class YandexSpellerApi {
    //todo https://speller.yandex.net/ лучше сделать отдельной переменной
    //todo разнеси это класс на отдельные
    // формирование параметров (один метод- один класс)
    //класс с методами для формирования запроса и вызов метода get
    //класс с асертами - см. комменты в тестах. там будет понятнее, что я имею ввиду
    public static final String spellerUrl = "https://speller.yandex.net/services/spellservice.json/checkTexts";
    private static long requestNumber = 0L;

    //builder pattern
    private Map<String,List<String>> params;

    private YandexSpellerApi(Map<String,List<String>> params){
        this.params = params;
    }

    public static ApiBuilder requestBuilder(){
        return new ApiBuilder();
    }

    public static class ApiBuilder {
        private Map<String, List<String>> params = new HashMap<>();

        public ApiBuilder text(String... text) {
            params.put(TEXT.getValue(), Arrays.asList(text));
            return this;
        }

        public ApiBuilder options(Option... options) {
            int resultParameter=0;
            for (Option i : options) {
                resultParameter += i.getValue();
            }
            params.put(OPTIONS.getValue(),Arrays.asList(String.valueOf(resultParameter)));
            return this;
        }

        public ApiBuilder language(Language... language) {
            params.put(LANGUAGE.getValue(), Arrays.stream(language).map(l->l.getValue()).collect(Collectors.toList()));
            return this;
        }

        public ApiBuilder format(Format... format){
            params.put(FORMAT.getValue(), Arrays.stream(format).map(f->f.getValue()).collect(Collectors.toList()));
            return this;
        }

//        public Response callApi() {
//            return RestAssured.with()
//                    .queryParams(params)
//                    .log().all()
//                    .get(YANDEX_SPELLER_API_URI).prettyPeek();
//        }

        public YandexSpellerApi buildRequest() {
            return new YandexSpellerApi(params);
        }
    } //end

    public Response sendGetRequest(){
        return RestAssured
                .given(baseRequestConfiguration())
                .queryParams(params)
                .get(spellerUrl)

                .prettyPeek();
    }

    public Response sendPostRequest(){
        return RestAssured
                .given(baseRequestConfiguration())
                .queryParams(params)
                .post(spellerUrl)
                .prettyPeek();
    }

    //get ready Speller answers list form api response
    public static List<YandexSpellerAnswer> getAnswers(Response response){
        List<List<YandexSpellerAnswer>> answers = new Gson()
                .fromJson(response.asString().trim(), new TypeToken<List<List<YandexSpellerAnswer>>>(){
                }.getType());
        return answers.stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public static List<String>getStringResult(Response response){
        return getAnswers(response).stream().map(res->res.word).collect(Collectors.toList());
    }

    //get ready Speller answers list form api response
    public static List<YandexSpellerAnswer> getYandexSpellerAnswers(Response response){
        return new Gson().fromJson( response.asString().trim(), new TypeToken<List<YandexSpellerAnswer>>() {}.getType());
    }

    //set base request and response specifications tu use in tests
    public static ResponseSpecification successResponse(){
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectHeader("Connection", "keep-alive")
                .expectResponseTime(lessThan(20000L))
                .expectStatusCode(HttpStatus.SC_OK)
                .build();
    }

    public static RequestSpecification baseRequestConfiguration(){
        return new RequestSpecBuilder()
                .setAccept(ContentType.XML)
                .addQueryParam("requestNumber", ++requestNumber)
                .setBaseUri(spellerUrl)
                .build();
    }

    public static ResponseSpecification failResponse(){
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.TEXT)
                .expectHeader("Connection","keep-alive")
                .expectResponseTime(lessThan(20000L))
                .expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .build();
    }
    public YandexSpellerApi buildRequest(){
        return new YandexSpellerApi(params);
    }
}
