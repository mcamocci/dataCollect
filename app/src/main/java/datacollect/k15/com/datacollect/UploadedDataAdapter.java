package datacollect.k15.com.datacollect;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by root on 9/28/15.
 */
public class UploadedDataAdapter extends ArrayAdapter<DataItem>{

    private Context context;
    private int resources;
    private List<DataItem> list;
    File file;
    public UploadedDataAdapter(Context context, int resources, List<DataItem> objects){

        super(context,resources,objects);
        this.context=context;
        this.resources=resources;
        this.list=objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataItem item=list.get(position);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view=(View)inflater.inflate(resources, null);


        TextView gps=(TextView)view.findViewById(R.id.gps);
        TextView gps2=(TextView)view.findViewById(R.id.gps2);
        TextView url=(TextView)view.findViewById(R.id.url_data);
        ImageView image=(ImageView)view.findViewById(R.id.image_data);
        if(position==2){


            gps=(TextView)view.findViewById(R.id.gps);
            gps2=(TextView)view.findViewById(R.id.gps2);
            url=(TextView)view.findViewById(R.id.url_data);

            ((ViewManager)gps.getParent()).removeView(gps);
            ((ViewManager)gps2.getParent()).removeView(gps2);
            ((ViewManager)url.getParent()).removeView(url);
        }else{

            if(item.getImage()!=null)
            {
                file=new File(item.getImage());

                if(FileOperations.isExternalStorageWritable()){

                    File appFolder=new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"" +
                            "datacollect"+File.separator+"Scholarnet images");

                    if(!appFolder.exists()){
                        appFolder.mkdirs();
                    }else{
                      //  Toast.makeText(context,"folder exist",Toast.LENGTH_LONG).show();

                    }
                }

                // First decode with inJustDecodeBounds=true to check dimensions
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getAbsolutePath(),options);
                // Calculate inSampleSize
                options.inSampleSize = ImageEfficient.calculateInSampleSize(options, 100, 100);

                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;
                Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath(),options);
                image.setImageBitmap(bitmap);

            }else{
                Toast.makeText(context,"null reference ",Toast.LENGTH_LONG).show();
            }
            url.setText("Name: "+item.getImage().substring(item.getImage().lastIndexOf(File.separator)+1,item.getImage().length()));

            String available="YES";

            File file=new File(item.getImage());
            if(!file.exists()){
                available="NO";
            }

            gps.setText("Availlable on phone : "+available);
            gps2.setText("Upload date : " + item.getDate());

        }



        return view;
    }
}
