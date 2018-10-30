package ml.shahidkamal.udacitybakingapp.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.shahidkamal.udacitybakingapp.MainActivity;
import ml.shahidkamal.udacitybakingapp.R;
import ml.shahidkamal.udacitybakingapp.adapter.RecipeListAdapter;
import ml.shahidkamal.udacitybakingapp.model.Recipe;
import ml.shahidkamal.udacitybakingapp.utils.IAPI;
import ml.shahidkamal.udacitybakingapp.utils.RetrofitApi;
import ml.shahidkamal.udacitybakingapp.utils.SimpleIdlingResource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment {
    private static final String TAG = "RecipeListFragment";
    @BindView(R.id.rv_recipe_list)
    RecyclerView recyclerView;
    SimpleIdlingResource idlingResource;
    RecipeListAdapter adapter;
    Boolean mTwoPane;
    ProgressDialog progressDialog;

    public RecipeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, view);
        progressDialog= new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading please wait...");

        idlingResource = (SimpleIdlingResource) ((MainActivity) getActivity()).getIdlingResource();
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        if (!isNetworkConnected()) {
            Snackbar.make(view, getActivity().getString(R.string.network_error), Snackbar.LENGTH_LONG).show();
        }else{
            progressDialog.show();
            getRecipes();
        }
        return view;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void getRecipes(){
        IAPI api = RetrofitApi.getRetrofit().create(IAPI.class);
        Call<List<Recipe>> call = api.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();
                adapter = new RecipeListAdapter(getContext(), recipes);
                mTwoPane = MainActivity.getNoPane();
                if(mTwoPane){
                    Log.d(TAG, "LayoutSize: GridLayout");
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                    recyclerView.setLayoutManager(gridLayoutManager);
                }else {
                    Log.d(TAG, "LayoutSize: LinearLayout");
                    LinearLayoutManager linearLayoutManager = new
                            LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                }
                recyclerView.setAdapter(adapter);
                idlingResource.setIdleState(true);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

}
