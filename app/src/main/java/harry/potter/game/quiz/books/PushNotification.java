package harry.potter.game.quiz.books;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Hemant Khorwal on 25-01-2018.
 */

public class PushNotification extends FirebaseMessagingService {
    String message,title="Harry Potter";
    Uri sound;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Intent push = new Intent(this,Home.class);
        push.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent open = PendingIntent.getActivity(this,0,push,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        message = remoteMessage.getNotification().getBody();
        notification.setContentTitle(title);
        notification.setContentText(message);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.mipmap.logo);
        notification.setContentIntent(open);
        notification.setSound(sound);
        NotificationManager notify = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            assert notify != null;
            notify.notify(0,notification.build());
        }catch (Exception e){
            Log.d("Exe",e.toString());
        }


    }
}
