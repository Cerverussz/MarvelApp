package com.devdaniel.marvelapp.util

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class RecyclerViewMatcher(val recyclerViewId: Int) {

    fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            var resources: Resources? = null
            var childView: View? = null

            override fun describeTo(description: Description?) {
                var idDescription: String? = recyclerViewId.toString()
                if (this.resources != null) {
                    idDescription = try {
                        this.resources?.getResourceName(recyclerViewId)
                    } catch (e: Exception) {
                        String.format(
                            "%s (resource name not found)",
                            Integer.valueOf(recyclerViewId)
                        )
                    }
                }

                description?.appendText("with id: $idDescription")
            }

            override fun matchesSafely(item: View?): Boolean {
                this.resources = item?.resources

                if (childView == null) {
                    val recyclerView: RecyclerView? = item?.rootView?.findViewById(recyclerViewId)

                    if (recyclerView != null && recyclerView.id == recyclerViewId) {
                        childView =
                            recyclerView.findViewHolderForAdapterPosition(position)?.itemView
                    } else {
                        return false
                    }
                }

                return if (targetViewId == -1) {
                    item == childView
                } else {
                    val targetView: View? = childView?.findViewById(targetViewId)
                    item == targetView
                }
            }
        }
    }
}
