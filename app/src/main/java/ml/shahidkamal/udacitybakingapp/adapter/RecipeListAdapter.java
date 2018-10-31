package ml.shahidkamal.udacitybakingapp.adapter;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.shahidkamal.udacitybakingapp.MainActivity;
import ml.shahidkamal.udacitybakingapp.R;
import ml.shahidkamal.udacitybakingapp.RecipeDetailsActivity;
import ml.shahidkamal.udacitybakingapp.model.Recipe;
import ml.shahidkamal.udacitybakingapp.utils.Constants;
import ml.shahidkamal.udacitybakingapp.widget.RecipeWidget;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    List<Recipe> recipes;
    Context context;
    Gson gson;

    public RecipeListAdapter(Context context, List<Recipe> recipes){
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe_list, viewGroup, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int i) {
        final Recipe recipe = recipes.get(i);
        recipeViewHolder.recipeCard.setText(recipe.getCardName());
        if(TextUtils.isEmpty(recipe.getImage())) {
            int drawableInt;
            switch (recipe.getName()) {
                case "Brownies":
                    drawableInt = R.drawable.brownies;
                    break;
                case "Nutella Pie":
                    drawableInt = R.drawable.nutellapie;
                    break;
                case "Yellow Cake":
                    drawableInt = R.drawable.yellowcake;
                    break;
                case "Cheesecake":
                    drawableInt = R.drawable.cheesecake;
                    break;
                default:
                    drawableInt = R.drawable.cheesecake;
                    break;
            }
            recipeViewHolder.recipeThumb.setImageResource(drawableInt);
        }else{
            Picasso.get().load(recipe.getImage()).into(recipeViewHolder.recipeThumb);
        }
        recipeViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gson = new Gson();
                String recipeJson = gson.toJson(recipe);
                updateSharedPrefs(recipeJson);
                updateWidget(recipe);
                String ingredientJson = gson.toJson(recipe.getIngredients());
                String stepsJson = gson.toJson(recipe.getSteps());
                Intent intent = new Intent(context, RecipeDetailsActivity.class);
                intent.putExtra(Constants.INTENT_KEY_INGREDIENTS, ingredientJson);
                intent.putExtra(Constants.INTENT_KEY_STEPS, stepsJson);
                intent.putExtra(Constants.INTENT_KEY_PANE, MainActivity.getNoPane());
                context.startActivity(intent);
            }
        });
    }

    private void updateSharedPrefs(String recipeJson) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.KEY_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.PREF_KEY_RECIPE, recipeJson);
        editor.apply();
    }

    private void updateWidget(Recipe recipe) {
        ComponentName widget = new ComponentName(context, RecipeWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        for (int widgetId: manager.getAppWidgetIds(widget)) {
            RecipeWidget.updateAppWidget(context, manager, widgetId, recipe);
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recipe_name)
        TextView recipeCard;
        @BindView(R.id.iv_recipe_list)
        ImageView recipeThumb;
        @BindView(R.id.card_recipe_list)
        CardView cardView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
