package nl.testchamber.mailordercoffeeshop;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;

public class BaseSteps {

    public static void clicker(ViewInteraction view, int amount) {
        view.perform(scrollTo());
        for (int i = 0 ; i < amount; i++){
            view.perform(click());
        }
    }

    public static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress(progress);
            }
            @Override
            public String getDescription() {
                return "Set a progress on a SeekBar";
            }
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(SeekBar.class);
            }
        };
    }

   String getText(final Matcher<View> matcher) {
       final String[] stringHolder = { null };
       onView(matcher).perform(new ViewAction() {
           @Override
           public Matcher<View> getConstraints() {
               return isAssignableFrom(TextView.class);
           }

           @Override
           public String getDescription() {
               return "getting text from a TextView";
           }

           @Override
           public void perform(UiController uiController, View view) {
               TextView tv = (TextView)view; //Save, because of check in getConstraints()
               stringHolder[0] = tv.getText().toString();
           }
       });
       return stringHolder[0];
   }
}
