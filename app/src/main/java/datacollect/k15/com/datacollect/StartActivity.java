package datacollect.k15.com.datacollect;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Calendar;

public class StartActivity extends AppCompatActivity {


    private String latitude=null;
    private String longitude=null;
    private String description=null;

    private TextView dateView;
    private TextView online;
    private TextView latitudeView;
    private TextView longitudeView;
    private EditText descriptionView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //codes for getting gps data... goes here....
        LocationManager locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month=c.get(Calendar.MONTH);
                int year=c.get(Calendar.YEAR);

                String date=Integer.toString(day)+"/" +
                        ""+Integer.toString(month+1)+"/"+Integer.toString(year);
                dateView.setText("Date: "+date);
                latitude=Double.toString(location.getLatitude());
                latitudeView.setText("latitude: "+latitude);
                longitude=Double.toString(location.getLongitude());
                longitudeView.setText("longitude: "+longitude);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck!=0){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }

        dateView=(TextView)findViewById(R.id.date_view);
        longitudeView=(TextView)findViewById(R.id.longitude_view);
        latitudeView=(TextView)findViewById(R.id.latitude_view);
        descriptionView=(EditText)findViewById(R.id.description_view);

        LinearLayout pick_image = (LinearLayout) findViewById(R.id.image_pick);
        pick_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ConnectionAvaillable.isInternetConnected(getBaseContext())){

                    description=descriptionView.getText().toString();
                    Bundle bundle=new Bundle();
                    bundle.putString("latitude",latitude);
                    bundle.putString("longitude",longitude);
                    bundle.putString("description", description);


                    Intent intent=new Intent(getBaseContext(),MasterActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{


                    Snackbar.make(view, "You have no internet connection", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
        }
        return true;
    }
}
