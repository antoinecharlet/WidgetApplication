package com.example.antoine.widgetapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.antoine.HandleXML.HandleXMLSaintJour;
import com.example.antoine.settings.SettingsActivity;

import org.json.JSONException;

import layout.AppWidget;

public class MainActivity extends AppCompatActivity {
    private String finalUrl="http://www.ephemeride-jour.fr/rss/rss_saints.php";
    private HandleXMLSaintJour obj;
    TextView title_saint, title_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSaintDuJour();
    }

    private void initSaintDuJour() {
        title_saint = (TextView) findViewById(R.id.text_saint);
        try {
            title_saint.setText(AppWidget.getFeteOfDay(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            lancerParametres();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void lancerParametres() {
        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
    }
}
