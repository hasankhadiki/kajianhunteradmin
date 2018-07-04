package tehhutan.app.kajianhunteradmin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tehhutan.app.kajianhunteradmin.Interface.ItemClickListener;
import tehhutan.app.kajianhunteradmin.ViewHolder.MenuViewHolder;
import tehhutan.app.kajianhunteradmin.model.BookingList;


public class BelumApproved extends Fragment {
    FirebaseDatabase database;
    DatabaseReference bookinglist;

    RecyclerView recyclerBookingList;
    RecyclerView.LayoutManager layoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_belum_approved, container, false);
        database = FirebaseDatabase.getInstance();
        bookinglist = database.getReference("KajianList");

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
}
