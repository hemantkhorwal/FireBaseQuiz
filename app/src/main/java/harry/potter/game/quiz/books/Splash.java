package harry.potter.game.quiz.books;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.ads.MobileAds;


public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.appId));
        setContentView(R.layout.activity_splash);
     new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {
             Intent game = new Intent(Splash.this,Home.class);
             startActivity(game);
             finish();
         }
     },1000);
  }
}

