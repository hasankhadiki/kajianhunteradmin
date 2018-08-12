package tehhutan.app.kajianhunteradmin.app;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import tehhutan.app.kajianhunteradmin.Adapter.ViewPagerAdapter;
import tehhutan.app.kajianhunteradmin.BelumApproved;
import tehhutan.app.kajianhunteradmin.KajianApproved;
import tehhutan.app.kajianhunteradmin.Menu.Kajian;
import tehhutan.app.kajianhunteradmin.Menu.Profile;
import tehhutan.app.kajianhunteradmin.Menu.Timeline;
import tehhutan.app.kajianhunteradmin.R;

public class MainAct extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public View kajianDialog;
    public Double plcLatitude=86.0, plcLongtitude=181.0;
    //public String locationUri="";
    private final int PLACE_PICKER_REQUEST = 442;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.fragmentContainer, new Kajian());
        tx.commit();
    }
    private boolean loadFragment(Fragment fragment){
        if(fragment != null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_finding:
                fragment = new Kajian();
                break;
            case R.id.navigation_timeline:
                fragment = new Timeline();
                break;
            case R.id.navigation_profile:
                fragment = new Profile();
                break;
        }
        return loadFragment(fragment);
    }

    boolean click_duaKali=false;
    public void onBackPressed() {
        //  super.onBackPressed();
        if(click_duaKali){
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            System.exit(0);
        }
        Toast.makeText(MainAct.this, "Silahkan tekan kembali untuk keluar!", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                click_duaKali=false;
            }
        },3000);
        click_duaKali=true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Add your code here
        //Toast.makeText(MainAct.this, "Fragment Got it: " + requestCode + ", " + resultCode, Toast.LENGTH_SHORT).show();
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, MainAct.this);
            String address = String.format("%s", place.getAddress());
            EditText tempat = (EditText) kajianDialog.findViewById(R.id.et_deskripsikegiatan);
            plcLatitude = place.getLatLng().latitude;
            plcLongtitude = place.getLatLng().longitude;
          //  locationUri = String.format("%s", String.valueOf(place.getWebsiteUri()));
            //Toast.makeText(MainAct.this, "alamat " + address + "\nlatitude : " + String.valueOf(latitude) + "\nlongtitude : " + String.valueOf(longitude), Toast.LENGTH_LONG).show();
            tempat.setText(address);
        }
    }
}
