package services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.rupeeredee.app.LoginActivity;
import com.rupeeredee.app.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext=context;
        Log.d("BroadcastReceiver::", "BroadcastReceiver");
        //sendNotification("1");

    }
/*
    private void sendNotification(String messageBody) {
        //1 - event / 2 - notice / 3 - fees / 4 - class_time / 5 - exam / 6 - homework / 7 - leave

        Intent resultIntent;
        resultIntent = new Intent(mContext, LoginActivity.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        if (messageBody.trim().equals("1")) {
            resultIntent = new Intent(mContext, LoginActivity.class);
            resultIntent.putExtra("status", "1");

        } else if (messageBody.trim().equals("2")) {
            resultIntent.putExtra("status", "2");
        } else if (messageBody.trim().equals("3")) {
            resultIntent = new Intent(mContext, LoginActivity.class);
            resultIntent.putExtra("status", "3");

        }


        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle("Igniteu")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "");
        builder.setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(0, builder.build());

    */
/*    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 *//*
*/
/* ID of notification *//*
*/
/*, notificationBuilder.build());*//*


    }
*/

}