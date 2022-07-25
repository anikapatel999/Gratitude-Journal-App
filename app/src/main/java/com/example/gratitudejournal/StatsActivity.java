package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
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
import com.anychart.charts.TagCloud;
import com.anychart.core.cartesian.series.Line;
import com.anychart.core.ui.Paginator;
import com.anychart.data.Mapping;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Anchor;
import com.anychart.graphics.vector.Stroke;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.example.myapplication.User;
import com.example.myapplication.Entry;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatsActivity extends AppCompatActivity {

    public static final String TAG = "StatsActivity";
    GraphView gvStats1;
    //AnyChartView acvStats2;
    PieChart pcStats2;
    AnyChartView acvStats3;
    TextView tvNo;
    TextView tvTitle1;
    TextView tvTitle2;
    TextView tvTitle3;
    Animation fade_in_anim;
    Animation slide_in;
    String entryText = "";
    String[] et;
    //int max_x;
    ArrayList<String> allMoodsArray = new ArrayList<>();
    boolean hasMoods = true;
    boolean hasEntries = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        ConstraintLayout cl = findViewById(R.id.cl);
        cl.setBackgroundResource(R.color.warm);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

        gvStats1 = findViewById(R.id.gvStats1);
        tvNo = findViewById(R.id.tvNo);
        tvTitle1 = findViewById(R.id.tvTitle1);
        tvTitle2 = findViewById(R.id.tvTitle2);
        pcStats2 = findViewById(R.id.pcStats2);
        tvTitle3 = findViewById(R.id.tvTitle3);
        acvStats3 = findViewById(R.id.acvStats3);
        fade_in_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);
        slide_in.setDuration(1400);

        //makeTagCloud();
        queryEntries();

        makeLineGraph();

        makePieChart();
    }

    private void queryEntries() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<Entry> query = ParseQuery.getQuery(Entry.class);
        query.whereMatches("user", currentUser.getObjectId());
        query.addDescendingOrder("createdAt");
        query.setLimit(30);
        query.findInBackground(new FindCallback<Entry>() {
            @Override
            public void done(List<Entry> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                if (objects.size() == 0){
                    hasEntries = false;
                }
                for (Entry entry : objects) {
                    entryText = entryText + " " + entry.getText();
                }
                et = split();
                makeTagCloud(et);
                setVisibility();
            }
        });
    }

    private String[] split() {
        // entryText = "I would love to have a dog, dogs are very fun, yes :)";
        String[] text = entryText.split("\\s+");
        for (int i = 0; i < text.length; i++) {
            text[i] = text[i].replaceAll("[^\\w]", "");
            Log.i(TAG, "THIS IS A WORD IN THE SPLIT " + text[i]);
        }
        Log.i(TAG, "THIS IS THE SPLIT STRING" + text);
        return text;
    }

    private void makeTagCloud(String[] et) {
        List<DataEntry> data = new ArrayList<>();

//        data.add(new ValueDataEntry("hello", 3));
//        data.add(new ValueDataEntry("aaa", 1));
//        data.add(new ValueDataEntry("dlkmvldfkvm", 30));
//        data.add(new ValueDataEntry("wheee", 3));

        List<String> entries = new ArrayList<>();
        entries = Arrays.asList(et);
        Set<String> distinct = new HashSet<>(entries);
        for (String w : distinct) {
            if (w.length() > 2 && !w.equals("the") && !w.equals("and") && !w.equals("entry") && !w.equals("that") && !w.equals("was"))
            data.add(new ValueDataEntry(w, Collections.frequency(entries, w)));
        }

        TagCloud tagCloud = AnyChart.tagCloud();

        tagCloud.data(data);

        acvStats3.setBackgroundColor(Globals.warmColor);
        tagCloud.background().enabled(true);
        tagCloud.background().fill(Globals.warmColor);

        String[] colors = {Globals.amazingColor, Globals.goodColor, Globals.okayColor, Globals.badColor, Globals.terribleColor};
        tagCloud.palette(colors);

        acvStats3.startAnimation(fade_in_anim);
        acvStats3.setChart(tagCloud);
    }

    private void makeLineGraph() {
        // get the array of moods
        JSONArray allMoods = getArray();

        // put the data in the correct format for the line graph
        LineGraphSeries<DataPoint> series;
        series = new LineGraphSeries<DataPoint>(getData(allMoods));

        // set the format of the line graph
        GridLabelRenderer gridLabel = gvStats1.getGridLabelRenderer();
        gridLabel = setGridLabel(gridLabel);

        // set the line graph
        gvStats1.startAnimation(slide_in);
        series = setSeries(series);
        gvStats1.addSeries(series);

        gvStats1.getViewport().setYAxisBoundsManual(true);
        gvStats1.getViewport().setMaxY(4);
        gvStats1.getViewport().setMinY(0);

//        gvStats1.getViewport().setXAxisBoundsManual(true);
//        gvStats1.getViewport().setMaxX(max_x + 2);
    }

    private void makePieChart() {

        setupPieChart();
        loadPieChartData();

        // This code is for all myChartView
//        Log.i(TAG, "freq amazing: " + Collections.frequency(allMoodsArray, Globals.amazing));
//        Log.i(TAG, "freq good: " + Collections.frequency(allMoodsArray, Globals.good));
//        Pie pie = AnyChart.pie();
//        List<DataEntry> data = new ArrayList<>();
//
//        data.add(new ValueDataEntry(Globals.amazing, Collections.frequency(allMoodsArray, Globals.amazing)));
//        data.add(new ValueDataEntry(Globals.good, Collections.frequency(allMoodsArray, Globals.good)));
//        data.add(new ValueDataEntry(Globals.okay, Collections.frequency(allMoodsArray, Globals.okay)));
//        data.add(new ValueDataEntry(Globals.bad, Collections.frequency(allMoodsArray, Globals.bad)));
//        data.add(new ValueDataEntry(Globals.terrible, Collections.frequency(allMoodsArray, Globals.terrible)));
//        pie.data(data);
//
//        String[] colors = {Globals.amazingColor, Globals.goodColor, Globals.okayColor, Globals.badColor, Globals.terribleColor};
//        pie.palette(colors);
//        //pie.labels().position("outside");
//        acvStats2.setBackgroundColor(Globals.warmColor);
//        pie.background().enabled(true);
//        pie.background().fill(Globals.warmColor);
//
//        acvStats2.startAnimation(fade_in_anim);
//        acvStats2.setChart(pie);
    }

    private void setupPieChart() {
        pcStats2.setDrawHoleEnabled(false);
        pcStats2.setUsePercentValues(true);
        pcStats2.setEntryLabelTextSize(12);
        pcStats2.setEntryLabelColor(Color.WHITE);
        pcStats2.setCenterTextSize(24);
        pcStats2.getDescription().setEnabled(false);

//        com.github.mikephil.charting.components.Legend l = pcStats2.getLegend();
//        l.setVerticalAlignment(com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.TOP);
////        l.setHorizontalAlignment(com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(com.github.mikephil.charting.components.Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);
//        l.setEnabled(true);
        Legend l = pcStats2.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
    }

    private void loadPieChartData() {
        List<PieEntry> data = new ArrayList<>();
        ArrayList<Float> percentages = calcFreqs();

        data.add(new PieEntry(percentages.get(0), Globals.amazing));
        data.add(new PieEntry(percentages.get(1), Globals.good));
        data.add(new PieEntry(percentages.get(2), Globals.okay));
        data.add(new PieEntry(percentages.get(3), Globals.bad));
        data.add(new PieEntry(percentages.get(4), Globals.terrible));

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.amazing));
        colors.add(getResources().getColor(R.color.good));
        colors.add(getResources().getColor(R.color.okay));
        colors.add(getResources().getColor(R.color.bad));
        colors.add(getResources().getColor(R.color.terrible));

        PieDataSet dataSet = new PieDataSet(data, "");
        dataSet.setColors(colors);

        PieData d = new PieData(dataSet);
        d.setDrawValues(true);
        d.setValueFormatter(new PercentFormatter(pcStats2));
        d.setValueTextSize(12f);
        d.setValueTextColor(Color.WHITE);

