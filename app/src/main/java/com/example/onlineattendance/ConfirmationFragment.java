package com.example.onlineattendance;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;


public class ConfirmationFragment extends Fragment {


    private Button confirm_code_btn;
    private EditText confirm_code_et;
    private DatabaseReference databaseReference;
    private Context context;
    private CheckSystem checkSystem;
    //private String bssid = "b0:7f:b9:8a:ae:44";
    private String bssid = "c8:3a:35:31:41:80";
    AlertDialog dialog;

    public ConfirmationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirmation, container, false);

        confirm_code_btn = view.findViewById(R.id.confirm_code_btn);
        confirm_code_et = view.findViewById(R.id.confirm_code_et);
        checkSystem = new CheckSystem(context);
        dialog = new SpotsDialog.Builder().setContext(context)
                .setCancelable(false).setTheme(R.style.login).build();

        databaseReference = FirebaseDatabase.getInstance().getReference("Security_Code");

        confirm_code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String confirmCode = confirm_code_et.getText().toString();

                if (!TextUtils.isEmpty(confirmCode)) {
                    //filed edit text
                    if (checkSystem.haveInternetConnection()) {
                        if (checkSystem.isConnectedToSpecificWifi(bssid)) {
                            getDataFromFireBase(confirmCode);
                            dialog.show();
                        }
                        else {
                            Toast.makeText(context, "You are not connected to DIU wifi", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(context, "Please Enter Confirmation Code First", Toast.LENGTH_SHORT).show();
                }


            }
        });


        return view;
    }

    private void getDataFromFireBase(final String confirmCode) {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String securityCode = dataSnapshot.getValue(String.class);
                    if (confirmCode.equals(securityCode)) {
                        dialog.dismiss();
                        Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        UserSetUpFragment userSetUpFragment = new UserSetUpFragment();
                        fragmentTransaction.replace(R.id.container, userSetUpFragment);
                        fragmentTransaction.commit();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(context, "Wrong security code", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(context, "Security code not set yet", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
