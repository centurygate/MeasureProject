package com.example.laser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.laser.HistoryActivity;
import com.example.laser.R;

public class HomeActivity extends AppCompatActivity {

    private ImageButton realButton;
    private ImageButton historyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        realButton = (ImageButton) findViewById(R.id.imageButton2);
        realButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "realTime", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        historyButton = (ImageButton) findViewById(R.id.imageButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "History", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(HomeActivity.this, HistoryNewActivity.class);
                startActivity(intent2);
            }
        });
    }

/*    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.real_time:
                //Toast.makeText(this, "You clicked RealTime", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.history:
                //Toast.makeText(this, "You clicked History", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(HomeActivity.this, HistoryActivity.class);
                startActivity(intent2);
                break;
            default:
        }
        return true;
    }*/
}
