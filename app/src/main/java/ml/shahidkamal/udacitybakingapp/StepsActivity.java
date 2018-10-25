package ml.shahidkamal.udacitybakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import ml.shahidkamal.udacitybakingapp.fragments.StepsFragment;

public class StepsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        StepsFragment stepsFragment= new StepsFragment();
        stepsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.steps_fragment, stepsFragment)
                .commit();

    }

    public void next(){

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
