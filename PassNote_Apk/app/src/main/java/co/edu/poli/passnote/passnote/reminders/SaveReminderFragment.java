package co.edu.poli.passnote.passnote.reminders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
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



public class SaveReminderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static int hour, min;
    TextView txtdate, txttime;
    Button btntimepicker, btndatepicker;
    java.sql.Time timeValue;
    SimpleDateFormat format;
    Calendar c;
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





    public SaveReminderFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        txtdate = (TextView) mFragmentInflatedView.findViewById(R.id.Date);
        txttime = (TextView) mFragmentInflatedView.findViewById(R.id.Time);

        btndatepicker = (Button) mFragmentInflatedView.findViewById(R.id.btndatepicker);
        btntimepicker = (Button) mFragmentInflatedView.findViewById(R.id.btntimepicker);
*/

        /*
        btndatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date

                DatePickerDialog dd = new DatePickerDialog(SaveReminderFragment2.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    Date date = formatter.parse(dateInString);

                                    txtdate.setText(formatter.format(date).toString());

                                    formatter = new SimpleDateFormat("dd/MMM/yyyy");

                                    txtdate.setText(txtdate.getText().toString()+"\n"+formatter.format(date).toString());

                                    formatter = new SimpleDateFormat("dd-MM-yyyy");

                                    txtdate.setText(txtdate.getText().toString()+"\n"+formatter.format(date).toString());

                                    formatter = new SimpleDateFormat("dd.MMM.yyyy");

                                    txtdate.setText(txtdate.getText().toString()+"\n"+formatter.format(date).toString());

                                } catch (Exception ex) {

                                }


                            }
                        }, year, month, day);
                dd.show();
            }
        });
        btntimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TimePickerDialog td = new TimePickerDialog(SaveReminderFragment.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                try {
                                    String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                                    format = new SimpleDateFormat("HH:mm");

                                    timeValue = new java.sql.Time(format.parse(dtStart).getTime());
                                    txttime.setText(String.valueOf(timeValue));
                                    String amPm = hourOfDay % 12 + ":" + minute + " " + ((hourOfDay >= 12) ? "PM" : "AM");
                                    txttime.setText(amPm + "\n" + String.valueOf(timeValue));
                                } catch (Exception ex) {
                                    txttime.setText(ex.getMessage().toString());
                                }
                            }
                        },
                        hour, min,
                        DateFormat.is24HourFormat(SaveReminderFragment.this)
                );
                td.show();
            }
        });
        */

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            this.mFragmentInflatedView = inflater.inflate(R.layout.fragment_save_reminder, container,false);
            setUpVariables();
            addCancelButtonHandler();

            return this.mFragmentInflatedView;
        } catch (Exception e) {
            showGeneralError(e);
        }
        return super.onCreateView(inflater,container,savedInstanceState);
    }
    private void setUpVariables(){
        mTitle = mFragmentInflatedView.findViewById(R.id.addReminderTitle);
        dateButton = mFragmentInflatedView.findViewById(R.id.btndatepicker);
        timeButton = mFragmentInflatedView.findViewById(R.id.btntimepicker);
        mCancelButton = mFragmentInflatedView.findViewById(R.id.addReminderCancelBtn);
        mSaveButton = mFragmentInflatedView.findViewById(R.id.addReminderSaveBtn);
    }

    private void goToHome(){
        MainNavigationActivity activity= (MainNavigationActivity)getActivity();
        activity.showFragment(AccountsFragment.class);
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

    }

    private class SaveReminderButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {

        }
    }

    private boolean validateParameter(ReminderItem item){
        if(isBlank(item.getName())){
            showNotification(R.string.addReminderNameRequired);
            return false;
        }

        return true;
    }

    private class AddReminderButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {

        }
    }



}



