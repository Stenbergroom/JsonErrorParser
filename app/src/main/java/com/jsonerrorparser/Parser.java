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

    public static String parse(String stringJson) {
        String errorMessage = "";
        errorMessage = simpleParsing(stringJson);
        return errorMessage;
    }

    private static String simpleParsing(String stringJson) {
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
            if (!isNullOrEmpty(errorKey) && !errorKey.equals("non_field_errors")) {
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
            if (!isNullOrEmpty(errorKey) && !errorKey.equals("non_field_errors")) {
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

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    private static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

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
        for (Map.Entry entry : map.entrySet()) {
            Log.d(TAG, entry.getKey().toString() + ", " + entry.getValue().toString());
        }
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


}
