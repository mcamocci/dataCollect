package datacollect.k15.com.datacollect;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class UploadedDataActivity extends AppCompatActivity {

    ListView list=null;
    private List<DataItem> uploaded;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list=(ListView)findViewById(R.id.uploaded_list);

        uploaded= new DatabaseCon(UploadedDataActivity.this).get_all_uploaded();
        list.setAdapter(new UploadedDataAdapter(getBaseContext(),R.layout.item_uploaded,uploaded));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                File path=new File(uploaded.get(position).getImage());

                if(FileOperations.isExternalStorageWritable()){

                    File appFolder=new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"" +
                            "datacollect"+File.separator+"Scholarnet images");

                    File file=new File(uploaded.get(position).getImage());

                    File copied=new File(appFolder+File.
                            separator+file.getName());

                    //  BufferedInputStream in=new BufferedInputStream(new InputStream(file));

                    Toast.makeText(getBaseContext(), "am writting file", Toast.LENGTH_SHORT).show();
                    try {
                        InputStream in=new FileInputStream(path.getAbsolutePath());

                        OutputStream out=new FileOutputStream(copied);
                        Toast.makeText(getBaseContext(),"in stream",Toast.LENGTH_LONG).show();
                        byte[] buff=new byte[1024];
                        int len;
                        while((len=in.read(buff))>0){

                            out.write(buff,0,len);
                        }
                        in.close();
                        out.close();
                        Toast.makeText(getBaseContext(),"i have written the file)",Toast.LENGTH_LONG).show();
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse("file://"+Environment
                        .getExternalStorageDirectory())));
                    } catch (IOException e) {
                        Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }

                    if(!appFolder.exists()){
                        appFolder.mkdirs();
                    }else{
                        Toast.makeText(getBaseContext(),"folder exist",Toast.LENGTH_LONG).show();

                    }
                }


                Toast.makeText(getBaseContext(),path.getAbsolutePath(),Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_VIEW);

                Uri uri = Uri.parse("file://" +path.getAbsolutePath());
                intent.setDataAndType(uri, "image/*");
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_uploaded,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }
}
