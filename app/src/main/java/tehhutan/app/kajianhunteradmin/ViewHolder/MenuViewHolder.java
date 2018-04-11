package tehhutan.app.kajianhunteradmin.ViewHolder;

import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import tehhutan.app.kajianhunteradmin.Interface.ItemClickListener;
import tehhutan.app.kajianhunteradmin.R;

/**
 * Created by tehhutan on 02/10/17.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txtNama, txtOrganisasi, txtKegiatan, txtJamMulai, txtJamAkhir;


    private ItemClickListener itemClickListener;
    public MenuViewHolder(View itemView) {
        super(itemView);

        txtNama = (TextView)itemView.findViewById(R.id.txt_namalengkap);
        txtKegiatan = (TextView)itemView.findViewById(R.id.txt_kegiatan);
        txtOrganisasi = (TextView)itemView.findViewById(R.id.txt_organisasi);
        txtJamMulai = (TextView)itemView.findViewById(R.id.txt_jammulai);
        txtJamAkhir = (TextView)itemView.findViewById(R.id.txt_jamakhir);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onCLick(view, getAdapterPosition(), false);
    }
}
