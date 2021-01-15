package com.example.flowmvvm.utils

import android.app.Activity
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.transition.Transition
import android.view.Gravity
import android.view.View
import androidx.core.util.Pair
import java.util.*

/**
 * Helper class for creating content transitions used with [android.app.ActivityOptions].
 */
internal object TransitionHelper {

    /**
     * Create the transition participants required during a activity transition while
     * avoiding glitches with the system UI.
     *
     * @param activity The activity used as start for the transition.
     * @param includeStatusBar If false, the status bar will not be added as the transition
     * participant.
     * @return All transition participants.
     */
    fun createSafeTransitionParticipants(
        activity: Activity,
        includeStatusBar: Boolean,
        vararg otherParticipants: Pair<View, String>
    ): Array<Pair<View, String>> {

        // Avoid system UI glitches as described here:
        // https://plus.google.com/+AlexLockwood/posts/RPtwZ5nNebb
        val decor = activity.window.decorView

        var statusBar: View? = null
        if (includeStatusBar) {
            statusBar = decor.findViewById(android.R.id.statusBarBackground)
        }
        val navBar = decor.findViewById<View>(android.R.id.navigationBarBackground)

        // Create pair of transition participants.
        val participants = ArrayList<Pair<View, String>>(3)

        addNonNullViewToTransitionParticipants(view = statusBar, participants = participants)

        addNonNullViewToTransitionParticipants(view = navBar, participants = participants)

        // only add transition participants if there's at least one none-null element
        participants.addAll(listOf(*otherParticipants))

        return participants.toTypedArray()
    }

    /**
     * Create the transition activity
     * @param type is TransitionType
     * @param duration is Time Animation
     * @return A transition.
     */
    fun create(type: TransitionType, duration: Long = Constant.DURATION.D_200): Transition {
        val transition = when (type) {
            TransitionType.EXPLODE -> Explode()
            TransitionType.FADE -> Fade()
            TransitionType.SLIDE -> Slide()
            TransitionType.SLIDE_LEFT -> Slide().apply { slideEdge = Gravity.END }
            TransitionType.SLIDE_RIGHT -> Slide().apply { slideEdge = Gravity.START }
            TransitionType.SLIDE_TOP_DOWN -> Slide().apply { slideEdge = Gravity.TOP }
            TransitionType.SLIDE_BOTTOM_UP -> Slide().apply { slideEdge = Gravity.BOTTOM }
        }
        transition.duration = duration
        return transition
    }

    private fun addNonNullViewToTransitionParticipants(
        view: View?,
        participants: MutableList<Pair<View, String>>
    ) {
        if (view == null) {
            return
        }
        participants.add(Pair(view, view.transitionName))
    }

}

enum class TransitionType {
    EXPLODE,
    FADE,
    SLIDE,
    SLIDE_LEFT,
    SLIDE_RIGHT,
    SLIDE_TOP_DOWN,
    SLIDE_BOTTOM_UP
}
