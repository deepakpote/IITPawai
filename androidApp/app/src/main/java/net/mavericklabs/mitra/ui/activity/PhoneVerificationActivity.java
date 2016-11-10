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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.receiver.SmsReceiver;
import net.mavericklabs.mitra.utils.StringUtils;

public class PhoneVerificationActivity extends AppCompatActivity {

    private EditText codeOneEditText;
    private EditText codeTwoEditText;
    private EditText codeThreeEditText;

    private EditText codeFourEditText;
    private EditText codeFiveEditText;
    private EditText codeSixEditText;

    private BroadcastReceiver smsReceiver = new SmsReceiver();

    private int retryCount;
    private final int MAX_RETRY_COUNT = 2;
    private final int TWO_MINUTES = 2*60*1000;
    private AlertDialog automaticVerificationDialog;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, filter);

        if (getIntent().getExtras() != null) {
            phoneNumber = getIntent().getExtras().getString("phoneNumber");
        }

        final String finalPhoneNumber = phoneNumber;

        final View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_phone_verification, null);

        automaticVerificationDialog = new AlertDialog.Builder(this, R.style.StyleDialog)
                .setView(dialogLayout)
                .setNegativeButton("Skip", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //skipVerification();
                    }
                })
                .setPositiveButton(getString(R.string.button_manually), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing. only dismiss
                    }
                })
                .setCancelable(false)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //HindSitesSharedPreferences.saveToPreferences(getApplicationContext(),"showAutoVerificationDialog",Boolean.FALSE);
                    }
                })
                .create();
        boolean dialogToBeShown = true;
                //HindSitesSharedPreferences.readFromPreferences(getApplicationContext(),"showAutoVerificationDialog",Boolean.TRUE);

        if(dialogToBeShown) {
            automaticVerificationDialog.show();
            final Button negativeButton = automaticVerificationDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negativeButton.setVisibility(View.INVISIBLE);
            final ProgressBar progressBar = (ProgressBar) dialogLayout.findViewById(R.id.progressBar);
            final TextView timeLeft = (TextView) dialogLayout.findViewById(R.id.timeTextView);

            final int totalTime = TWO_MINUTES;
            progressBar.setMax(totalTime / 1000);
            new CountDownTimer(totalTime, 1000) {

                public void onTick(long millisUntilFinished) {
                    progressBar.setProgress((int) (totalTime - millisUntilFinished) / 1000);
                    timeLeft.setText("" + millisUntilFinished / 1000);
                    if(((totalTime*2)/3) > millisUntilFinished) {
                        //show the skip verification button
                        negativeButton.setVisibility(View.VISIBLE);
                    }
                }

                public void onFinish() {
                    progressBar.setProgress(totalTime / 1000);
                    timeLeft.setText("0");
                    if(automaticVerificationDialog.isShowing()) {
                        automaticVerificationDialog.dismiss();
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.toast_automatic_detect),
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }.start();

        }

        EditText phoneNumberEditText = (EditText) findViewById(R.id.phone_number_edit_text);
        if (phoneNumberEditText != null) {
            phoneNumberEditText.setText(phoneNumber);
        }
        phoneNumberEditText.setKeyListener(null);

        ImageView editPhoneNumber = (ImageView) findViewById(R.id.editPhoneNumber);

        codeOneEditText = (EditText) findViewById(R.id.verificationCode1);
        codeTwoEditText = (EditText) findViewById(R.id.verificationCode2);
        codeThreeEditText = (EditText) findViewById(R.id.verificationCode3);

        codeFourEditText = (EditText) findViewById(R.id.verificationCode4);
        codeFiveEditText = (EditText) findViewById(R.id.verificationCode5);
        codeSixEditText = (EditText) findViewById(R.id.verificationCode6);

        final TextView resendSmsTextView = (TextView) findViewById(R.id.resendSms);
        final TextView skipVerificationTextView = (TextView) findViewById(R.id.skipVerification);
        addTextChangeListenerToCodes();

        Button button = (Button) findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isIncompleteVerificationCode()){
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.toast_6_digits),Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                    startActivity(intent);
                }
            }
        });

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getString(R.string.text_resend_code));
        CharacterStyle span = new UnderlineSpan();
        spannableStringBuilder.setSpan(span,26,32, 0);
        if (resendSmsTextView != null) {
            resendSmsTextView.setText(spannableStringBuilder);
        }

        if (resendSmsTextView != null) {
            resendSmsTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    retryCount++;
                    if (retryCount == MAX_RETRY_COUNT) {
                        //skipVerification();
                    } else {

                        //new ResendVerificationCode(getApplicationContext(), finalPhoneNumber, PhoneVerificationActivity.this).execute();

                    }
                }
            });
        }

        if (skipVerificationTextView != null) {
            skipVerificationTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //skipVerification();
                }
            });
        }

        phoneNumberEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_edit_phone_number,null);
                final EditText phoneNumberEditText = (EditText) dialogLayout.findViewById(R.id.phone_number);
                final EditText isdCodeEditText = (EditText) dialogLayout.findViewById(R.id.isd_code);
                isdCodeEditText.setText("");
                phoneNumberEditText.setHint(getString(R.string.phone_hint));
                phoneNumberEditText.setText(finalPhoneNumber.substring(finalPhoneNumber.length() - 10,
                        finalPhoneNumber.length()));
                AlertDialog alertDialog = new AlertDialog.Builder(PhoneVerificationActivity.this)
                        .setTitle(getString(R.string.title_edit_phone))
                        .setView(dialogLayout)
                        .setPositiveButton(getString(R.string.button_send_code), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(StringUtils.isEmpty(isdCodeEditText.getText().toString())){
                                    Toast.makeText(getApplicationContext(),getString(R.string.toast_isd_code), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(StringUtils.isEmpty(phoneNumberEditText.getText().toString())
                                        || phoneNumberEditText.getText().toString().length() < 8 ){
                                    Toast.makeText(getApplicationContext(),getString(R.string.toast_valid_phone_number),Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String newPhoneNumber = isdCodeEditText.getText().toString()
                                        + phoneNumberEditText.getText().toString();
                                //dataSource.updateUserPhoneNumber(newPhoneNumber,dataSource.getMyEmail());
                                //new ResendVerificationCode(getApplicationContext(), newPhoneNumber
                                //        , PhoneVerificationActivity.this).execute();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //do nothing
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
    }

    public static void redirectTo(Context context,String verificationCode){

        //new SubmitVerificationCode(context, verificationCode, false).execute();

    }

    @Override
    public void onBackPressed() {
        //do nothing.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(automaticVerificationDialog.isShowing()) {
            automaticVerificationDialog.dismiss();
        }
    }


    private String getVerificationCode() {
        return codeOneEditText.getText().toString()
                + codeTwoEditText.getText().toString()
                + codeThreeEditText.getText().toString()
                + codeFourEditText.getText().toString()
                + codeFiveEditText.getText().toString()
                + codeSixEditText.getText().toString();

    }

    private boolean isIncompleteVerificationCode() {
        return StringUtils.isEmpty(codeOneEditText.getText().toString())
                || StringUtils.isEmpty(codeTwoEditText.getText().toString())
                || StringUtils.isEmpty(codeThreeEditText.getText().toString())
                || StringUtils.isEmpty(codeFourEditText.getText().toString())
                || StringUtils.isEmpty(codeFiveEditText.getText().toString())
                || StringUtils.isEmpty(codeSixEditText.getText().toString());
    }

    private void addTextChangeListenerToCodes() {

        //code verification #1
        codeOneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable != null && editable.length() ==1){
                    codeOneEditText.clearFocus();
                    codeTwoEditText.requestFocus();
                }
            }
        });

        codeTwoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable != null && editable.length() ==1){
                    codeTwoEditText.clearFocus();
                    codeThreeEditText.requestFocus();
                }
            }
        });

        codeThreeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable != null && editable.length() ==1){
                    codeThreeEditText.clearFocus();
                    codeFourEditText.requestFocus();
                }
            }
        });

        codeFourEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable != null && editable.length() ==1){
                    codeFourEditText.clearFocus();
                    codeFiveEditText.requestFocus();
                }
            }
        });

        codeFiveEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && editable.length() == 1) {
                    codeFiveEditText.clearFocus();
                    codeSixEditText.requestFocus();
                }
            }
        });

    }

}

