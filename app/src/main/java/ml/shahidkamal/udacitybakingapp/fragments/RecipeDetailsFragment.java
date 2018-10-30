package ml.shahidkamal.udacitybakingapp.fragments;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.shahidkamal.udacitybakingapp.R;
import ml.shahidkamal.udacitybakingapp.adapter.StepsListAdapter;
import ml.shahidkamal.udacitybakingapp.model.Ingredients;
import ml.shahidkamal.udacitybakingapp.model.Steps;
import ml.shahidkamal.udacitybakingapp.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailsFragment extends Fragment {

    @BindView(R.id.tv_ingredient_list)
    TextView tvIngredients;
    @BindView(R.id.rv_steps)
    RecyclerView recyclerViewSteps;

    List<Steps> stepsList;
    List<Ingredients> ingredientsList;
    Boolean mTwoPane;
    LinearLayoutManager linearLayoutManager;

    Parcelable mListState;


    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        Bundle bundle = getArguments();
        stepsList = gson.fromJson(
                bundle.getString(Constants.INTENT_KEY_STEPS),
                new TypeToken<List<Steps>>(){}.getType()
        );

        ingredientsList = gson.fromJson(
                bundle.getString(Constants.INTENT_KEY_INGREDIENTS),
                new TypeToken<List<Ingredients>>(){}.getType()
        );

        mTwoPane = bundle.getBoolean(Constants.INTENT_KEY_PANE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ButterKnife.bind(this, view);

        StringBuilder builder = new StringBuilder();
        for(Ingredients ingredient: ingredientsList){
            builder.append("\u2022 ")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getMeasure())
                    .append(" ")
                    .append(ingredient.getIngredient())
                    .append("\n");
        }
        tvIngredients.setText(builder.toString());

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewSteps.setLayoutManager(linearLayoutManager);
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(Constants.RECYCLER_VIEW_STATE);
        }

        StepsListAdapter stepsListAdapter = new StepsListAdapter(getActivity(), stepsList);
        recyclerViewSteps.setAdapter(stepsListAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListState != null) {
            //Restoring recycler view state
            linearLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //storing recycler view state
        outState.putParcelable(Constants.RECYCLER_VIEW_STATE, linearLayoutManager.onSaveInstanceState());
    }
}
