package com.example.kitaplistesikayitekle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class kitapAdapter extends RecyclerView.Adapter<kitapAdapter.kitapHolder> {

    private ArrayList<Kitap>kitaplist;
    private Context context;
    private OnItemClickListener listener;

    public kitapAdapter(ArrayList<Kitap> kitaplist, Context context) {
        this.kitaplist = kitaplist;
        this.context = context;
    }

    @NonNull
    @Override
    public kitapHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.kitap_item,parent,false);
        return new kitapHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull kitapHolder holder, int position) {

        Kitap kitap=kitaplist.get(position);
        holder.setData(kitap);
    }

    @Override
    public int getItemCount() {
        return kitaplist.size();
    }

    class kitapHolder extends  RecyclerView.ViewHolder {
        TextView txtkitapAdi,txtkitapYazari,txtkitapOzeti;
        ImageView imgkitapresim;

        public kitapHolder(@NonNull View itemView) {
            super(itemView);

            txtkitapAdi=itemView.findViewById(R.id.kitapitem_textviewadi);
            txtkitapYazari=itemView.findViewById(R.id.kitapitem_textviewyazari);
            txtkitapOzeti=itemView.findViewById(R.id.kitapitem_textviewozeti);
            imgkitapresim=itemView.findViewById(R.id.kitapitem_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(listener !=null && position !=RecyclerView.NO_POSITION){
                        listener.OnItemClick(kitaplist.get(position));
                    }
                }
            });
        }

        public  void setData(Kitap kitap){
            this.txtkitapAdi.setText(kitap.getKitapAdi());
            this.txtkitapYazari.setText(kitap.getKitapYazari());
            this.txtkitapOzeti.setText(kitap.getKitapOzeti());
            this.imgkitapresim.setImageBitmap(kitap.getKitapResmi());

        }


    }

  public interface OnItemClickListener{
        void OnItemClick(Kitap kitap);

  }
  public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
  }
}
