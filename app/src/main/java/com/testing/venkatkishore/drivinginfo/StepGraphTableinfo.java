package com.testing.venkatkishore.drivinginfo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StepGraphTableinfo extends AppCompatActivity {

    private List<Drivinginfolist> drivingList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DrivingAdapter mAdapter;
    TextView tvdate;

    Bitmap screen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_graph_tableinfo);
        recyclerView = (RecyclerView) findViewById(R.id.listv);

        tvdate = (TextView)findViewById(R.id.txtdate) ;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
        String currentDateandTime = sdf.format(new Date());
        Toast.makeText(StepGraphTableinfo.this,"Hi " +  getIntent().getSerializableExtra("h").toString(),Toast.LENGTH_SHORT).show();

        Log.i("hashmapinfo",getIntent().getSerializableExtra("h").toString());

        tvdate.setText("Date: "+currentDateandTime);

        mAdapter = new DrivingAdapter(drivingList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareDrivingData();


//        View v1 = MyView.getRootView();
//        v1.setDrawingCacheEnabled(true);
//        screen= Bitmap.createBitmap(v1.getDrawingCache());
//        v1.setDrawingCacheEnabled(false);
//
//        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
//        RelativeLayout root = (RelativeLayout) inflater.inflate(R.layout.step_graph_tableinfo, null); //RelativeLayout is root view of my UI(xml) file.
//        root.setDrawingCacheEnabled(true);
//        Bitmap screen= getBitmapFromView(this.getWindow().findViewById(R.id.step_graph_tableinfo)); // here give id of our root layout (here its my RelativeLayout's id

    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    private void prepareDrivingData() {

        Drivinginfolist drivinginfo = new Drivinginfolist("00:00 am", "06:00 am", "6","Off Duty");
        drivingList.add(drivinginfo);

        drivinginfo = new Drivinginfolist("06:00 am", "09:00 am", "3","Sleep");
        drivingList.add(drivinginfo);

        drivinginfo = new Drivinginfolist("09:00 am", "10:00 am", "1","On Duty");
        drivingList.add(drivinginfo);

        drivinginfo = new Drivinginfolist("10:00 am", "01:00 pm", "4","Driving");
        drivingList.add(drivinginfo);

        drivinginfo = new Drivinginfolist("01:00 pm", "02:00 pm", "1","On Duty");
        drivingList.add(drivinginfo);

        drivinginfo = new Drivinginfolist("02:00 pm", "06:00 pm", "4","Driving");
        drivingList.add(drivinginfo);

        drivinginfo = new Drivinginfolist("06:00 pm", "10:00 pm", "4","Off Duty");
        drivingList.add(drivinginfo);

        drivinginfo = new Drivinginfolist("10:00 pm", "00:00 pm", "2","Sleep");
        drivingList.add(drivinginfo);

    }
}
