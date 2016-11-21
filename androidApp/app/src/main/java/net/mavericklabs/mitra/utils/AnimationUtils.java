/*
 *
 *  * ************************************************************************
 *  *
 *  *  MAVERICK LABS CONFIDENTIAL
 *  *  __________________
 *  *
 *  *   [2015] Maverick Labs
 *  *   All Rights Reserved.
 *  *
 *  *  NOTICE:  All information contained herein is, and remains
 *  *  the property of Maverick Labs and its suppliers,
 *  *  if any.  The intellectual and technical concepts contained
 *  *  herein are proprietary to Maverick Labs
 *  *  and its suppliers and may be covered by U.S. and Foreign Patents,
 *  *  patents in process, and are protected by trade secret or copyright law.
 *  *  Dissemination of this information or reproduction of this material
 *  *  is strictly forbidden unless prior written permission is obtained
 *  *  from Maverick Labs.
 *  * /
 *
 */

package net.mavericklabs.mitra.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

/**
 * Created by vishakha on 11/11/16.
 */

public class AnimationUtils {

    public static void fadeInView(View viewToShow, AnimatorListenerAdapter listenerAdapter) {
        // Set the viewToShow to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.

        viewToShow.setAlpha(0f);
        viewToShow.setVisibility(View.VISIBLE);

        // Animate the viewToShow to 100% opacity
        viewToShow.animate()
                .alpha(1f)
                .setDuration(200)
                .setListener(listenerAdapter);

    }

    public static void fadeOutView(final View viewToHide) {

        // Animate the viewToHide to 0% opacity.
        // After the animation ends, set its visibility to GONE

        viewToHide.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        viewToHide.setVisibility(View.GONE);
                    }
                });

    }

}
