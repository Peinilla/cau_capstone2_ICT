package com.melon.cau_capstone2_ict;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.melon.cau_capstone2_ict.Manager.MyUserData;

public class TabFragment_profile extends Fragment {

    TextView idView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_profile, container, false);

        idView = rootView.findViewById(R.id.profile_ID);
        idView.setText(MyUserData.getInstance().getId());

        return rootView;
    }
}
