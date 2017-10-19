package co.edu.poli.passnote.passnote.notes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.edu.poli.passnote.passnote.R;

import static co.edu.poli.passnote.passnote.utils.ImageUtils.getImageIdByName;

/**
 * Created by julianCastro on 19/10/2017.
 */

public class NotesItemAdapter extends RecyclerView.Adapter<NotesItemAdapter.ViewHolder> {

    private List<NotesItem> noteItems;

    public NotesItemAdapter(List<NotesItem> noteItems) {
        this.noteItems = noteItems;
    }

    @Override
    public NotesItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        NotesItem currentNoteItem = noteItems.get(position);

        holder.setText(currentNoteItem.getText());
        holder.setImage(R.drawable.notesicon);

    }

    public int getItemCount() {
        return noteItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.notesItemImage);
            textView = itemView.findViewById(R.id.noteItemName);
        }

        public void setImage(int idSource) {
            imageView.setImageResource(idSource);
        }

        public void setText(String text) {
            textView.setText(text);
        }
    }
}
