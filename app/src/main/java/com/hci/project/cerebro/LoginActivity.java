package com.hci.project.cerebro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.DrawableWrapper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static android.Manifest.permission.READ_CONTACTS;
import static com.hci.project.cerebro.R.string.action_sign_in_short;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hci.project.cerebro.CreateUserAPI;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordConfView;
    private View mProgressView;
    private View mLoginFormView;
    //public final String[] skillNames = new String[10];
    //public final int[] skillIds = new int[10];


    String flag;
    String message;

    public String user_fn;
    public String user_ln;
    public int user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView register_prompt = findViewById(R.id.register_prompt);
        Button sin_sup_button = findViewById(R.id.email_sign_in_button);

        EditText first_name = findViewById(R.id.first_name);
        EditText last_name = findViewById(R.id.last_name);
        EditText pass_conf = findViewById(R.id.password_confirm);

        Intent intent = getIntent();
        flag = intent.getStringExtra(IntroActivity.EXTRA_MESSAGE);

        if (Objects.equals(flag, "0")){
            register_prompt.setText(R.string.sin_prompt);
            first_name.setVisibility(View.GONE);
            last_name.setVisibility(View.GONE);
            sin_sup_button.setText(action_sign_in_short);
            pass_conf.setVisibility(View.GONE);
//            Intent intent1 = new Intent(getApplicationContext(), DrawerActivity.class);
//            startActivity(intent1);

        }
        String sflag = getIntent().getStringExtra("Logout");
        if( sflag == null)    {        }
        else
        {
            register_prompt.setText(R.string.sin_prompt);
            first_name.setVisibility(View.GONE);
            last_name.setVisibility(View.GONE);
            sin_sup_button.setText(action_sign_in_short);
            pass_conf.setVisibility(View.GONE);
        }
        //Logging out code
        SharedPreferences sp = getSharedPreferences("LoginState",
                MODE_PRIVATE);
        boolean stateValue  = sp.getBoolean("setLoggingOut", false);
        if(stateValue && Objects.equals(flag, "0")){
            register_prompt.setText(R.string.sin_prompt);
            first_name.setVisibility(View.GONE);
            last_name.setVisibility(View.GONE);
            sin_sup_button.setText(action_sign_in_short);
            pass_conf.setVisibility(View.GONE);
            flag = "0";
        }
        //END

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordConfView = (EditText) findViewById(R.id.password_confirm);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = sin_sup_button;
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
                //Intent newIntent = new Intent(LoginActivity.this, DrawerActivity.class);
                //startActivity(newIntent);
                if(!Objects.equals(mPasswordView.getText().toString(), mPasswordConfView.getText().toString()) && Objects.equals(flag, "1")){
                    //passwords do not match. Show toast
                    Toast toast = Toast.makeText(getApplicationContext(),"Passwords do not match!",Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    //registerUser();
                    //String t = "";
//                UserController controller = new UserController();
//                controller.start();
                }
            }

        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

