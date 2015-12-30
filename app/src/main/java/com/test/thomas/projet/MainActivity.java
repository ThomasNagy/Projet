package com.test.thomas.projet;
import android.content.BroadcastReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static Button button1;
    private static final String TAG = "FragmentActivity";

    public  void OnclickButtonListener(){

        button1 = (Button)findViewById(R.id.button);
        button1.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                public void onClick(View v){
                        Intent intent = new Intent(".Project");
                        startActivity(intent);
                    }
                }
        );
    }

    private void handleActionBiers(){

        Log.d(TAG, "Thread service name:" + Thread.currentThread().getName());
        URL url=null;
        try{
            url = new URL("http://binouze.fabrigli.fr/bieres.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(),"bieres.json"));
                Log.d(TAG,"bieres json downloaded!");
                }
            } catch (MalformedURLException e) {

            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }


    }

    private void copyInputStreamToFile(InputStream in, File file){
        try{
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len =in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OnclickButtonListener();
    }

    public void Notif(View view){

        Intent intent = new Intent(this, DisplayNotification.class);
        PendingIntent pending = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notifications = new Notification.Builder(this)
                .setContentTitle("New notification like message from")
                .setContentText("Hii").setSmallIcon(R.drawable.notif)
                .setContentIntent(pending)
                .addAction(R.drawable.notif, "Reply", pending)
                .addAction(R.drawable.notif, "cancel", pending)
                .addAction(R.drawable.notif, "setings", pending).build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifications.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(0, notifications);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id==R.id.navigate){
            startActivity(new Intent(this, Project.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
