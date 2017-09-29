package co.edu.poli.passnote.passnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class Login extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
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
        EditText usernameField = findViewById(R.id.loginUsername);
        EditText passwordField = findViewById(R.id.loginPassword);

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if (isNotBlank(username) && isNotBlank(password)) {
            login(username, password);
        } else {
            Toast.makeText(Login.this, R.string.login_credentials_required,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(Login.this, R.string.login_authentication_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            goToAccounts();
                        }
                    }
                });
    }

    private void goToAccounts() {
        Intent intent = new Intent(this, AccountsActivity.class);
        startActivity(intent);
    }
}
