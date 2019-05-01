package com.example.onlineattendance.NavigationItem;


import android.app.AlertDialog;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineattendance.CheckSystem;
import com.example.onlineattendance.Pojo.ClassPoJo;
import com.example.onlineattendance.Pojo.User;
import com.example.onlineattendance.R;
import com.example.onlineattendance.UserPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dmax.dialog.SpotsDialog;


public class GiveAttendanceFragment extends Fragment {


    private TextView semester_tv, department_tv, id_tv, name_tv, subject_tv, teacher_tv, section_tv;
    private Button give_attendance_btn;
    private Context context;
    private DatabaseReference databaseReference, attendanceDatabaseReference;
    private Calendar calendar;
    private int hour, minute, day, dayOfMonth, month, year;
    private User user;
    private CheckSystem checkSystem;
    private UserPreference userPreference;
    //private String bssid = "b0:7f:b9:8a:ae:44";
    private String bssid = "c8:3a:35:31:41:80";
    AlertDialog dialog;

    public GiveAttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        checkSystem = new CheckSystem(context);
        userPreference = new UserPreference(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_give_attendance, container, false);

        name_tv = view.findViewById(R.id.name_tv);
        id_tv = view.findViewById(R.id.id_tv);
        department_tv = view.findViewById(R.id.department_tv);
        semester_tv = view.findViewById(R.id.semester_tv);
        subject_tv = view.findViewById(R.id.subject_tv);
        teacher_tv = view.findViewById(R.id.teacher_tv);
        section_tv = view.findViewById(R.id.section_tv);
        give_attendance_btn = view.findViewById(R.id.give_attendance_btn);

        dialog = new SpotsDialog.Builder().setContext(context)
                .setCancelable(false).setTheme(R.style.normal).build();
        give_attendance_btn.setEnabled(false);
        //***********getting user information from Shared Preference**************///

        user = userPreference.getUserObject();
        if (user != null) {
            name_tv.setText("Name : " + user.getName());
            id_tv.setText("Id : " + user.getId());
            department_tv.setText("Department : " + user.getDepartment());
            semester_tv.setText("Semester : " + user.getSemester());
            section_tv.setText("Section : " + user.getSection());
        }

        //*****************get calender information*********************/////////
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK);

        //****************get information from database********************////

        databaseReference = FirebaseDatabase.getInstance().getReference("Routine");
        attendanceDatabaseReference = FirebaseDatabase.getInstance().getReference("Attendance");

        if (checkSystem.haveInternetConnection()) {
            if (checkSystem.isConnectedToSpecificWifi(bssid)) {
                dialog.show();
                gettingPreliminaryDataFromFireBase();
            }
            else {
                Toast.makeText(context, "You are not connected to DIU wifi", Toast.LENGTH_SHORT).show();
            }
        }


        give_attendance_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                User userSend = new User(user.getName(), user.getId());

                attendanceDatabaseReference.child(user.getDepartment()).child(user.getSemester())
                        .child(user.getSection())
                        .child(subject_tv.getText().toString())
                        .child(getCurrentDate()).child(user.getId()).setValue(userSend)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });
        return view;
    }

    public void timeCheck(ClassPoJo classPoJo) {
        SimpleDateFormat parser = new SimpleDateFormat("kk:mm");
        Date start = null;

        try {
            start = parser.parse(classPoJo.getStartingTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date end = null;
        try {
            end = parser.parse(classPoJo.getEndingTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            Date userDate = parser.parse(getCurrentTime());
            if (userDate.after(start) && userDate.before(end)) {

                subject_tv.setText(classPoJo.getSubjectName());
                teacher_tv.setText(classPoJo.getTeacherName());
                give_attendance_btn.setEnabled(true);

            } else {
               // Toast.makeText(context, "No class is happening now", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            // Invalid date was entered
        }
    }

    private String getDayOfWeek(int value) {
        String day = "";
        switch (value) {
            case 1:
                day = "Sun";
                break;
            case 2:
                day = "Mon";
                break;
            case 3:
                day = "Tue";
                break;
            case 4:
                day = "Wed";
                break;
            case 5:
                day = "Thu";
                break;
            case 6:
                day = "Fri";
                break;
            case 7:
                day = "Sat";
                break;
        }
        return day;
    }

    private String getCurrentTime() {

        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
        calendar.set(0, 0, 0, hour, minute);
        String finalTime = sdf.format(calendar.getTime());

        return finalTime;
    }

    private String getCurrentDate() {

        calendar = Calendar.getInstance();
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        calendar.set(year, month, dayOfMonth);
        String finalDate = sdf.format(calendar.getTime());
        return finalDate;

    }

    private void gettingPreliminaryDataFromFireBase() {


        databaseReference.child(user.getDepartment()).
                child(user.getSemester()).
                child(user.getSection()).
                child(getDayOfWeek(day)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot classInfo : dataSnapshot.getChildren()) {

                        ClassPoJo classPoJo = classInfo.getValue(ClassPoJo.class);
                        timeCheck(classPoJo);
                    }
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    Toast.makeText(context, "No classes Today", Toast.LENGTH_SHORT).show();
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
