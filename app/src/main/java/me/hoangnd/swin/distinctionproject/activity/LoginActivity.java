package me.hoangnd.swin.distinctionproject.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

import me.hoangnd.swin.distinctionproject.R;
import me.hoangnd.swin.distinctionproject.fragment.LoginFragment;
import me.hoangnd.swin.distinctionproject.fragment.RegisterFragment;

public class LoginActivity extends AppCompatActivity implements
        LoginFragment.FragmentLoginListener,
        RegisterFragment.FragmentRegisterListener {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private UserRegisterTask mRegisterTask = null;

    private View mProgressView;
    private View mLoginFormView;
    private TextView mErrorLabel;

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mErrorLabel = (TextView)findViewById(R.id.error_label);

        loginFragment = LoginFragment.newInstance();
        registerFragment = RegisterFragment.newInstance();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.email_login_form, loginFragment);
        transaction.commit();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show) {
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
    public void onOpenRegister() {
        mErrorLabel.setText("");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(loginFragment);
        transaction.add(R.id.email_login_form, registerFragment);
        transaction.commit();
    }

    @Override
    public void onOpenLoginPage() {
        mErrorLabel.setText("");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(registerFragment);
        transaction.add(R.id.email_login_form, loginFragment);
        transaction.commit();
    }

    @Override
    public void onDoRegister(String email, String password) {
        showProgress(true);
        mErrorLabel.setText("");
        mRegisterTask = new UserRegisterTask(email, password);
        mRegisterTask.execute();
    }

    @Override
    public void onDoLogin(String email, String password) {
        showProgress(true);
        mErrorLabel.setText("");
        mAuthTask = new UserLoginTask(email, password);
        mAuthTask.execute();
    }

    /**
     * Represents an asynchronous login task used to authenticate the user.
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
            try {
                ParseUser.logIn(mEmail, mPassword);
                return true;
            } catch (ParseException e) {
                final String message = e.getMessage();
                runOnUiThread(new Runnable() {
                    public void run() {
                        mErrorLabel.setText(message);
                    }
                });
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Represents an asynchronous login task used to authenticate the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserRegisterTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                ParseUser user = new ParseUser();
                user.setUsername(mEmail);
                user.setEmail(mEmail);
                user.setPassword(mPassword);
                user.signUp();
                finish();
                return true;
            } catch (ParseException e) {
                final String message = e.getMessage();
                runOnUiThread(new Runnable() {
                    public void run() {
                        mErrorLabel.setText(message);
                    }
                });
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegisterTask = null;
            showProgress(false);

            if (success) {
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
            showProgress(false);
        }
    }
}
