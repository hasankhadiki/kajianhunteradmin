package tehhutan.app.kajianhunteradmin.Menu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tehhutan.app.kajianhunteradmin.Adapter.ViewPagerAdapter;
import tehhutan.app.kajianhunteradmin.BelumApproved;
import tehhutan.app.kajianhunteradmin.KajianApproved;
import tehhutan.app.kajianhunteradmin.R;

public class Kajian extends Fragment {
    private ViewPagerAdapter adapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_kajian, container, false);
        mViewPager = (ViewPager)v.findViewById(R.id.container_vp);
        tabLayout = (TabLayout)v.findViewById(R.id.tabs);
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.AddFragment(new KajianApproved(), "POST DISETUJUI");
        adapter.AddFragment(new BelumApproved(), "TIDAK DISETUJUI");
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
        return v;
    }

}
