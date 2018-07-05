package tehhutan.app.kajianhunteradmin.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tehhutan.app.kajianhunteradmin.Menu.Kajian;
import tehhutan.app.kajianhunteradmin.R;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<android.support.v4.app.Fragment> lstFragment = new ArrayList<>();
    private final List<String> lstTitles = new ArrayList<>();
    private String[] judul = {"DISETUJUI", "PERLU DISETUJUI"};

    public ViewPagerAdapter(android.support.v4.app.FragmentManager fm){
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return lstFragment.get(position);
    }

    @Override
    public int getCount() {
        return lstTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return judul[position];
    }

    public void AddFragment(android.support.v4.app.Fragment fragment, String title){
        lstFragment.add(fragment);
        lstTitles.add(title);
    }

    public View getTabView(int position, Context context,String tdkApproveCount) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        //TextView judulTAB2 = (TextView)v.findViewById(R.id.judulTab2);
        View v;
        if(position==1){
            v = LayoutInflater.from(context).inflate(R.layout.layout_notification_tablayout, null);
            TextView tdkSetuju = (TextView)v.findViewById(R.id.jlm_belum_approve);
            RelativeLayout notifikasi = (RelativeLayout)v.findViewById(R.id.notif);
            if(tdkApproveCount.equals("0")){
                notifikasi.setVisibility(View.GONE);
            }else {
                tdkSetuju.setText(tdkApproveCount);
            }
            //judulTAB2.setTextColor(Color.parseColor("#A9A9A9"));
        }else{
            v = LayoutInflater.from(context).inflate(R.layout.layout_notification_tablayout1, null);
            TextView judulTAB2 = (TextView)v.findViewById(R.id.judulTab0);
            judulTAB2.setTextColor(context.getResources().getColor(R.color.darkerGrey));
          //  judulTAB2.setTextColor(Color.parseColor("#000000"));
        }
        return v;
    }

    public void SetOnSelectView(TabLayout tabLayout, int position, Context c) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        TextView iv_text;
        if(position==1){
            RelativeLayout notifBG = (RelativeLayout)selected.findViewById(R.id.notif);
            notifBG.getBackground().setColorFilter(Color.parseColor("#00b29d"), PorterDuff.Mode.SRC_ATOP);
            iv_text = (TextView) selected.findViewById(R.id.judulTab1);
        }else{
            iv_text = (TextView) selected.findViewById(R.id.judulTab0);
        }
        iv_text.setTextColor(c.getResources().getColor(R.color.darkerGrey));
    }

    public void SetOnUnSelectView(TabLayout tabLayout, int position, Context c) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        TextView iv_text;
        if(position==1){
            RelativeLayout notifBG = (RelativeLayout)selected.findViewById(R.id.notif);
            notifBG.getBackground().setColorFilter(c.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            iv_text = (TextView) selected.findViewById(R.id.judulTab1);
        }else{
            iv_text = (TextView) selected.findViewById(R.id.judulTab0);
        }
        iv_text.setTextColor(Color.parseColor("#6b6b6b"));
    }
}
