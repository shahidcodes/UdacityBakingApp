package ml.shahidkamal.udacitybakingapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ml.shahidkamal.udacitybakingapp.R;
import ml.shahidkamal.udacitybakingapp.model.Recipe;
import ml.shahidkamal.udacitybakingapp.utils.IAPI;
import ml.shahidkamal.udacitybakingapp.utils.RetrofitApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment {
    private static final String TAG = "RecipeListFragment";

    public RecipeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        IAPI api = RetrofitApi.getRetrofit().create(IAPI.class);
        Call<List<Recipe>> call = api.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();
                Log.d(TAG, "onResponse: "+ recipes.size());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
        return inflater.inflate(R.layout.fragment_recipe_list, container, false);
    }

}
