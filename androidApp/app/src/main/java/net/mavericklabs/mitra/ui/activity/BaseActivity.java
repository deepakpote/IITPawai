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

package net.mavericklabs.mitra.ui.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.mavericklabs.mitra.utils.LanguageUtils;
import net.mavericklabs.mitra.utils.MitraContextWrapper;

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = MitraContextWrapper.wrap(newBase, LanguageUtils.getCurrentLocale());
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
