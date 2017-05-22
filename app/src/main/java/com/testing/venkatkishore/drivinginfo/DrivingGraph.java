package com.testing.venkatkishore.drivinginfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepFormatter;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.testing.venkatkishore.drivinginfo.WebServices.UPLOAD_URL;

public class DrivingGraph extends AppCompatActivity implements View.OnClickListener {
    TextView off, sleep, drive, ondu, totaltime, tv_table, tv_history;
    private XYPlot mySimpleXYPlot;
    Button btn_offduty, btn_driving, btn_sleep, btn_onduty, btn_save;
    List<String> list = new ArrayList<String>();
    List<String> timelist = new ArrayList<String>();
    String currentTime;
    SimpleDateFormat sdf;

    //    Map mMap = new HashMap();
    HashMap<String, String> mMap = new HashMap<String, String>();
    ArrayList<String> offdutyarray, sleeparray, drivingarray, ondutyarray;

    ArrayList<HashMap<String, String>> finalar_details;
    Date now;
    String mPath, pathinfo, path12;
    DatabaseHelper db;
    Image image;
    PdfWriter writer;
    Uri imageuri, imageurii;
    private ProgressDialog pDialog;
    String pathtest, pathtest1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driving_graph);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // initialize our XYPlot reference:
        off = (TextView) findViewById(R.id.off);
        sleep = (TextView) findViewById(R.id.sleep);
        drive = (TextView) findViewById(R.id.drive);
        ondu = (TextView) findViewById(R.id.onDuty);
        totaltime = (TextView) findViewById(R.id.totaltime);
        tv_table = (TextView) findViewById(R.id.tabeldata);
        tv_history = (TextView) findViewById(R.id.history);

        btn_offduty = (Button) findViewById(R.id.butoffduty);
        btn_driving = (Button) findViewById(R.id.butdriving);
        btn_sleep = (Button) findViewById(R.id.butsleep);
        btn_onduty = (Button) findViewById(R.id.butonduty);
        btn_save = (Button) findViewById(R.id.savebtn);


        btn_driving.setOnClickListener(this);
        btn_offduty.setOnClickListener(this);
        btn_sleep.setOnClickListener(this);
        btn_onduty.setOnClickListener(this);
        tv_history.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        finalar_details = new ArrayList<HashMap<String, String>>();

        offdutyarray = new ArrayList<String>();
        sleeparray = new ArrayList<String>();
        drivingarray = new ArrayList<String>();
        ondutyarray = new ArrayList<String>();

        //getting time info

        sdf = new SimpleDateFormat("HH:mm:ss");

        db = new DatabaseHelper(this);
        // initialize our XYPlot reference:
        mySimpleXYPlot = (XYPlot) findViewById(R.id.stepChartExamplePlot);

        // y-vals to plot:
//        Number[] series1Numbers = {6,6,6,6,6,6,6,6,6,6,3,3,4,4,4,4,6,4,4,4,4,6,3,3,2};
        Number[] series1Numbers = {5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 4, 4, 4, 4, 4, 3, 3, 3, 3, 4, 4, 4, 5, 5, 2};
//        Number[] series1Numbers = {5,5,5,5,5,5,5,0,0,0,0,4,4,4,4,0,0,0,0,0,0,0,0,0,0};

        int offV = 3;
        int sleepV = 9;
        int driveV = 8;
        int onV = 4;
        int total = 24;
        off.setText("\n" + "Off Duty: " + offV);
        sleep.setText("\n" + "Sleep: " + sleepV);
        drive.setText("\n" + "Driving: " + driveV);
        ondu.setText("\n" + "On Duty: " + onV);
        totaltime.setText("\n" + "Total: " + total);
        tv_table.setText("Table Data");

        Log.i("listarrayinfo", "" + list);

        tv_table.setOnClickListener(this);

