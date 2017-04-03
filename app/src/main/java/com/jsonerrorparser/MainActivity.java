package com.jsonerrorparser;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Button   btnValidate;
    private Snackbar snackbar;

    //private String error = "{\"error\":\"Error some field\"}";
    //private String error = "{\"non_fields_error\":[\" \",\"Error some field\",\"Error some field1\"]}";
    //private String error = "{\"non_fields_error\":[\"Error some field\",\"Error some field1\"], \"non_fields_error2\":[\"Error some field2\",\"Error some field2\"]}";
    //private String error = "{\"non_fields_error\":[[\"Error some field\",\"Error some field1\"],[\"Error some field in\",\"Error some field1 in\"]], \"non_fields_error2 in\":[\"Error some field2\",\"Error some field2\"]}";
    private String error = "{\"non_fields_error\":[[\"Error some field\",\"Error some field1\"],[]], \"non_fields_error2\":[\"Error some field2\",\"Error some field2\"]}";
    //private String error = "{\"non_fields_error\":[[{},{}],[{\"non_fields_error\":[[\"Error some field\",{}],[]], \"non_fields_error2\":[{}]}]], \"non_fields_error2\":[{\"filds\":[{}, {\"key\":\"value\"}]},\"Error some field2\"]}";
//    private String error = "{\n" +
//            "  \"field1\": [\"error1\", {}],\n" +
//            "  \"field2\": [\"error1\", \"error2\"],\n" +
//            "  \"nested_fields\": [\n" +
//            "    {\n" +
//            "      \"field1\": [\"error1\", {}],\n" +
//            "      \"field2\": [{}, {}],\n" +
//            "      \"non_fields_error\": [[{}, []], [\"\", \"error2\"]]\n" +
//            "    },\n" +
//            "    {\n" +
//            "      \"field1\": [[], {}],\n" +
//            "      \"field2\": [[], [\"error2\"]],\n" +
//            "      \"non_fields_error\": [\"error1\", \"error2\"]\n" +
//            "    }\n" +
//            "  ],\n" +
//            "  \"non_fields_error\": [\"error1\", \"error2\"],\n" +
//            "  \"turbo_field\":{\"user\":{\"countries\":[[],[],[\"city\",\"kiev\"]], \"province\":{}}}\n" +
//            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnValidate = (Button) findViewById(R.id.btnValidate);
        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Parser.jsonToMap(error);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                snackbar = Snackbar.make(view, Parser.parse(error), Snackbar.LENGTH_SHORT)
                        .setAction("Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        });
                snackbar.show();
            }
        });
    }
}
