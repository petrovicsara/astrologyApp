package com.example.sarapetrovic.astrologyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

/**
 * Created by sarapetrovic on 7/7/19.
 */

public class ProfileFragment extends Fragment {
    TextView tvNameAge;
    EditText etDescription;
    private String name;
    private String dob;
    private String description;
    private String gender;
    private String prefGender;
    ImageView ivProfilePic;
    ImageButton ivEditDescription;
    Button btnSave, btnDelete, btnLogOut;
    RadioGroup rgGender, rgPrefGender;
    RadioButton radioButton;

    public ProfileFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_profile, container, false);

        tvNameAge = v.findViewById(R.id.tvNameAge);
        etDescription = v.findViewById(R.id.etDescription);
        ivProfilePic = v.findViewById(R.id.ivProfilePic);
        ivEditDescription = v.findViewById(R.id.ivEditDescription);
        btnSave = v.findViewById(R.id.btnSave);
        btnDelete = v.findViewById(R.id.btnDeleteAcc);
        btnLogOut = v.findViewById(R.id.btnLogOut);
        rgGender = v.findViewById(R.id.rgGender);
        rgPrefGender = v.findViewById(R.id.rgPrefGender);

        loadData();
//        int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(dob.split("/")[2].trim());
        tvNameAge.setText(name);
        etDescription.setText(description);

        if (gender.equals("female")){
            rgGender.check(R.id.female);
        }else if (gender.equals("male")){
            rgGender.check(R.id.male);
        }else rgGender.check(R.id.other);
        if (prefGender.equals("female")){
            rgPrefGender.check(R.id.prefFemale);
        }else if (prefGender.equals("male")){
            rgPrefGender.check(R.id.prefMale);
        }else rgPrefGender.check(R.id.prefOther);

        ivEditDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etDescription.setEnabled(true);
                etDescription.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = etDescription.getText().toString();
                String gender = getCheckedRadioButton(v, rgGender.getCheckedRadioButtonId());
                String prefGender = getCheckedRadioButton(v, rgPrefGender.getCheckedRadioButtonId());
                updateData(description, gender, prefGender);
                updateFirebase(description, gender, prefGender);
                Toast.makeText(getActivity(), "Data updated!", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                firebaseAuth.signOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseReference.removeValue();
                getActivity().finish();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        return v;
    }

    private void updateData(String description, String gender, String prefGender) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (description != ""){
            editor.putString("description", description);
        }
        editor.putString("gender", gender);
        editor.putString("genderPreference", prefGender);
        editor.apply();
    }

    private void updateFirebase(String description, String gender, String prefGender) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.child("description").setValue(description);
        databaseReference.child("gender").setValue(gender);
        databaseReference.child("genderPreference").setValue(prefGender);
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE);
        name = sharedPreferences.getString("name", "Not found");
        dob = sharedPreferences.getString("dob","not found");
        description = sharedPreferences.getString("description", "not found");
        gender = sharedPreferences.getString("gender", "not found");
        prefGender = sharedPreferences.getString("prefGender", "not found");
    }

    public String getCheckedRadioButton(View v, int radioButtonID){
        radioButton = v.findViewById(radioButtonID);
        return radioButton.getText().toString();
    }
}
