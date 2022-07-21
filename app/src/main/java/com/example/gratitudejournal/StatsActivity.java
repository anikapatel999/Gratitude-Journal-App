package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Anchor;
import com.anychart.graphics.vector.Stroke;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.parse.ParseUser;
import com.example.myapplication.User;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    public static final String TAG = "StatsActivity";
    GraphView gvStats1;
    TextView tvNoMoods;
    TextView tvTitle1;
    Animation fade_in_anim;
    AnyChartView acvStats2;
    //int max_x;
    ArrayList<String> allMoodsArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        ConstraintLayout cl = findViewById(R.id.cl);
        cl.setBackgroundResource(R.color.warm);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

        gvStats1 = findViewById(R.id.gvStats1);
        tvNoMoods = findViewById(R.id.tvNoMoods);
        tvTitle1 = findViewById(R.id.tvTitle1);

        acvStats2 = findViewById(R.id.acvStats2);

        fade_in_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        // get the array of moods
        JSONArray allMoods = getArray();

        LineGraphSeries<DataPoint> series;
        series = new LineGraphSeries<DataPoint>(getData(allMoods));

        GridLabelRenderer gridLabel = gvStats1.getGridLabelRenderer();
        gridLabel = setGridLabel(gridLabel);

        gvStats1.startAnimation(fade_in_anim);
        gvStats1.addSeries(series);

        series = setSeries(series);

//        gvStats1.getViewport().setXAxisBoundsManual(true);
//        gvStats1.getViewport().setMaxX(max_x + 2);

        // set axes of line graph
        gvStats1.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
        {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                String[] moods = {Globals.terrible, Globals.bad, Globals.okay, Globals.good, Globals.amazing,};

                if(isValueX) {
                    return "\n" + super.formatLabel(value, isValueX) + "\n ";
                    //return "";
                }
                else {
                    int val = (int) value;
                    return moods[val] + "  ";
                }
            }
        });

        makePieChart();
    }

    private void makePieChart() {

        HashSet<String> allMoodsSet = new HashSet<>(allMoodsArray);
        Log.i(TAG, "freq amazing: " + Collections.frequency(allMoodsArray, Globals.amazing));
        Log.i(TAG, "freq good: " + Collections.frequency(allMoodsArray, Globals.good));
        Pie pie = AnyChart.pie();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry(Globals.amazing, Collections.frequency(allMoodsArray, Globals.amazing)));
        data.add(new ValueDataEntry(Globals.good, Collections.frequency(allMoodsArray, Globals.good)));
        data.add(new ValueDataEntry(Globals.okay, Collections.frequency(allMoodsArray, Globals.okay)));
        data.add(new ValueDataEntry(Globals.bad, Collections.frequency(allMoodsArray, Globals.bad)));
        data.add(new ValueDataEntry(Globals.terrible, Collections.frequency(allMoodsArray, Globals.terrible)));
        pie.data(data);
//        String[] colors = {"#90caf9", "#80cbc4", "#aed581", "#e6ee9c", "#ffcc80"};
//        pie.palette(colors);
        acvStats2.setChart(pie);
    }

    private LineGraphSeries<DataPoint> setSeries(LineGraphSeries<DataPoint> series) {
        series.setColor(getResources().getColor(R.color.new_color));
        //series.setThickness(10);
        series.setDrawBackground(true);
        series.setBackgroundColor(getResources().getColor(R.color.amazing_transparent));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(5);
        return series;
    }

    private GridLabelRenderer setGridLabel(GridLabelRenderer gridLabel) {
        gridLabel.setHorizontalAxisTitleTextSize(50);
        gridLabel.setHorizontalAxisTitle("Each day you logged a mood");
        gridLabel.setHorizontalAxisTitleColor(getResources().getColor(R.color.new_color));
        //gridLabel.setVerticalAxisTitleTextSize(50);
        //gridLabel.setVerticalAxisTitle("Happiness");
        gridLabel.setVerticalAxisTitleColor(getResources().getColor(R.color.new_color));
        gridLabel.setHumanRounding(false);
        return gridLabel;
    }

    private JSONArray getArray() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        User currentUser2 = (User) currentUser;
        return currentUser2.getMoods();
    }

    public DataPoint[] getData(JSONArray allMoods) {
//        ArrayList<String> allMoodsArray = new ArrayList<>();
        for(int i = 0; i < allMoods.length(); i++) {
            try {
                if (!allMoods.get(i).toString().equals(Globals.skip) && !allMoods.get(i).toString().equals("No mood selected")){
                    allMoodsArray.add(allMoods.get(i).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (allMoodsArray.size() == 0) {
            gvStats1.setVisibility(View.GONE);
            tvNoMoods.setVisibility(View.VISIBLE);
            tvTitle1.setVisibility(View.GONE);
        }

        int n = allMoodsArray.size();
        DataPoint[] values = new DataPoint[n];

        int j = 0;
        for(int i = 0; i < allMoodsArray.size(); i++) {
            switch (allMoodsArray.get(i)) {
                case Globals.terrible:
                    DataPoint a = new DataPoint(j, 0);
                    values[j] = a;
                    j += 1;
                    break;

                case Globals.bad:
                    DataPoint b = new DataPoint(j, 1);
                    values[j] = b;
                    j += 1;
                    break;

                case Globals.okay:
                    DataPoint c = new DataPoint(j, 2);
                    values[j] = c;
                    j += 1;
                    break;

                case Globals.good:
                    DataPoint d = new DataPoint(j, 3);
                    values[j] = d;
                    j += 1;
                    break;

                case Globals.amazing:
                    DataPoint e = new DataPoint(j, 4);
                    values[j] = e;
                    j += 1;
                    break;
            }
        }
//        max_x = values.length;
        Log.i(TAG, "# POINTS: " + values.length);
        return values;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        menu.findItem(R.id.mentions).setVisible(false);
        return true;
    }

    public void onMentions(MenuItem item) {
        Intent intent = new Intent(StatsActivity.this, ViewMentionsActivity.class);
        startActivity(intent);
    }

    public void onLogout(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(StatsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHome(MenuItem item) {
        Intent intent = new Intent(StatsActivity.this, HomeActivity.class);
        startActivity(intent);
        // setVisible(false);
        //finish();
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(StatsActivity.this, SettingsActivity.class);
        startActivity(intent);
        // setVisible(false);
        //finish();
    }
}