package com.indo.app.mygcmnetworkmanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSetScheduler, btnCancelSheduler;
    private SchedulerTask mSchedulerTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSetScheduler = (Button) findViewById(R.id.btnstart);
        btnCancelSheduler = (Button)findViewById(R.id.btncancel);
        btnCancelSheduler.setOnClickListener(this);
        btnSetScheduler.setOnClickListener(this);
        mSchedulerTask = new SchedulerTask(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnstart){
         mSchedulerTask.createPeriodicTask();
            Toast.makeText(this, "Periodic Task Created", Toast.LENGTH_LONG).show();
        }
        if (v.getId() == R.id.btncancel){
            mSchedulerTask.cancelPeriodicTask();
            Toast.makeText(this, "Periodic Task Cancelled", Toast.LENGTH_LONG).show();
        }

    }
}
