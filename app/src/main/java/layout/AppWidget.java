package layout;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.antoine.widgetapplication.MainActivity;
import com.example.antoine.widgetapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        CharSequence widgetText;

        // Try to get the Fete of day
        try {
            widgetText = getFeteOfDay(context);
        } catch (JSONException e) {
            widgetText = context.getString(R.string.aucun_resultat);
        }

        // Create an Intent to launch ExampleActivity
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Get the layout for the App Widget and attach an on-click listener
        // to the button
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        /*set on click*/
        views.setOnClickPendingIntent(R.id.appwidget, pendingIntent);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        setViewStyle(context, views);

        views.setTextViewText(R.id.appwidget_text, widgetText);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        Log.d("update", "widgetUpdate");
    }

    private static void setViewStyle(Context context, RemoteViews views) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        int color = prefs.getInt("widget_color", R.color.colorPrimary);
        Log.d("color", String.valueOf(color));
        int[] colors = {color, color};
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, colors);

        float dpi = context.getResources().getDisplayMetrics().xdpi;
        float dp = context.getResources().getDisplayMetrics().density;

        Bitmap bitmap = Bitmap.createBitmap(Math.round(288 * dp), Math.round(72 * dp), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        gradientDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        //corner
        gradientDrawable.setCornerRadius(5 * (dpi / 160));
        gradientDrawable.mutate();
        //contour blanc
        //gradientDrawable.setStroke(Math.round(2 * dp), Color.WHITE);
        //setAlpha (transparence)
        gradientDrawable.setAlpha(210);

        gradientDrawable.draw(canvas);
        views.setImageViewBitmap(R.id.bck_image, bitmap);
    }


    //recupere le saint du jour depuis le fichier JSON parsé
    private static String getFeteOfDay(Context context) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromAsset(context));
        JSONArray array = obj.getJSONArray(getMonth());
        array = array.getJSONArray(getDay());
        return array.get(1) + " " + array.get(0);
    }

    //parse le Json
    private static String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("saints.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * @return Mois
     */
    private static String getMonth() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MM", Locale.FRANCE);
        Integer month = Integer.valueOf(month_date.format(cal.getTime()));

        if (month == 1)
            return "january";
        if (month == 2)
            return "february";
        if (month == 3)
            return "march";
        if (month == 4)
            return "april";
        if (month == 5)
            return "may";
        if (month == 6)
            return "june";
        if (month == 7)
            return "july";
        if (month == 8)
            return "august";
        if (month == 9)
            return "september";
        if (month == 10)
            return "october";
        if (month == 11)
            return "november";
        if (month == 12)
            return "december";
        return null;
    }

    /**
     * @return jour du mois
     */
    private static Integer getDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH) - 1;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("bouton", intent.getAction());
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

