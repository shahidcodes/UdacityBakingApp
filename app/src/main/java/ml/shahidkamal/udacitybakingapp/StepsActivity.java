package ml.shahidkamal.udacitybakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.List;

import ml.shahidkamal.udacitybakingapp.adapter.StepsListAdapter;
import ml.shahidkamal.udacitybakingapp.fragments.StepsFragment;
import ml.shahidkamal.udacitybakingapp.model.Steps;
import ml.shahidkamal.udacitybakingapp.utils.Constants;

public class StepsActivity extends AppCompatActivity{

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bundle = getIntent().getExtras();
        replaceFragment(bundle);

    }

    public void nextRecipe(int pos){
        bundle.putInt(Constants.INTENT_KEY_STEPS_POSITION, pos+1);
        replaceFragment(bundle);
    }

    private void replaceFragment(Bundle bundle) {
        StepsFragment stepsFragment= new StepsFragment();
        stepsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.steps_fragment, stepsFragment)
                .commit();
    }

    public void prevRecipe(int pos) {
        bundle.putInt(Constants.INTENT_KEY_STEPS_POSITION, pos-1);
        replaceFragment(bundle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
