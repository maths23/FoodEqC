package com.ecp_project.carriere_eung.foodeqc.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ecp_project.carriere_eung.foodeqc.DatabaseHandler;
import com.ecp_project.carriere_eung.foodeqc.Entity.Repas;
import com.ecp_project.carriere_eung.foodeqc.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by eung on 05/06/16.
 */
public class StatisticsActivity extends AppCompatActivity{
    DatabaseHandler db;

    int temporaryNumberOfDaysShown;
    double average;


    GraphView graphLast7Days;
    Button buttonChangeGraphNumberOfDaysStats;
    TextView textViewStatisticEmissionPerDayAverage;
    ProgressBar progressBarTodayEmission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        graphLast7Days = (GraphView)findViewById(R.id.graphLast7Days);

        db = new DatabaseHandler(getApplication());

        temporaryNumberOfDaysShown = 5;
        initialiseGraph();

        buttonChangeGraphNumberOfDaysStats = (Button) findViewById(R.id.buttonChangeGraphNumberOfDaysStats);
        buttonChangeGraphNumberOfDaysStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(StatisticsActivity.this);
                alert.setTitle(R.string.set_number_days_for_graph_stats); //Set Alert dialog title here

                // Set an EditText view to get user input
                final EditText input = new EditText(StatisticsActivity.this);
                input.setText(String.valueOf(temporaryNumberOfDaysShown));
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(input);

                alert.setPositiveButton(R.string.confirm_proportion, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //You will get as string input data in this variable.
                        // here we convert the input to a string and show in a toast.
                        temporaryNumberOfDaysShown = Integer.parseInt(input.getEditableText().toString());
                        printGraph(temporaryNumberOfDaysShown);
                    }
                });

                alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        textViewStatisticEmissionPerDayAverage = (TextView)findViewById(R.id.textViewStatisticEmissionPerDayAverage);
        average = getEmissionPerDayAverage();
        textViewStatisticEmissionPerDayAverage.setText(String.valueOf(average));
        Log.e("Statistics",""+average);




    }




    private String labelGraphDateFormat(GregorianCalendar c) {
        return String.format("%02d",c.get(Calendar.DAY_OF_MONTH)) +"/"+String.format("%02d",c.get(Calendar.MONTH)+1);
    }

    private void initialiseGraph() {
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(c.getTimeInMillis()-1000*60*60*24*4);
        graphLast7Days.getGridLabelRenderer().setNumHorizontalLabels(4);
        graphLast7Days.getViewport().setXAxisBoundsManual(true);
        graphLast7Days.getViewport().setMinX(0);
        graphLast7Days.getViewport().setMaxX(4);
        List<String> days = new ArrayList<>();
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphLast7Days);
        for (int i = 0;i<5;i++) {
            days.add(labelGraphDateFormat(c));
            c.setTimeInMillis(c.getTimeInMillis()+1000*60*60*24);
        }
        String[] dayslabel = days.toArray(new String[days.size()]);
        staticLabelsFormatter.setHorizontalLabels(dayslabel);
        graphLast7Days.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        LineGraphSeries<DataPoint> series = lastDaysRepasSeries(4);


        graphLast7Days.addSeries(series);
    }

    private void printGraph(int number_of_days) {
        GregorianCalendar c = new GregorianCalendar();
        graphLast7Days.removeAllSeries();
        graphLast7Days.addSeries(lastDaysRepasSeries(number_of_days));
        graphLast7Days.getViewport().setMaxX(number_of_days);
        graphLast7Days.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphLast7Days);
        String today = labelGraphDateFormat(c);
        c.setTimeInMillis(c.getTimeInMillis() - 1000*60*60*24*number_of_days);
        String endday = labelGraphDateFormat(c);
        //staticLabelsFormatter.setHorizontalLabels(new String[] {today,endday});
        graphLast7Days.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

    private LineGraphSeries<DataPoint> lastDaysRepasSeries(int number_of_days) {
        List<DataPoint> points = new ArrayList<>();
        List<Repas> repasList = db.getLastDaysRepas(number_of_days);
        if (repasList.size() > 0) {
            GregorianCalendar latestDate = new GregorianCalendar();
            latestDate.clear(Calendar.HOUR_OF_DAY);
            latestDate.clear(Calendar.MINUTE);
            latestDate.clear(Calendar.SECOND);
            latestDate.clear(Calendar.MILLISECOND);

            latestDate.set(Calendar.HOUR_OF_DAY, 0);
            latestDate.set(Calendar.MINUTE,0);
            latestDate.set(Calendar.SECOND,0);
            latestDate.set(Calendar.MILLISECOND,0);


            long time = latestDate.getTimeInMillis();
            long last_day = time - 1000*60*60*24*number_of_days;
            int index = number_of_days;
            double cO2EquivalentDay = 0;
            int pos = 0;

            while (time >= last_day) {
                while (pos < repasList.size()) {
                    Repas repas = repasList.get(pos);
                    long repasTime = repas.getDate().getTimeInMillis();
                    if (repasTime >= time) {
                        cO2EquivalentDay += repas.getCo2Equivalent();
                        pos += 1;
                    }
                    else {
                        points.add(new DataPoint(index,cO2EquivalentDay));
                        Log.e("Stats points", "New point " + index + " "+ cO2EquivalentDay);
                        index -= 1;
                        cO2EquivalentDay =0;
                        time -= 1000*60*60*24;
                    }
                }

                points.add(new DataPoint(index,cO2EquivalentDay));
                Log.e("Stats points", "New point " + index + " "+ cO2EquivalentDay);
                index -= 1;
                cO2EquivalentDay =0;
                time -= 1000*60*60*24;

            }
        }


        return new LineGraphSeries<>(points.toArray(new DataPoint[points.size()]));
    }

    public double getEmissionPerDayAverage() {
        GregorianCalendar latestDate = new GregorianCalendar();
        latestDate.clear(Calendar.HOUR_OF_DAY);
        latestDate.clear(Calendar.MINUTE);
        latestDate.clear(Calendar.SECOND);
        latestDate.clear(Calendar.MILLISECOND);

        latestDate.set(Calendar.HOUR_OF_DAY, 0);
        latestDate.set(Calendar.MINUTE,0);
        latestDate.set(Calendar.SECOND,0);
        latestDate.set(Calendar.MILLISECOND,0);


        long time = latestDate.getTimeInMillis();
        double totalCO2Equivalent = 0;
        int number_of_day =0;
        for (Repas repas: db.getAllRepas()) {
            if (repas.getDate().getTimeInMillis()<time) {
                number_of_day+=1;
                time -= 1000*60*60*24;
            }
            totalCO2Equivalent+=repas.getCo2Equivalent();
        }

        return totalCO2Equivalent/number_of_day;
    }
}
