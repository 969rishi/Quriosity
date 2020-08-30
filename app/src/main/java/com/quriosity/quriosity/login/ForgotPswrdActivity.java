package com.quriosity.quriosity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.utils.FirebaseUtil;

import static com.quriosity.quriosity.utils.FirebaseUtil.MOBILE_10_FIRESTORE;
import static com.quriosity.quriosity.utils.FirebaseUtil.PRIMARYEMAIL_FIRESTORE;
import static com.quriosity.quriosity.utils.FirebaseUtil.USERNAME_FIRESTORE;
import static com.quriosity.quriosity.utils.MyUtil.isValidMobile;
import static com.quriosity.quriosity.utils.MyUtil.validateEml;

public class ForgotPswrdActivity extends AppCompatActivity {
    private static final String TAG = "ForgotPswrdActivity";
    private LinearLayout wholeDataLayout, askAnyIDLayout, sentMailLayout, askAnyIdButtonLayout;
    private ProgressBar progressBar;
    private TextView mailSentTextview, signupTextview;
    private EditText anyIdEditText;
    private Button submitBtn, loginBtn;
    private FirebaseFirestore mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pswrd);
        wholeDataLayout = findViewById(R.id.main_linearlayout_forget_activity);
        askAnyIDLayout = findViewById(R.id.ask_for_id_layout);
        sentMailLayout = findViewById(R.id.mail_sent_activity);
        mailSentTextview = findViewById(R.id.fogot_mail_sent_textview);
        signupTextview = findViewById(R.id.dont_have_account_txtview);
        submitBtn = findViewById(R.id.forgot_btn_loginactivity);
        loginBtn = findViewById(R.id.takeme_loginactivity_frgtpswd);
        anyIdEditText = findViewById(R.id.any_user_id_forgot_layout);
        askAnyIdButtonLayout = findViewById(R.id.ask_for_id_button_layout);
        progressBar = findViewById(R.id.progress_bar_login_activity);
        mDatabase = FirebaseFirestore.getInstance();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                Log.d(TAG, "onCreate: Email EXTRASSS " + extras.getString(Intent.EXTRA_EMAIL));
                if (extras.getString(Intent.EXTRA_EMAIL) != null) {
                    anyIdEditText.setText(extras.getString(Intent.EXTRA_EMAIL));
                }
            }
        }

        signupTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPswrdActivity.this, SignupActivity.class));
                finish();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPswrdActivity.this, LoginActivity.class));
                finish();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anyIdEditText.setError(null);

                if (anyIdEditText.getText().toString().isEmpty()) {
                    anyIdEditText.setError("Field can not be empty");
                    return;
                }
                if (anyIdEditText.getText().toString().length() < 4) {
                    anyIdEditText.setError("Please enter valid username, email or mobile");
                    anyIdEditText.requestFocus();
                    return;
                }
                checkUserName(anyIdEditText.getText().toString().trim());
            }
        });
    }

    private void checkUserName(String userInput) {
        if (validateEml(userInput)) {
            checkUserExistance(userInput, PRIMARYEMAIL_FIRESTORE);
            return;
        }
        if (isValidMobile(userInput)) {
            checkUserExistance(userInput, MOBILE_10_FIRESTORE);
            return;
        }
        checkUserExistance(userInput, USERNAME_FIRESTORE);
    }

    private void checkUserExistance(String userInput, final String searchField) {
        showProgress();
        //fetch user exist in login table or not
        Query docRef = mDatabase.collection(FirebaseUtil.USERS_FIRESTORE)
                .whereEqualTo(searchField, userInput);
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG, "onComplete: login sts " + task.getResult().getDocuments());
                if (!task.getResult().getDocuments().isEmpty()) {
                    SendResetEmail(String.valueOf(task.getResult().getDocuments().get(0).get(PRIMARYEMAIL_FIRESTORE)));
                } else {
                    Log.d(TAG, "onComplete: sorry no data found");
                    Toast.makeText(ForgotPswrdActivity.this, "No data found. Please check", Toast.LENGTH_LONG).show();
                    hideProgress();
                }
            }
        });
    }

    private void SendResetEmail(final String emailid) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(emailid)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPswrdActivity.this, getString(R.string.foget_mail_snt_msg1) + emailid, Toast.LENGTH_LONG).show();
                            ((ImageView) findViewById(R.id.imageviewForgotPaswdActivity)).setImageResource(R.drawable.tick);
                            showEmailSentLayout();
                            mailSentTextview.setText(getString(R.string.foget_mail_snt_msg2));
                            hideProgress();
                        } else {
                            Toast.makeText(ForgotPswrdActivity.this, "No data found. Please check", Toast.LENGTH_LONG).show();
                            hideProgress();
                        }
                    }
                });
    }


    private void showProgress() {
        wholeDataLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        wholeDataLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void showEmailSentLayout() {
        wholeDataLayout.setVisibility(View.VISIBLE);
        sentMailLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        askAnyIDLayout.setVisibility(View.GONE);
        askAnyIdButtonLayout.setVisibility(View.GONE);

    }


}
