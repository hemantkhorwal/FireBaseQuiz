package harry.potter.game.quiz.books;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


public class SubLevels extends AppCompatActivity {
    ListView listView;
    AdView banner;
    String[] title = {"Level 1","Level 2","Level 3","Level 4","Level 5"};
    String[] subtitles = {"","","","",""};
    Typeface font;
    String[] Levelname = {"Squib","Muggle Born","Half Blood","Pure Blood"};
    String PLAY_STORE = "https://play.google.com/store/apps/details?id=harrypottergame.novussoft.pottergameofquiz";
    int MAXLEVEL,MAXSUBLEVEL,STARTLEVEL;
    TextView LevelName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sublevels);
        retriveMaxSubLevel();
        Bundle extra = getIntent().getExtras();
        if(extra!=null){
            STARTLEVEL = (int) extra.get("START_LEVEL");
        }
        banner = (AdView)findViewById(R.id.adviewSubLevel);
        AdRequest adRequest = new AdRequest.Builder()
                .addKeyword("free")
                .addKeyword("car")
                .addKeyword("new")
                .addKeyword("harry potter")
                .addKeyword("harry potter game")
                .addKeyword("games")
                .addKeyword("always")
                .addKeyword("harry potter books")
                .addKeyword("harry potter and")
                .addKeyword("lawyer")
                .addKeyword("attorneys")
                .addKeyword("personal injury")
                .addKeyword("wrongful death lawyer")
                .addKeyword("imaginejustice.com")
                .addKeyword("Business VOIP Solutions")
                .addKeyword("voip services")
                .addKeyword("top insurance compines")
                .addKeyword("mortage")
                .addKeyword("remortage lenders")
                .addKeyword("mesothelioma law frim")
                .addKeyword("law frim")
                .addKeyword("fix")
                .addKeyword("home loan").build();
        banner.loadAd(adRequest);
        font = Typeface.createFromAsset(getAssets(),"font/hptext.TTF");
        LevelName = (TextView) findViewById(R.id.LevelName);
        LevelName.setText(Levelname[STARTLEVEL]);
        LevelName.setTypeface(font);
        listView = (ListView)findViewById(R.id.sublevleslistid);
        listView.setAdapter(new Adapter(SubLevels.this,title,subtitles,STARTLEVEL,1,font));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(connection()){
                if(MAXLEVEL>STARTLEVEL){
                    Intent quiz = new Intent(SubLevels.this,Quiz.class);
                    quiz.putExtra("SET_LEVEL",STARTLEVEL);
                    quiz.putExtra("SET_QUIZ",position);
                    finish();
                    startActivity(quiz);
                }else if(MAXLEVEL==STARTLEVEL){
                    if(position>MAXSUBLEVEL){
                        alert();
                    }else {
                        Intent quiz = new Intent(SubLevels.this,Quiz.class);
                        quiz.putExtra("SET_LEVEL",STARTLEVEL);
                        quiz.putExtra("SET_QUIZ",position);
                        finish();
                        startActivity(quiz);
                    }
                }
                }else {
                    Toast.makeText(getApplicationContext(),"Please Connect To Internet",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void retriveMaxSubLevel(){
        SharedPreferences retrive = getApplicationContext().getSharedPreferences("novus",MODE_PRIVATE);
        MAXLEVEL=retrive.getInt("MAXLEVEL",0);
        MAXSUBLEVEL=retrive.getInt("MAXSUBLEVEL",0);
    }
    public void alert(){
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(SubLevels.this);
        LayoutInflater inflater = SubLevels.this.getLayoutInflater();
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
    @Override
    public void onBackPressed(){
        Intent level = new Intent(SubLevels.this,Levels.class);
        finish();
        startActivity(level);
    }
    public boolean connection(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

    }
}
