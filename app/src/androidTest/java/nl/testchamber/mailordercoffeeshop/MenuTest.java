package nl.testchamber.mailordercoffeeshop;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import nl.testchamber.mailordercoffeeshop.model.MenuNames;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class MenuTest extends BaseSteps {
    @Rule
    public IntentsTestRule<MainActivity> activityTestRule = new IntentsTestRule<MainActivity>(MainActivity.class) {
        @Override
        public void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            SharedPreferences.Editor editor = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE).edit();
            editor.putBoolean("is_first_launch", false);
            editor.commit();
        }

        @Test
        public void orderEspressoWithoutName() {
            onView(withId(R.id.use_menu)).perform(click());
            onView(withId(R.id.beverage_recycler_view))
                    .perform(actionOnItem(hasDescendant(withText(MenuNames.ESPRESSO)), click()));
            onView(withId(R.id.name_text_box)).perform(scrollTo(), typeText(""));
            onView(withId(R.id.mail_order_button)).perform(click());
            onView(withText("Enter your name please")).check(matches(isDisplayed()));
        }

        @Test
        public void orderCappuccinoWithIncorrectEmail() {
            onView(withId(R.id.use_menu)).perform(click());
            onView(withId(R.id.beverage_recycler_view))
                    .perform(actionOnItem(hasDescendant(withText(MenuNames.CAPPUCCINO)), click()));
            onView(withId(R.id.name_text_box)).perform(scrollTo(), typeText("My name"));
            onView(withId(R.id.email_text_box)).perform(scrollTo(), typeText(UUID.randomUUID().toString()));
            onView(withId(R.id.mail_order_button)).perform(scrollTo(),click());
            onView(withText("E-mail address is invalid")).check(matches(isDisplayed()));
        }
    };
}
