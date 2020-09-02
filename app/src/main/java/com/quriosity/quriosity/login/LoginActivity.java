package com.quriosity.quriosity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.quriosity.quriosity.BaseActivity;
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.activity.DashboardMainActivity;
import com.quriosity.quriosity.utils.FirebaseUtil;
import com.quriosity.quriosity.utils.MyUtil;
import com.quriosity.quriosity.utils.WedAmorApplication;

import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_ONLINE_STS1;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_ONLINE_TIMESTAMP;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_USER_ONLINE_STATUS;
import static com.quriosity.quriosity.utils.FirebaseUtil.FIRE_UID_FIRESTORE;
import static com.quriosity.quriosity.utils.FirebaseUtil.FIRSTNAME_FIRESTORE;
import static com.quriosity.quriosity.utils.FirebaseUtil.MOBILE_10_FIRESTORE;
import static com.quriosity.quriosity.utils.FirebaseUtil.MOBILE_FIRESTORE;
import static com.quriosity.quriosity.utils.FirebaseUtil.PASSWORD_FIRESTORE;
import static com.quriosity.quriosity.utils.FirebaseUtil.PRIMARYEMAIL_FIRESTORE;
import static com.quriosity.quriosity.utils.FirebaseUtil.SIGNUP_METHOD_FIRESTORE;
import static com.quriosity.quriosity.utils.FirebaseUtil.USERNAME_FIRESTORE;
import static com.quriosity.quriosity.utils.FirebaseUtil.USERS_LAST_LOGIN;
import static com.quriosity.quriosity.utils.FirebaseUtil.USERS_REGISTRATION_TOKEN_FIELD;
import static com.quriosity.quriosity.utils.MyUtil.isValidMobile;
import static com.quriosity.quriosity.utils.MyUtil.validateEml;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signupTextview, forgotTextview;
    private ProgressBar progressBar;
    private LinearLayout mainLayout;
    private FirebaseFirestore mDatabase;
    private Toolbar toolbar;
    private boolean isEyeVisible;
    private ImageView eyeImageView;
    private MaterialCheckBox checkBox;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        toolbar = findViewById(R.id.toolbar_login_activity_layout);
        emailEditText = findViewById(R.id.emaillEditTxt_login);
        passwordEditText = findViewById(R.id.password_loginEdittext);
        loginButton = findViewById(R.id.login_btn_loginactivity);
        signupTextview = findViewById(R.id.dont_have_account_txtview);
        forgotTextview = findViewById(R.id.forgot_password_login_activity);
        progressBar = findViewById(R.id.progress_bar_login_activity);
        eyeImageView = findViewById(R.id.password_visibility_eye_login);
        mainLayout = findViewById(R.id.main_linearlayout_login_activity);
        checkBox = findViewById(R.id.rememberme_checkbox_loginactivity);
        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        hideProgress();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailEditText.getText().toString().isEmpty()) {
                    emailEditText.setError("Field can not be empty");
                    emailEditText.requestFocus();
                    return;
                }

                if (emailEditText.getText().toString().length() < 4) {
                    emailEditText.setError("Please enter valid username, email or mobile");
                    emailEditText.requestFocus();
                    return;
                }

                if (passwordEditText.getText().toString().isEmpty()) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, 90, 0);
                    params.addRule(RelativeLayout.ALIGN_TOP, R.id.password_loginEdittext);
                    params.addRule(RelativeLayout.ALIGN_END, R.id.password_loginEdittext);
                    params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.password_loginEdittext);
                    eyeImageView.setPadding(10, 0, 20, 0);
                    eyeImageView.setLayoutParams(params);
                    passwordEditText.setError("Password field can not be empty");
                    passwordEditText.requestFocus();
                    return;
                }

                if (passwordEditText.getText().toString().length() < 6) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, 90, 0);
                    params.addRule(RelativeLayout.ALIGN_TOP, R.id.password_loginEdittext);
                    params.addRule(RelativeLayout.ALIGN_END, R.id.password_loginEdittext);
                    params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.password_loginEdittext);
                    eyeImageView.setPadding(10, 0, 20, 0);
                    eyeImageView.setLayoutParams(params);
                    passwordEditText.setError("Password should be 6 digit");
                    passwordEditText.requestFocus();
                    return;
                }

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 30, 0);
                params.addRule(RelativeLayout.ALIGN_TOP, R.id.password_loginEdittext);
                params.addRule(RelativeLayout.ALIGN_END, R.id.password_loginEdittext);
                params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.password_loginEdittext);
                eyeImageView.setPadding(10, 0, 20, 0);
                eyeImageView.setLayoutParams(params);

                checkUserName(emailEditText.getText().toString().trim(), passwordEditText.getText().toString().trim());
            }
        });


        signupTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        forgotTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPswrdActivity.class)
                        .putExtra(Intent.EXTRA_EMAIL, emailEditText.getText().toString().trim()));
                finish();
            }
        });
        eyeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEyeVisible) {
                    passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
                    eyeImageView.setImageResource(R.drawable.ic_eye_icon);
                    isEyeVisible = false;
                } else {
                    passwordEditText.setTransformationMethod(null);
                    eyeImageView.setImageResource(R.drawable.ic_eye_not_visible);
                    isEyeVisible = true;
                }
                emailEditText.clearFocus();
                passwordEditText.clearFocus();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(LoginActivity.this, "Data saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Data removed", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void checkUserName(String userInput, String password) {
        if (validateEml(userInput)) {
            checkUserExistance(userInput, password, PRIMARYEMAIL_FIRESTORE);
            return;
        }
        if (isValidMobile(userInput)) {
            checkUserExistance(userInput, password, MOBILE_10_FIRESTORE);
            return;
        }
        checkUserExistance(userInput, password, USERNAME_FIRESTORE);
    }

    private void checkUserExistance(String userInput, final String password, String searchField) {
        //Toast.makeText(this, "you entered " + searchField, Toast.LENGTH_SHORT).show();
        showProgress();
        //fetch user exist in login table or not
        Query docRef = mDatabase.collection(FirebaseUtil.USERS_FIRESTORE)
                .whereEqualTo(searchField, userInput);
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG, "onComplete: login sts " + task.getResult().getDocuments());
                if (!task.getResult().getDocuments().isEmpty()) {
                    //Log.d(TAG, "onComplete: " + task.getResult().getDocuments().get(0).get("username"));
                    loginWithEmailAndPassword(String.valueOf(task.getResult().getDocuments().get(0).get(PRIMARYEMAIL_FIRESTORE)), password, task);
                } else {
                    Log.d(TAG, "onComplete: sorry no data found");
                    Toast.makeText(LoginActivity.this, "Invalid Credentials. Please try again.", Toast.LENGTH_LONG).show();
                    hideProgress();
                }
            }
        });
    }

    private void loginWithEmailAndPassword(String email, final String password, final Task<QuerySnapshot> task) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> taskauth) {
                if (taskauth.isSuccessful()) {
                    FirebaseUtil.fbUpdateLoginTable(String.valueOf(task.getResult().getDocuments().get(0).get(FIRE_UID_FIRESTORE)), USERS_LAST_LOGIN, MyUtil.getTimeStampInMillis());
                    FirebaseUtil.fbUpdateLoginTable(String.valueOf(task.getResult().getDocuments().get(0).get(FIRE_UID_FIRESTORE)),
                            PASSWORD_FIRESTORE, password);
                    FirebaseUtil.fbUpdateLoginTable(String.valueOf(task.getResult().getDocuments().get(0).get(FIRE_UID_FIRESTORE)),
                            EXTRAS_ONLINE_TIMESTAMP, MyUtil.getTimeStampInMillis());
                    FirebaseUtil.fbUpdateLoginTable(String.valueOf(task.getResult().getDocuments().get(0).get(FIRE_UID_FIRESTORE)),
                            EXTRAS_USER_ONLINE_STATUS, EXTRAS_ONLINE_STS1);

                    updateRegistrationToken(String.valueOf(task.getResult().getDocuments().get(0).get(FIRE_UID_FIRESTORE)));

                    ((WedAmorApplication) getApplicationContext()).setfirebaseUid(String.valueOf(task.getResult().getDocuments().get(0).get(FIRE_UID_FIRESTORE)));
                    ((WedAmorApplication) getApplicationContext()).setFirstUserName(String.valueOf(task.getResult().getDocuments().get(0).get(FIRSTNAME_FIRESTORE)));
                    ((WedAmorApplication) getApplicationContext()).setCheckLogin(true);
                    ((WedAmorApplication) getApplicationContext()).setPrimaryEmailId(String.valueOf(task.getResult().getDocuments().get(0).get(PRIMARYEMAIL_FIRESTORE)));
                    ((WedAmorApplication) getApplicationContext()).setPhoneOnly10Digit(String.valueOf(task.getResult().getDocuments().get(0).get(MOBILE_10_FIRESTORE)));
                    ((WedAmorApplication) getApplicationContext()).setPhoneNoWithCountryCode(String.valueOf(task.getResult().getDocuments().get(0).get(MOBILE_FIRESTORE)));
                    ((WedAmorApplication) getApplicationContext()).setFirebaseRegistrationToken(String.valueOf(task.getResult().getDocuments().get(0).get(USERS_REGISTRATION_TOKEN_FIELD)));

                    //fetching user profile id
                    Query docRef = mDatabase.collection(FirebaseUtil.USERS_FIRESTORE)
                            .whereEqualTo("ulid", task.getResult().getDocuments().get(0).get(FIRE_UID_FIRESTORE).toString());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> snapshotTask) {
                            Intent i = new Intent(getApplicationContext(), DashboardMainActivity.class);
                            i.putExtra("Email", String.valueOf(task.getResult().getDocuments().get(0).get(PRIMARYEMAIL_FIRESTORE)));
                            i.putExtra("provider", String.valueOf(task.getResult().getDocuments().get(0).get(SIGNUP_METHOD_FIRESTORE)));
                            i.putExtra("Name", String.valueOf(task.getResult().getDocuments().get(0).get(FIRSTNAME_FIRESTORE)));
                            i.putExtra("uuid", String.valueOf(task.getResult().getDocuments().get(0).get(FIRE_UID_FIRESTORE)));
                            hideProgress();
                            startActivity(i);
                            finish();
                        }
                    });
                } else {
                    hideProgress();
                    Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateRegistrationToken(final String myfuid) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        Log.d(TAG, "onComplete: registrationToken " + task.getResult().getToken());
                        FirebaseUtil.fbUpdateLoginTable(myfuid,
                                USERS_REGISTRATION_TOKEN_FIELD,
                                task.getResult().getToken());
                    }
                });
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
    }

}
