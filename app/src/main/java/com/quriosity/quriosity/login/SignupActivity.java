package com.quriosity.quriosity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.quriosity.quriosity.BaseActivity;
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.utils.CountryDetails;
import com.quriosity.quriosity.utils.FirebaseUtil;

import java.util.ArrayList;
import java.util.Arrays;

import static com.quriosity.quriosity.utils.FirebaseUtil.MOBILE_10_FIRESTORE;
import static com.quriosity.quriosity.utils.FirebaseUtil.PRIMARYEMAIL_FIRESTORE;
import static com.quriosity.quriosity.utils.MyUtil.EXTRAS_MOBILE_10_DIGIT;
import static com.quriosity.quriosity.utils.MyUtil.EXTRAS_MOBILE_VERIFICATION;
import static com.quriosity.quriosity.utils.MyUtil.EXTRAS_USER_EMAIL;
import static com.quriosity.quriosity.utils.MyUtil.EXTRAS_USER_FULL_NAME;
import static com.quriosity.quriosity.utils.MyUtil.EXTRAS_USER_PASSWORD;
import static com.quriosity.quriosity.utils.MyUtil.validateEml;

public class SignupActivity extends BaseActivity {
    private static final String TAG = "SignupActivity";
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private LinearLayout relativeLayoutData;
    private Button submitBtton;
    private EditText fullName, userEmail, userMobile, userPassword1, userPassword2;
    private TextView phoneCodeEditSignup;
    private Spinner countrycodeedittext;
    private ArrayList<String> codeArrayList;
    private ArrayList<String> countryArrayList;
    private FirebaseFirestore mDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        toolbar = findViewById(R.id.toolbar_signup_activity_layout);
        initToolbar();
        mDatabase = FirebaseFirestore.getInstance();
        relativeLayoutData = findViewById(R.id.whole_relativelayout_add_functions_activity);
        progressBar = findViewById(R.id.progress_bar_add_functions_layout);
        submitBtton = findViewById(R.id.submit_btn_activity_signup);
        phoneCodeEditSignup = findViewById(R.id.countrycodes);
        countrycodeedittext = findViewById(R.id.countrycodeedittext);
        fullName = findViewById(R.id.userfirstname_signup_activity);
        userEmail = findViewById(R.id.email_signup_activity);
        userMobile = findViewById(R.id.input_co_nt_ac_t_n_o);
        userPassword1 = findViewById(R.id.user_password_activity_layout);
        userPassword2 = findViewById(R.id.user_confirm_password_activity_layout);

////GETTING THE COUNTRY AND CODE ARRAY ARE AS FOLLOW:-
        codeArrayList = new ArrayList<String>(Arrays.asList(CountryDetails.getCode()));
        countryArrayList = new ArrayList<String>(Arrays.asList(CountryDetails.getCountry()));

        /////SETTING THE ADAPTER ACCORDING TO THE COUNTRY NAMES

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignupActivity.this, android.R.layout.simple_list_item_1, countryArrayList);

        countrycodeedittext.setAdapter(adapter);///HERE YOUR_LIST_VIEW IS YOUR LISTVIEW NAME

        countrycodeedittext.setSelection(94);
        countrycodeedittext.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
//                Toast.makeText(SignupActivity.this, "COUNTRY NAME==>>" + countryArrayList.get(i)
//                        + " HERE IS THE COUNTY CODE==>>" + codeArrayList.get(i), Toast.LENGTH_LONG).show();
                Log.d(TAG, "onItemSelected: position id = " + id + " position i " + i);
                phoneCodeEditSignup.setText("+" + codeArrayList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SignupActivity.this, "Nothing selected", Toast.LENGTH_SHORT).show();
            }
        });

        phoneCodeEditSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countrycodeedittext.performClick();
            }
        });
        submitBtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBttonMethod();
            }
        });
    }

    private void submitBttonMethod() {
        if (fullName.getText().toString().trim().isEmpty()) {
            fullName.requestFocus();
            fullName.setError("Field cannot be empty");
            return;
        }
        if (fullName.getText().toString().trim().length() <= 2) {
            fullName.requestFocus();
            fullName.setError("Please enter correct name");
            return;
        }
        if (userEmail.getText().toString().trim().isEmpty()) {
            userEmail.requestFocus();
            userEmail.setError("Field cannot be empty");
            return;
        }
        if (!validateEml(userEmail.getText().toString().trim())) {
            userEmail.setError("Please enter a valid email address");
            userEmail.requestFocus();
            return;
        }
        if (userMobile.getText().toString().trim().isEmpty()) {
            userMobile.requestFocus();
            userMobile.setError("Field cannot be empty");
            return;
        }
        if (userMobile.getText().toString().trim().length() < 10) {
            userMobile.requestFocus();
            userMobile.setError("Please enter correct phone");
            return;
        }
        if (userPassword1.getText().toString().trim().isEmpty()) {
            userPassword1.requestFocus();
            userPassword1.setError("Field cannot be empty");
            return;
        }
        if (userPassword1.getText().toString().trim().length() < 6) {
            userPassword1.requestFocus();
            userPassword1.setError("Password Should be more than 6 characters");
            return;
        }

        if (!userPassword2.getText().toString().trim().equalsIgnoreCase(userPassword1.getText().toString().trim())) {
            userPassword2.requestFocus();
            userPassword2.setError("Password mismatch. Please check.");
            return;
        }
        showProgress();
        checkEmailExist(userEmail.getText().toString().trim());
    }

    private void checkEmailExist(String trim) {
        Query docRef = mDatabase.collection(FirebaseUtil.USERS_FIRESTORE)
                .whereEqualTo(PRIMARYEMAIL_FIRESTORE, trim);
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().getDocuments().isEmpty()) {
                    hideProgress();
                    userEmail.requestFocus();
                    userEmail.setError("This email is already registered with us. Please enter new email");
                } else {
                    checkMobileExist(userMobile.getText().toString().trim());
                }
            }
        });


    }

    private void checkMobileExist(String trim) {

        Query docRef = mDatabase.collection(FirebaseUtil.USERS_FIRESTORE)
                .whereEqualTo(MOBILE_10_FIRESTORE, trim);
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().getDocuments().isEmpty()) {
                    hideProgress();
                    userMobile.requestFocus();
                    userMobile.setError("This mobile no. is already registered with us. Please enter new mobile no.");
                } else {
                    hideProgress();
                    startActivity(new Intent(SignupActivity.this, VerificationActivity.class)
                            .putExtra(EXTRAS_MOBILE_VERIFICATION, phoneCodeEditSignup.getText().toString().trim() + userMobile.getText().toString().trim())
                            .putExtra(EXTRAS_MOBILE_10_DIGIT, userMobile.getText().toString().trim())
                            .putExtra(EXTRAS_USER_FULL_NAME, fullName.getText().toString().trim())
                            .putExtra(EXTRAS_USER_EMAIL, userEmail.getText().toString().trim())
                            .putExtra(EXTRAS_USER_PASSWORD, userPassword2.getText().toString().trim())

                    );
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            }
        });
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
        //((TextView) findViewById(R.id.base_toolbar_textView)).setText("trave");
    }

    private void showProgress() {
        relativeLayoutData.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        relativeLayoutData.setVisibility(View.VISIBLE);
    }
}
