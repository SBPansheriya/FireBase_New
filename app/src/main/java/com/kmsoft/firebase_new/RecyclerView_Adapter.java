package com.kmsoft.firebase_new;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerView_Adapter.viewholder> {
    MainActivity context;
    ArrayList<Users> usersArrayList;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Users users;

    public RecyclerView_Adapter(ArrayList<Users> usersArrayList, MainActivity context) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        viewholder holder = new viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, @SuppressLint("RecyclerView") int position) {
        users = usersArrayList.get(position);
        System.out.println("" + users);
        holder.name.setText(users.getName());
        holder.number.setText(users.getNumber());

        Log.d("OOO", "onBindViewHolder: " + users.getImg());
        Glide.with(context).load(users.getImg()).centerCrop().into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.itemView, Gravity.RIGHT);
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());
                firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseStorage = FirebaseStorage.getInstance();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        users = usersArrayList.get(holder.getAdapterPosition());
                        if (menuItem.getItemId() == R.id.update) {
                            Intent intent = new Intent(context, Add_Activity.class);
                            intent.putExtra("button", "update");
                            intent.putExtra("id", users.getUid());
                            intent.putExtra("position", position);
                            intent.putExtra("name", users.getName());
                            intent.putExtra("number", users.getNumber());
                            intent.putExtra("img", users.getImg());
                            context.startActivity(intent);
                        }
                        if (menuItem.getItemId() == R.id.delete) {
                            StorageReference storageRef = firebaseStorage.getReferenceFromUrl(users.getImg());
                            storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                }
                            });

                            firebaseFirestore.collection("Contact").document(users.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    notifyDataSetChanged();
                                    Log.d("TTT", "onSuccess: " + users.getUid());
                                    Log.d("TTT", "DocumentSnapshot successfully deleted!");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TTT", "Error deleting document", e);
                                }
                            });
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView name, number;
        CircleImageView imageView;
        LinearLayout linearLayout;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recycname);
            number = itemView.findViewById(R.id.recycnumber);
            imageView = itemView.findViewById(R.id.recycimage);
            linearLayout = itemView.findViewById(R.id.linear);
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    Log.d("TTT", "onLongClick: ");
//                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//                    Query query = ref.child("Contact").orderByChild("id").equalTo(list.get(getAdapterPosition()).uid);
//
//                    query.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @SuppressLint("NotifyDataSetChanged")
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
//                                dataSnapshot1.getRef().removeValue();
//                                notifyDataSetChanged();
//                            }
//                        }
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            Log.e("TTT", "onCancelled", databaseError.toException());
//                        }
//                    });
//                    return true;
//                }
//            });
        }
    }
}