package datacollect.k15.com.datacollect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class SplashActivity extends AppCompatActivity {

    private TextView demo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        Thread splash=new Thread(){

            public void run(){

                try{
                    Thread.sleep(2000);
                    Intent intent=new Intent(SplashActivity.this,StartActivity.class);
                    startActivity(intent);
                    finish();
                }catch(InterruptedException ex){
                    ex.getMessage();
                }

            }

        };


        splash.start();

    }


}
