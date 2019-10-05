package online.lahloba.www.lahloba.data.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;


import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.repository.AppRepository;
import online.lahloba.www.lahloba.utils.Injector;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyFirebaseInstanceService extends FirebaseMessagingService  {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String type = remoteMessage.getData().get("type");

            if (type.equals("seller_new_order")){
                makeNotification(
                        R.drawable.order_pending_icon,
                        "Lahloba : New Order No. "+ remoteMessage.getData().get("orderNumber"),
                        "You have new Order Click to go"
                );
            }else if (type.equals("customer_order_recieved")){
                makeNotification(
                        R.drawable.order_recieved_icon,
                        "Lahloba : Order No. "+ remoteMessage.getData().get("orderNumber") + " has recieved",
                        "Seller has recieved your order and he is preparing it"
                );
            }else if (type.equals("delivery_supervisor_order_prepared")){
                makeNotification(
                        R.drawable.order_processed_icon,
                        "Lahloba : Order No. "+ remoteMessage.getData().get("orderNumber") + " has prepared",
                        "Seller has prepared new order"
                );
            }else if (type.equals("delivery_order_allocated")){
                makeNotification(
                        R.drawable.order_shipped_icon,
                        "Lahloba : Order No. "+ remoteMessage.getData().get("orderNumber") + " has allocated to you",
                        "Delivery supervisor allocated new order to you"
                );
            }else if (type.equals("customer_order_shipped")){
                makeNotification(
                        R.drawable.order_shipped_icon,
                        "Lahloba : Order No. "+ remoteMessage.getData().get("orderNumber") + " has shipped",
                        "Seller has shipped your order"
                );
            }


        }

     }

    private void makeNotification(int icon, String title, String message) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Seller")
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(alarmSound)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());

    }


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        if (FirebaseAuth.getInstance().getUid() == null) return;
        AppRepository appRepository = Injector.provideRepository(this);
        appRepository.startUpdateMessagingToken();

    }


}
