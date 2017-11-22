package co.edu.poli.passnote.passnote.reminders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showNotification;


import co.edu.poli.passnote.passnote.MainNavigationActivity;
import co.edu.poli.passnote.passnote.R;
import co.edu.poli.passnote.passnote.accounts.AccountItem;
import co.edu.poli.passnote.passnote.accounts.AccountsFragment;

import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showNotification;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showGeneralError;



public class SaveReminderFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference remindersCollRef = db.collection("reminders");
    private CollectionReference usersCollRef = db.collection("users");

    int year, month, day;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mFragmentInflatedView;

    private TextView mTitle;
    private Button dateButton;
    private Button timeButton;
    private Button mCancelButton;
    private Button mSaveButton;
    private TextView DateText;
    private TextView TimeText;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String reminderNameSave;
    private EditText reminderName;

    public SaveReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            this.mFragmentInflatedView = inflater.inflate(R.layout.fragment_save_reminder, container,false);
            setUpVariables();
            addCancelButtonHandler();
            setActualDate();
            setOnClickDate();
            setonClickTime();
            addSaveReminderButtonHandler();
            return this.mFragmentInflatedView;
        } catch (Exception e) {
            showGeneralError(e);
        }
        return super.onCreateView(inflater,container,savedInstanceState);
    }
    private void setUpVariables(){
        mTitle = mFragmentInflatedView.findViewById(R.id.addReminderTitle);
        reminderName = mFragmentInflatedView.findViewById(R.id.reminderName);
        dateButton = mFragmentInflatedView.findViewById(R.id.btndatepicker);
        timeButton = mFragmentInflatedView.findViewById(R.id.btntimepicker);
        mCancelButton = mFragmentInflatedView.findViewById(R.id.addReminderCancelBtn);
        mSaveButton = mFragmentInflatedView.findViewById(R.id.addReminderSaveBtn);
        DateText = mFragmentInflatedView.findViewById(R.id.Date);
        TimeText = mFragmentInflatedView.findViewById(R.id.Time);
    }

    private ReminderItem getReminderItem(){
        final ReminderItem reminderItem = new ReminderItem();
        reminderItem.setName(reminderNameSave);
        Calendar reminderTime=Calendar.getInstance();
        reminderTime.set(mYear,mMonth,mDay,mHour,mMinute);
        Timestamp time= new Timestamp(reminderTime.getTimeInMillis());
        reminderItem.setDate(time);
        return  reminderItem;
    }

    private void readParameters(){
        reminderNameSave=reminderName.getText().toString();
    }

    @Override
    public void onClick(View view) {
        if(view == dateButton){
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            MainNavigationActivity activity = (MainNavigationActivity) getActivity();

            DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            DateText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if(view == timeButton){

            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            MainNavigationActivity activity = (MainNavigationActivity) getActivity();

            TimePickerDialog timePickerDialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker,int hourOfDay,int minute) {
                    TimeText.setText(hourOfDay+":"+minute);
                }
            },mHour,mMinute,false);
            timePickerDialog.show();

        }
    }


    private void setOnClickDate(){
        dateButton.setOnClickListener(this);
    }
    private void setonClickTime(){
        timeButton.setOnClickListener(this);
    }
    private void setActualDate(){
        Calendar c = Calendar.getInstance();
        mDay=c.get(Calendar.DAY_OF_MONTH);
        mMonth = c.get(Calendar.MONTH+1);
        mYear = c.get(Calendar.YEAR);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        DateText.setText(mDay+"/"+mMonth+"/"+mYear);
        TimeText.setText(mHour+":"+mMinute);
    }

    private void goToHome(){
        MainNavigationActivity activity= (MainNavigationActivity)getActivity();
        activity.showFragment(ReminderFragment.class);
    }

    private void addCancelButtonHandler(){
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHome();
            }
        });
    }

    private void addSaveReminderButtonHandler(){
        mSaveButton.setOnClickListener(new AddReminderButtonListener());
    }

    private class SaveReminderButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            try{
                readParameters();
                //final ReminderItem reminderItem
            }catch (Exception e){
                showGeneralError();
            }
        }
    }

    private boolean validateParameter(ReminderItem item){
        if(isBlank(item.getName())){
            showNotification(R.string.addReminderNameRequired);
            return false;
        }

        return true;
    }

    private void showProgressBar() {
        mFragmentInflatedView.findViewById(R.id.RemindersLoadingPanel).setVisibility(View.VISIBLE);
    }
    private void hideProgressBar() {
        mFragmentInflatedView.findViewById(R.id.RemindersLoadingPanel).setVisibility(View.GONE);
    }


    private class AddReminderButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            try{
                readParameters();
                final ReminderItem newReminder = getReminderItem();
                if(validateParameter(newReminder)){
                    showProgressBar();
                    findCurrentUserId(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                String userId = "";
                                for(DocumentSnapshot user: task.getResult()){
                                    userId =user.getId();
                                    break;
                                }
                                newReminder.setUserId(userId);

                                SaveReminderFragment.this.remindersCollRef.document().set(newReminder).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        showNotification(R.string.addReminderDataSavedOK);
                                        hideProgressBar();
                                    }
                                });
                                goToHome();
                            }else{
                                hideProgressBar();
                            }
                        }
                    });
                }
            }catch (Exception e){
                showGeneralError();
            }
        }
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
}



