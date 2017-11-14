package co.edu.poli.passnote.passnote.accounts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import co.edu.poli.passnote.passnote.MainNavigationActivity;
import co.edu.poli.passnote.passnote.R;

import static co.edu.poli.passnote.passnote.utils.EncryptionUtils.decrypt;
import static co.edu.poli.passnote.passnote.utils.EncryptionUtils.encrypt;
import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showGeneralError;
import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showNotification;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SaveAccountFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference accountsCollRef = db.collection("accounts");
    private CollectionReference usersCollRef = db.collection("users");

    private View mFragmentInflatedView;
    private String mWebsiteName;
    private String mURL;
    private String mUsername;
    private String mPassword;

    private String mSelectedAccountId;
    private AccountItem mSelectedAccount;
    private DocumentReference mSelectedAccountReference;
    private TextView mTitle;
    private TextView mAccountNameTextView;
    private TextView mAccountUsernameTextView;
    private TextView mAccountURLTextView;
    private TextView mAccountPasswordTextView;
    private Button mCancelButton;
    private Button mSaveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            this.mFragmentInflatedView = inflater.inflate(R.layout.fragment_save_account, container, false);

            setupMemberVariables();
            setupAddOrUpdateVariables();
            addSaveButtonHandler();
            addCancelButtonHandler();

            return this.mFragmentInflatedView;
        } catch (Exception e) {
            showGeneralError(e);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void addCancelButtonHandler() {
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHome();
            }
        });
    }

    private void setupAddOrUpdateVariables() {
        Bundle fragmentArguments = getArguments();
        if (fragmentArguments != null && fragmentArguments.containsKey(AccountsFragment.KEY_SELECTED_ACCOUNT_ID) &&
                isNotBlank(fragmentArguments.getString(AccountsFragment.KEY_SELECTED_ACCOUNT_ID))) {
            mSelectedAccountId = fragmentArguments.getString(AccountsFragment.KEY_SELECTED_ACCOUNT_ID);
            mSelectedAccountReference = accountsCollRef.document(mSelectedAccountId);
            fillAccountInfo();
            mTitle.setText(getString(R.string.updateAccountTitle));
        } else {
            mTitle.setText(getString(R.string.addAccountTitle));
        }
    }

    private void addSaveButtonHandler() {
        if (isNotBlank(mSelectedAccountId)) {
            mSaveButton.setOnClickListener(new SaveAccountButtonListener());
        } else {
            mSaveButton.setOnClickListener(new AddAccountButtonListener());
        }
    }

    private void setupMemberVariables() {
        mTitle = mFragmentInflatedView.findViewById(R.id.addAccountTitle);
        mCancelButton = mFragmentInflatedView.findViewById(R.id.addAccountCancelBtn);
        mSaveButton = mFragmentInflatedView.findViewById(R.id.addAccountSaveBtn);
        mAccountNameTextView = mFragmentInflatedView.findViewById(R.id.addAccountName);
        mAccountUsernameTextView = mFragmentInflatedView.findViewById(R.id.addAccountUsername);
        mAccountURLTextView = mFragmentInflatedView.findViewById(R.id.addAccountURL);
        mAccountPasswordTextView = mFragmentInflatedView.findViewById(R.id.addAccountPassword);
    }

    private void fillAccountInfo() {
        mSelectedAccountReference
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        synchronizeLocalFieldsWithAccountInfo(documentSnapshot);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showGeneralError(e);
            }
        });
    }

    private void synchronizeLocalFieldsWithAccountInfo(DocumentSnapshot documentSnapshot) {
        mSelectedAccount = documentSnapshot.toObject(AccountItem.class);
        mAccountNameTextView.setText(mSelectedAccount.getName());
        mAccountURLTextView.setText(mSelectedAccount.getURL());
        mAccountUsernameTextView.setText(mSelectedAccount.getUsername());
        mAccountPasswordTextView.setText(decrypt(mSelectedAccount.getPassword()));
    }

    private void goToHome() {
        MainNavigationActivity activity = (MainNavigationActivity) getActivity();
        activity.showFragment(AccountsFragment.class);
    }

    private void findCurrentUserId(OnCompleteListener<QuerySnapshot> callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String currentUserEmail = user.getEmail();
            usersCollRef
                    .whereEqualTo("email", currentUserEmail)
                    .get()
                    .addOnCompleteListener(callback);
        }
    }

    private boolean validateParameters(AccountItem item) {
        if (isBlank(item.getName())) {
            showNotification(R.string.addAccountSiteNameRequired);
            return false;
        }
        if (isBlank(item.getURL())) {
            showNotification(R.string.addAccountURLRequired);
            return false;
        }
        if (isBlank(item.getUsername())) {
            showNotification(R.string.addAccountUsernameRequired);
            return false;
        }
        if (isBlank(item.getPassword())) {
            showNotification(R.string.addAccountPasswordRequired);
            return false;
        }

        return true;
    }

    private void readParameters() {
        mWebsiteName = mAccountNameTextView.getText().toString();
        mURL = mAccountURLTextView.getText().toString();
        mUsername = mAccountUsernameTextView.getText().toString();
        mPassword = mAccountPasswordTextView.getText().toString();
    }

    @NonNull
    private AccountItem getAccountItem() {
        final AccountItem newAccount = new AccountItem();
        newAccount.setImageEntryName("passnotelogowhite");
        newAccount.setName(mWebsiteName);
        newAccount.setURL(mURL);
        newAccount.setUsername(mUsername);
        newAccount.setPassword(encrypt(mPassword));
        return newAccount;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class AddAccountButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                readParameters();

                final AccountItem newAccount = getAccountItem();

                if (validateParameters(newAccount)) {
                    findCurrentUserId(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                String userId = "";
                                for (DocumentSnapshot user : task.getResult()) {
                                    userId = user.getId();
                                    break;
                                }
                                newAccount.setUserId(userId);

                                SaveAccountFragment.this.accountsCollRef.document().set(newAccount)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                showNotification(R.string.addAccountDataSavedOK);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        showGeneralError();
                                    }
                                });

                                goToHome();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                showGeneralError();
            }
        }
    }

    private class SaveAccountButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            try {
                readParameters();
                final AccountItem updatedAccount = getAccountItem();
                updatedAccount.setUserId(mSelectedAccount.getUserId());
                if (validateParameters(updatedAccount)) {
                    mSelectedAccountReference.set(updatedAccount)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    showNotification(R.string.addAccountDataSavedOK);
                                    goToHome();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showGeneralError(e);
                        }
                    });
                }
            } catch (Exception e) {
                showGeneralError();
            }
        }
    }
}
