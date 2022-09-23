package com.devdaniel.marvelapp.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

class RecyclerViewItemCountAssertion(private val count: Int) : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        if (view !is RecyclerView) {
            throw IllegalStateException("The asserted view is not RecyclerView")
        }

        if (view.adapter == null) {
            throw IllegalStateException("No adapter is assigned to RecyclerView")
        }

        assertThat(
            "RecyclerView item count",
            view.adapter?.itemCount,
            equalTo(count)
        )
    }
}
