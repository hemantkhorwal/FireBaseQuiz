package harry.potter.game.quiz.books;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Hemant Khorwal on 25-01-2018.
 */

public class FirebaseService extends FirebaseInstanceIdService {
    private static final String REG_TOCKEN="REG_TOCKEN";
    @Override
    public void onTokenRefresh(){
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOCKEN,token);
    }
}
