package online.lahloba.www.lahloba.data.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ui.delivery.DeliveryMainActivity;
import online.lahloba.www.lahloba.ui.delivery_supervisor.DeliverySupervisorMainActivity;
import online.lahloba.www.lahloba.ui.order.OrderDetailsActivity;
import online.lahloba.www.lahloba.utils.Constants;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyFirebaseInstanceService extends FirebaseMessagingService  {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            String type = remoteMessage.getData().get("type");

            if (type.equals("seller_new_order")){
                Intent intent = new Intent(this, OrderDetailsActivity.class);
                intent.putExtra(Constants.ORDER_ID, remoteMessage.getData().get("orderId"));

                makeNotification(
                        R.drawable.order_pending_icon,
                        "Lahloba : New Order No. "+ remoteMessage.getData().get("orderNumber"),
                        "You have new Order Click to go",
                        intent
                );
            }
            else if (type.equals("customer_order_recieved")){
                Intent intent = new Intent(this, OrderDetailsActivity.class);
                intent.putExtra(Constants.ORDER_ID, remoteMessage.getData().get("orderId"));

                makeNotification(
                        R.drawable.order_recieved_icon,
                        "Lahloba : Order No. "+ remoteMessage.getData().get("orderNumber") + " has recieved",
                        "Seller has recieved your order and he is preparing it",
                        intent
                );
            }
            else if (type.equals("delivery_supervisor_order_prepared")){
                Intent intent = new Intent(this, DeliverySupervisorMainActivity.class);

                makeNotification(
                        R.drawable.order_processed_icon,
                        "Lahloba : Order No. "+ remoteMessage.getData().get("orderNumber") + " has prepared",
                        "Seller has prepared new order",
                        intent
                );
            }
            else if (type.equals("delivery_order_allocated")){
                Intent intent = new Intent(this, DeliveryMainActivity.class);
                intent.putExtra(Constants.ORDER_ID, remoteMessage.getData().get("orderNumber"));

                makeNotification(
                        R.drawable.order_shipped_icon,
                        "Lahloba : Order No. "+ remoteMessage.getData().get("orderNumber") + " has allocated to you",
                        "Delivery supervisor allocated new order to you",
                        intent
                );
            }
            else if (type.equals("customer_order_shipped")){
                Intent intent = new Intent(this, OrderDetailsActivity.class);
                 intent.putExtra(Constants.ORDER_ID, remoteMessage.getData().get("orderId"));

                makeNotification(
                        R.drawable.order_shipped_icon,
                        "Lahloba : Order No. "+ remoteMessage.getData().get("orderNumber") + " has shipped",
                        "Seller has shipped your order",
                        intent
                );
            }


        }

     }

    private void makeNotification(int icon, String title, String message, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) Math.random(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Lahloba")
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(Uri.parse("android.resource://"+this.getPackageName()+"/"+R.raw.definite))
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify((int) Math.random(), builder.build());

    }


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        if (FirebaseAuth.getInstance().getUid() == null) return;
        FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(FirebaseAuth.getInstance().getUid())
                .child("notificationToken")
                .setValue(token);

    }


}
