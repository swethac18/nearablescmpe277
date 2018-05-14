package com.estimote.notification;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.estimote.notification.R;
import com.estimote.notification.estimote.NotificationsManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;


public class AnalyticsActivity extends AppCompatActivity {

    private static String TAG = "AnalysticsActivity";

    private String[] xData = {"near", "ambiguos", "out-of-range"};

    PieChart pieChart;
    @Override
    protected  void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        Log.d(TAG, "onCreate: Starting to create chart");

        pieChart = (PieChart) findViewById(R.id.idPieChart);
        pieChart.setRotationEnabled(true);

        pieChart.setHoleRadius(25);
        pieChart.setCenterText("Pet/Kid-Analystics");
        pieChart.setCenterTextSize(10);
        addDataset(pieChart);

    }

    private void addDataset(PieChart chart) {
        ArrayList<PieEntry> xEntry = new ArrayList<>();
        ArrayList<PieEntry> yEntry = new ArrayList<>();

        for(int i = 0; i < xData.length; i++) {
            xEntry.add(new PieEntry(i, xData[i]));

        }
        try {
            double zero = NotificationsManager.Entry.get(0) + NotificationsManager.Exit.get(0);
            zero /= 2;

            double four = NotificationsManager.Entry.get(4) + NotificationsManager.Exit.get(4);
            four /= 2;

            double ten = NotificationsManager.Entry.get(10) + NotificationsManager.Exit.get(10);
            ten /= 2;

            double faraway = NotificationsManager.OutOfRangeCounter;


            four = (four + ten) / 2;

            double total = four + faraway + zero;

            four = (four * 100) / total;
            zero = (zero * 100) / total;
            faraway = (faraway * 100) / total;

            yEntry.add(new PieEntry((float) (zero), "% near"));
            yEntry.add(new PieEntry((float) (four), "% ambiguos"));
            yEntry.add(new PieEntry((float) (faraway), "% out-of-range"));


            PieDataSet dataSet = new PieDataSet(yEntry, "Where was my kid/pet?");
            dataSet.setSliceSpace(2);
            dataSet.setValueTextSize(12);

            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.GREEN);
            colors.add(getResources().getColor(android.R.color.holo_orange_light));
            colors.add(Color.RED);

            Legend legend = pieChart.getLegend();
            legend.setForm(Legend.LegendForm.CIRCLE);
            legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
            dataSet.setColors(colors);

            PieData pieData = new PieData(dataSet);
            chart.setData(pieData);
            chart.invalidate();
        } catch (Exception e) {
            Toast.makeText(AnalyticsActivity.this, "Not sufficient data captured", Toast.LENGTH_LONG).show();
            return;
        }


    }
}


