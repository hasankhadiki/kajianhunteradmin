package tehhutan.app.kajianhunteradmin;

import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import tehhutan.app.kajianhunteradmin.Interface.ItemClickListener;
import tehhutan.app.kajianhunteradmin.ViewHolder.MenuViewHolder;
import tehhutan.app.kajianhunteradmin.model.BookingList;


public class Booking extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference bookinglist;

    RecyclerView recyclerBookingList;
    RecyclerView.LayoutManager layoutManager;

    BookingList newBooking;

    EditText editNamaPeminjam, editOrganisasi, editKegiatan, editJamMulai, editJamAkhir, editlinkTempat;
    Button btnSubmit;
    int PLACE_PICKER_REQUEST = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Kajian List");
        setSupportActionBar(toolbar);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        bookinglist = database.getReference("KajianList");



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Booking.this);
                View mView = getLayoutInflater().inflate(R.layout.add_booking, null);
                editNamaPeminjam = (EditText) mView.findViewById(R.id.et_namapeminjam);
                editOrganisasi = (EditText) mView.findViewById(R.id.et_organisasi);
                editKegiatan = (EditText) mView.findViewById(R.id.et_deskripsikegiatan);
                editJamMulai = (EditText) mView.findViewById(R.id.et_jammulai);
                editJamAkhir = (EditText) mView.findViewById(R.id.et_jamakhir);
                editlinkTempat = (EditText) mView.findViewById(R.id.et_linktempat);
                btnSubmit = (Button) mView.findViewById(R.id.btn_submit);

//                editKegiatan = (EditText) mView.findViewById(R.id.et_deskripsikegiatan);
//                editKegiatan.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//                        Intent intent;
//                        try {
//                            intent = builder.build(Booking.this);
//                            startActivityForResult(intent, PLACE_PICKER_REQUEST);
//                        } catch (GooglePlayServicesRepairableException e){
//                            e.printStackTrace();
//                        } catch (GooglePlayServicesNotAvailableException e){
//                            e.printStackTrace();
//                        }
//
//                    }
//                });




                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                editJamMulai.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final Calendar c = Calendar.getInstance();
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(Booking.this,

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

                        TimePickerDialog timePickerDialog = new TimePickerDialog(Booking.this,
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
                editlinkTempat.addTextChangedListener(mTextWatcher);

                // run once to disable if empty
                checkFieldsForEmptyValues();

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ConnectivityManager connectivity = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
                        if (activeNetwork != null) { // connected to the internet
                            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                                // connected to wifi
                                BookingList newBooking = new BookingList(editOrganisasi.getText().toString()
                                        , editNamaPeminjam.getText().toString()
                                        , editKegiatan.getText().toString()
                                        , editJamMulai.getText().toString()
                                        , editJamAkhir.getText().toString()
                                        , editlinkTempat.getText().toString()
                                        ,1.0
                                        ,1.0
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
                                        , editlinkTempat.getText().toString()
                                        ,1.0
                                        ,1.0
                                );
                                bookinglist.push().setValue(newBooking);
                                dialog.dismiss();
                            }
                        } else {
                            Toast.makeText(Booking.this, "Internet Connection Not Available, Please Check Your Connection Setting", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        });

        //Load Booking List
        recyclerBookingList = (RecyclerView) findViewById(R.id.recycler_booking);
        recyclerBookingList.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        recyclerBookingList.setLayoutManager(layoutManager);
        loadBookingList();



    }

//        @Override
//    public  void onActivityResult(int requestCode, int resultCode, Intent data){
//        if(requestCode == PLACE_PICKER_REQUEST && requestCode==RESULT_OK){
//
//            final Place place = PlacePicker.getPlace(this, data);
////                Place place = PlacePicker.getPlace(data, this);
////                String address = String.format("Place: ", place.getAddress());
//            final CharSequence address = place.getAddress();
//            editKegiatan.setText(address);
//
//        }
//    }


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

    void checkFieldsForEmptyValues(){

        String s1 = editNamaPeminjam.getText().toString();
        String s2 = editOrganisasi.getText().toString();
//        String s3 = editKegiatan.getText().toString();
        String s4 = editJamMulai.getText().toString();
        String s5 = editJamAkhir.getText().toString();

        if(s1.equals("")
                || s2.equals("")
//                || s3.equals("")
                || s4.equals("")
                || s5.equals("")
                ){
            btnSubmit.setEnabled(false);
        } else {
            btnSubmit.setEnabled(true);
        }
    }

}

