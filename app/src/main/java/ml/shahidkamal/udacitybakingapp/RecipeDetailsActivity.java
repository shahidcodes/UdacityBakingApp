package ml.shahidkamal.udacitybakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import ml.shahidkamal.udacitybakingapp.fragments.RecipeDetailsFragment;
import ml.shahidkamal.udacitybakingapp.fragments.StepsFragment;
import ml.shahidkamal.udacitybakingapp.model.Steps;
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

        mTwoPane = findViewById(R.id.recipe_details_container) != null;


        if (savedInstanceState == null) {
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

            if (mTwoPane) {
                showStepsFragmentAt(0);
            }
        }
    }

    private void showStepsFragmentAt(int position) {
        StepsFragment stepsFragment = new StepsFragment();
        Bundle stepsFragmentBundle = new Bundle();
        stepsFragmentBundle.putString(Constants.INTENT_KEY_STEPS_FRAGMENT, stepJson);
        stepsFragmentBundle.putInt(Constants.INTENT_KEY_STEPS_POSITION, position);
        stepsFragmentBundle.putBoolean(Constants.INTENT_KEY_PANE, mTwoPane);
        stepsFragment.setArguments(stepsFragmentBundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.steps_fragment, stepsFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void changeStepsFragment(int position) {
        if (mTwoPane) {
            showStepsFragmentAt(position);
        } else {
            Intent intent = new Intent(this, StepsActivity.class);
            intent.putExtra(Constants.INTENT_KEY_STEPS_FRAGMENT, stepJson);
            intent.putExtra(Constants.INTENT_KEY_STEPS_POSITION, position);
            startActivity(intent);
        }
    }

}
