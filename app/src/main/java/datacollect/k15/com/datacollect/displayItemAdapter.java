package datacollect.k15.com.datacollect;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by root on 9/28/15.
 */
public class displayItemAdapter extends ArrayAdapter<DataItem>{

    private Context context;
    private int resources;
    private List<DataItem> list;

    public displayItemAdapter(Context context, int resources, List<DataItem> objects){

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

        if(item.getImage()!=null)
        {
            File file=new File(item.getImage());

           // Glide.with(context).load(file.getAbsolutePath()).into(image);
           //image.setImageBitmap(bitmap);
        }else{
            Toast.makeText(context,"null reference ",Toast.LENGTH_LONG).show();
        }
        url.setText("Name: "+item.getImage().substring(item.getImage().lastIndexOf(File.separator)+1,item.getImage().length()));
        gps.setText("Latitude: "+item.getLatitude());
        gps2.setText("Longitude: " + item.getLongitude());


        return view;
    }
}
