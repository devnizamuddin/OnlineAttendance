package com.example.onlineattendance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.onlineattendance.NavigationItem.GiveAttendanceFragment;
import com.example.onlineattendance.NavigationItem.RoutineViewFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_attendance:
                    if (userPreference.getUserSituation()){
                        changeFragment(new GiveAttendanceFragment());
                    }
                    else {
                        changeFragment(new ConfirmationFragment());
                    }
                    return true;
                case R.id.navigation_routine:
                    changeFragment(new RoutineViewFragment());
                    return true;
            }
            return false;
        }
    };

    UserPreference userPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

         userPreference = new UserPreference(this);
        if (userPreference.getUserSituation()){
            GiveAttendanceFragment giveAttendanceFragment = new GiveAttendanceFragment();
            changeFragment(giveAttendanceFragment);
        }
        else {
            ConfirmationFragment confirmationFragment = new ConfirmationFragment();
            changeFragment(confirmationFragment);
        }



    }
    public void changeFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();

    }
}
