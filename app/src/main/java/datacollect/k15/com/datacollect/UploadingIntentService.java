package datacollect.k15.com.datacollect;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;

import cz.msebera.android.httpclient.Header;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class UploadingIntentService extends IntentService {

    private Context context=this;
    private String longitude;
    private String latitude;
    private String description;
    private String files;

    public UploadingIntentService() {
        super("UploadingIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

            Toast.makeText(UploadingIntentService.this,"hello world",Toast.LENGTH_LONG).show();
            Bundle toSendBundle=intent.getExtras();
            longitude=toSendBundle.getString("longitude");
            latitude=toSendBundle.getString("latitude");
            description=toSendBundle.getString("description");
            files=toSendBundle.getString("file");

            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();

            File file=new File(files);
            try{

                params.put("file",file);
                params.put("description",description);
                params.put("latitude",latitude);
                params.put("longitude",longitude);
                params.setHttpEntityIsRepeatable(true);
                params.setUseJsonStreamer(false);
                //params.put("area",area);
            }catch (Exception ex){

            }
            client.post("http://192.168.43.234/start.php", params, new AsyncHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(context, "upload failed", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    String response = new String(responseBody);
                    Toast.makeText(context,response, Toast.LENGTH_LONG).show();

                    DatabaseCon database=new DatabaseCon(context);
                    DataItem item =new DataItem();
                    item.setImage(files);
                    item.setLatitude(latitude);
                    item.setLongitude(longitude);
                    item.setDescription(description);

                    database.add_uploaded(item);

                }

                @Override
                public void onStart() {
                    Toast.makeText(context, "starting", Toast.LENGTH_LONG).show();

                }

            });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(context,"upload completed",Toast.LENGTH_LONG).show();
    }
}
