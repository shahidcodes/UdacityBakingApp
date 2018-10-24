package ml.shahidkamal.udacitybakingapp.utils;

import com.google.gson.Gson;

import java.util.List;

import ml.shahidkamal.udacitybakingapp.model.Recipe;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RetrofitApi {

    static String BASE_URL = "http://d17h27t6h515a5.cloudfront.net";
    static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
