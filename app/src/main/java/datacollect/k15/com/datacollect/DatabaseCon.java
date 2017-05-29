package datacollect.k15.com.datacollect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 3/2/16.
 */

public class DatabaseCon extends SQLiteOpenHelper {

    private  Context context;
    private static  final String DATABASE_NAME ="uploader_db";
    private static final int DATABASE_VERSION =1;


    public DatabaseCon(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql="CREATE TABLE IF NOT EXISTS uploaded_data (id INTEGER PRIMARY KEY AUTOINCREMENT,path TEXT" +
                ",description TEXT,date TEXT,latitude TEXT,longitude TEXT)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="DROP TABLE IF EXISTS uploaded_data";
        db.execSQL(sql);
        onCreate(db);
    }

    public void add_uploaded(DataItem item){

        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("date",item.getDate());
        values.put("description",item.getDescription());
        values.put("latitude",item.getLatitude());
        values.put("longitude",item.getLongitude());
        values.put("path", item.getImage());
        database.insert("uploaded_data", null, values);
    }

    public List<DataItem> get_all_uploaded(){
        List<DataItem> items=new ArrayList<>();
        String sql="select * from uploaded_data";
        SQLiteDatabase database=this.getWritableDatabase();

        Cursor cursor=database.rawQuery(sql,null);

        while(cursor.moveToNext()){
            DataItem item=new DataItem();
            item.setDate(cursor.getString(3));
            item.setImage(cursor.getString(1));
            item.setDescription(cursor.getString(2));
            item.setLatitude(cursor.getString(4));
            item.setLongitude(cursor.getString(5));

            items.add(item);
        }

        return items;

    }
    public void remove_item(DataItem item){
        SQLiteDatabase database=this.getWritableDatabase();
        String sql="DELETE FROM uploaded_data where path='"+item.getImage()+"'";
        database.execSQL(sql);
    }

}
