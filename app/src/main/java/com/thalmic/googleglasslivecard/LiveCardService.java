package com.thalmic.googleglasslivecard;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * A {@link Service} that publishes a {@link LiveCard} in the timeline.
 */
public class LiveCardService extends Service {

    private String TAG = "LiveCardService";

    private LiveCard mLiveCard;
    private MyReceiver myReceiver;
    public RemoteViews remoteViews;
    public boolean isInConversation = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLiveCard == null) {
            mLiveCard = new LiveCard(this, TAG);

            remoteViews = new RemoteViews(getPackageName(), R.layout.live_card);
            mLiveCard.setViews(remoteViews);

            // Display the options menu when the live card is tapped.
            Intent menuIntent = new Intent(this, LiveCardMenuActivity.class);
            mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.thalmic.livecarddemo.switchmode");
            myReceiver = new MyReceiver();
            registerReceiver(myReceiver, intentFilter);

            mLiveCard.publish(PublishMode.REVEAL);
        } else {
            mLiveCard.navigate();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished()) {
            mLiveCard.unpublish();
            mLiveCard = null;
            unregisterReceiver(myReceiver);
        }
        super.onDestroy();
    }

    public class MyReceiver extends BroadcastReceiver {
        public MyReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received");
            Log.d(TAG, Boolean.toString(isInConversation));
            if(isInConversation) {
                Log.d(TAG, "isinconversation");
                remoteViews.setTextViewText(R.id.status, getString(R.string.no_conversation));
                isInConversation = false;
            } else {
                Log.d(TAG, "notinconversation");
                remoteViews.setTextViewText(R.id.status, getString(R.string.in_conversation));
                isInConversation = true;
            }
            mLiveCard.setViews(remoteViews);
        }
    }
}
