package com.example.mateuszkaflowski.mediastylepalette;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import mkaflowski.mediastylepalette.MediaNotificationProcessor;


public class MainActivity extends Activity {

    ImageView ivPlay;
    ImageView ivPrevious;
    ImageView ivRewinf;
    ImageView ivFF;
    ImageView ivNext;
    TextView tvTitle;
    TextView tvArtist;
    TextView tvDesc;
    TextView tvStart;
    TextView tvEnd;
    private String CHANNEL_ID = "testchannel";
    private MediaSessionCompat mediaSessionCompat;
    private Bitmap icon;

    int[] ids = {R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e,
            R.drawable.f, R.drawable.g, R.drawable.h};
    private int drawableId;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawableId = ids[i];
        init();
    }

    private void init() {
        View background = findViewById(R.id.main_container);

        icon = BitmapFactory.decodeResource(getResources(),
                drawableId);

        ImageView ivCover = findViewById(R.id.cover);
        ivCover.setImageResource(drawableId);
        MediaNotificationProcessor processor = new MediaNotificationProcessor(this, icon);

        int backgroundColor = processor.getBackgroundColor();
        int foregroundColor = processor.getPrimaryTextColor();
        int secondaryColor = processor.getSecondaryTextColor();

        background.setBackgroundColor(backgroundColor);

        ivPlay = findViewById(R.id.playButton);
        ivPrevious = findViewById(R.id.previous);
        ivRewinf = findViewById(R.id.minusButton);
        ivFF = findViewById(R.id.plusButton);
        ivNext = findViewById(R.id.next);

        tvTitle = findViewById(R.id.title);
        tvArtist = findViewById(R.id.episodeTitle);
        tvDesc = findViewById(R.id.episodeDesc);
        tvStart = findViewById(R.id.time);
        tvEnd = findViewById(R.id.totalTime);

        tvTitle.setTextColor(foregroundColor);
        tvArtist.setTextColor(secondaryColor);
        tvDesc.setTextColor(foregroundColor);

        SeekBar sb = findViewById(R.id.progressBar);
        sb.getProgressDrawable().setColorFilter(foregroundColor, PorterDuff.Mode.SRC_ATOP);
        sb.getThumb().setColorFilter(foregroundColor, PorterDuff.Mode.SRC_ATOP);
        sb.getIndeterminateDrawable().setColorFilter(secondaryColor, PorterDuff.Mode.SRC_ATOP);

        showNotification(drawableId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(backgroundColor);
            window.setNavigationBarColor(backgroundColor);
        }

        ivPlay.setColorFilter(foregroundColor);

        if(processor.isLight()){
            ivPrevious.setColorFilter(Color.DKGRAY);
            ivRewinf.setColorFilter(Color.DKGRAY);
            ivFF.setColorFilter(Color.DKGRAY);
            ivNext.setColorFilter(Color.DKGRAY);
            tvStart.setTextColor(Color.DKGRAY);
            tvEnd.setTextColor(Color.DKGRAY);
        }else{
            ivPrevious.setColorFilter(Color.WHITE);
            ivRewinf.setColorFilter(Color.WHITE);
            ivFF.setColorFilter(Color.WHITE);
            ivNext.setColorFilter(Color.WHITE);
            tvStart.setTextColor(Color.WHITE);
            tvEnd.setTextColor(Color.WHITE);
        }
    }

    private void showNotification(int drawableId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
        initMediaSession();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        Intent playIntent = new Intent(this, MainActivity.class);
        playIntent.setAction("s");
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        builder.addAction(R.drawable.minus, "Play", pplayIntent);
        builder.addAction(R.drawable.play_notification, "Play", pplayIntent);
        builder.addAction(R.drawable.plus, "Play", pplayIntent);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setShowCancelButton(true)
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setShowWhen(false)
                .setContentTitle("Title Name")
                .setContentText("Content text")
                .setSmallIcon(R.drawable.pause)
                .setWhen(0)
                .setAutoCancel(true)
                .setLargeIcon(icon);


        Notification notification = builder.build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(112, notification);

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationManager
                mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // The id of the channel.
        String id = CHANNEL_ID;
        // The user-visible name of the channel.
        CharSequence name = "Media playback";
        // The user-visible description of the channel.
        String description = "Media playback controls";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        // Configure the notification channel.
        mChannel.setDescription(description);
        mChannel.setShowBadge(false);
        mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        mNotificationManager.createNotificationChannel(mChannel);

    }

    private void initMediaSession() {
        ComponentName mediaButtonReceiver = new ComponentName(this, MediaButtonReceiver.class);
        mediaSessionCompat = new MediaSessionCompat(getApplicationContext(), "MediaTAG", mediaButtonReceiver, null);


        mediaSessionCompat.setCallback(null);
        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setClass(this, MediaButtonReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0);
        mediaSessionCompat.setMediaButtonReceiver(pendingIntent);

        //setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING);
        mediaSessionCompat.setActive(true);
        //setSessionToken(mediaSessionCompat.getSessionToken());

    }

    public void next(View view) {
        i++;
        if (i >= ids.length)
            i = 0;
        drawableId = ids[i];
        init();
    }
}