//

        // create our series from our array of nums:
        XYSeries series2 = new SimpleXYSeries(
                Arrays.asList(series1Numbers),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
                "");


        final int screenHeightPx = getWindowManager().getDefaultDisplay().getHeight();
        // setup our line fill paint to be a slightly transparent gradient:,,
        Paint lineFill = new Paint();
        lineFill.setAlpha(200);
        lineFill.setShader(new LinearGradient(0, 0, 0, screenHeightPx, Color.WHITE, Color.BLACK, Shader.TileMode.MIRROR));


        StepFormatter stepFormatter = new StepFormatter(Color.RED, Color.BLUE);
        stepFormatter.setVertexPaint(null); // don't draw individual points
        stepFormatter.getLinePaint().setStrokeWidth(PixelUtils.dpToPix(5));


        stepFormatter.getLinePaint().setAntiAlias(false);
//        stepFormatter.setFillPaint(lineFill);
        mySimpleXYPlot.addSeries(series2, stepFormatter);

        // adjust the domain/range ticks to make more sense; label per line for range and label per 5 ticks domain:
        mySimpleXYPlot.setRangeStep(StepMode.INCREMENT_BY_VAL, 1);
        mySimpleXYPlot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1);
        mySimpleXYPlot.setLinesPerRangeLabel(1);
        mySimpleXYPlot.setLinesPerDomainLabel(5);

        // get rid of decimal points in our domain labels:
        mySimpleXYPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).
                setFormat(new DecimalFormat("0"));


        // create a custom getFormatter to draw our state names as range tick labels:
        mySimpleXYPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo,
                                       FieldPosition pos) {
                Number num = (Number) obj;
                switch (num.intValue()) {
                    case 1:
                        toAppendTo.append("");
                        break;
                    case 2:
                        toAppendTo.append("");
                        break;
                    case 3:
                        toAppendTo.append("OnD");
                        break;
                    case 4:
                        toAppendTo.append("Drive");
                        break;
                    case 5:
                        toAppendTo.append("Sleep");
                        break;
                    case 6:
                        toAppendTo.append("OffD");
                        break;
                    default:
                        toAppendTo.append("");
                        break;
                }
                return toAppendTo;
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.butoffduty:

                list.add(btn_offduty.getText().toString());

                currentTime = sdf.format(new Date());
                timelist.add(currentTime);

                offdutyarray.add(currentTime);

//                Drivinginfolist dfinfo = new Drivinginfolist();
//
//                dfinfo.setStarttime(currentTime);

                mMap.put("Off Duty", currentTime);

                finalar_details.add(mMap);
                Log.i("Hashmapinfo1", "" + mMap);
//                Log.i("listarrayinfo1",""+list+"\n"+timelist);
                break;
            case R.id.butdriving:

                list.add(btn_driving.getText().toString());

                currentTime = sdf.format(new Date());
                timelist.add(currentTime);

                mMap.put("Driving", currentTime);

                drivingarray.add(currentTime);

//                Log.i("listarrayinfo2",""+list+"\n"+timelist);
                Log.i("Hashmapinfo2", "" + mMap);
                break;

            case R.id.tabeldata:

                finalar_details.add(mMap);
//                Log.i("listarrayinfotav",""+list+"\n"+timelist);
                Log.i("Hashmapinfo", "" + finalar_details);

                Intent innew = new Intent(DrivingGraph.this, StepGraphTableinfo.class);
                innew.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                innew.putExtra("h", "" + finalar_details);
                startActivity(innew);

                break;
            case R.id.butsleep:

                currentTime = sdf.format(new Date());
                mMap.put("Sleep", currentTime);

                sleeparray.add(currentTime);

                Log.i("Hashmapinfo22", "" + mMap);

                break;

            case R.id.butonduty:

                currentTime = sdf.format(new Date());
                mMap.put("OnDuty", currentTime);

                ondutyarray.add(currentTime);
                Log.i("Hashmapinfo44", "" + mMap);


                break;
            case R.id.savebtn:
                currentTime = sdf.format(new Date());
                takeScreenshot();


                break;

            case R.id.history:

                Intent ihistory = new Intent(DrivingGraph.this, DrivingHistory.class);
                ihistory.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ihistory);

            default:
                break;
        }
    }

    private void takeScreenshot() {
        now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";


            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            db.Add_preslocInboxDetails(new Drivingimagelistdetails(currentTime, mPath));

            ConvertPdftoImg();


//            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    private void ConvertPdftoImg() {

        new creatingPDF().execute();

//        UploadInfoServer();
    }


    public class creatingPDF extends AsyncTask<String, String, String> {

        // Progress dialog
        MaterialDialog.Builder builder = new MaterialDialog.Builder(DrivingGraph.this)
                .title("Please Wait")
                .content("Creating PDF. This may take a while.")
                .cancelable(false)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DrivingPDFfiles/");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }


            pathinfo = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DrivingPDFfiles/";

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DrivingPDFfiles/");

            path12 = pathinfo + now + ".pdf";

            Log.v("stage 1", "store the pdf in sd card");
            Document document = new Document(PageSize.A4, 38, 38, 50, 38);

            Log.v("stage 2", "Document Created");

            Rectangle documentRect = document.getPageSize();


            try {
                writer = PdfWriter.getInstance(document, new FileOutputStream(path12));

                Log.v("Stage 3", "Pdf writer");

                document.open();

                Log.v("Stage 4", "Document opened");

//                for (int i = 0; i < imagesUri.size(); i++) {


                Bitmap bmp = BitmapFactory.decodeFile(mPath);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 70, stream);


                image = Image.getInstance(mPath);


                if (bmp.getWidth() > documentRect.getWidth() || bmp.getHeight() > documentRect.getHeight()) {
                    //bitmap is larger than page,so set bitmap's size similar to the whole page
                    image.scaleAbsolute(documentRect.getWidth(), documentRect.getHeight());
                } else {
                    //bitmap is smaller than page, so add bitmap simply.[note: if you want to fill page by stretching image, you may set size similar to page as above]
                    image.scaleAbsolute(bmp.getWidth(), bmp.getHeight());
                }


                Log.v("Stage 6", "Image path adding");

                image.setAbsolutePosition((documentRect.getWidth() - image.getScaledWidth()) / 2, (documentRect.getHeight() - image.getScaledHeight()) / 2);
                Log.v("Stage 7", "Image Alignments");

                image.setBorder(Image.BOX);

                image.setBorderWidth(15);

                document.add(image);

                document.newPage();
//                }

                Log.v("Stage 8", "Image adding");

                document.close();

                Log.v("Stage 7", "Document Closed" + path12);
            } catch (Exception e) {
                e.printStackTrace();
            }

            document.close();

