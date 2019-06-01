package com.github.durazno.secretsharing;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import com.github.durazno.secretsharing.entities.User;
import com.github.durazno.secretsharing.models.AuthResponse;
import com.github.durazno.secretsharing.utils.Constants;
import com.github.durazno.secretsharing.utils.NotificationHelper;
import com.github.durazno.secretsharing.utils.RetrofitClient;
import com.github.durazno.secretsharing.utils.SessionManagement;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    public static final String CHANNEL_ID = "simplified_coding";
    private static final String CHANNEL_NAME = "Simplified Coding";
    private static final String CHANNEL_DESC = "Simplified Coding Notifications";

    private BiometricPrompt.PromptInfo promptInfo;

    private SessionManagement sessionManagement;
    private User user;
    private Gson gson;

    private String emailText;
    private String passwordText;

    private FirebaseAuth mAuth;

    @BindView(R.id.et_email)
    TextInputEditText email;
    @BindView(R.id.et_password)
    TextInputEditText password;
    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.til_pw)
    TextInputLayout tilPassword;

    @BindString(R.string.hint_email)
    String hEmail;
    @BindString(R.string.hint_password)
    String hPassword;

    @BindString(R.string.fingerTitle)
    String fingerprintDialogTitle;
    @BindString(R.string.fingerDescription)
    String fingerprintDialogDescription;
    @BindString(R.string.fingerButton)
    String fingerprintDialogNegativeText;

    @BindString(R.string.errorMail)
    String errorMail;
    @BindString(R.string.errorPassword)
    String errorPassword;
    @BindString(R.string.generalError)
    String generalError;

    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NotificationHelper.CHANNEL_ID, NotificationHelper.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(NotificationHelper.CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        mAuth = FirebaseAuth.getInstance();

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                token = task.getResult().getToken();
            }
        });

        gson = new Gson();
        mAuth = FirebaseAuth.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onResume() {
        super.onResume();
        checkAccount();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        if (!validateFields()) {
            return;
        }

        SessionManagement sessionManagement = new SessionManagement(getApplicationContext(), SessionManagement.AUTHORIZATION);

        Call<AuthResponse> call = RetrofitClient.getInstance().getApi().login(emailText, passwordText, token);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                try {
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "onResponse: " + authResponse.getAccessToken());
                    if (response.code() == 200) {
                        sessionManagement.setAuthorization(authResponse.getAccessToken());
                        sessionManagement.setLogged(1);
                        startActivity(new Intent(getApplicationContext(), SessionActivity.class)
                                .putExtra(Constants.ACCESS_TOKEN, authResponse.getAccessToken())
                                .putExtra(Constants.TOKEN_TYPE, authResponse.getTokenType())
                                .putExtra(Constants.EMAIL, emailText));
                    }
                } catch (Exception e) {
                    sessionManagement.setLogged(0);
                    tilEmail.setError(generalError);
                    tilPassword.setError(generalError);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: Error");
                t.printStackTrace();
            }
        });
    }

    private boolean validateFields() {
        boolean valid = true;

        if (emailText.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            this.tilEmail.setError(errorMail);
            valid = false;
        } else {
            this.tilEmail.setError(null);
        }

        if (passwordText.isEmpty()) {
            this.tilPassword.setError(errorPassword);
        }

        return valid;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void checkAccount() {

    }

    @OnEditorAction(R.id.et_password)
    public void onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEND:
                login();
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                break;
        }
    }

    @OnTextChanged({R.id.et_email, R.id.et_password})
    public void textChanged(CharSequence charSequence) {

        emailText = this.email.getText().toString();
        passwordText = this.password.getText().toString();

        if (String.valueOf(charSequence).hashCode() == emailText.hashCode()) {
            tilEmail.setError(null);
        } else if (String.valueOf(charSequence).hashCode() == passwordText.hashCode()) {
            tilPassword.setError(null);
        }
    }

    @OnFocusChange({R.id.et_email, R.id.et_password})
    public void placeHolderTrick(boolean focus, TextInputEditText textInputEditText) {
        if (focus) {
            if (textInputEditText == password) {
                textInputEditText.setHint(hPassword);
            } else {
                textInputEditText.setHint(hEmail);
            }
        } else {
            textInputEditText.setHint("");
        }
    }


    /**
     * Creates the fingerprint dialog
     *
     * @param subtitle Shows the subtitle as the user to log in.
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void fingerPrintDialogPrompt(String subtitle) {

        Log.d(TAG, "fingerPrintDialogPrompt: no sirve");
        Executor newExecutor = Executors.newSingleThreadExecutor();
        BiometricCallback biometricCallback = new BiometricCallback();

        final BiometricPrompt myBiometricPrompt = new BiometricPrompt(this, newExecutor, biometricCallback);

        if (promptInfo == null) {
            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle(fingerprintDialogTitle)
                    .setSubtitle(subtitle)
                    .setDescription(fingerprintDialogDescription)
                    .setNegativeButtonText(fingerprintDialogNegativeText)
                    .build();
        }

        myBiometricPrompt.authenticate(promptInfo);
    }

    /**
     * Handles the fingerprint callbacks.
     */
    private class BiometricCallback extends BiometricPrompt.AuthenticationCallback {

        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            startActivity(new Intent(getApplicationContext(), SessionActivity.class));
        }
    }
}
