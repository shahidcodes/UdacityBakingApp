package ml.shahidkamal.udacitybakingapp;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.contrib.RecyclerViewActions;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityRecipeListTest {
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
    public void checkIfRecipeListLoads(){
        onView(ViewMatchers.withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText(RECIPE_ITEM)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResources(){
        if(idlingResource != null){
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

}
