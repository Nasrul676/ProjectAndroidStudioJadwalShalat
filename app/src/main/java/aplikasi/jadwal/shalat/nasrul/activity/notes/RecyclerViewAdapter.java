package aplikasi.jadwal.shalat.nasrul.activity.notes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import info.blogbasbas.jadwalshalat.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    List<ActivityAsmaulHusna.User> values;
    Context context1;

    public RecyclerViewAdapter(Context context, List<ActivityAsmaulHusna.User> userInformation) {
        context1 = context;
        values = userInformation;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.costum_layout_asmaul_husna, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.no.setText(values.get(position).getNo());
        holder.ayat.setText(values.get(position).getAyat());
        holder.bacaan.setText(values.get(position).getBacaan());
        holder.arti.setText(values.get(position).getArti());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context1, ActivityAsmaulHusnaDetail.class);
                myIntent.putExtra("position", position);
                myIntent.putExtra("ayat", values.get(position).getAyat());
                myIntent.putExtra("bacaan", values.get(position).getBacaan());
                myIntent.putExtra("arti", values.get(position).getArti());
                view.getContext().startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView ayat, bacaan, arti, no;
        CardView cardView;
        LinearLayout linearLayout;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ayat = itemView.findViewById(R.id.tv_ayat);
            bacaan = itemView.findViewById(R.id.tv_bacaan);
            arti = itemView.findViewById(R.id.tv_arti);
            no = itemView.findViewById(R.id.tv_no);
            cardView = itemView.findViewById(R.id.card_view);
            linearLayout = itemView.findViewById(R.id.tes);
        }

        @Override
        public void onClick(View view) {

        }
    }
}