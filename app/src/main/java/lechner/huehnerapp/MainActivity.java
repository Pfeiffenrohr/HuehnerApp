package lechner.huehnerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.Socket;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import android.os.StrictMode;

public class MainActivity extends AppCompatActivity {
    //String ip = "192.168.2.108";
    String ip = "richardlechner.spdns.de";
    Socket socket = null;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    TextView statuszeile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button oeffnen;
        Button schliessen;
        Button auto;
        Button info;
        Button exit;

        
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        statuszeile = (TextView) findViewById(R.id.TextStatus);
        try {
            Log.d("Huehner", "Try to connect to server ...");
            socket = new Socket(ip, 8976);
            statuszeile.setText("Connectet!");
            oos = new ObjectOutputStream(socket.getOutputStream());
             ois = new ObjectInputStream(socket.getInputStream());
            oeffnen = (Button) findViewById(R.id.buttonOeffnen);
            oeffnen.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    try {

                       // oos.writeObject("debug_ein");
                        oos.writeObject("set open");
                        Log.d("huehner", (String)ois.readObject());

                    } catch (Exception ex )
                      {  ex.printStackTrace();
                        Log.d("Huehner", "Exception");}
                }
            });

            schliessen = (Button) findViewById(R.id.buttonSchliessen);
            schliessen.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    try {

                        //oos.writeObject("debug_aus");
                        oos.writeObject("set close");
                        Log.d("huehner", (String)ois.readObject());

                    } catch (Exception ex )
                    {  ex.printStackTrace();
                        Log.d("Huehner", "Exception");}
                }
            });

            auto = (Button) findViewById(R.id.buttonAuto);
            auto.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    try {

                        oos.writeObject("set auto");
                        Log.d("huehner", (String)ois.readObject());

                    } catch (Exception ex )
                    {  ex.printStackTrace();
                        Log.d("Huehner", "Exception");}
                }
            });

            info = (Button) findViewById(R.id.buttonInfo);
            info.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    try {
                       // Log.d("Huehner", "InfoButton pressed");
                        String ret;
                        String feld [];
                        oos.writeObject("i");
                        ret= (String)ois.readObject();
                        feld= ret.split("\n");
                        Log.d("Huehner", feld [6]);
                        statuszeile.setText(feld [6]);
                    } catch (Exception ex )
                    {  ex.printStackTrace();
                        Log.d("Huehner", "Exception");}
                }
            });

            exit = (Button) findViewById(R.id.buttonExit);
            exit.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    try {
                        // Log.d("Huehner", "InfoButton pressed");

                        oos.close();
                        ois.close();
                        socket.close();
                       System.exit(0);
                    } catch (Exception ex )
                    {  ex.printStackTrace();
                        Log.d("Huehner", "Exception");}
                }
            });

        } catch (Exception io) {
            System.err.println(io.getStackTrace());
            io.printStackTrace();
            Log.d("Huehner", "connectError");
            statuszeile.setText("Connect failed!");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            oos.close();
            ois.close();
            socket.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
