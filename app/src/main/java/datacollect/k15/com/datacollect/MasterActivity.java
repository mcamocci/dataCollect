package datacollect.k15.com.datacollect;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MasterActivity extends AppCompatActivity {

    private ListView pickedItemList;
    private TextView empty;
    private int RESULT_LOAD_IMAGE = 2;
    private List<DataItem> list;
    private String longitude;
    private String picturePath;
    private String latitude;
    private String description;
    private String date;
    private Bundle toSendBundle;
    private Bundle sendBundle;
    private boolean uploading=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toSendBundle = getIntent().getExtras();
        longitude = toSendBundle.getString("longitude");
        latitude = toSendBundle.getString("latitude");
        description = toSendBundle.getString("description");


        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = new ArrayList<>();
        pickedItemList=(ListView)findViewById(R.id.picked_items);
        empty=(TextView)findViewById(R.id.empty_list);
        pickedItemList.setEmptyView(empty);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(ConnectionAvaillable.isInternetConnected(getBaseContext())){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                }else{
                    Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            DataItem dataItem = new DataItem();
            dataItem.setLatitude(latitude);
            dataItem.setLongitude(longitude);
            dataItem.setDate(date);
            dataItem.setImage(picturePath);
            dataItem.setDescription(description);

            list.add(dataItem);
            UpdateData(list);
            // String picturePath contains the path of selected Image
        }
    }
    public void UpdateData(List<DataItem> list) {
        pickedItemList.setAdapter(new displayItemAdapter(getBaseContext(), R.layout.item_upload, list));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_upload, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final View view=item.getActionView();
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.upload_menu) {

            if(FileOperations.isExternalStorageWritable()){

                File appFolder=new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"" +
                        "datacollect"+File.separator+"images");

                if(!appFolder.exists()){
                    appFolder.mkdirs();
                }else{
                    Toast.makeText(getBaseContext(),"folder exist",Toast.LENGTH_LONG).show();
                }
            }

            if(list.isEmpty()){

            }else{
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();

                    File file=new File(list.get(0).getImage());
                    File file2=new File(list.get(1).getImage());

                    try{

                        params.put("file[0]",file);
                        params.put("description",description);
                        params.put("latitude",latitude);
                        params.put("longitude",longitude);
                        params.put("date", date);
                        params.setHttpEntityIsRepeatable(true);
                        params.setUseJsonStreamer(false);
                        params.put("file[1]",file2);
                    }catch (FileNotFoundException ex){

                    }
                //client.post("http://192.168.43.234/start.php", params, new AsyncHttpResponseHandler()
                    client.post(CommonInformation.UPLOAD_SPRING, params, new AsyncHttpResponseHandler() {

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(getBaseContext(), new String(responseBody), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            String response = new String(responseBody);
                            Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();

                            DatabaseCon database = new DatabaseCon(getBaseContext());
                            DataItem item = new DataItem();
                            Calendar c = Calendar.getInstance();
                            int day = c.get(Calendar.DAY_OF_MONTH);
                            int month = c.get(Calendar.MONTH);
                            int year = c.get(Calendar.YEAR);

                            String date = Integer.toString(day) + "/" +
                                    "" + Integer.toString(month + 1) + "/" + Integer.toString(year);
                            item.setImage(picturePath);
                            item.setLatitude(latitude);
                            item.setLongitude(longitude);
                            item.setDate(date);
                            item.setDescription(description);

                            database.add_uploaded(item);

                        }

                        @Override
                        public void onStart() {
                            Toast.makeText(getBaseContext(), "starting", Toast.LENGTH_LONG).show();

                        }

                    });

            }

        }else if (id == R.id.uploaded_menu){

            Intent intent=new Intent(getBaseContext(),UploadedDataActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            if(uploading){
                Toast.makeText(getBaseContext(),"please wait",Toast.LENGTH_LONG);
            }
        }

        if(uploading){
            Toast.makeText(getBaseContext(),"please wait",Toast.LENGTH_LONG);
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }


    }
}

