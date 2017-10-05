package co.edu.poli.passnote.passnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static co.edu.poli.passnote.passnote.utils.FieldUtils.getTextFromField;
import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showGeneralError;
import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showNotification;

public class SignupActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
    }

    public void signup(View view) {
        try {
            String email = getTextFromField(findViewById(R.id.signupEmail));
            String password = getTextFromField(findViewById(R.id.signupPassword));
            String confirmPassword = getTextFromField(findViewById(R.id.signupConfirmPassword));

            if (StringUtils.equals(password, confirmPassword)) {
                signup(email, password);
            } else {
                showNotification(this, R.string.signupErrorPasswordMismatch);
            }
        } catch (Exception e) {
            showGeneralError(this, e);
        }
    }

    private void signup(String email, String password) {
        showProgressBar();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressBar();
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            showNotification(SignupActivity.this, R.string.login_authentication_failed);
                        } else {
                            createUserInDatabase();
                            goToLogin();
                        }
                    }
                });
    }

    private void createUserInDatabase() {
        String names = getTextFromField(findViewById(R.id.signupNames));
        String surnames = getTextFromField(findViewById(R.id.signupSurnames));
        String email = getTextFromField(findViewById(R.id.signupEmail));
        String username = getTextFromField(findViewById(R.id.signupUsername));

        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("names", names);
        user.put("surnames", surnames);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void goToLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    private void showProgressBar() {
        findViewById(R.id.signupLoadingPanel).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        findViewById(R.id.signupLoadingPanel).setVisibility(View.GONE);
    }
}
