package com.jsonerrorparser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 3/31/17.
 */

public class Parser {

    private static final String TAG = Parser.class.getSimpleName();

    private static Map<String, Object> clearedMap    = new HashMap<>();
    private static Map<String, Object> notClearedMap = new HashMap<>();
    private static Map<String, Object> notNestingMap = new HashMap<>();
    private static String source;

    public static String simpleParsing(String stringJson) {
        String errorMessage = "";
        errorMessage = parseFirstError(stringJson, "");
        return errorMessage;
    }

    public static String simpleParsing(String stringJson, String exceptKey) {
        String errorMessage = "";
        errorMessage = parseFirstError(stringJson, exceptKey);
        return errorMessage;
    }

    private static String parseFirstError(String stringJson, String exceptKey) {
        source = stringJson;
        String errorKey = "";
        String message = "";
        try {
            int defaultValueIndex = 0;
            JSONObject jsonObject = new JSONObject(stringJson);
            Iterator<String> keys = jsonObject.keys();

            message = jsonObject.getJSONArray(keys.next()).get(defaultValueIndex).toString();

            //return message;
//            for (Iterator<String> iter = jsonObject.keys(); iter.hasNext(); ) {
//                errorKey = iter.next();
//            }
            Iterator<String> iter = jsonObject.keys();
            errorKey = iter.next();
            if (!isNullOrEmpty(errorKey) && !errorKey.equals(exceptKey)) {
                Log.d(TAG, "parsed JSONArray");
                return errorKey + " - " + message;
            } else {
                return message;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(stringJson);
            Iterator<String> keys = jsonObject.keys();

            message = jsonObject.getString(keys.next());
            //return message;
            for (Iterator<String> iter = jsonObject.keys(); iter.hasNext(); ) {
                errorKey = iter.next();
            }
            if (!isNullOrEmpty(errorKey) && !errorKey.equals(exceptKey)) {
                Log.d(TAG, "parsed JSONObject");
                return errorKey + " - " + message;
            } else {
                return message;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    private static Map<String, Object> jsonToMap(String source) throws JSONException {
        JSONObject json = new JSONObject(source);
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (json != JSONObject.NULL) {
            retMap = toMap(json);
            notClearedMap = retMap;
        }
        clearUpMap(retMap);
//        try {
//            Log.d(TAG, "message by position - " + getMessageByPosition(3, 0, true));
//        } catch (IndexOutOfBoundsException e) {
//            e.printStackTrace();
//        }
        Log.d(TAG, "notnestingMap- " + notNestingMap.toString());
        return retMap;
    }

    private static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            notNestingMap.put(key, value);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            if (value instanceof List) {
                if (!((List<Object>) value).isEmpty()) {
                    map.put(key, value);
                }
            } else if (!value.toString().equals("{}")) {
                map.put(key, value);
            }
        }
        //
//        for (Map.Entry entry : map.entrySet()) {
//            Log.d(TAG, entry.getKey().toString() + ", " + entry.getValue().toString());
//        }
        //
        return map;
    }

    private static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            if (value instanceof List) {
                if (!((List<Object>) value).isEmpty()) {
                    list.add(value);
                }
            } else if (!value.toString().equals("{}")) {
                list.add(value);
            }
        }
        return list;
    }

    private static void clearUpMap(Map<String, Object> map) {
        Log.d(TAG, "================================");
        for (Map.Entry entry : map.entrySet()) {
            String value = entry.getValue().toString();
            value = value
                    .replaceAll("\\[", "")
                    .replaceAll("\\]", "")
                    .replaceAll("\\{", "")
                    .replaceAll("\\}", "")
                    .replaceAll("=", ":")
                    .trim();
            clearedMap.put(entry.getKey().toString(), value);
            Log.d(TAG, entry.getKey().toString() + ", " + entry.getValue().toString());
            Log.d(TAG, "||||||||||||||||||||||||||||");
        }
        for (Map.Entry entry : clearedMap.entrySet()) {
            Log.d(TAG, entry.getKey().toString() + ", " + entry.getValue().toString());
        }
        Log.d(TAG, "================================");
    }

    public static String getMessageByPosition(String source, int keyPosition, int messagePosition, boolean cleared) throws Exception {
        jsonToMap(source);
        Map<String, Object> map = cleared ? clearedMap : notClearedMap;
        if (keyPosition >= map.size()) {
            throw new IndexOutOfBoundsException("Make sure that indexes of keyPosition and messagePosition are valid");
        }
        String neededMessage = "";
        int i = 0;
        for (Map.Entry entry : map.entrySet()) {
            if (i++ == keyPosition) {
                String[] messagesArr = entry.getValue().toString().split(", ");
                neededMessage = messagesArr[messagePosition];
                break;
            }
        }
        return neededMessage;
    }

    public static String getKeyWithMessageByPosition(String source, int keyPosition, int messagePosition, boolean cleared) throws Exception {
        jsonToMap(source);
        Map<String, Object> map = cleared ? clearedMap : notClearedMap;
        if (keyPosition >= map.size()) {
            throw new IndexOutOfBoundsException("Make sure that indexes of keyPosition and messagePosition are valid");
        }
        String neededMessage = "";
        String neededKey = "";
        int i = 0;
        for (Map.Entry entry : map.entrySet()) {
            if (i++ == keyPosition) {
                String[] messagesArr = entry.getValue().toString().split(", ");
                neededKey = entry.getKey().toString();
                neededMessage = messagesArr[messagePosition];
                break;
            }
        }
        return neededKey + " - " + neededMessage;
    }

    public static String getMessageByKey(String source, String key, boolean cleared) throws Exception {
        jsonToMap(source);
        //Map<String, Object> map = cleared ? clearedMap : notClearedMap;
        Map<String, Object> map = notNestingMap;
        String neededMessage = "";
        for (Map.Entry entry : map.entrySet()) {
            if (key.equals(entry.getKey().toString())) {
                neededMessage = entry.getValue().toString().trim();
                break;
            }
        }
        return neededMessage;
    }

    public static String getMessageByKey(String source, String key, int messagePosition, boolean cleared) throws Exception {
        jsonToMap(source);
        Map<String, Object> map = cleared ? clearedMap : notClearedMap;
        String neededMessage = "";
        int i = 0;
        for (Map.Entry entry : map.entrySet()) {
            if (key.equals(entry.getKey().toString())) {
                String[] messagesArr = entry.getValue().toString().split(", ");
                neededMessage = messagesArr[messagePosition];
                break;
            }
        }
        return neededMessage;
    }

    // TODO: 4/4/17 all errors and keys

}
