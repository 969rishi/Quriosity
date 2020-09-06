package com.quriosity.quriosity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.quriosity.quriosity.BaseActivity;
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.activity.DashboardMainActivity;
import com.quriosity.quriosity.utils.FirebaseUtil;
import com.quriosity.quriosity.utils.WedAmorApplication;

import java.util.concurrent.TimeUnit;

import static com.quriosity.quriosity.utils.MyUtil.EXTRAS_MOBILE_10_DIGIT;
import static com.quriosity.quriosity.utils.MyUtil.EXTRAS_MOBILE_VERIFICATION;
import static com.quriosity.quriosity.utils.MyUtil.EXTRAS_USER_EMAIL;
import static com.quriosity.quriosity.utils.MyUtil.EXTRAS_USER_FULL_NAME;
import static com.quriosity.quriosity.utils.MyUtil.EXTRAS_USER_PASSWORD;
import static com.quriosity.quriosity.utils.MyUtil.KEY_VERIFY_IN_PROGRESS;

public class VerificationActivity extends BaseActivity {
    private String userMobileNumber = null;
    private String userFullName = null;
    private String userEmail = null;
    private String userPassword = null;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private Toolbar toolbar;
    private LinearLayout relativeLayoutData, verify_linearLayout_button, timerLayout;
    private Button buttonManualOtp;
    private EditText editTextOtp;
    private ImageView imageView;
    private ProgressBar progressBar;
    private boolean mVerificationInProgress = false;
    private TextView statusTextview, timerTextview, resenOtpTextview;
    private CountDownTimer countDownTimer;
    private String user10digitNumber;
    private String registrationToken = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        toolbar = findViewById(R.id.toolbar_signup_activity_layout);
        relativeLayoutData = findViewById(R.id.whole_relativelayout_add_functions_activity);
        verify_linearLayout_button = findViewById(R.id.verify_linearLayout_button);
        buttonManualOtp = findViewById(R.id.submit_otp_code_button);
        editTextOtp = findViewById(R.id.otp_verification_number);
        progressBar = findViewById(R.id.progress_phone_verification_layout);
        imageView = findViewById(R.id.imageview_phone_verification);
        statusTextview = findViewById(R.id.textview_phone_verification);
        timerTextview = findViewById(R.id.timer_textview);
        timerLayout = findViewById(R.id.bottom_otp_counter_layout);
        resenOtpTextview = findViewById(R.id.send_eotp_again);
        initToolbar();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                userMobileNumber = null;
                userFullName = null;
                userEmail = null;
                userPassword = null;
            } else {
                userMobileNumber = extras.getString(EXTRAS_MOBILE_VERIFICATION);
                user10digitNumber = extras.getString(EXTRAS_MOBILE_10_DIGIT);
                userPassword = extras.getString(EXTRAS_USER_PASSWORD);
                userEmail = extras.getString(EXTRAS_USER_EMAIL);
                userFullName = extras.getString(EXTRAS_USER_FULL_NAME);
                Log.d(TAG, "onCreate: mobile code is " + userMobileNumber);
                Log.d(TAG, "onCreate: mobile EXTRASSS " + extras.getString(EXTRAS_MOBILE_VERIFICATION));
                mVerificationInProgress = true;
            }
        }

        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;
                countDownTimer.cancel();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                mVerificationInProgress = false;
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
                countDownTimer.cancel();
                hideProgress();
                Toast.makeText(VerificationActivity.this, "Verification request failed\n" + e, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                showProcessingLayout();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Log.d(TAG, "onCodeAutoRetrievalTimeOut: " + s);
                showManualOtpLayout();
            }
        };

        onClickEvents();
        start();
    }

    public void start() {
        resenOtpTextview.setClickable(false);
        resenOtpTextview.setTextColor(getResources().getColor(R.color.light_gray));
        timerTextview.setVisibility(View.VISIBLE);
        timerTextview.setText("00:60");
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUnitFinished) {
                timerTextview.setText("Resend in " + millisUnitFinished / 1000 + " sec");
            }

            @Override
            public void onFinish() {
                timerLayout.setVisibility(View.VISIBLE);
                timerTextview.setVisibility(View.GONE);
                resenOtpTextview.setClickable(true);
                resenOtpTextview.setTextColor(getResources().getColor(R.color.lightcoral));
                countDownTimer.cancel();
            }
        };
        countDownTimer.start();
    }

    private void onClickEvents() {
        buttonManualOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextOtp.getText().toString().trim().isEmpty()) {
                    editTextOtp.requestFocus();
                    editTextOtp.setError("Field cannot be empty");
                    return;
                }
                if (editTextOtp.getText().toString().trim().length() < 6) {
                    editTextOtp.requestFocus();
                    editTextOtp.setError("Please enter 6 digit code");
                    return;
                }
                showProcessingLayout();
                verifyPhoneNumberWithCode(mVerificationId, editTextOtp.getText().toString().trim());
                hideProgress();
            }
        });

        resenOtpTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
                resendVerificationCode(userMobileNumber, mResendToken);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mVerificationInProgress) {
            mobileVerification(userMobileNumber);
            mVerificationInProgress = false;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private void mobileVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

        //mVerificationInProgress = true;
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            AuthCredential emailAuthCredential = EmailAuthProvider.getCredential(userEmail, userPassword);
                            linkBothAuthMethod(emailAuthCredential);

                            Log.d(TAG, "signInWithCredential:success by phone method");
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                showManualOtpLayout();
                                editTextOtp.setError("Invalid Code.");
                            }
                        }
                    }
                });
    }

    private void linkBothAuthMethod(AuthCredential emailAuthCredential) {
        mAuth.getCurrentUser().linkWithCredential(emailAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            saveMySharedPrefrence(user);
                            registerUserFirebase(user);
                        } else {
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void saveMySharedPrefrence(FirebaseUser user) {
        ((WedAmorApplication) getApplicationContext()).setfirebaseUid(user.getUid());
        String[] frstlastname = userFullName.split(" ", 2);
        ((WedAmorApplication) getApplicationContext()).setFirstUserName(frstlastname[0]);

        ((WedAmorApplication) getApplicationContext()).setCheckLogin(true);
        ((WedAmorApplication) getApplicationContext()).setPrimaryEmailId(userEmail);

        ((WedAmorApplication) getApplicationContext()).setPhoneOnly10Digit(user10digitNumber);
        ((WedAmorApplication) getApplicationContext()).setPhoneNoWithCountryCode(userMobileNumber);

    }

    private void registerUserFirebase(final FirebaseUser user) {
        showUserVerifiedLayout();
        Log.d(TAG, "registerUserFirebase");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        registrationToken = task.getResult().getToken();
                        ((WedAmorApplication) getApplicationContext()).setFirebaseRegistrationToken(registrationToken);
                        FirebaseUtil.firestoreNewUserEntryUserTable(user.getUid(), userEmail, userMobileNumber, userPassword,
                                user10digitNumber, userFullName, registrationToken);
                    }
                });

        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(getApplicationContext(), DashboardMainActivity.class);
                    i.putExtra("Email", userEmail);
                    i.putExtra("provider", FirebaseUtil.SIGNUP_METHOD_PHONE);
                    i.putExtra("Name", userFullName);
                    i.putExtra("uuid", user.getUid());
                    Log.e("uuidlogin", "" + user.getUid());
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(getApplicationContext(), DashboardMainActivity.class);
                    i.putExtra("Email", userEmail);
                    i.putExtra("provider", FirebaseUtil.SIGNUP_METHOD_PHONE);
                    i.putExtra("Name", userFullName);
                    i.putExtra("uuid", user.getUid());
                    Log.e("uuidlogin", "" + user.getUid());
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        start();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setupActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        findViewById(R.id.base_toolbar_base_textView).setVisibility(View.VISIBLE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showManualOtpLayout() {
        hideProgress();
        timerLayout.setVisibility(View.VISIBLE);
        verify_linearLayout_button.setVisibility(View.VISIBLE);
        editTextOtp.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.otp_img);
        statusTextview.setText(R.string.enter_otp_msg);
    }

    private void showProcessingLayout() {
        showProgress();
        editTextOtp.setVisibility(View.GONE);
        timerLayout.setVisibility(View.VISIBLE);
        verify_linearLayout_button.setVisibility(View.GONE);
        imageView.setImageResource(R.drawable.ic_access_time);
        statusTextview.setText(R.string.verification_in_progress);
    }

    private void showUserVerifiedLayout() {
        showProgress();
        editTextOtp.setVisibility(View.GONE);
        timerLayout.setVisibility(View.GONE);
        verify_linearLayout_button.setVisibility(View.GONE);
        imageView.setImageResource(R.drawable.ic_email_confirmed);
        statusTextview.setText(R.string.phone_verified_msg);
    }

    @Override
    public void onBackPressed() {
        showDialogOnBackPress(VerificationActivity.this, getResources().getString(R.string.cancel_phone_verification_warning));
    }

    private void showDialogOnBackPress(final Activity context, String msg) {
        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(context);
        View mView = View.inflate(context, R.layout.backpress_dialog_layout, null);
        mBuilder.setCancelable(true);
        final Button yesBtn, noBtn;
        final TextView msgTextview;
        msgTextview = mView.findViewById(R.id.textview_util_actiondailog);
        yesBtn = mView.findViewById(R.id.yes_btn_util);
        noBtn = mView.findViewById(R.id.not_btn_util);
        msgTextview.setText(msg);
        mBuilder.setView(mView);
        final android.app.AlertDialog dialog = mBuilder.create();
        dialog.show();

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                VerificationActivity.super.onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                FirebaseAuth.getInstance().signOut();
//                LoginManager.getInstance().logOut();
//                Log.d(TAG, "onDismiss:  dialog.setOnDismissListener");
//            }
//        });
    }
}