package harry.potter.game.quiz.books;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Levels extends AppCompatActivity {
    ListView listView;
    InterstitialAd inter;
    Typeface font;
    DatabaseReference myRef;
    FirebaseDatabase database;
    AdView banner;
    String[] title = {"SQUIB","MUGGLE BORN","HALF BLOOD","PURE BLOOD"};
    String[] subtiltes = {"Easiest","Medium","Hard","True Potterheads only"};
    int MAXLEVEL;
    String PLAY_STORE = "https://play.google.com/store/apps/details?id=harrypottergame.novussoft.pottergameofquiz";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retriveMaxLevel();
        database = FirebaseDatabase.getInstance();
        myRef= database.getReference().child("Show_Offer");
        font = Typeface.createFromAsset(getAssets(),"font/hptext.TTF");
        setContentView(R.layout.activity_levels);
        banner = (AdView)findViewById(R.id.adviewLevel);
        AdRequest adRequest =  new AdRequest.Builder().addKeyword("harry potter")
                                                      .addKeyword("harry potter game")
                                                      .addKeyword("games")
                                                      .addKeyword("albus dumbledore")
                                                      .addKeyword("harry potter books")
                                                      .addKeyword("harry potter and")
                                                      .addKeyword("avira")
                                                      .addKeyword("policy")
                                                      .addKeyword("sarbanes oxley compliance")
                                                      .addKeyword("cmmi")
                                                      .addKeyword("insurance")
                                                      .addKeyword("donate")
                                                      .addKeyword("auto insurance")
                                                      .addKeyword("amazon")
                                                      .addKeyword("car insurance quote")
                                                      .addKeyword("proggressive")
                                                      .addKeyword("proggressive insurance")
                                                      .addKeyword("arizona auto insurance quote")
                                                      .addKeyword("avast antivirus")
                                                      .addKeyword("best antivirus software").build();
        banner.loadAd(adRequest);

        inter = new InterstitialAd(this);
        inter.setAdUnitId(getResources().getString(R.string.inter2));
        inter.loadAd(adRequest);

        listView  = (ListView)findViewById(R.id.levellistid);
        listView.setAdapter(new Adapter(Levels.this,title,subtiltes,MAXLEVEL,0,font));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(position>MAXLEVEL){
                    alert();
                }else {
                    Intent sublevel = new Intent(Levels.this,SubLevels.class);
                    sublevel.putExtra("START_LEVEL",position);
                    startActivity(sublevel);
                    if(inter.isLoaded()){
                        inter.show();
                        finish();
                    }
                    else {
                        finish();
                    }
                }
            }
        });
    }
    public void retriveMaxLevel(){
        SharedPreferences retrive = getApplicationContext().getSharedPreferences("novus",MODE_PRIVATE);
        MAXLEVEL=retrive.getInt("MAXLEVEL",0);
    }
    public void alert(){
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(Levels.this);
        LayoutInflater inflater = Levels.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog,null,true);
        alertdialog.setView(view);
        TextView title = view.findViewById(R.id.errorTitle);
        TextView message = view.findViewById(R.id.errorMessage);
        Button positive = view.findViewById(R.id.positive);
        Button negative = view.findViewById(R.id.negative);
        title.setText("Complete Previous Level");
        message.setText("To play this level you have to complete the previous level");
        positive.setText("Ok");
        negative.setText("Rate Us");
        final AlertDialog alert = alertdialog.create();
        alert.setCancelable(false);
        alert.show();
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rateUs = new Intent(Intent.ACTION_VIEW);
                rateUs.setData(Uri.parse(PLAY_STORE));
                startActivity(rateUs);

            }
        });
    }
}
