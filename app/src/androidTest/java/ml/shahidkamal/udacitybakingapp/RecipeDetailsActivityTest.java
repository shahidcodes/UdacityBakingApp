package ml.shahidkamal.udacitybakingapp;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.not;

import android.support.test.espresso.contrib.RecyclerViewActions;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityTest {
    private String RECIPE_ITEM = "Brownies\nServings: 8";
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);
    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResources(){
        idlingResource = mainActivityActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void clickRecipeItem_LaunchesActivity(){
        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withText(RECIPE_ITEM)), click()
        ));

        Context targetContext = InstrumentationRegistry.getTargetContext();
        targetContext.getResources().getBoolean(R.bool.tab);
        Boolean isTabletUsed = targetContext.getResources().getBoolean(R.bool.tab);
        if (!isTabletUsed) {
            onView(withId(R.id.steps_fragment)).check(doesNotExist());
        }

        if (isTabletUsed) {
            //To ensure that video fragment is present and master flow is correctly implemented
            onView(withId(R.id.recipe_details_fragment)).check(matches(isDisplayed()));
        }
    }

    @After
    public void unregisterIdlingResources(){
        if(idlingResource != null){
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

}
