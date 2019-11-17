package com.casper.testdrivendevelopment;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.casper.testdrivendevelopment.data.BookSaver;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BookListMainActivityTest2 {

    @Rule
    public ActivityTestRule<BookListMainActivity> mActivityTestRule = new ActivityTestRule<>(BookListMainActivity.class);
    private BookSaver bookKeeper;
    private Context context;
    @Before
    public void setUp() throws Exception {
        context= InstrumentationRegistry.getTargetContext();
        bookKeeper=new BookSaver(context);
        bookKeeper.load();//测试前读取数据

    }

    @After
    public void tearDown() throws Exception {
        bookKeeper.save();//测试号保存数据
    }
    @Test
    public void bookListMainActivityTest2() {
        ViewInteraction linearLayout=onView(
                allOf(childAtPosition(
                        withId(R.id.list_view_books),
                        1),
                        isDisplayed()));
        linearLayout.perform(longClick());
        ViewInteraction textView = onView(
                allOf(withId(android.R.id.title), withText("更新"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.android.internal.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed()));
        textView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edit_text_book_title),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2=onView(
                allOf(withId(R.id.edit_text_book_title),isDisplayed()));
        appCompatEditText.perform(replaceText("创新工程实践1"),closeSoftKeyboard());

        ViewInteraction appCompatEditText3=onView(
                allOf(withId(R.id.edit_text_book_title),withText("创新工程实践1"),isDisplayed()));
        appCompatEditText3.perform(click());



        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.edit_text_book_price),
                        isDisplayed()));
        appCompatEditText4.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.edit_text_book_price),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("1.01"),closeSoftKeyboard());

        ViewInteraction appCompatEditText6=onView(
                allOf(withId(R.id.edit_text_book_price),withText("1.01"),isDisplayed()));
        appCompatEditText6.perform(click());


        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button_ok), withText("确定"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.text_view_book_title), withText("创新工程实践1"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list_view_books),
                                        1),
                                1),
                        isDisplayed()));


        ViewInteraction textView3 = onView(
                allOf(withId(R.id.text_view_book_price), withText("1.01"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list_view_books),
                                        1),
                                2),
                        isDisplayed()));


        ViewInteraction imageView = onView(
                allOf(withId(R.id.image_view_book_cover),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list_view_books),
                                        1),
                                0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction imageView2 = onView(
                allOf(withId(R.id.image_view_book_cover),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list_view_books),
                                        1),
                                0),
                        isDisplayed()));
        imageView2.check(matches(isDisplayed()));
        mActivityTestRule.finishActivity();
        try {
            Thread.sleep(700);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