//    public void fetchUserDetails()
//    {
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//        final String BASE_URL = "http://cerebro-api.herokuapp.com/api/";
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//        SharedPreferences settings = getApplicationContext().getSharedPreferences("MyPref",0);
//        String token = settings.getString("Current_User", "defaultvalue");
//
//        Map<String, String> map = new HashMap<>();
//        map.put("X-Authorization", token);
//
//        CerebroAPI cerebro_api = retrofit.create(CerebroAPI.class);
//
//        cerebro_api.loadChanges(map).enqueue(new Callback<User>()
//        {
//            @Override
//            public void onResponse(Call<User> call, Response <User> response)
//            {
//                if (response.isSuccessful()) {
//                    System.out.println("Fetching User details :: " + response.toString());
//                    String first_name = response.body().first_name;
//                    String last_name = response.body().last_name;
//                    String emailID = response.body().email;
//                    int userID = response.body().id;
//                    float rating = response.body().rating;
//                    float x_coordinate = response.body().x_coordinate;
//                    float y_coordinate = response.body().y_coordinate;
//                    Time start_time = response.body().start_time;
//                    Time end_time = response.body().end_time;
//                    String start_time_string =" ";
//                    String end_time_string = " ";
//                    if(start_time != null){
//                        start_time_string = start_time.toString();
//                    }
//                    if(end_time != null){
//                        end_time_string = end_time.toString();
//                    }
//                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
//                    SharedPreferences.Editor editor = pref.edit();
//                    //on the login store the login
//                    editor.putString("Current_User_fName", first_name);
//                    editor.putInt("Current_User_Id", userID);
//                    editor.putString("Current_User_lName", last_name);
//                    editor.putString("Current_User_email", emailID);
//                    editor.putFloat("Current_User_rating", rating);
//                    editor.putFloat("Current_User_x_coordinate", x_coordinate);
//                    editor.putFloat("Current_User_y_coordinate", y_coordinate);
//                    editor.putString("Current_User_starttime",start_time_string );
//                    editor.putString("Current_User_endtime", end_time_string);
//                    editor.commit();
//                }
//            }
//            public void onFailure(Call<User> call, Throwable t)
//            {
//                t.printStackTrace();
//            }
//         });
//    }

    public void registerUser()
    {
        EditText fn = findViewById(R.id.first_name);
        String firstname = fn.getText().toString();
        EditText ln = findViewById(R.id.last_name);
        String lastname = ln.getText().toString();
        EditText email = findViewById(R.id.email);
        String emailID = email.getText().toString();
        EditText password = findViewById(R.id.password);
        String pass = password.getText().toString();
        EditText confirm_password = findViewById(R.id.password_confirm);
        String confirm_pass = confirm_password.getText().toString();

        // Common across If and else
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CreateUser.class, new CustomGsonAdapter.UserAdapter())
                .setLenient()
                .create();
        final String BASE_URL = "http://cerebro-api.herokuapp.com/api/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        if(!TextUtils.isEmpty(firstname) && !TextUtils.isEmpty(lastname) &&
                !TextUtils.isEmpty(emailID) && !TextUtils.isEmpty(pass)) {
            CreateUserAPI createUser_api = retrofit.create(CreateUserAPI.class);
            CreateUser createUser = new CreateUser(firstname,lastname,emailID,pass,confirm_pass);
            createUser_api.addPost(createUser).enqueue(new Callback<UserToken>() {
                @Override
                public void onResponse(Call<UserToken> call, Response<UserToken> response) {
                    if (response.isSuccessful()) {
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        //on the login store the login
                        editor.putString("Current_User", response.body().token);
                        editor.commit();
                        //fetchUserDetails();
                        //System.out.println("Response Bodyyyyy : :: : " + response.toString());
                        //System.out.println("Token : :: : " + response.body().token);
                        //System.out.println("Response  :::" + response.body().toString());
                        if(response.body().token != null) {
                            //getSkills();
                            //getDeviceID();
                            Intent learnerIntent = new Intent(LoginActivity.this, TutorProfileActivity.class);
                            startActivity(learnerIntent);
                        }
                    }
                }
                @Override
                public void onFailure(Call<UserToken> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        else
        {
            UserSigninAPI signin_api = retrofit.create(UserSigninAPI.class);
            UserSignin login = new UserSignin(emailID,pass);
            signin_api.userSignin(login).enqueue(new Callback<UserToken>() {
                @Override
                public void onResponse(Call<UserToken> call, Response<UserToken> response) {
                    if (response.isSuccessful()) {
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        //on the login store the login
                        editor.putString("Current_User", response.body().token);
                        editor.commit();
                        //fetchUserDetails();
                        //System.out.println("Response Bodyyyyy : :: : " + response.toString());
                        //System.out.println("Token : :: : " + response.body().token);
                        //System.out.println("Response  :::" + response.body().toString());
                        if(response.body().token != null) {
                            //getSkills();
                            //getDeviceID();
                            Intent learnerIntent = new Intent(LoginActivity.this, DrawerActivity.class);
                            startActivity(learnerIntent);
                        }
                    }
                }
                @Override
                public void onFailure(Call<UserToken> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        if(Objects.equals(flag, "0")){
            message = "Are you sure you want to abort Sign In?";
        }
        else if(Objects.equals(flag, "1")){
            message = "Are you sure you want to abort Sign Up?";
        }
        else
        {
            message = "Are you sure you want to abort?";
        }
        builder.setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //go back
                        LoginActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
            //if login details correct, send user to dashboard
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@uic.edu");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            registerUser();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                showProgress(true);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

