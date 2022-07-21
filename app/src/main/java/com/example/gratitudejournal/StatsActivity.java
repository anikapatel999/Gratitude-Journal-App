package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
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
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    // AnyChartView cvStats1;
    GraphView gvStats1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        ConstraintLayout cl = findViewById(R.id.cl);
        cl.setBackgroundResource(R.color.warm);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

        //cvStats1 = findViewById(R.id.cvStats1);
        gvStats1 = findViewById(R.id.gvStats1);

        ParseUser currentUser = ParseUser.getCurrentUser();
        User currentUser2 = (User) currentUser;
        JSONArray allMoods = currentUser2.getMoods();

//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(0, 0),
//                new DataPoint(1, 1),
//                new DataPoint(2, 2),
//                new DataPoint(3, 4),
//                new DataPoint(4, 3)
//        });

        LineGraphSeries<DataPoint> series;
        series = new LineGraphSeries<DataPoint>(getData(allMoods));

        series.setTitle("Happiness Over Time");

        GridLabelRenderer gridLabel = gvStats1.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitleTextSize(50);
        gridLabel.setHorizontalAxisTitle("\n \n \n \n \n \n \n Each day you logged a mood");
        gridLabel.setHorizontalAxisTitleColor(getResources().getColor(R.color.new_color));
        gridLabel.setVerticalAxisTitleTextSize(50);
        gridLabel.setVerticalAxisTitle("Happiness");
        gridLabel.setVerticalAxisTitleColor(getResources().getColor(R.color.new_color));

//        mGraph.getGridLabelRenderer().setVerticalAxisTitleTextSize(64);
//        mGraph.getGridLabelRenderer().setVerticalAxisTitle("Y Axis Title");
//        mGraph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(64);
//        mGraph.getGridLabelRenderer().setHorizontalAxisTitle("X Axis Title");

        gvStats1.addSeries(series);

        //series.setColor(R.color.new_color);
        series.setColor(getResources().getColor(R.color.new_color));
        series.setThickness(15);
        series.setDrawBackground(true);
        series.setBackgroundColor(getResources().getColor(R.color.amazing_transparent));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(12);

        gvStats1.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
        {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                //String[] moods =  {"Amazing", "Good", "Okay", "Bad", "Terrible"};
                // String[] moods =  {"Terrible", "Bad", "Okay", "Good", "Amazing"};
                String[] moods = {Globals.terrible, Globals.bad, Globals.okay, Globals.good, Globals.amazing,};

                if(isValueX) {
                    return super.formatLabel(value, isValueX);
                    //return "";
                }
                else {
                    int val = (int) value;
                    return moods[val];
                }
            }
        });

    }

    public DataPoint[] getData(JSONArray allMoods) {
        ArrayList<String> allMoodsArray = new ArrayList<>();
        for(int i = 0; i < allMoods.length(); i++) {
            try {
                if (!allMoods.get(i).toString().equals(Globals.skip) && !allMoods.get(i).toString().equals("No mood selected")){
                    allMoodsArray.add(allMoods.get(i).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        int n = allMoodsArray.size();     //to find out the no. of data-points
        DataPoint[] values = new DataPoint[n];

        int j = 0;
        //LineGraphSeries<DataPoint> data = new LineGraphSeries<DataPoint>();

        for(int i = 0; i < allMoodsArray.size(); i++) {
            switch (allMoodsArray.get(i)) {
                case "Terrible": // it didn't like me using the globals for these
                    //new DataPoint(j, 0);
                    DataPoint a = new DataPoint(j, 0);
                    values[j] = a;
                    j += 1;
                    break;

                case "Bad": // it didn't like me using the globals for these
                    // new DataPoint(j, 1);
                    DataPoint b = new DataPoint(j, 1);
                    values[j] = b;
                    j += 1;
                    break;

                case "Okay": // it didn't like me using the globals for these
                    // new DataPoint(j, 2);
                    DataPoint c = new DataPoint(j, 2);
                    values[j] = c;
                    j += 1;
                    break;

                case "Good": // it didn't like me using the globals for these
                    // new DataPoint(j, 3);
                    DataPoint d = new DataPoint(j, 3);
                    values[j] = d;
                    j += 1;
                    break;

                case "Amazing": // it didn't like me using the globals for these
                    // new DataPoint(j, 4);
                    DataPoint e = new DataPoint(j, 4);
                    values[j] = e;
                    j += 1;
                    break;
            }
        }
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