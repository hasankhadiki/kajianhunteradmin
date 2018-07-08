package tehhutan.app.kajianhunteradmin.Menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tehhutan.app.kajianhunteradmin.Adapter.ViewPagerAdapter;
import tehhutan.app.kajianhunteradmin.BelumApproved;
import tehhutan.app.kajianhunteradmin.KajianApproved;
import tehhutan.app.kajianhunteradmin.R;

public class Kajian extends Fragment {
    private ViewPagerAdapter adapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private long jumlahTdkApprove ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setTitle("Kajian");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_kajian, container, false);
        mViewPager = (ViewPager)v.findViewById(R.id.container_vp);
        tabLayout = (TabLayout)v.findViewById(R.id.tabs);

        adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.AddFragment(new KajianApproved(), "POST DISETUJUI");
        adapter.AddFragment(new BelumApproved(), "TIDAK DISETUJUI");
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);

        for(int i=0;i<2;i++){
            TabLayout.Tab tab_ = tabLayout.getTabAt(i);
            tab_.setCustomView(adapter.getTabView(tab_.getPosition(), getActivity()));
        }
        DatabaseReference tdkDisetujui = FirebaseDatabase.getInstance().getReference("KajianList/Unverified");
        tdkDisetujui.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                jumlahTdkApprove = dataSnapshot.getChildrenCount();
                adapter.SetNotApprovedCount(String.valueOf(jumlahTdkApprove), tabLayout);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==1) {
                    adapter.SetOnSelectView(tabLayout, 1, getActivity());
                    adapter.SetOnUnSelectView(tabLayout, 0, getActivity());
                }else if(tab.getPosition()==0){
                    adapter.SetOnSelectView(tabLayout, 0, getActivity());
                    adapter.SetOnUnSelectView(tabLayout, 1, getActivity());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        /*
        for (int i = 0; i < 2; i++) {
            TabLayout.Tab tab = tabLayout.newTab()
                    .setText("tab name")
                    .setCustomView(R.layout.layout_notification_tablayout);
            tabLayout.addTab(tab);
        }*/
        return v;
    }

    public void setTitle(String title) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = new TextView(getActivity());
        textView.setText(title);
        textView.setTextSize(18);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);


        textView.setTextColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(textView);
    }
}
