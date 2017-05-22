package com.testing.venkatkishore.drivinginfo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DrivingHistory extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private TextView Output;
    private Button changeDate;

    private int year;
    private int month;
    private int day;

    static final int DATE_PICKER_ID = 1111;

    String str_date,str_image,str_drivingdata;

    DatabaseHelper db;
    ArrayList<Drivingimagelistdetails> firstList = new ArrayList<Drivingimagelistdetails>();
    Spinner spinner;
    ImageView img_view;

    String drivingdatd;

    List<String> spinnerdati = new ArrayList<String>();

    List<String> imagedata = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driving_history);
        spinner = (Spinner) findViewById(R.id.spinner_driving);

        Output = (TextView) findViewById(R.id.Output);
        changeDate = (Button) findViewById(R.id.changedrivinginfo);
        img_view = (ImageView)findViewById(R.id.drivingimage) ;

        db = new DatabaseHelper(this);

        FirstTableData();

        spinner.setOnItemSelectedListener(this);
        loadSpinnerData();
        changeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                for (int i = 0; i < imagedata.size(); i++) {
                    str_drivingdata = imagedata.get(i);
                    Log.i("sdfff",str_drivingdata);
                    Log.i("select date",drivingdatd);

                    if (str_drivingdata.contains(drivingdatd)){
//                        Output.setText(str_drivingdata);
                        File imgFile = new File(str_image);
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
                                .getAbsolutePath());
                        img_view
                                .setImageBitmap(myBitmap);
                    }else {
                        Toast.makeText(DrivingHistory.this,"no image present",Toast.LENGTH_LONG).show();
                    }

                }
            }

        });

    }

    private void loadSpinnerData() {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerdati);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    private void FirstTableData() {


        ArrayList<Drivingimagelistdetails> response_array_from_db = db.Get_firstOutbox();
        for (int i = 0; i < response_array_from_db.size(); i++) {
            str_date = response_array_from_db.get(i).getNowinfo();
            str_image = response_array_from_db.get(i).getImagefileinfo();
            spinnerdati.add(str_date);
            imagedata.add(str_image);
        }
        db.close();
        Log.i("testing",str_date+"\n"+str_image);


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        drivingdatd = adapterView.getItemAtPosition(i).toString();

        // Showing selected spinner item
        Toast.makeText(adapterView.getContext(), "You selected: " + drivingdatd,
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
