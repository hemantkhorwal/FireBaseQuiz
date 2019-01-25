package harry.potter.game.quiz.books;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OfferActive extends AppCompatActivity {
    boolean offer = true;
    TextView blog,printEmail,printPoints,referPoints;
    Button newUser,oldUser,winnerBtn;
    String password = "@#def_HPQ_NS@#_";
    DatabaseReference myRef,getPoints;
    FirebaseDatabase database;
    String nu = "New User",ou="Old User",sub ="Submit",log="Login",ofe="Offer Ended",bck="Back";
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_active);
        printEmail = findViewById(R.id.printEmail);
        printPoints =findViewById(R.id.printPoints);
        winnerBtn =  findViewById(R.id.winnner);
        referPoints =findViewById(R.id.printRPoints);
        database = FirebaseDatabase.getInstance();
        myRef= database.getReference().child("Show_Offer");
        progressDialog = new ProgressDialog(OfferActive.this);
        progressDialog.setMessage("Please Wait..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        check();
        if(offer){
            goahead();
        }
        newUser =  findViewById(R.id.offer_take_part);
        oldUser =  findViewById(R.id.offer_old_user);
        blog =      findViewById(R.id.readRules);
        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendblog = new Intent(Intent.ACTION_VIEW);
                sendblog.setData(Uri.parse("http://freetechnohgeek.blogspot.in/p/privacy-policy-for-quiz-for-harry-potter.html"));
                startActivity(sendblog);
            }
        });
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!offer)
                {
                  Toast.makeText(getApplicationContext(),"Offer has ended",Toast.LENGTH_SHORT).show();
                }
                else
                newUser(0);
            }
        });
        oldUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!offer)
                {
                    Toast.makeText(getApplicationContext(),"Offer has ended",Toast.LENGTH_SHORT).show();
                }
                else
                oldUser();
            }
        });
        winnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent offer = new Intent(Intent.ACTION_VIEW);
                offer.setData(Uri.parse("http://freetechnohgeek.blogspot.in/2018/01/free-harry-potter-books.html"));
                startActivity(offer);
            }
        });
    }

    public void newUser(final int code){
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(OfferActive.this);
        LayoutInflater inflater = OfferActive.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.offer_layout,null,true);
        alertdialog.setView(view);
        TextView title = view.findViewById(R.id.titleText);
        final Button positive = view.findViewById(R.id.button1);
        final EditText getEmail = view.findViewById(R.id.getEmail);
        ImageView close = view.findViewById(R.id.close);
        if(code==0){
        title.setText(nu);
        positive.setText(sub);
        }else {
            title.setText(ou);
            positive.setText(log);
        }
        final AlertDialog alert = alertdialog.create();
        alert.setCancelable(false);
        alert.show();
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String email = getEmail.getText().toString();
                 if(!email.isEmpty()){
                     if(code==0){
                     register(email);}
                     else {
                         mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                             @Override
                             public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful()) {
                                   SharedPreferences.Editor saveGameState = getApplicationContext().getSharedPreferences("novus", MODE_PRIVATE).edit();
                                   saveGameState.putInt("MAXLEVEL", 0);
                                   saveGameState.putInt("MAXSUBLEVEL", 0);
                                   if (saveGameState.commit()) {
                                       Toast.makeText(getApplicationContext(), "Start Game", Toast.LENGTH_SHORT).show();
                                   } else{
                                      saveGameState.apply();
                                   }
                                   firebaseUser = mAuth.getCurrentUser();
                                   progressDialog.dismiss();
                                   Toast.makeText(getApplicationContext(),"Welcome Back",Toast.LENGTH_SHORT).show();
                                   alert.dismiss();
                                   oldUser();
                               }else {
                                   Log.d("Error",""+task.getException());
                                   Toast.makeText(getApplicationContext(),"Some Error, Pleas Try Again",Toast.LENGTH_LONG).show();
                               }
                             }
                         });
                     }
                 }
                 else
                     Toast.makeText(getApplicationContext(),"Enter Email",Toast.LENGTH_SHORT).show();
                alert.dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
    }
    public void oldUser(){
       if(firebaseUser!=null) {
           progressDialog.show();
           Log.d("Not NUll",firebaseUser.getUid());
           firebaseUser = mAuth.getCurrentUser();
           if(firebaseUser!=null){
           String uid = firebaseUser.getUid();
           getPoints = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
           }

           getPoints.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   String points = dataSnapshot.child("points").getValue().toString();
                   String refer = dataSnapshot.child("refer").getValue().toString();
                   Log.d("Points", "" + points);
                   progressDialog.dismiss();
                   String userEmail = firebaseUser.getEmail();
                   printEmail.setText(userEmail);
                   referPoints.setText(refer);
                   printPoints.setText(points);
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });
       }else{
           newUser(1);
       }
    }
    public void check(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String show = dataSnapshot.child("active").getValue().toString();
                Log.d("active",show);
                String info = dataSnapshot.child("info").getValue().toString();
                offer = Boolean.valueOf(show);
               if(!offer){
                   progressDialog.dismiss();
                   AlertDialog.Builder alertdialog = new AlertDialog.Builder(OfferActive.this);
                   LayoutInflater inflater = OfferActive.this.getLayoutInflater();
                   View view = inflater.inflate(R.layout.old_user_point,null,true);
                   alertdialog.setView(view);
                   TextView putEmail = view.findViewById(R.id.printUser);
                   TextView putPoint = view.findViewById(R.id.printPoints);
                   TextView points = view.findViewById(R.id.Points);
                   points.setVisibility(View.GONE);
                   ImageView close = view.findViewById(R.id.close);
                   Button winner = view.findViewById(R.id.winnner);
                   final AlertDialog alert = alertdialog.create();

                   putEmail.setText(ofe);
                   putPoint.setText(info);
                   winner.setText(bck);
                   alert.setCancelable(false);
                   alert.show();
                   close.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           alert.dismiss();
                       }
                   });
                   winner.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           alert.dismiss();
                         finish();
                       }
                   });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Check Your Internet Connection",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void register(final String email){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "createUserWithEmail:success");
                            firebaseUser = mAuth.getCurrentUser();
                            if(firebaseUser!=null) {
                                String uid = firebaseUser.getUid();
                                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("points");
                             db.setValue(0);
                            db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("refer");
                            db.setValue(0);
                            }
                            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                        firebaseUser = mAuth.getCurrentUser();
                                        oldUser();
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "createUserWithEmail:failure", task.getException());
                            progressDialog.dismiss();
                            Toast.makeText(OfferActive.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    public void goahead(){
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser!=null){
            oldUser();
        }else
            progressDialog.dismiss();
    }
    @Override
    public void onBackPressed(){
        OfferActive.this.finish();
        super.onBackPressed();
    }
}
