package harry.potter.game.quiz.books;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class Adapter extends ArrayAdapter<String> {
    private Activity context;
    private String[] title;
    private String[] subtitles;
    private int StartLevel;
    private int MAXLEVEL,MAXSUBLEVEL,code;
    private final Typeface font;
    String com = "Completed",ply="Play";
    public Adapter(Activity context,String[] title,String[] subtitles,int Level,int code,Typeface font) {
        super(context,R.layout.listview,title);
        this.subtitles=subtitles;
        this.title = title;
        this.context = context;
        this.StartLevel = Level;
        this.code = code;
        this.font = font;
        retiveData();

    }
    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup viewGroup){
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.listview,null,true);
        TextView titleName = rowView.findViewById(R.id.title);
        TextView subtitle = rowView.findViewById(R.id.subtitle);
        TextView play = rowView.findViewById(R.id.play);
        titleName.setTypeface(font);
        subtitle.setTypeface(font);
        play.setTypeface(font);
        titleName.setText(title[position]);
        subtitle.setText(subtitles[position]);
        if(code==0){
            if(position<StartLevel)
                play.setText(com);
            else if(position==StartLevel)
                play.setText(ply);
        }else if(code==1){
            if(StartLevel==MAXLEVEL){
                if(position<MAXSUBLEVEL)
                    play.setText(com);
                if(position==MAXSUBLEVEL)
                    play.setText(ply);
            }else{
                play.setText(com);
            }
        }
        return rowView;
    }
    private void retiveData(){
        SharedPreferences retrive = context.getApplicationContext().getSharedPreferences("novus",MODE_PRIVATE);
        MAXLEVEL=retrive.getInt("MAXLEVEL",0);
        MAXSUBLEVEL = retrive.getInt("MAXSUBLEVEL",0);
    }
}