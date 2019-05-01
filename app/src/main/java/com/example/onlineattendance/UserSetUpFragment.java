package com.example.onlineattendance;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.onlineattendance.NavigationItem.GiveAttendanceFragment;


public class UserSetUpFragment extends Fragment {

    private EditText name_et, id_et;
    private Button setup_btn;
    private Spinner department_Sp, semester_sp, section_sp;
    private CheckSystem checkSystem;
    private UserPreference userPreference;
    private String bssid = "c8:3a:35:31:41:80";
    private Context context;


    public UserSetUpFragment() {
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_set_up, container, false);

        semester_sp = view.findViewById(R.id.semester_sp);
        name_et = view.findViewById(R.id.name_et);
        id_et = view.findViewById(R.id.id_et);
        setup_btn = view.findViewById(R.id.setup_btn);
        department_Sp = view.findViewById(R.id.department_Sp);
        section_sp = view.findViewById(R.id.section_sp);

        ArrayAdapter<CharSequence> departmentAdapter = ArrayAdapter.
                createFromResource(context, R.array.department, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> semesterAdapter = ArrayAdapter.
                createFromResource(context, R.array.semester, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> sectionAdapter = ArrayAdapter.
                createFromResource(context, R.array.section, android.R.layout.simple_spinner_dropdown_item);

        department_Sp.setAdapter(departmentAdapter);
        semester_sp.setAdapter(semesterAdapter);
        section_sp.setAdapter(sectionAdapter);

        setup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String semester = semester_sp.getSelectedItem().toString();
                String name = name_et.getText().toString();
                String id = id_et.getText().toString();
                String department = department_Sp.getSelectedItem().toString();
                String section = section_sp.getSelectedItem().toString();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(id)) {

                            boolean saveUser = userPreference.saveUser(name, id, department, semester, section);
                            if (saveUser) {
                                boolean setUserSituation = userPreference.setUserSituation("login");
                                if (setUserSituation) {
                                    Toast.makeText(context, "SetUp Successfully", Toast.LENGTH_SHORT).show();
                                    changeFragment(new GiveAttendanceFragment());
                                }
                                else {
                                    Toast.makeText(context, "Setup unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                else {
                    Toast.makeText(context, "All field required", Toast.LENGTH_SHORT).show();
                }




            }
        });

        return view;
    }

    public void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }

}
