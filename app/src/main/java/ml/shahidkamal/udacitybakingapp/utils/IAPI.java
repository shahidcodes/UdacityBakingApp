package ml.shahidkamal.udacitybakingapp.utils;

import java.util.List;

import ml.shahidkamal.udacitybakingapp.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IAPI {
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}