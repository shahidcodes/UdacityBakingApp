package ml.shahidkamal.udacitybakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import ml.shahidkamal.udacitybakingapp.fragments.RecipeDetailsFragment;
import ml.shahidkamal.udacitybakingapp.utils.Constants;

public class RecipeDetailsActivity extends AppCompatActivity {
    private static final String TAG = "RecipeDetailsActivity";
    static Boolean mTwoPane;
    String stepJson, ingredientJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(findViewById(R.id.recipe_details_container) != null){
            mTwoPane = true;
        }else{
            mTwoPane = false;
        }

        if(savedInstanceState == null){
            stepJson = getIntent().getStringExtra(Constants.INTENT_KEY_STEPS);
            ingredientJson = getIntent().getStringExtra(Constants.INTENT_KEY_INGREDIENTS);
            Log.d(TAG, "onCreate: Steps:");
            Bundle bundle = new Bundle();
            bundle.putString(Constants.INTENT_KEY_STEPS, stepJson);
            bundle.putString(Constants.INTENT_KEY_INGREDIENTS, ingredientJson);
            bundle.putBoolean(Constants.INTENT_KEY_PANE, mTwoPane);
            RecipeDetailsFragment detailsFragment = new RecipeDetailsFragment();
            detailsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_details_fragment, detailsFragment)
                    .commit();
        }
    }

    public static Boolean getmTwoPane() {
        return mTwoPane;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
