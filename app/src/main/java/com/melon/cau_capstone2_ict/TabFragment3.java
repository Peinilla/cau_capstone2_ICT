package com.melon.cau_capstone2_ict;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.melon.cau_capstone2_ict.Manager.MyChatAdapter;

public class TabFragment3 extends Fragment {

    MyChatAdapter adapter;

    FloatingActionsMenu fam;
    FloatingActionsMenu add;
    ListView chatList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_chat, container, false);

        add = (FloatingActionsMenu)rootView.findViewById(R.id.chat_add);
        fam = (FloatingActionsMenu)rootView.findViewById(R.id.chat_floatingButton);
        chatList = (ListView) rootView.findViewById(R.id.chat_list);

        adapter = new MyChatAdapter();
        adapter.addItem(2,"369 하자","물 마시는 토끼");
        adapter.addItem(2,"너부터","물 마시는 토끼");
        adapter.addItem(2,"시작","물 마시는 토끼");

        adapter.addItem(0,"1","물 마시는 토끼");
        adapter.addItem(2,"2","물 마시는 토끼");
        adapter.addItem(0,"짝","물 마시는 토끼");
        adapter.addItem(2,"4","물 마시는 토끼");
        adapter.addItem(2,"중복 채팅 테스트","물 마시는 토끼");
        adapter.addItem(2,"줄바꿈 테스트 가나다라마바사아자차타파하","물 마시는 토끼");
        adapter.addItem(0,"test","물 마시는 토끼");

        chatList.setAdapter(adapter);
        add.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                fam.expand();
            }

            @Override
            public void onMenuCollapsed() {
                fam.collapse();
            }
        });

        return  rootView;
    }
}
