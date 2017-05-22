package com.testing.venkatkishore.drivinginfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DrivingMain extends AppCompatActivity {
    Button btn_reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driving_main);
        btn_reg = (Button)findViewById(R.id.reg_submit);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DrivingMain.this,DrivingGraph.class));
            }
        });
    }
}
