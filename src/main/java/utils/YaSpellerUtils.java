package utils;

import beans.YandexSpellerAnswer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class YaSpellerUtils {
    public static List<YandexSpellerAnswer> getExpectedAnswersForWrongInput(String text) {
        List<YandexSpellerAnswer> listExpectedAnswer = new ArrayList<YandexSpellerAnswer>();
        List<String> expectedTips = getExpectedTips(text);
        listExpectedAnswer.add(new YandexSpellerAnswer(1, text, expectedTips));
        return listExpectedAnswer;

    }

    public static List<String> getExpectedTips(String wordJson) {
        List<String> tips = null;
        try {
            File file = new File("src/main/resources/testData.json");
            JsonReader reader = new JsonReader(new FileReader(file.getAbsolutePath()));
            Map<String, String[]> dataFromJson = new Gson().fromJson(reader, new TypeToken<Map<String,String[]>>() {}.getType());
            tips = Arrays.asList(dataFromJson.get(wordJson));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return tips;
    }
}
