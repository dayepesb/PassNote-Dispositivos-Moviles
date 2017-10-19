package co.edu.poli.passnote.passnote.reminders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.edu.poli.passnote.passnote.R;
import co.edu.poli.passnote.passnote.accounts.AccountItem;
import co.edu.poli.passnote.passnote.accounts.AccountItemAdapter;


/**
 * Created by julianCastro on 19/10/2017.
 */

public class ReminderItemAdapter extends RecyclerView.Adapter<ReminderItemAdapter.ViewHolder> {
    private List<ReminderItem> reminderItems;
    private Context context;

    public ReminderItemAdapter(List<ReminderItem>reminderItems,Context context){
        this.reminderItems=reminderItems;
        this.context=context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReminderItem currentReminderItem=reminderItems.get(position);

        holder.setText(currentReminderItem.getName());
        holder.setImage(100);
    }

    @Override
    public int getItemCount() {
        return reminderItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.reminderItemImage);
            textView = itemView.findViewById(R.id.reminderItemName);
        }

        public void setImage(int idSource) {
            imageView.setImageResource(idSource);
        }

        public void setText(String text) {
            textView.setText(text);
        }
    }
}
