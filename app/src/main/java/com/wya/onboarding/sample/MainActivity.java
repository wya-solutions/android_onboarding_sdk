package com.wya.onboarding.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.wya.identityValidator.StepperActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, StepperActivity.class);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("data");
                TextView text= findViewById(R.id.textView);
                try {
                    JSONObject obj = new JSONObject(result);
                    text.setText(obj.toString(5));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                TextView text= findViewById(R.id.textView);
                text.setText("Code: " + data.getStringExtra("code") + " - " + data.getStringExtra("message"));
            }
        }
    }
}
