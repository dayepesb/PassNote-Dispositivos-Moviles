package co.edu.poli.passnote.passnote.notes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
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

import co.edu.poli.passnote.passnote.R;
import co.edu.poli.passnote.passnote.utils.NotificationUtils;


import java.util.ArrayList;
import java.util.List;

import static co.edu.poli.passnote.passnote.utils.ImageUtils.getImageIdByName;
import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showGeneralError;

/**
 * Created by johansteven on 19/10/2017.
 */

public class NotesFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<NotesItemAdapter.ViewHolder>adapter;
    private List<NotesItem>notesItemList;

    private FirebaseFirestore db;
    private CollectionReference notesCollection;
    private CollectionReference usersCollection;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view=null;
        try {
            view= inflater.inflate(R.layout.fragment_notes,container,false);
        }catch (Exception e){
            NotificationUtils.showGeneralError(getContext(),e);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        try {
            showProgressBar();
            db = FirebaseFirestore.getInstance();
            notesCollection = db.collection("notes");
            usersCollection = db.collection("users");

            recyclerView = getActivity().findViewById(R.id.notesRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(false);

            loadNotes();
            super.onActivityCreated(savedInstanceState);
        }catch (Exception e){
            NotificationUtils.showGeneralError(getContext(),e);
        }
    }

    private void loadNotes(){
        findCurrentUserId(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    String userId="";
                    for (DocumentSnapshot user: task.getResult()){
                        userId=user.getId();
                        break;
                    }
                    findNotes(userId, new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                notesItemList =new ArrayList<NotesItem>();
                                for(DocumentSnapshot note: task.getResult()){
                                    int imageResorceId = getImageIdByName(getContext(),"notesicon");
                                    String text = note.getString("name");
                                    String nota =note.getString("note");
                                    notesItemList.add(new NotesItem(text,nota));
                                }
                                adapter = new NotesItemAdapter(notesItemList,getContext());
                                recyclerView.setAdapter(adapter);
                                hideProgressBar();
                            }else{
                                hideProgressBar();
                                showGeneralError(getContext());
                            }
                        }
                    });
                }else{
                    hideProgressBar();
                    showGeneralError(getContext());
                }
            }
        });
    }

    private void findCurrentUserId(OnCompleteListener<QuerySnapshot>callback){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            String currentUserEmail = user.getEmail();
            usersCollection.whereEqualTo("email",currentUserEmail).get().addOnCompleteListener(callback);
        }
    }

    private void findNotes(String userId, OnCompleteListener<QuerySnapshot>callback){
        notesCollection.whereEqualTo("userId",userId).get().addOnCompleteListener(callback);
    }

    private void showProgressBar(){
        getActivity().findViewById(R.id.notesLoadingPanel).setVisibility(View.VISIBLE);
    }
    private void hideProgressBar(){
        getActivity().findViewById(R.id.notesLoadingPanel).setVisibility(View.GONE);
    }
}
