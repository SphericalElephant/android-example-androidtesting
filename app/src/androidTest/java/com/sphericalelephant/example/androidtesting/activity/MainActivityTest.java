package com.sphericalelephant.example.androidtesting.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.sphericalelephant.example.androidtesting.R;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Extending {@link ActivityInstrumentationTestCase2} simplifies matters.
 * Way up the hierarchy, ActivityInstrumentationTestCase2 extends TestCase!
 */
// the actual test runner, not the instrumentation test runner.
@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    // could be used instead of extending ActivityInstrumentationTestCase2
    // @Rule public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    private static final String TAG = "MainActivityTest";

    private static final String TEST_TEXT = "this is some test text";

    private static final String TEST_SHAREDPREFERENCES_TEXT = "this is yet another test";

    private SharedPreferences sp;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    /**
     * Called once for the entire test suite (this class)
     */
    @BeforeClass
    public static void setUpGlobal() {
        Log.e(TAG, "setUpGlobal called");
    }

    /**
     * Called once for the entire test suite (this class)
     */
    @AfterClass
    public static void tearDownGlobal() {
        Log.e(TAG, "tearDownGlobal called");
    }

    /**
     * called before every test case
     */
    @Before
    public void before() throws Exception {
        Log.e(TAG, "before called");
        // required for espresso, allows interfacing with android! the instrumentation registry grants
        // access to the instrumentation running the test, the instrumentation runner is set in out build file
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        // we can obtain a Context from our InstrumentationRegistry, make sure to use getTargetContext() if you
        // wish to obtain the Context of the application, getContext() will fail!
        Context context = InstrumentationRegistry.getTargetContext();
        sp = context.getSharedPreferences(MainActivity.SHAREDPREFERENCES, Context.MODE_PRIVATE);

        // we need our activity in every test, so we might as well start it in before(), not that sometimes
        // this behaviour is not exactly desirable!
        getActivity();

    }

    /**
     * called after each test case
     */
    @After
    public void after() throws Exception {
        Log.e(TAG, "after called");

        // clear the shared preferences before each test to make sure that test cases are not tainted!
        sp.edit().clear().apply();
    }

    public void testThisDoesNotRun() {
        // this test case is not run, since we did not annotate it with @Test, prefixing does not work any more and is not required!
    }

    @Ignore
    @Test
    public void thisTestIsIgnored() {
        assertEquals(true, false); // unless we are working in a this would most certainly fail
    }

    @Test
    public void testButtonDisabled() {
        // check if a button is disabled
        onView(withId(R.id.activity_mainactivity_b_disabledbutton)).check(matches(not(isEnabled())));
    }

    @Test
    public void testTypeText() {
        // typing text
        onView(withId(R.id.activity_mainactivity_et_input1)).perform(typeText(TEST_TEXT));

        // checking if the text we typed is actually displayed
        onView(withId(R.id.activity_mainactivity_et_input1)).check(matches(withText(TEST_TEXT)));
    }

    @Test
    public void testTextViewOnClickListener() {

        // we are not using the TextViews id to find it this time, but its text, we are now performing an onclick event that
        // should change the displayed text
        onView(withText(R.string.activity_mainactivity_thisisatest)).perform(click());

        // checks if the text changed after the onclick event
        onView(withId(R.id.activity_mainactivity_tv_textview)).check(matches(withText(R.string.activity_mainactivity_textviewclicked)));
    }

    @Test
    public void thisTestFails() {
        // trying to query a view by a non unique criteria yields an error if there are multiple views
        // matching the criteria!
        onView(withText(R.string.activity_mainactivity_sametext)).check(matches(isEnabled()));
    }

    @Test
    public void saveToSharedPreferencesTest() {
        // typing the text that will be saved to the shared preferences
        onView(withId(R.id.activity_mainactivity_et_input1)).perform(typeText(TEST_SHAREDPREFERENCES_TEXT));

        // clicking the button to save the text
        onView(withId(R.id.activity_mainactivity_b_normalbutton)).perform(click());

        // check if everything was saved correctly
        assertEquals(TEST_SHAREDPREFERENCES_TEXT, sp.getString(MainActivity.SHARED_PREFERENCES_KEY, null));
    }
}
