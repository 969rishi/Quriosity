package com.quriosity.quriosity.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import com.quriosity.quriosity.R;
import com.quriosity.quriosity.login.LoginActivity;
import com.quriosity.quriosity.login.SignupActivity;

import java.io.FileDescriptor;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUtil {
    //data payload keys
    public static final String NOTI_NAME= "actorOtherName";
    public static final String NOTI_chatID= "chatID";
    public static final String NOTI_body= "body";
    public static final String NOTI_action= "click_action";
    public static final String NOTI_msgtype= "msgtype";
    public static final String NOTI_url= "url";
    public static final String NOTI_actorOtherId= "actorOther";
    public static final String NOTI_actorMeId= "actorMe";
    public static final String NOTI_msgID= "msgID";

    public static final String DEFAULT_USER_ICON_URL = "https://firebasestorage.googleapis.com/v0/b/tribesandtraditions.appspot.com/o/default%2Fusericon.png?alt=media&token=29feac7b-ac96-4117-9be5-3901de9218a0";
    public static final String FIREBASE_STORAGE_MAIN = "https://firebasestorage.googleapis.com/v0/b/tribesandtraditions.appspot.com/o/";
    public static final String FIREBASE_STORAGE_BASE_URL = FIREBASE_STORAGE_MAIN + "cdn%2Fis%2F";
    public static final String FIREBASE_STORAGE_END_URL = "?alt=media";


    public static final String EXTRAS_CONTACTS_FRAG = "ContactsFrag";
    public static final String EXTRAS_FCM_SERVICES = "MyFirebaseMessagingService";
    public static final String EXTRAS_CREATE_GROUP = "CreateGroupActivity";


    public static final String EXTRAS_MOBILE_VERIFICATION = "Mobile_verification_number";
    public static final String EXTRAS_MOBILE_10_DIGIT = "Mobile_10_digit";
    public static final String EXTRAS_USER_FULL_NAME = "User_full_name";
    public static final String EXTRAS_USER_EMAIL = "User_Email_ID";
    public static final String EXTRAS_USER_PASSWORD = "User_password";
    public static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                    Pattern.CASE_INSENSITIVE);
    private static final String TAG = "Util_Class";
    private static final long FIVE_SECONDS = 1000 * 5; //5 second in milliseconds
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static String getTimeStampInMillis() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return String.valueOf(timestamp.getTime());
    }

    public static boolean validateEml(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() == 10;
        }
        return false;
    }

    public static boolean fiveSecWaiting(String tempTimestamp) {
        boolean retrunme;
        //3000(millliseconds in a second)*60(seconds in a minute)*5(number of minutes)=300000
        //server timestamp is within 5 minutes of current system time
        //server is not within 5 minutes of current system time
        retrunme = Math.abs(Long.parseLong(tempTimestamp) - System.currentTimeMillis()) < FIVE_SECONDS;
        return retrunme;
    }

    public static String firstLetterCap(String inputString) {

        String str = inputString;
        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);

        return cap;
    }

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static Bitmap getBitmapFromUri(Uri uri, Context context) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    // input should be like this 1581775261777
    public static String lastSeenConverter(String inputdata) {
        String outputdats = "";
        try {
            Date past = new Time(Long.valueOf(inputdata));
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
            if (hours < 24) {
                try {
                    Date date = null;
                    try {
                        date = new Time(Long.valueOf(inputdata));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DateFormat displayFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
                    outputdats = "Last seen at " + displayFormat.format(date);
                } catch (Exception j) {
                    j.printStackTrace();
                }
            } else if (days == 1) {
                outputdats = "Last seen 1 day ago";
            } else if (days == 2) {
                outputdats = "Last seen 2 day ago";
            } else if (days == 3) {
                outputdats = "Last seen 3 day ago";
            } else if (days == 4) {
                outputdats = "Last seen 4 day ago";
            } else if (days == 5) {
                outputdats = "Last seen 5 day ago";
            } else if (days == 6) {
                outputdats = "Last seen 6 day ago";
            } else if (days == 7) {
                outputdats = "Last seen 7 day ago";
            } else {
                Date date = null;
                try {
                    date = new Time(Long.valueOf(inputdata));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                outputdats = "Last seen on " + displayFormat.format(date);
            }
        } catch (
                Exception j) {
            j.printStackTrace();
        }
        return outputdats;
    }

    public static String dayAgoForChatting(String inputdata) {
        String outputdats = "";
        try {
//            Date past = new Time(Long.valueOf(inputdata));
//            Date now = new Date();
//            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
//            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
//            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
//            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
            Date date = null;
            try {
                date = new Time(Long.valueOf(inputdata));
            } catch (Exception e) {
                e.printStackTrace();
            }
            DateFormat displayFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
            outputdats = displayFormat.format(date) + "";
        } catch (Exception j) {
            j.printStackTrace();
        }
        return outputdats;
    }

    public static String dayAgoForChat(String inputdata) {
        String outputdats = "";
        try {
            Date past = new Time(Long.valueOf(inputdata));
            Date now = new Date();
//            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
//            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
            if (hours < 24) {
                Date date = null;
                try {
                    date = new Time(Long.valueOf(inputdata));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DateFormat displayFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
                outputdats = displayFormat.format(date) + "";
            } else if (days == 1) {
                outputdats = "Yesterday";
            } else {
                Date date = null;
                try {
                    date = new Time(Long.valueOf(inputdata));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                outputdats = displayFormat.format(date) + "";
            }
        } catch (Exception j) {
            j.printStackTrace();
        }
        return outputdats;
    }


    // input should be like this 1581775261777
    public static String dayAgoFunMilli(String inputdata) {
        String outputdats = "";
        try {
            Date past = new Time(Long.valueOf(inputdata));
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
            if (seconds < 60) {
                System.out.println(seconds + " seconds ago");
                outputdats = seconds + " seconds ago";
            } else if (minutes < 60) {
                outputdats = minutes + " minutes ago";
            } else if (hours < 24) {
                outputdats = hours + " hours ago";
            } else if (days == 1) {
                outputdats = "1 day ago";
            } else if (days == 2) {
                outputdats = "2 day ago";
            } else if (days == 3) {
                outputdats = "3 day ago";
            } else if (days == 4) {
                outputdats = "4 day ago";
            } else if (days == 5) {
                outputdats = "5 day ago";
            } else if (days == 6) {
                outputdats = "6 day ago";
            } else if (days == 7) {
                outputdats = "7 day ago";
            } else {
                Date date = null;
                try {
                    date = new Time(Long.valueOf(inputdata));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DateFormat displayFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                outputdats = displayFormat.format(date) + "";
            }
        } catch (Exception j) {
            j.printStackTrace();
        }
        return outputdats;
    }


    public static void showLoginDialog(final Context context) {
        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(context);
        View mView = View.inflate(context, R.layout.dialog_show_login_msg, null);
        final Button button_save, button_skip;
        final ImageButton closebtn;
        button_save = mView.findViewById(R.id.dialog_button_login);
        button_skip = mView.findViewById(R.id.dialog_button_upload_skip_btn);
        closebtn = mView.findViewById(R.id.bt_close_dialog_show_login_msg);
        mBuilder.setView(mView);
        final android.app.AlertDialog dialog = mBuilder.create();
        dialog.show();

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                dialog.cancel();
            }
        });
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        button_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, SignupActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                dialog.cancel();
            }
        });

