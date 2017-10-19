package co.edu.poli.passnote.passnote.reminders;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.edu.poli.passnote.passnote.R;
import co.edu.poli.passnote.passnote.utils.NotificationUtils;


/**
 * Created by johansteven on 19/10/2017.
 */

public class ReminderFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<ReminderItemAdapter.ViewHolder> adapter;
    private List<ReminderItem> reminderItemList;

    private FirebaseFirestore db;
    private CollectionReference remindersCollection;
    private CollectionReference usersCollection;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_reminders, container, false);
        } catch (Exception e) {
            NotificationUtils.showGeneralError(getContext(), e);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        try {
            showProgressBar();
            db = FirebaseFirestore.getInstance();
            remindersCollection = db.collection("reminders");
            usersCollection = db.collection("users");

            recyclerView = getActivity().findViewById(R.id.remindersRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(false);

            loadReminders();
            super.onActivityCreated(savedInstanceState);
        } catch (Exception e) {
            NotificationUtils.showGeneralError(getContext(), e);
        }
    }

    private void loadReminders() {
        findCurrentUserId(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String userId = "";
                    for (DocumentSnapshot user : task.getResult()) {
                        userId = user.getId();
                        break;
                    }
                    findReminders(userId, new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                reminderItemList = new ArrayList<ReminderItem>();
                                for (DocumentSnapshot reminder : task.getResult()) {
                                    String text = reminder.getString("name");
                                    Date time = (Date) reminder.get("fecha");
                                    reminderItemList.add(new ReminderItem(text, new Timestamp(time.getTime())));
                                }
                                adapter = new ReminderItemAdapter(reminderItemList, getContext());
                                recyclerView.setAdapter(adapter);
                                hideProgressBar();
                            } else {
                                hideProgressBar();
                                NotificationUtils.showGeneralError(getContext());
                            }
                        }
                    });

                } else {
                    hideProgressBar();
                    NotificationUtils.showGeneralError(getContext());
                }
            }
        });

    }

    private void findCurrentUserId(OnCompleteListener<QuerySnapshot> callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String currentUserEmail = user.getEmail();
            usersCollection.whereEqualTo("email", currentUserEmail).get().addOnCompleteListener(callback);
        }
    }

    private void findReminders(String userId, OnCompleteListener<QuerySnapshot> callback) {
        remindersCollection.whereEqualTo("userId", userId).get().addOnCompleteListener(callback);
    }

    private void showProgressBar() {
        getActivity().findViewById(R.id.remindersLoadingPanel).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        getActivity().findViewById(R.id.remindersLoadingPanel).setVisibility(View.GONE);
    }
}
