package ml.shahidkamal.udacitybakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.google.gson.Gson;

import java.util.List;

import ml.shahidkamal.udacitybakingapp.R;
import ml.shahidkamal.udacitybakingapp.model.Ingredients;
import ml.shahidkamal.udacitybakingapp.model.Recipe;
import ml.shahidkamal.udacitybakingapp.utils.Constants;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    SharedPreferences sharedPreferences;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {

        StringBuilder bldr = new StringBuilder();
        bldr.append(recipe.getName()).append("\n");
        for (Ingredients ingredient: recipe.getIngredients()){
            bldr.append("\u2022 ")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getMeasure())
                    .append(" ")
                    .append(ingredient.getIngredient())
                    .append("\n");
        }

        String widgetText = bldr.toString();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        sharedPreferences = context.getSharedPreferences(Constants.KEY_SHARED_PREF,
                Context.MODE_PRIVATE);
        String recipeJSON = sharedPreferences.getString(Constants.PREF_KEY_RECIPE, null);
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(recipeJSON, Recipe.class);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

