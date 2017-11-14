package co.edu.poli.passnote.passnote.accounts;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.edu.poli.passnote.passnote.R;

public class AccountItemAdapter extends RecyclerView.Adapter<AccountItemAdapter.ViewHolder> {

    private List<AccountItem> accountItems;
    private Activity context;

    private int position;

    public AccountItemAdapter(List<AccountItem> accountItems, Activity context) {
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        AccountItem currentAccountItem = accountItems.get(position);

        holder.setImage(currentAccountItem.getLocalImageId());
        holder.setText(currentAccountItem.getName());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });

    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return accountItems.size();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private ImageView imageView;
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.accountItemImage);
            textView = itemView.findViewById(R.id.accountItemName);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void setImage(int idSource) {
            imageView.setImageResource(idSource);
        }

        public void setText(String text) {
            textView.setText(text);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuInflater menuInflater = context.getMenuInflater();
            menuInflater.inflate(R.menu.accounts_context_menu, menu);
        }
    }
}
