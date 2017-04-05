package com.jsonerrorparser;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button   btnValidate;
    private Snackbar snackbar;

//    private String error = "{\"error\":\"Error some field\"}";
//    private String error = "{\"non_fields_error\":[\" \",\"Error some field\",\"Error some field1\"]}";
    //private String error = "{\"non_fields_error\":[\"Error some field\",\"Error some field1\"], \"non_fields_error2\":[\"Error some field2\",\"Error some field3\"]}";
    //private String error = "{\"non_fields_error\":[[\"Error some field\",\"Error some field1\"],[\"Error some field in\",\"Error some field1 in\"]], \"non_fields_error2 in\":[\"Error some field2\",\"Error some field2\"]}";
    //private String error = "{\"non_fields_error\":[[\"Error some field\",\"Error some field1\"],[]], \"non_fields_error2\":[\"Error some field2\",\"Error some field3\"]}";
    //private String error = "{\"non_fields_error\":[[{},{}],[{\"non_fields_error\":[[\"Error some field\",{}],[]], \"non_fields_error2\":[{}]}]], \"non_fields_error2\":[{\"filds\":[{}, {\"key\":\"value\"}]},\"Error some field2\"]}";
    private String error = "{\n" +
            "  \"field1\": [\"error1\", {}],\n" +
            "  \"field2\": [\"error2\", \"error3\"],\n" +
            "  \"nested_fields\": [\n" +
            "    {\n" +
            "      \"field3\": [\"error4\", {}],\n" +
            "      \"field4\": [{}, {}],\n" +
            "      \"non_fields_error\": [[{}, []], [\"\", \"error5\"]]\n" +
            "    },\n" +
            "    {\n" +
            "      \"field5\": [[], {}],\n" +
            "      \"field6\": [[], [\"error6\"]],\n" +
            "      \"non_fields_error2\": [\"error7\", \"error8\"]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"non_fields_error1\": [\"error9\", \"error10\"],\n" +
            "  \"turbo_field\":{\"user\":{\"countries\":[[],[],[\"city\",\"kiev\"]], \"province\":{}}}\n" +
            "}";
//    private String error = "{\n" +
//        "  \"field1\": [\"error1\", \"error2\"],\n" +
//        "  \"field2\": [\"error3\", \"error4\"],\n" +
//        "  \"nested_fields\": [\n" +
//        "    {\n" +
//        "      \"field3\": [],\n" +
//        "      \"field4\": [],\n" +
//        "      \"non_fields_error\": [\"error5\", \"error6\"]\n" +
//        "    },\n" +
//        "    {\n" +
//        "      \"field5\": [\"error7\", \"error8\"],\n" +
//        "      \"field6\": [\"error9\", \"error10\"],\n" +
//        "      \"non_fields_error1\": [\"error11\", \"error12\"]\n" +
//        "    }\n" +
//        "  ],\n" +
//        "  \"non_fields_error2\": [\"error13\", \"error14\"]\n" +
//        "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnValidate = (Button) findViewById(R.id.btnValidate);
        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    snackbar = Snackbar.make(view, Parser.simpleParsing(error, "field1"), Snackbar.LENGTH_SHORT)
                            .setAction("Cancel", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackbar.dismiss();
                                }
                            });
                    snackbar.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
