package datacollect.k15.com.datacollect;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by root on 2/11/16.
 */
public class DataItem {

    private int id;
    private ArrayList<String> gps;


    private String latitude;
    private String longitude;
    private String description;
    private String date;
    private String image;


    public DataItem(){}
    public DataItem(int id,ArrayList<String> gps,String description,String date,String image){
        this.id=id;
        this.image=image;
        this.date=date;
        this.description=description;
        this.gps=gps;
    }
    public DataItem(ArrayList<String> gps,String description,String date,String image){
        this.image=image;
        this.date=date;
        this.description=description;
        this.gps=gps;
    }
    private void setId(int id){
        this.id=id;
    }
    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    private void setCoords(String latitude,String longitude){
        gps=new ArrayList<>();
        gps.add(latitude);
        gps.add(longitude);
    }

    public void setDate(String date){
        this.date=date;
    }
    public void setImage(String image){
        this.image=image;
    }
    public void setDescription(String description){
        this.description=description;
    }

    public int getId(){
        return this.id;
    }
    public String getDescription(){
        return this.description;
    }
    public ArrayList<String> getCoords(){
        return this.gps;
    }
    public String getImage(){
        return this.image;
    }

    public String getDate(){
        return this.date;
    }
}