//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                dialog.cancel();
//            }
//        });
    }

    public static Boolean isUserLoggedIn(final Context context) {
        boolean returnMe;
        if (!((WedAmorApplication) context.getApplicationContext()).getCheckLogin()) {
            returnMe = false;
        } else
            returnMe = true;
        return returnMe;
    }

    public static void isInternetConnected(final Context context, final InternetConnectionListener mListener) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                Runtime runtime = Runtime.getRuntime();
                if (context != null)
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (context != null) {
                                ConnectivityManager cm =
                                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                                boolean isConnected = activeNetwork != null &&
                                        activeNetwork.isConnectedOrConnecting();
                                return isConnected;
                            } else {
                                return false;
                            }
                        } else {
                            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                            int mExitValue = mIpAddrProcess.waitFor();
                            //                        Toast.makeText(context, "NetWork Not Available", Toast.LENGTH_SHORT).show();
                            return mExitValue == 0;
                        }
                    } catch (InterruptedException ignore) {
                        ignore.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println(" Exception:" + e);
                    }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean connected) {
                super.onPostExecute(connected);
                if (mListener != null) {
                    mListener.isConnected(connected);
                }
            }
        }.execute();
    }

    public interface InternetConnectionListener {
        public void isConnected(boolean connected);
    }
}
