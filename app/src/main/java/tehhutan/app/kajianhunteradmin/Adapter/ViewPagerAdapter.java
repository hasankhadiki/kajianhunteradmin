package tehhutan.app.kajianhunteradmin.Adapter;

import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<android.support.v4.app.Fragment> lstFragment = new ArrayList<>();
    private final List<String> lstTitles = new ArrayList<>();
    private String[] judul = {"DISETUJUI", "BELUM DISETUJUI"};

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
}
