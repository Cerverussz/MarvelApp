package com.devdaniel.marvelapp.util

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.google.android.material.R
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.instanceOf

fun checkViewIsDisplayedByText(viewText: String) {
    onView(withText(viewText)).check(matches(isDisplayed()))
}

fun performClickInViewPositionRecyclerView(
    @IdRes viewId: Int,
    @IdRes childViewId: Int,
    position: Int
) {
    onView(withId(viewId)).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
            position,
            clickOnViewChild(childViewId)
        )
    )
}

fun checkViewWithIdAndPartialTextIsDisplayed(@IdRes viewId: Int, viewText: String) {
    onView(withId(viewId))
        .check(matches(withText(containsString(viewText))))
        .check(matches(isDisplayed()))
}

fun checkCustomViewWithIdAndTextIsDisplayedWithParentId(
    @IdRes parentViewId: Int,
    @IdRes viewId: Int,
    viewText: String
) {
    onView(
        allOf(
            withId(viewId),
            isDescendantOfA(withId(parentViewId))
        )
    ).check(matches(withText(viewText)))
}

fun checkViewInRecyclerWithIdAndTextIsDisplayed(
    @IdRes viewId: Int,
    viewText: String,
    position: Int
) {
    onView(withId(viewId)).check(
        matches(
            recyclerItemAtPosition(
                position,
                hasDescendant(
                    withText(viewText)
                )
            )
        )
    )
}

fun checkViewWithIdAndTextIsDisplayed(@IdRes viewId: Int, viewText: String) {
    onView(withId(viewId))
        .check(matches(withText(viewText)))
        .check(matches(isDisplayed()))
}

fun checkViewWithIdAndTextIsDisplayed(@IdRes viewId: Int, @StringRes viewText: Int) {
    onView(withId(viewId))
        .check(matches(withText(viewText)))
        .check(matches(isDisplayed()))
}

fun performClickOnBackButtonInToolbar(@IdRes viewId: Int) {
    onView(
        allOf(
            instanceOf(AppCompatImageButton::class.java),
            withParent(withId(viewId))
        )
    ).perform(click())
}

fun checkSnackBarIsDisplayed(withText: String): ViewInteraction {
    return onView(
        allOf(
            withId(R.id.snackbar_text),
            withText(withText)
        )
    )
}
