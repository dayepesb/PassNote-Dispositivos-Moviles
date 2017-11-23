package co.edu.poli.passnote.passnote.access;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import co.edu.poli.passnote.passnote.MainNavigationActivity;
import co.edu.poli.passnote.passnote.R;
import co.edu.poli.passnote.passnote.utils.NotificationUtils;

import static co.edu.poli.passnote.passnote.utils.FieldUtils.getTextFromField;
import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showGeneralError;
import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showNotification;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class Login extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db;
    private CollectionReference usersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("users");
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void register(View v) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void login(View v) {
        String username = getTextFromField(findViewById(R.id.loginUsername));
        String password = getTextFromField(findViewById(R.id.loginPassword));

        if (isNotBlank(username) && isNotBlank(password)) {
            if (username.contains("@")) {
                loginWithEmail(username, password);
            } else {
                loginWithUsername(username, password);
            }
        } else {
            Toast.makeText(Login.this, R.string.login_credentials_required,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void resetPassword(View v) {
        try {
            String username = getTextFromField(findViewById(R.id.loginUsername));

            if (isNotBlank(username)) {
                showProgressBar();
                firebaseAuth.sendPasswordResetEmail(username)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                hideProgressBar();
                                if (task.isSuccessful()) {
                                    showNotification(R.string.loginPasswordResetEmailSent);
                                    Log.d(TAG, "Email sent.");
                                } else {
                                    showNotification(R.string.loginEmailNotFound);
                                    Log.d(TAG, "Email not found.");
                                }
                            }
                        });
            } else {
                showNotification(R.string.loginEmailRequiredForPasswordReset);
            }
        } catch (Exception e) {
            hideProgressBar();
            NotificationUtils.showGeneralError();
        }
    }

    private void loginWithUsername(String username, final String password) {
        showProgressBar();

        usersCollection
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String email = "";
                            for (DocumentSnapshot user : task.getResult()) {
                                email = user.getString("email");
                                break;
                            }
                            if (isNotBlank(email)) {
                                loginWithEmail(email, password);
                            } else {
                                hideProgressBar();
                                showNotification(R.string.login_authentication_failed);
                            }
                        } else {
                            hideProgressBar();
                            showGeneralError();
                        }
                    }
                });
    }

    private void loginWithEmail(String email, String password) {
        showProgressBar();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressBar();
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(Login.this, R.string.login_authentication_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            sendEmailVerification();
                            goToAccounts();
                        }
                    }
                });
    }

    private void sendEmailVerification() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

    private void goToAccounts() {
        Intent intent = new Intent(this, MainNavigationActivity.class);
        startActivity(intent);
    }

    private void showProgressBar() {
        findViewById(R.id.loginLoadingPanel).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        findViewById(R.id.loginLoadingPanel).setVisibility(View.GONE);
    }
}
