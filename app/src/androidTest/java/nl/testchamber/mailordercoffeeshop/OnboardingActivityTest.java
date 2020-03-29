package nl.testchamber.mailordercoffeeshop;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import nl.testchamber.mailordercoffeeshop.onboarding.OnboardingActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class OnboardingActivityTest {

    private static final String MAIN_TITLE = "Valori Mail Order Coffeeshop";

    @Rule
    public IntentsTestRule<OnboardingActivity> activityTestRule = new IntentsTestRule<>(OnboardingActivity.class);

    @Test
    public void goThroughOnboarding() {
        onView(withId(R.id.go_on_button)).perform(click(), click());
        onView(withId(R.id.done_button)).perform(click());
        onView(withText(MAIN_TITLE)).check(matches(isDisplayed()));
    }

    @Test
    public void skipOnboardingInTheMiddle() {
        onView(withId(R.id.go_on_button)).perform(click(), click());
        onView(withId(R.id.close_button)).perform(click());
        onView(withText(MAIN_TITLE)).check(matches(isDisplayed()));
    }

    @Test
    public void skipOnboarding() {
        onView(withId(R.id.close_button)).perform(click());
        onView(withText(MAIN_TITLE)).check(matches(isDisplayed()));
    }
}
