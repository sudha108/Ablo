package com.ablo;


import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.os.Build.*;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

/**
 * Created by sudhams on 20/02/18.
 */

public class BlockService extends Service {

    private WindowManager windowManager;
    private ImageView popup;

    @Override
    public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        popup = new ImageView(this);
        popup.setImageResource(R.drawable.block);

        int wal;
        if (VERSION.SDK_INT >= VERSION_CODES.M){
            wal = LayoutParams.TYPE_PHONE ;
        } else{
            wal = LayoutParams.TYPE_SYSTEM_OVERLAY;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                wal,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        windowManager.addView(popup, params);

        Intent i = new Intent(BlockService.this, DrawOverActivity.class);
        startActivity(i);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (popup != null) windowManager.removeView(popup);
    }


}