//            imagesUri.clear();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            openPdf.setVisibility(View.VISIBLE);
//            textView.append("done");
            dialog.dismiss();
//            drivername,driverlicense,vehicleid ,offduty,onduty,sleep,driving, pdf

            imageuri = Uri.fromFile(new File(path12));
            pathtest = FilePath.getPath(DrivingGraph.this, imageuri);

            imageurii = Uri.fromFile(new File(path12));
            pathtest1 = FilePath.getPath(DrivingGraph.this, imageurii);

//            UploadInfoServer1("drivername","driverlicense","vehicleid","offduty","onduty","sleep","driving",pathtest,pathtest1);
            new UploadInfoServer().execute();
//            new creatingPDF().execute();
//            morphToSuccess(createPdf);
        }


    }

   /* private void UploadInfoServer1(final String drivername, final String
            driverlicense, final String vehicleid, final String offduty,
                                   final String onduty, final String sleep,
                                   final String driving, final String imageuri, final String imageurii) {

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                UPLOAD_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("dkd", "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
//                        String uid = jObj.getString("uid");
//
//                        JSONObject user = jObj.getJSONObject("user");
//                        String name = user.getString("name");
//                        String email = user.getString("email");
//                        String mobile = user.getString("mobile");
//                        String created_at = user
//                                .getString("created_date");
//
//                        // Inserting row in users table
//                        db.addUser(name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(),
                                "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
//                        onSignupSuccess();
                        // Launch login activity
                        Intent intent = new Intent(
                                DrivingGraph.this,
                                DrivingHistory.class);
                        startActivity(intent);
                        finish();
                    } else {
//                        _signupButton.setEnabled(true);
                        // Error occurred in registration. Get the error
                        // messageerror_msg

                        String errorMsg = jObj.getString("error_msg");
                        Log.i("errorinfo",errorMsg);
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("info", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
//                drivername,driverlicense,vehicleid ,offduty,onduty,sleep,driving, pdf
                Map<String, String> params = new HashMap<String, String>();
                params.put("drivername", drivername);
                params.put("driverlicense", driverlicense);
                params.put("vehicleid", vehicleid);
                params.put("offduty",offduty);
                params.put("onduty", onduty);
                params.put("sleep", sleep);
                params.put("driving", driving);
                params.put("pdf", pathtest);
                params.put("tableimage", pathtest1);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();


    }



}*/


    private class UploadInfoServer extends AsyncTask<String, String, String> {

        // Progress dialog
        MaterialDialog.Builder builder = new MaterialDialog.Builder(DrivingGraph.this)
                .title("Please Wait")
                .content("Loading to server.")
                .cancelable(false)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            Uri.fromFile(new File("/sdcard/sample.jpg")
            imageuri = Uri.fromFile(new File(path12));
            String path = FilePath.getPath(DrivingGraph.this, imageuri);
            Log.i("imageinformation", imageuri + "\n" + path);

            if (path == null) {

//                Toast.makeText(DrivingGraph.this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
            } else {
                //Uploading code
                try {
                    String uploadId = UUID.randomUUID().toString();

                    Log.i("testein",uploadId);
//                drivername,driverlicense,vehicleid ,offduty,onduty,sleep,driving, pdf
                    //Creating a multi part request
                    new MultipartUploadRequest(DrivingGraph.this, uploadId, UPLOAD_URL)
                            .addFileToUpload(path, "pdf") //Adding file
                            .addFileToUpload(path, "tableimage") //Adding file
                            .addParameter("drivername", "Raju") //Adding text parameter to the request
                            .addArrayParameter("driverlicense", "LJFLOHFOH")
                            .addArrayParameter("vehicleid", "TS059162")
                            .addArrayParameter("offduty", "9.00")
                            .addArrayParameter("onduty", "10.00")
                            .addArrayParameter("sleep", "9.00")
                            .addArrayParameter("driving", "10.00")
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload(); //Starting the upload

                } catch (Exception exc) {
                    Toast.makeText(DrivingGraph.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Log.i("response",s);
//            openPdf.setVisibility(View.VISIBLE);
//            textView.append("done");
            Toast.makeText(DrivingGraph.this, "sucessfully uploaded to server" + "\n" + s, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }
}



   /* private void UploadInfoServer() {
//        String name = editText.getText().toString().trim();

        //getting the actual path of the image
        String path = FilePath.getPath(this, imageuri);

        if (path == null) {

            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

//                drivername,driverlicense,vehicleid ,offduty,onduty,sleep,driving, pdf
                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addFileToUpload(path, "tableimage") //Adding file
                        .addParameter("drivername", "Raju") //Adding text parameter to the request
                       .addArrayParameter("driverlicense","LJFLOHFOH")
                        .addArrayParameter("vehicleid","TS059162")
                        .addArrayParameter("offduty","9.00")
                        .addArrayParameter("onduty","10.00")
                        .addArrayParameter("sleep","9.00")
                        .addArrayParameter("driving","10.00")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }*/


//}
