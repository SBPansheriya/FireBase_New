package com.kmsoft.firebase_new;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.kmsoft.firebase_new.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView_Adapter adapter;
    RecyclerView recyclerView;
    ActivityMainBinding binding;
//    DatabaseReference myRef;
//    FirebaseDatabase database;
    FirebaseFirestore firebaseFirestore;
    ArrayList<Users> usersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        recyclerView  =findViewById(R.id.recycler_view);
//        list = new ArrayList<>();
//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference().child("Contact");



//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                    Users modal = dataSnapshot1.getValue(Users.class);
//                    list.add(modal);
//                }
//                adapter.notifyDataSetChanged();
////                String value = dataSnapshot.getValue(String.class);
////                Log.d("TTT", "Value is: " + value);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Failed to read value
//                Log.w("TTT", "Failed to read value.", error.toException());
//            }
//        });
//
//        adapter = new RecyclerView_Adapter(MainActivity.this,list);
//        LinearLayoutManager manager= new LinearLayoutManager(getApplicationContext());
//        manager.setOrientation(RecyclerView.VERTICAL);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(adapter);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore.collection("Contact").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                usersArrayList = new ArrayList<>();

                if(!value.isEmpty()){
                    List<DocumentSnapshot> list = value.getDocuments();
                    for(DocumentSnapshot d : list){
                        Users users = new Users().getClassFromDocumentSnapshot(d);
                        usersArrayList.add(users);
                    }
                    adapter = new RecyclerView_Adapter(usersArrayList,MainActivity.this);
                    recyclerView.setAdapter(adapter);

                }else{
                    Toast.makeText(MainActivity.this, "No data found in database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Add_Activity.class);
                intent.putExtra("button","add");
                startActivity(intent);
            }
        });
    }
}