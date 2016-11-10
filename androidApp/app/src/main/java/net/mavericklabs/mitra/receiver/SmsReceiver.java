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

package net.mavericklabs.mitra.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import net.mavericklabs.mitra.utils.StringUtils;
import net.mavericklabs.mitra.ui.activity.PhoneVerificationActivity;

/**
 * Created by root on 3/11/16.
 */

public class SmsReceiver extends BroadcastReceiver {
    private final int VERIFICATION_CODE_LENGTH = 6;


    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pduObjects = (Object[]) bundle.get("pdus");
                if(pduObjects != null) {
                    for (Object pdu : pduObjects) {
                        SmsMessage currentMessage;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            String format = (String) bundle.get("format");
                            currentMessage = SmsMessage.createFromPdu((byte[]) pdu, format);
                        } else {
                            currentMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        }
                        String senderAddress = currentMessage.getDisplayOriginatingAddress();
                        String message = currentMessage.getDisplayMessageBody();
                        if (!StringUtils.isEmpty(message) && isFromHindSites(message)) {
                            String verificationCode = getVerificationCode(message);
                            PhoneVerificationActivity.redirectTo(context,verificationCode);
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isFromHindSites(String message) {
        return message.toLowerCase().contains("hindsites")
                && message.toLowerCase().contains("verification");
    }

    private String getVerificationCode(String message) {
        String trimmedMessage = message.trim();
        return trimmedMessage.substring(trimmedMessage.length() - VERIFICATION_CODE_LENGTH);
    }
}
