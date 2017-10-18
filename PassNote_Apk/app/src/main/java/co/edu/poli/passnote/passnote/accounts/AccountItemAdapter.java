package co.edu.poli.passnote.passnote.accounts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.edu.poli.passnote.passnote.R;

public class AccountItemAdapter extends RecyclerView.Adapter<AccountItemAdapter.ViewHolder> {

    private List<AccountItem> accountItems;
    private Context context;

    public AccountItemAdapter(List<AccountItem> accountItems, Context context) {
        this.accountItems = accountItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AccountItem currentAccountItem = accountItems.get(position);
        holder.setImage(currentAccountItem.getIconId());
        holder.setText(currentAccountItem.getText());
    }

    @Override
    public int getItemCount() {
        return accountItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.accountItemImage);
            textView = itemView.findViewById(R.id.accountItemName);
        }

        public void setImage(int idSource) {
            imageView.setImageResource(idSource);
        }

        public void setText(String text) {
            textView.setText(text);
        }
    }
}