//        pcStats2.startAnimation(fade_in_anim);

        pcStats2.setData(d);
        pcStats2.invalidate();

        pcStats2.animateY(2000, Easing.EaseInOutQuad);
    }

    private ArrayList calcFreqs() {
        ArrayList<Float> percentages = new ArrayList();

        float amazingFreq = Collections.frequency(allMoodsArray, Globals.amazing);
        float goodFreq = Collections.frequency(allMoodsArray, Globals.good);
        float okayFreq = Collections.frequency(allMoodsArray, Globals.okay);
        float badFreq = Collections.frequency(allMoodsArray, Globals.bad);
        float terribleFreq = Collections.frequency(allMoodsArray, Globals.terrible);

        float total = amazingFreq + goodFreq + okayFreq + badFreq + terribleFreq;

        percentages.add((float) (amazingFreq/total));
        percentages.add((float) (goodFreq/total));
        percentages.add((float) (okayFreq/total));
        percentages.add((float) (badFreq/total));
        percentages.add((float) (terribleFreq/total));
        return percentages;
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
            hasMoods = false;
            // setVisibility();
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

    private void setVisibility() {
        if (!hasMoods && !hasEntries) {
            tvNo.setText(R.string.no_stats_to_display);
            tvNo.setVisibility(View.VISIBLE);
        }
        if (hasEntries) {
            tvTitle3.setVisibility(View.VISIBLE);
            acvStats3.setVisibility(View.VISIBLE);
        }

        if (hasMoods) {
            gvStats1.setVisibility(View.VISIBLE);
            gvStats1.setVisibility(View.VISIBLE);
            pcStats2.setVisibility(View.VISIBLE);
            tvTitle2.setVisibility(View.VISIBLE);
            tvTitle1.setVisibility(View.VISIBLE);
        }
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