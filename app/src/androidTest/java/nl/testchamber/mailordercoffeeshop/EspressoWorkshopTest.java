package nl.testchamber.mailordercoffeeshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import nl.testchamber.mailordercoffeeshop.model.TextMessage;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

public class EspressoWorkshopTest extends BaseSteps {

    @Rule
    public IntentsTestRule<MainActivity> activityTestRule = new IntentsTestRule<MainActivity>(MainActivity.class) {
        @Override
        public void beforeActivityLaunched() {
            super.beforeActivityLaunched();

            Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

            // Solution one
            SharedPreferences.Editor editor = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE).edit();
            editor.putBoolean("is_first_launch", false);
            editor.commit();

            // Alternate Solution: Using the code from the app itself
//            SharedPreferencesUtil.INSTANCE.setIsFirstLaunchToFalse(context);
        }
    };

    // For the 'Plus button' I used a hardcoded string. But if a developer has added the text
    // to the Android resources, then it's also available using R.string.*
    // but it's still possible to use "Review order" instead of R.string.review_order_button
    @Test
    public void orderOverViewShouldDisplayIngredients() {
        onView(withText("+")).perform(click(), click());
        onView(withId(R.id.chocolate)).perform(click());
        onView(withText(R.string.review_order_button)).perform(click());
        onView(withId(R.id.beverage_detail_ingredients)).check(matches(withText("Ingredients:\n2 shots of espresso\nChocolate")));
    }

    @Test
    public void shouldBeAbleToSelectAnItemInTheRecyclerView() {
        onView(withId(R.id.use_menu)).perform(click());
        onView(withId(R.id.beverage_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("CON PANNA")), click()));
        onView(withId(R.id.beverage_detail_title)).check(matches(withText("Con Panna")));
    }

    @Test
    public void shouldSendAnIntentContainingTheRightOrderName() {
        onView(withText("+")).perform(click(), click());
        onView(withId(R.id.chocolate)).perform(click());
        onView(withText(R.string.review_order_button)).perform(click());
        onView(withId(R.id.name_text_box)).perform(scrollTo(), typeText("My name"));
        onView(withId(R.id.custom_order_name_box)).perform(scrollTo(), typeText("Custom Order Name"));
        onView(withId(R.id.mail_order_button)).perform(scrollTo(), click());

        intended(allOf(
                hasAction(equalTo(Intent.ACTION_SENDTO)),
                hasExtra(Intent.EXTRA_SUBJECT, "Order: My name - Custom Order Name")));
    }

    @Test
    public void orderEspressoWithMilk() {
        int fatMilk = (int)(Math.random() * 12) ;

        onView(withId(R.id.milk_type)).perform(click());
        onView(withText("Custom %")).perform(click());
        onView(withId(R.id.simpleSeekBar)).perform(setProgress(fatMilk));
        onView(withText(R.string.review_order_button)).perform(scrollTo(), click());
        onView(withText(TextMessage.MINIMUM_ESPRESSO_FOR_ORDER))
                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        onView(withText("+")).perform(scrollTo(), click());
        onView(withText(R.string.review_order_button)).perform(scrollTo(), click());

        Assert.assertEquals("The ingredients are not specified correctly",
                "Ingredients:\n1 shot of espresso\n "+ fatMilk +"% fat milk", getText(withId(R.id.beverage_detail_ingredients)));
    }

    @Test
    public void orderEspressoWithCottageCheese() {
        int fatMilk = 13 + (int)(Math.random() * 17) ;

        onView(withId(R.id.milk_type)).perform(click());
        onView(withText("Custom %")).perform(click());
        onView(withId(R.id.simpleSeekBar)).perform(setProgress(fatMilk));
        onView(withText("+")).perform(scrollTo(), click());
        onView(withId(R.id.beverage_temperature)).perform(click()).check(matches(isDisplayed()));
        onView(withText(R.string.review_order_button)).perform(scrollTo(), click());

        Assert.assertEquals("The ingredients are not specified correctly",
                "Ingredients:\n1 shot of cold espresso\n Cottage Cheese", getText(withId(R.id.beverage_detail_ingredients)));
    }

    @Test
    public void orderManyShotsEspressoWithCheese() {
        int fatMilk = 30 + (int)(Math.random() * 10) ;
        int numberShot = 6;

        onView(withId(R.id.milk_type)).perform(click());
        onView(withText("Custom %")).perform(click());
        onView(withId(R.id.simpleSeekBar)).perform(setProgress(fatMilk));
        onView(withText(R.string.review_order_button)).perform(scrollTo(), click());
        clicker(onView(withText("+")), numberShot);
        onView(withText(R.string.review_order_button)).perform(scrollTo(), click());

        Assert.assertEquals("The ingredients are not specified correctly",
                "Ingredients:\n" + numberShot + " shots of espresso\n Cheese", getText(withId(R.id.beverage_detail_ingredients)));
        Assert.assertEquals("The volume of the drink if non specified correctly",
                numberShot*30 + 30 + " ml.", getText(withId(R.id.beverage_detail_volume_text)));
    }

    @Test
    public void orderEspressoWithWhipped() {
        onView(withText("+")).perform(click());
        onView(withId(R.id.milk_type)).perform(click());
        onView(withText("Soy")).perform(click());
        onView(withId(R.id.Whipped)).perform(scrollTo(), click());
        onView(withText(R.string.review_order_button)).perform(scrollTo(), click());

        Assert.assertEquals("The ingredients are not specified correctly",
                "Ingredients:\n1 shot of espresso\nWhipped Soy", getText(withId(R.id.beverage_detail_ingredients)));

    }

    @Test
    public void checkLessThanZeroEspresso() {
        onView(withText("-")).perform(click());
        onView(withText(TextMessage.LESS_ZERO_ESPRESSO))
                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

}
