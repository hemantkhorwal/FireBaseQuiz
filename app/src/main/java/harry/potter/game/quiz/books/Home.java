package harry.potter.game.quiz.books;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Home extends AppCompatActivity{
    Button start,option,exit,fbpage,refer;
    Typeface font;
    private FirebaseAnalytics mFirebaseAnalytics;
    DatabaseReference db;
    String PLAY_STORE = "https://play.google.com/store/apps/details?id=harry.potter.game.quiz.books";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        SharedPreferences retrive = getApplicationContext().getSharedPreferences("novus",MODE_PRIVATE);
        int opend=retrive.getInt("opened",0);
        if(opend==0)
        goToOffer();
        db = FirebaseDatabase.getInstance().getReference().child("Questions");
        findView();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connection()){
                Intent levels = new Intent(Home.this,Levels.class);
                    Runtime.getRuntime().gc();
                startActivity(levels);
                }else
                    alert("net");
            }
        });

        fbpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,OfferActive.class);
                startActivity(intent);
               /* try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/830754297028126"));
                    startActivity(intent);
                } catch(Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FB_PAGE)));
                }*/
            }
        });
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              alert("reset");
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent rateUs = new Intent(Home.this,Result.class);
                //startActivity(rateUs);
            }
        });

        refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rateUs = new Intent(Home.this,ReferAndGet.class);
                startActivity(rateUs);
            }
        });
    }
    public void findView(){
        font = Typeface.createFromAsset(getAssets(),"font/hptext.TTF");
        start = findViewById(R.id.start);
        fbpage= findViewById(R.id.fbpage);
        option =findViewById(R.id.options);
        exit = findViewById(R.id.exit);
        refer = findViewById(R.id.refer);
        refer.setTypeface(font);
        start.setTypeface(font);
        fbpage.setTypeface(font);
        option.setTypeface(font);
        exit.setTypeface(font);
    }
    public void alert(final String code){
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(Home.this);
        LayoutInflater inflater = Home.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog,null,true);
        alertdialog.setView(view);
        TextView title = view.findViewById(R.id.errorTitle);
        TextView message = view.findViewById(R.id.errorMessage);
        Button positive = view.findViewById(R.id.positive);
        Button negative = view.findViewById(R.id.negative);
        if(code.equals("reset")){
        title.setText(R.string.Reset);
        message.setText(R.string.ResetMSG);
        positive.setText(R.string.ResetButton);
        negative.setText(R.string.Back);
        }else if(code.equals("net")){
            title.setText(R.string.InternetTitle);
            message.setText(R.string.InternetMSg);
            positive.setText("");
            positive.setEnabled(false);
            negative.setText(R.string.Back);
        }
        final AlertDialog alert = alertdialog.create();
        alert.setCancelable(false);
        alert.show();
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(code.equals("reset")) {
                    alert.dismiss();
                    SharedPreferences.Editor saveGameState = getApplicationContext().getSharedPreferences("novus", MODE_PRIVATE).edit();
                    saveGameState.putInt("MAXLEVEL",3);
                    saveGameState.putInt("MAXSUBLEVEL",4);

                    if(saveGameState.commit())
                        Toast.makeText(Home.this, "Reset successfully done", Toast.LENGTH_SHORT).show();
                    else
                    {
                        saveGameState.apply();
                    Toast.makeText(Home.this, "Reset successfully done", Toast.LENGTH_SHORT).show();
                }
                }
            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();

            }
        });
    }

    public void abtus(){
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(Home.this);
        LayoutInflater inflater = Home.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog,null,true);
        alertdialog.setView(view);
        TextView title = view.findViewById(R.id.errorTitle);
        TextView message = view.findViewById(R.id.errorMessage);
        Button positive = view.findViewById(R.id.positive);
        Button negative = view.findViewById(R.id.negative);
        title.setText(R.string.Novus);
        message.setText(R.string.NovusMsg);
        positive.setText(R.string.ChkUpdate);
        negative.setText(R.string.Back);
        final AlertDialog alert = alertdialog.create();
        alert.setCancelable(false);
        alert.show();
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                Intent rateUs = new Intent(Intent.ACTION_VIEW);
                rateUs.setData(Uri.parse(PLAY_STORE));
                startActivity(rateUs);
            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();

            }
        });
    }

    public boolean connection(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            assert cm != null;
            return cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public void goToOffer(){
        SharedPreferences.Editor saveGameState = getApplicationContext().getSharedPreferences("novus", MODE_PRIVATE).edit();
        saveGameState.putInt("opened",1);
        saveGameState.commit();
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(Home.this);
        LayoutInflater inflater = Home.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog,null,true);
        alertdialog.setView(view);
        final TextView title = view.findViewById(R.id.errorTitle);
        TextView message = view.findViewById(R.id.errorMessage);
        Button positive = view.findViewById(R.id.positive);
        Button negative = view.findViewById(R.id.negative);
        title.setText("Win Books");
        message.setText("Take Part in Competition");
        positive.setText("Go");
        negative.setText(R.string.Back);
        final AlertDialog alert = alertdialog.create();
        alert.setCancelable(false);
        alert.show();
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                Intent rateUs = new Intent(Home.this,OfferActive.class);
                startActivity(rateUs);
            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();

            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
    }
}
