package tehhutan.app.kajianhunteradmin;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import tehhutan.app.kajianhunteradmin.Interface.ItemClickListener;
import tehhutan.app.kajianhunteradmin.ViewHolder.MenuViewHolder;
import tehhutan.app.kajianhunteradmin.model.BookingList;


public class KajianApproved extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference bookinglist;

    private RecyclerView recyclerBookingList;
    private RecyclerView.LayoutManager layoutManager;

    private BookingList newBooking;

    private EditText editNamaPeminjam, editOrganisasi, editKegiatan, editJamMulai, editJamAkhir;
    private Button btnSubmit;
    private ImageView pickPlace;
    private Double plcLatitude=0.0, plcLongtitude=0.0;
    private final int PLACE_PICKER_REQUEST = 442;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_kajian_approved, container, false);
        //Toolbar toolbar = (Toolbar)v.findViewById(R.id.toolbar);
     //   toolbar.setTitle("Kajian List");
       // ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        bookinglist = database.getReference("KajianList/Verified");



        FloatingActionButton fab = (FloatingActionButton)v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.add_booking, null);
                editNamaPeminjam = (EditText) mView.findViewById(R.id.et_namapeminjam);
                editOrganisasi = (EditText) mView.findViewById(R.id.et_organisasi);
                editKegiatan = (EditText) mView.findViewById(R.id.et_deskripsikegiatan);
                editJamMulai = (EditText) mView.findViewById(R.id.et_jammulai);
                editJamAkhir = (EditText) mView.findViewById(R.id.et_jamakhir);
                btnSubmit = (Button) mView.findViewById(R.id.btn_submit);
                pickPlace = (ImageView) mView.findViewById(R.id.pickPlace);

                pickPlace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                        Intent intent;
                        try {
                            intent = builder.build(getActivity());
                            startActivityForResult(intent, PLACE_PICKER_REQUEST);
                        } catch (GooglePlayServicesRepairableException e){
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e){
                            e.printStackTrace();
                        }

                    }
                });



                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                editJamMulai.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final Calendar c = Calendar.getInstance();
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),

                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        editJamMulai.setText(hourOfDay + ":" + minute);
                                    }
                                }, hour, minute, false);
                        timePickerDialog.show();

                    }
                });


                editJamAkhir.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final Calendar c = Calendar.getInstance();
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        editJamAkhir.setText(hourOfDay + ":" + minute);
                                    }
                                }, hour, minute, false);
                        timePickerDialog.show();

                    }
                });


                // set listeners
                editNamaPeminjam.addTextChangedListener(mTextWatcher);
                editOrganisasi.addTextChangedListener(mTextWatcher);
                editKegiatan.addTextChangedListener(mTextWatcher);
                editJamMulai.addTextChangedListener(mTextWatcher);
                editJamAkhir.addTextChangedListener(mTextWatcher);

                // run once to disable if empty
                checkFieldsForEmptyValues();

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ConnectivityManager connectivity = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
                        if (activeNetwork != null) { // connected to the internet
                            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                                // connected to wifi
                                BookingList newBooking = new BookingList(editOrganisasi.getText().toString()
                                        , editNamaPeminjam.getText().toString()
                                        , editKegiatan.getText().toString()
                                        , editJamMulai.getText().toString()
                                        , editJamAkhir.getText().toString()
                                );
                                bookinglist.push().setValue(newBooking);
                                dialog.dismiss();
                            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                                // connected to the mobile provider's data plan
                                BookingList newBooking = new BookingList(editOrganisasi.getText().toString()
                                        , editNamaPeminjam.getText().toString()
                                        , editKegiatan.getText().toString()
                                        , editJamMulai.getText().toString()
                                        , editJamAkhir.getText().toString()
                                );
                                bookinglist.push().setValue(newBooking);
                                dialog.dismiss();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Internet Connection Not Available, Please Check Your Connection Setting", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        });

        //Load Booking List
        recyclerBookingList = (RecyclerView)v.findViewById(R.id.recycler_booking);
        recyclerBookingList.hasFixedSize();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerBookingList.setLayoutManager(layoutManager);
        loadBookingList();
        return v;
    }



    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PLACE_PICKER_REQUEST && requestCode== Activity.RESULT_OK){
            Place place = PlacePicker.getPlace(getActivity(), data);
            String address = String.format("", place.getAddress());
            double latitude = place.getLatLng().latitude;
            double longitude = place.getLatLng().longitude;
            editKegiatan.setText(address);
        }
    }


    public void loadBookingList() {
        FirebaseRecyclerAdapter<BookingList, MenuViewHolder> adapter = new FirebaseRecyclerAdapter<BookingList, MenuViewHolder>(BookingList.class, R.layout.activity_booking_fragment, MenuViewHolder.class, bookinglist) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, BookingList model, int position) {
                viewHolder.txtNama.setText(model.getNama());
                viewHolder.txtOrganisasi.setText(model.getDepartemen());
                viewHolder.txtKegiatan.setText(model.getKegiatan());
                viewHolder.txtJamMulai.setText(model.getJamMulai());
                viewHolder.txtJamAkhir.setText(model.getJamAkhir());

                final BookingList clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onCLick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };
        recyclerBookingList.setAdapter(adapter);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };
/*
    void mintaPerizinan(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED)){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, MY_PERMISSION_FINE_LOCATION});
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSION_FINE_LOCATION:{
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    finish();
                }
            }
        }
    }
    */

    void checkFieldsForEmptyValues(){

        String s1 = editNamaPeminjam.getText().toString();
        String s2 = editOrganisasi.getText().toString();
        String s4 = editJamMulai.getText().toString();
        String s5 = editJamAkhir.getText().toString();

        if(s1.equals("")
                || s2.equals("")
                || s4.equals("")
                || s5.equals("")
                ){
            btnSubmit.setEnabled(false);
        } else {
            btnSubmit.setEnabled(true);
        }
    }
}
