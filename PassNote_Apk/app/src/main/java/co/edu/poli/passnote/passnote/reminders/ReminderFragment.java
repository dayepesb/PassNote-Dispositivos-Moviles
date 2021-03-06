package co.edu.poli.passnote.passnote.reminders;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.edu.poli.passnote.passnote.MainNavigationActivity;
import co.edu.poli.passnote.passnote.R;
import co.edu.poli.passnote.passnote.accounts.AccountItem;
import co.edu.poli.passnote.passnote.accounts.AccountItemAdapter;
import co.edu.poli.passnote.passnote.utils.NotificationUtils;

import static co.edu.poli.passnote.passnote.Application.getAppContext;
import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showGeneralError;

public class ReminderFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<ReminderItemAdapter.ViewHolder> adapter;
    private List<ReminderItem> reminderItemList;

    private FirebaseFirestore db;
    private CollectionReference remindersCollection;
    private CollectionReference usersCollection;
    private int mSelectedReminderPosition;
    private AccountItemAdapter mAdapter;
    private List<ReminderItem> mReminderItems;


    private View fragmentInflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            fragmentInflatedView = inflater.inflate(R.layout.fragment_reminders, container, false);
            FloatingActionButton addReminderBtn= fragmentInflatedView.findViewById(R.id.remindersFab);
            addReminderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showFragment(SaveReminderFragment.class);
                }
            });
        } catch (Exception e) {
            NotificationUtils.showGeneralError(e);
        }
        return fragmentInflatedView;
    }
    private void showFragment(Class fragment) {
        showFragment(fragment, null);
    }
    private void showFragment(Class fragment, Bundle bundle) {
        MainNavigationActivity parentActivity =
                (MainNavigationActivity) ReminderFragment.this.getActivity();
        parentActivity.showFragment(fragment, bundle);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            showProgressBar();
            db = FirebaseFirestore.getInstance();
            remindersCollection = db.collection("reminders");
            usersCollection = db.collection("users");

            recyclerView = fragmentInflatedView.findViewById(R.id.remindersRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getAppContext()));
            recyclerView.setHasFixedSize(false);
            recyclerView.addOnItemTouchListener(new ReminderItemClickListener(getActivity(), recyclerView, new ReminderItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                 mSelectedReminderPosition=position;
                 recyclerView.showContextMenuForChild(view);
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            }));
            registerForContextMenu(recyclerView);

            loadReminders();
        } catch (Exception e) {
            NotificationUtils.showGeneralError(e);
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
                                    //Timestamp time = (Timestamp) reminder.get("fecha");
                                    Timestamp time= new Timestamp(1);
                                    reminderItemList.add(new ReminderItem(text,time));
                                }
                                adapter = new ReminderItemAdapter(reminderItemList);
                                recyclerView.setAdapter(adapter);
                                hideProgressBar();
                            } else {
                                hideProgressBar();
                                NotificationUtils.showGeneralError();
                            }
                        }
                    });

                } else {
                    hideProgressBar();
                    NotificationUtils.showGeneralError();
                }
            }
        });

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position =-1;
        try{
            position=mAdapter.getPosition();
        }catch (Exception e){
            showGeneralError();
            return super.onContextItemSelected(item);
        }
        ReminderItem reminder = ReminderFragment.this.reminderItemList.get(mSelectedReminderPosition);
        switch (item.getItemId()){

        }
        return true;
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
        fragmentInflatedView.findViewById(R.id.remindersLoadingPanel).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        fragmentInflatedView.findViewById(R.id.remindersLoadingPanel).setVisibility(View.GONE);
    }
}
