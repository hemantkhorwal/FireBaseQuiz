package harry.potter.game.quiz.books;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Quiz extends AppCompatActivity{
        AdView banner,banner1;
        FirebaseAuth mAuth;
        FirebaseUser mUser;
        String UID;
        DatabaseReference mDb,write,readQuestion;
        InterstitialAd inter;
        String fb_level,fb_sublevel;
        String mPoints;
        int value_points=0,value_lives=3,LEVEL,SUBLEVEL,MAXLEVEL,MAXSUBLEVEL;
        Random random_question,random_option;
        Button[] option = new Button[4];
        Typeface font;
        TextView question,show_points,show_lives,levelname,levelnum,correctQuestion;
        List<Integer> askedQuestionID = new ArrayList<>(),optionSetID = new ArrayList<>(),ButttonFilled = new ArrayList<>();
        String Questions[] = {"a","b","c","d","e","f","g","h","i","j"};
        String[] split;
        String askingQuestion,CorrectAnswer,UserAnswer,PLAY_STORE,LevelName;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            if(mUser!=null){UID = mUser.getUid();
            mDb = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);
            write = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("points");
                mDb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mPoints = dataSnapshot.child("points").getValue().toString();
                        value_points = Integer.valueOf(mPoints);
                        String value = String.valueOf(value_points);
                        show_points.setText(value);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        value_points=0;
                    }
                });
            }
            readQuestion = FirebaseDatabase.getInstance().getReference().child("Questions");

            retriveMax();
            Bundle extra = getIntent().getExtras();
            if(extra!=null){
                LEVEL  = (int) extra.get("SET_LEVEL");
                SUBLEVEL = (int) extra.get("SET_QUIZ");
            }
            setContentView(R.layout.activity_quiz);
            oneTimeSetup();
            setQuestion();

        }
        public void oneTimeSetup(){
            //Typeface
            font = Typeface.createFromAsset(getAssets(),"font/hptext.TTF");

            //Get questions form resource according to the level and sublevel
            getQuestions();

            //Adrequest

            AdRequest adRequest = new AdRequest.Builder()
                    .addKeyword("data room")
                    .addKeyword("dataroom software")
                    .addKeyword("bail bonds")
                    .addKeyword("bail bonds san francisco")
                    .addKeyword("plumber")
                    .addKeyword("pest control")
                    .addKeyword("banking")
                    .addKeyword("google adwords")
                    .addKeyword("degree")
                    .addKeyword("insuurance")
                    .addKeyword("mesothelioma lawyers")
                    .addKeyword("mesothelioma attorney")
                    .addKeyword("mesothelioma law")
                    .addKeyword("asbestos lawyer")
                    .addKeyword("texas mesothelioma lawyer")
                    .addKeyword("car accident lawyer")
                    .addKeyword("accident attorney")
                    .addKeyword("auto accident attorneys")
                    .addKeyword("donate car")
                    .addKeyword("donation car to charity")
                    .addKeyword("harry potter")
                    .addKeyword("harry potter games").build();

            //ALL findViewById
            inter = new InterstitialAd(this);
            inter.setAdUnitId(getResources().getString(R.string.inter));
            inter.loadAd(adRequest);
            banner = findViewById(R.id.adviewQuiz);
            banner.loadAd(adRequest);
            banner1 = findViewById(R.id.adviewQuiz2);
            banner1.loadAd(adRequest);
            option[0]   =  findViewById(R.id.option0);
            option[1]   =  findViewById(R.id.option1);
            option[2]   =  findViewById(R.id.option2);
            option[3]   =  findViewById(R.id.option3);
            question    =  findViewById(R.id.qiestion);
            show_points =  findViewById(R.id.points);
            show_lives  =  findViewById(R.id.lives);
            levelname   =  findViewById(R.id.levellabel);
            levelnum    =  findViewById(R.id.levelnumber);
            show_lives.setText(String.valueOf(value_lives));
            show_points.setText(String.valueOf(value_points));
            correctQuestion =findViewById(R.id.correctQuestion);
            levelname.setText(LevelName);
            int temp = SUBLEVEL+1;
            String con = String.valueOf(temp);
            String Le = "Level ";
            Le = Le.concat(con);
            levelnum.setText(Le);

            //Typeface
            //option[0].setTypeface(font);
            //option[1].setTypeface(font);
            //option[2].setTypeface(font);
            //option[3].setTypeface(font);
            //question.setTypeface(font);
            show_points.setTypeface(font);
            show_lives.setTypeface(font);
            levelname.setTypeface(font);
            levelnum.setTypeface(font);

            //PLAY STORE LINK RATE US
            PLAY_STORE = "https://play.google.com/store/apps/details?id=harry.potter.game.quiz.books";


            //Setup for setOnClickListner
            option[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(connection()){
                    for(int i=0;i<4;i++)
                        option[i].setEnabled(false);
                    UserAnswer = option[0].getText().toString();
                    if(UserAnswer.equals(CorrectAnswer)){
                        UserGiveRightAnswer(0);
                    }else {
                        UserGiveWrongAnswer(0);
                    }
                    }else {
                        Toast.makeText(getApplicationContext(),"Conncet To Internet",Toast.LENGTH_SHORT).show();
                    }

                }
            });
            option[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(connection()){
                    for(int i=0;i<4;i++)
                        option[i].setEnabled(false);
                    UserAnswer = option[1].getText().toString();
                    if(UserAnswer.equals(CorrectAnswer)){
                        UserGiveRightAnswer(1);
                    }else {
                        UserGiveWrongAnswer(1);
                    }}
                    else {
                        Toast.makeText(getApplicationContext(),"Conncet To Internet",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            option[2].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(connection()){
                    for(int i=0;i<4;i++)
                        option[i].setEnabled(false);
                    UserAnswer = option[2].getText().toString();
                    if(UserAnswer.equals(CorrectAnswer)){
                        UserGiveRightAnswer(2);
                    }else {
                        UserGiveWrongAnswer(2);
                    }
                    }else {
                        Toast.makeText(getApplicationContext(),"Conncet To Internet",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            option[3].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(connection()){
                    for(int i=0;i<4;i++)
                        option[i].setEnabled(false);
                    UserAnswer = option[3].getText().toString();
                    if(UserAnswer.equals(CorrectAnswer)){
                        UserGiveRightAnswer(3);
                    }else {
                        UserGiveWrongAnswer(3);
                    }}
                    else {
                        Toast.makeText(getApplicationContext(),"Conncet To Internet",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            correctQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                correct();
                }
            });

        }
        public void retriveMax(){
            SharedPreferences retrive = getApplicationContext().getSharedPreferences("novus",MODE_PRIVATE);
            MAXLEVEL=retrive.getInt("MAXLEVEL",0);
            MAXSUBLEVEL = retrive.getInt("MAXSUBLEVEL",0);
        }

        public void setQuestion(){
            if(askedQuestionID.size()==5){
                if(inter.isLoaded())
                    inter.show();
            }

            final int Question_number;
            random_question = new Random();
            Question_number = random_question.nextInt(Questions.length);
            if(askedQuestionID.contains(Question_number)){
                setQuestion();
            }
            else {
                askedQuestionID.add(Question_number);
                Log.d("Level/SubLevel",fb_level+"/"+fb_sublevel);
                DatabaseReference read = FirebaseDatabase.getInstance().getReference().child("Questions").child(fb_level).child(fb_sublevel);
                read.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("QUE LOG",dataSnapshot.child(String.valueOf(Question_number)).toString());
                        String q_n = String.valueOf(Question_number);
                       split(dataSnapshot.child(q_n).getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                      Toast.makeText(getApplicationContext(),databaseError.toString(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
        public void split(String string){
            split = string.split("#");
            askingQuestion = split[4];
            CorrectAnswer = split[0];
            split[4]=null;
            Log.d("Que",askingQuestion);
            question.setText(askingQuestion);
            setOption();
        }
        public void setOption(){
            int Option_number,Button_Number;
            optionSetID.clear();
            ButttonFilled.clear();
            random_option = new Random();
            for(;ButttonFilled.size()<4;){
                Option_number = random_option.nextInt(4);
                Button_Number = random_option.nextInt(4);
                if(optionSetID.contains(Option_number) || ButttonFilled.contains(Button_Number)){
                }else{
                    optionSetID.add(Option_number);
                    ButttonFilled.add(Button_Number);
                    option[Button_Number].setText(split[Option_number]);
                }
            }
        }
        public void UserGiveRightAnswer(int buttonId){
            option[buttonId].setBackgroundColor(getResources().getColor(R.color.green));
            alert("RIGHT");
            value_points = value_points+10;
            if(mUser!=null)
            write.setValue(value_points);
            show_points.setText(String.valueOf(value_points));
        }
        public void UserGiveWrongAnswer(int buttonId){
            option[buttonId].setBackgroundColor(getResources().getColor(R.color.red));
            value_lives--;
            show_lives.setText(String.valueOf(value_lives));
            value_points = value_points-5;
            if(mUser!=null)
            write.setValue(value_points);
            show_points.setText(String.valueOf(value_points));
            alert("WRONG");
        }
        public void refreshPage(){
            for(int i=0;i<4;i++){
                option[i].setEnabled(true);
                option[i].setBackgroundColor(getResources().getColor(R.color.half));
            }
            if(askedQuestionID.size()<=9){
                if(value_lives>0) {
                    setQuestion();
                }else {
                    LevelLost();
                }
            }else {
                if(value_lives>0) {
                    LevelWon();
                }else {
                    LevelLost();
                }
            }
        }

        public void LevelLost(){
            value_points=value_points-5;
            try {
                write.setValue(value_points);
            }catch(Exception e){
                   Log.d("Ex",e.toString());
                }
                alert("LOST");
        }
        public void LevelWon(){
            value_points=value_points+5;
            try {
                write.setValue(value_points);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
            }

            if(inter.isLoaded()){
                inter.show();
            }
            if(LEVEL==MAXLEVEL&&SUBLEVEL==MAXSUBLEVEL){
                boolean saved = saveData();
                if(LEVEL==4){
                    GameCompleted();
                }else if(saved){
                    alert("NEW_LEVEL");
                }
            }else {
                setNewLevel_miscCondition();
                alert("WON");
            }
        }
        public boolean saveData(){
            SUBLEVEL++;
            if(SUBLEVEL==5) {
                if (LEVEL <= 3) {
                    LEVEL++;
                    if(LEVEL!=4)
                    SUBLEVEL = 0;
                }
            }
            SharedPreferences.Editor saveGameState = getApplicationContext().getSharedPreferences("novus", MODE_PRIVATE).edit();
            saveGameState.putInt("MAXLEVEL", LEVEL);
            saveGameState.putInt("MAXSUBLEVEL", SUBLEVEL);
            return saveGameState.commit();
        }
        public void alert(final String code){
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(Quiz.this);
            LayoutInflater inflater = Quiz.this.getLayoutInflater();
            View view = inflater.inflate(R.layout.alert_dialog,null,true);
            alertdialog.setView(view);
            TextView title = view.findViewById(R.id.errorTitle);
            TextView message = view.findViewById(R.id.errorMessage);
            Button positive = view.findViewById(R.id.positive);
            Button negative = view.findViewById(R.id.negative);
            switch (code) {
                case "RIGHT":
                    title.setText(R.string.TitleOnRight);
                    message.setText(R.string.MsgOnRightAns);
                    positive.setText(R.string.Next);
                    negative.setText(R.string.RateUs);
                    break;
                case "WRONG":
                    title.setText(R.string.TitleOnWrong);
                    message.setText(R.string.MsgOnWrong);
                    positive.setText(R.string.Next);
                    negative.setText(R.string.RateUs);
                    break;
                case "WON":
                    title.setText(R.string.TitleOnLevelCom);
                    message.setText(R.string.MsgOnLevelCom);
                    positive.setText(R.string.Next);
                    negative.setText(R.string.ButtonBack);
                    break;
                case "LOST":
                    title.setText(R.string.TitleOnLevelLost);
                    message.setText(R.string.MsgOnLevelLost);
                    positive.setText(R.string.ButtonRestart);
                    negative.setText(R.string.ButtonBack);
                    break;
                case "NEW_LEVEL":
                    title.setText(R.string.TitleOnLevelCom);
                    message.setText(R.string.MsgOnLevelCom);
                    positive.setText(R.string.Next);
                    negative.setText(R.string.ButtonBack);
                    break;
                case "BACK":
                    title.setText(R.string.BackTitle);
                    message.setText(R.string.ProgressLost);
                    positive.setText(R.string.Back);
                    negative.setText(R.string.Continue);

            }
            final AlertDialog alert = alertdialog.create();
            alert.setCancelable(false);
            alert.show();
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (code) {
                        case "WON":
                        case "NEW_LEVEL":
                            Intent quiz = new Intent(Quiz.this, Quiz.class);
                            quiz.putExtra("SET_LEVEL", LEVEL);
                            quiz.putExtra("SET_QUIZ", SUBLEVEL);
                            startActivity(quiz);
                            if(inter.isLoaded()){
                                inter.show();
                                finish();
                            }
                            else {
                                finish();
                            }

                            break;
                        case "LOST":
                            if(inter.isLoaded()){
                                inter.show();
                            }
                            recreate();

                            break;
                        case "RIGHT":
                            refreshPage();
                            break;
                        case  "WRONG":
                            refreshPage();
                            break;
                        case "BACK":
                            Intent Sublevel = new Intent(Quiz.this,SubLevels.class);
                            Sublevel.putExtra("START_LEVEL",LEVEL);
                            startActivity(Sublevel);
                            if(inter.isLoaded()){
                                inter.show();
                                finish();
                            }
                            else {
                                finish();
                            }
                    }
                    alert.dismiss();
                }
            });
            negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(code.equals("LOST") || code.equals("WON")){
                        Intent Sublevel = new Intent(Quiz.this,SubLevels.class);
                        Sublevel.putExtra("START_LEVEL",LEVEL);
                        startActivity(Sublevel);
                        if(inter.isLoaded()){
                            inter.show();
                            finish();
                        }
                        else {
                            finish();
                        }
                    }else if(code.equals("BACK")){
                        if(inter.isLoaded()){
                            inter.show();
                        }
                        alert.dismiss();
                    }else {
                        Intent rateUs = new Intent(Intent.ACTION_VIEW);
                        rateUs.setData(Uri.parse(PLAY_STORE));
                        startActivity(rateUs);
                    }
                }
            });
        }
        public void setNewLevel_miscCondition() {
            if(SUBLEVEL<=5){
                SUBLEVEL++;
            }else{
                LEVEL++;
                SUBLEVEL=0;
            }
        }
        public void getQuestions(){
            switch (LEVEL){
                case 0 :
                    fb_level="1";
                    LevelName = "SQUIB";
                    fb_sublevel=String.valueOf(SUBLEVEL+1);
                    return;
                case 1:
                    fb_level="2";
                    LevelName = "MUGGLE BORN";
                    fb_sublevel=String.valueOf(SUBLEVEL+1);
                    return;
                case 2:
                    fb_level="3";
                    LevelName = "HALF BLOOD";
                    fb_sublevel=String.valueOf(SUBLEVEL+1);
                    return;
                case 3:
                    fb_level="4";
                    LevelName = "PURE BLOOD";
                    fb_sublevel=String.valueOf(SUBLEVEL+1);
            }
        }
        @Override
        public void onBackPressed(){
            alert("BACK");
        }



    public void GameCompleted(){
            mDb.child("Completed").setValue("true");
            mDb.child("ScoreWhenDone").setValue(value_points);
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(Quiz.this);
        LayoutInflater inflater = Quiz.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog,null,true);
        alertdialog.setView(view);
        TextView title = view.findViewById(R.id.errorTitle);
        TextView message = view.findViewById(R.id.errorMessage);
        Button positive = view.findViewById(R.id.positive);
        Button negative = view.findViewById(R.id.negative);
        title.setText("True Potterhead");
        message.setText("You have completed the quiz,you are greate harry potter fan.");
        positive.setText("Home");
        negative.setText(R.string.RateUs);
        final AlertDialog alert = alertdialog.create();
        alert.setCancelable(false);
        alert.show();
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                finish();
                Intent home = new Intent(Quiz.this,Home.class);
                startActivity(home);
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
    public  void onPause(){
        super.onPause();
    }
    @Override
    public  void onResume(){
        super.onResume();
    }

    public boolean connection(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

    }
    public  void  correct(){
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(Quiz.this);
        LayoutInflater inflater = Quiz.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.offer_layout,null,true);
        alertdialog.setView(view);
        TextView title = view.findViewById(R.id.titleText);
        final Button positive = view.findViewById(R.id.button1);
        final EditText getEmail = view.findViewById(R.id.getEmail);
        TextView hint = view.findViewById(R.id.hint);
        hint.setText("You Will get 5 Points and chance for being admin of this app");
        getEmail.setText(askingQuestion);
        ImageView books = view.findViewById(R.id.books_png);
        books.setVisibility(View.GONE);
        final ImageView close = view.findViewById(R.id.close);
        title.setText("Let Me Correct");
        positive.setText("Submit");

        final AlertDialog alert = alertdialog.create();
        alert.setCancelable(false);
        alert.show();
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Correction");
                String email = getEmail.getText().toString();
                if(!email.isEmpty()) {
                    String cor = fb_level+"/"+fb_sublevel+"/";
                    cor= cor.concat(email);
                    if(UID!=null){
                    db.child(UID).setValue(cor);
                    value_points=value_points+5;
                    write.setValue((value_points));
                    }else {
                        UID = "Unregistered";
                        db.child(UID).setValue(cor);
                    }
                    Toast.makeText(getApplicationContext(),"Thank you for your help",Toast.LENGTH_SHORT).show();
                    alert.dismiss();
                }else {
                    Toast.makeText(getApplicationContext(),"Don't Leave it empty",Toast.LENGTH_SHORT).show();
                }

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
    }
    }
