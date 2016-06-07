package com.stfalcon.android.ui.base;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by troy379 on 05.01.16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = BaseActivity.class.getSimpleName();

    public String currentFragmentName = "";

    public void changeFragmentWithoutRepeat(Fragment fragment, int containerID, boolean addToBackStack) {
        String fragmentName = fragment.getClass().getName();

        if (!currentFragmentName.contentEquals(fragmentName)) {
            changeFragment(fragment, containerID, addToBackStack);
        }
    }

    public void changeFragment(Fragment fragment, int containerID, boolean addToBackStack) {
        String fragmentName = fragment.getClass().getName();

        currentFragmentName = fragmentName;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, fragment, fragmentName);
        if (addToBackStack) transaction.addToBackStack(fragmentName);
        transaction.commit();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
        window.getDecorView().getBackground().setDither(true);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    public interface OnKeyboardVisibilityListener {


        void onVisibilityChanged(boolean visible);
    }

    public final void setKeyboardListener(final OnKeyboardVisibilityListener listener) {
        final View activityRootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);

        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private boolean wasOpened;

            private final int DefaultKeyboardDP = 100;

            private final int EstimatedKeyboardDP = DefaultKeyboardDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);

            private final Rect r = new Rect();

            @Override
            public void onGlobalLayout() {
                // Convert the dp to pixels.
                int estimatedKeyboardHeight = (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, activityRootView.getResources().getDisplayMetrics());

                // Conclude whether the keyboard is shown or not.
                activityRootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;

                if (isShown == wasOpened) {
                    Log.d("Keyboard state", "Ignoring global layout change...");
                    return;
                }

                wasOpened = isShown;
                listener.onVisibilityChanged(isShown);
            }
        });
    }
}
