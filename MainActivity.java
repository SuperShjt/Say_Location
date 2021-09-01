package com.example.voicelocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SpeechRecognizer Sp;
    TextView t1, t2, t3;
    TextToSpeech Tp;
    LocationManager l;
    String Mylocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1 = findViewById(R.id.Lat);
        t2 = findViewById(R.id.Longtu);
        t3 = findViewById(R.id.Voicemsg);
        l = (LocationManager) getSystemService(LOCATION_SERVICE);
        Sp = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        Sp.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> x = (ArrayList<String>) bundle.get(SpeechRecognizer.RESULTS_RECOGNITION);
                String result = x.get(0);
                t3.setText(result);
                if (result.equals("location")) {
                    Reader();
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        l.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double lat = location.getLatitude();
                double longx = location.getLongitude();
                Geocoder gc = new Geocoder(getApplicationContext());
                try {
                    List<Address> add=gc.getFromLocation(lat,longx,1);
                    String sadd=add.get(0).getAddressLine(0);
                    Mylocation=sadd;
                    Toast.makeText(getApplicationContext(),sadd,Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(lat!=0&&longx!=0){
                    t1.setText("Latiude: "+ lat);
                    t2.setText("Langtiude: "+ longx);
                }
            }
        });
    }
    //------Start-----------------
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            Intent i= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            Sp.startListening(i);
        }
        return super.dispatchKeyEvent(event);
    }
    public void Reader(){
        Tp=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                Tp.speak(Mylocation,TextToSpeech.QUEUE_ADD,null);
            }
        });
    }

}
