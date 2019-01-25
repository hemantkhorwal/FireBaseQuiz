package harry.potter.game.quiz.books;

import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReferAndGet extends AppCompatActivity {
    Button whatsapp,fbshare,star,more;
    DatabaseReference db;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String UID;
    int points;
    String PLAY_STORE = "https://play.google.com/store/apps/details?id=harry.potter.game.quiz.books";
    String Novus = "https://play.google.com/store/apps/dev?id=4845224765523158602";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_get);
        whatsapp =(Button) findViewById(R.id.refer);
        fbshare = (Button) findViewById(R.id.fbshare);
        star = (Button)findViewById(R.id.star);
        more = (Button) findViewById(R.id.more);
         mAuth = FirebaseAuth.getInstance();
         mUser = mAuth.getCurrentUser();
         if(mUser==null){
             Intent newUser = new Intent(ReferAndGet.this,OfferActive.class);
             finish();
             startActivity(newUser);
         }
         else {
             UID = mUser.getUid();
         db = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("refer");
             db.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     points = Integer.valueOf(dataSnapshot.getValue().toString());
                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });

         }

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent what = new Intent(Intent.ACTION_SEND);
             what.setType("text/plain");
             what.putExtra(Intent.EXTRA_TEXT,"Win Harry Potter Books just by completing simple harry potter quiz.Download this app and get a chance to win ".concat(PLAY_STORE));
             what.setPackage("com.whatsapp");
              try{
                  startActivity(what);
                  db.setValue(points+100);
              }catch (Exception e) {
                  startActivity(Intent.createChooser(what, "Share"));
              }
            }
        });
        fbshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent what = new Intent(Intent.ACTION_SEND);
                what.setType("text/plain");
                what.putExtra(Intent.EXTRA_TEXT,"http://freetechnohgeek.blogspot.in/2018/01/free-harry-potter-books.html");
                startActivity(Intent.createChooser(what, "Share"));
                try{
                    db.setValue(points+100);
                }catch (Exception e) {

                }
            }
        });
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rateUs = new Intent(Intent.ACTION_VIEW);
                rateUs.setData(Uri.parse(PLAY_STORE));
                startActivity(rateUs);
                db.setValue(points+10);

            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rateUs = new Intent(Intent.ACTION_VIEW);
                rateUs.setData(Uri.parse(Novus));
                startActivity(rateUs);

            }
        });
    }
}
