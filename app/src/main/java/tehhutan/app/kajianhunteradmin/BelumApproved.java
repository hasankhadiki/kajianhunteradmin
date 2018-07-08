package tehhutan.app.kajianhunteradmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tehhutan.app.kajianhunteradmin.Interface.ItemClickListener;
import tehhutan.app.kajianhunteradmin.Menu.Kajian;
import tehhutan.app.kajianhunteradmin.ViewHolder.MenuViewHolder;
import tehhutan.app.kajianhunteradmin.app.MainAct;
import tehhutan.app.kajianhunteradmin.model.BookingList;


public class BelumApproved extends Fragment {
    FirebaseDatabase database;
    DatabaseReference bookinglist, addKajian;

    MainAct mainRef;
    RecyclerView recyclerBookingList;
    RecyclerView.LayoutManager layoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_belum_approved, container, false);
        database = FirebaseDatabase.getInstance();
        bookinglist = database.getReference("KajianList/Unverified");
        addKajian = database.getReference("KajianList/Verified");
        mainRef = (MainAct)getActivity();
        //Load Booking List
        recyclerBookingList = (RecyclerView)v.findViewById(R.id.recycler_booking);
        recyclerBookingList.hasFixedSize();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerBookingList.setLayoutManager(layoutManager);
        loadBookingList();
        return v;
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

                final DatabaseReference postRef= getRef(position);
                final String postKey = postRef.getKey();

                final BookingList clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onCLick(View view, int position, boolean isLongClick) {
                        AlertDialog.Builder builder_ = new AlertDialog.Builder(getActivity());
                        builder_.setMessage("Apakah anda yakin menyetujui kajian ini?")
                                .setCancelable(false)
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        setujuiKajian(postRef, addKajian.child(postKey));
                                    }
                                })
                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alert = builder_.create();
                        alert.setTitle("Peringatan!");
                        alert.show();
                    }
                });
            }
        };
        recyclerBookingList.setAdapter(adapter);
    }

    public void setujuiKajian(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(getActivity(), "Telah terjadi kesalahan!", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "Kajian telah disetujui.", Toast.LENGTH_LONG).show();
                            fromPath.removeValue();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
