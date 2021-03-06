package ml.shahidkamal.udacitybakingapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ml.shahidkamal.udacitybakingapp.utils.SimpleIdlingResource;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static boolean mTwoPane;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View tabView = findViewById(R.id.tab_list_recipe_container);
        if (tabView != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
        Log.d(TAG, "mTwoPane: " + mTwoPane);
        getIdlingResource();
    }

    public static boolean getNoPane() {
        return mTwoPane;
    }
}
