package com.example.sarapetrovic.astrologyapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by sarapetrovic on 7/7/19.
 */

public class SwipeFragment extends Fragment {

    String sign;
    ListView lvCompatibles;
    ArrayAdapter<String> adapter;
    ArrayList<String> displayNames;
    String userEmail;
    Button btnSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipe, container, false);

        lvCompatibles = view.findViewById(R.id.lvCompatibles);
        btnSearch = view.findViewById(R.id.btnSearch);

        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        sign = sharedPreferences.getString("sign", "not found");

        displayNames = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.user_info, R.id.tvUserInfo, displayNames);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> keys = new ArrayList<>();
                        for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                            keys.add(keyNode.getKey());
                            User user = keyNode.getValue(User.class);
                            displaySuggestions(user);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        return view;

    }

    public void displaySuggestions(User user){
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
            String compatible1 = sharedPreferences.getString("compatible1", "not found");
            String compatible2 = sharedPreferences.getString("compatible2", "not found");

            if (!user.getEmail().equals(userEmail)) {
                if (user.getSign().equals(compatible1) || user.getSign().equals(compatible2)) {
                    int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(user.getDateOB().split("/")[2].trim());
                    displayNames.add(user.getName() + ", " + age + "   " + user.getSign());
                    lvCompatibles.setAdapter(adapter);
                }
            }
        }
    }
}
