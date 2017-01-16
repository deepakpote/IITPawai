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

import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import net.mavericklabs.mitra.R;

/**
 * Created by vishakha on 10/11/16.
 */

public class DisplayUtils {
    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static void displayFileIcon(Integer fileType, ImageView fileIconImageView) {
        switch (fileType) {
            case Constants.FileTypeVideo :
                fileIconImageView.setImageResource(R.drawable.ic_play_arrow_black_48dp);
                break;

            case Constants.FileTypeAudio:
                fileIconImageView.setImageResource(R.drawable.ic_audiotrack_black_48dp);
                break;

            case Constants.FileTypePPT:
                fileIconImageView.setImageResource(R.drawable.ic_slideshow_black_48dp);
                break;

            case Constants.FileTypeWorksheet:
                fileIconImageView.setImageResource(R.drawable.ic_picture_as_pdf_black_48dp);
                break;

            case Constants.FileTypePDF:
                fileIconImageView.setImageResource(R.drawable.ic_picture_as_pdf_black_48dp);
                break;

            default:
                fileIconImageView.setImageResource(R.drawable.ic_toys_black_48dp);
                break;

        }
    }
}
