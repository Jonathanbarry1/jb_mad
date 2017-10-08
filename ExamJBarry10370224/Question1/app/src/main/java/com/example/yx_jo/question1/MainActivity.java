package com.example.yx_jo.question1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private static final int PICK_STATION_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.buttonMaps);

        // Link UI elements to actions in code
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                startActivityForResult(new Intent(MainActivity.this, MapsActivity.class), PICK_STATION_REQUEST);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_STATION_REQUEST && resultCode == RESULT_OK){

             String location  = data.getStringExtra("location");

            Toast.makeText(MainActivity.this,
                    location + " is your favourite location in Westeros", Toast.LENGTH_LONG).show();

        }





        }

}
