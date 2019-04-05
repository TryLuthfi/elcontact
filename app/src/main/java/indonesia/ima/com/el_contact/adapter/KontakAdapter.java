package indonesia.ima.com.el_contact.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import indonesia.ima.com.el_contact.R;
import indonesia.ima.com.el_contact.json.Group;
import indonesia.ima.com.el_contact.json.Kontak;


public class KontakAdapter extends RecyclerView.Adapter<KontakAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Kontak> contactList;
    private List<Kontak> contactListFiltered;
    private ContactsAdapterListener listener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone, text_thumbnail;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            phone = view.findViewById(R.id.phone);
            text_thumbnail = view.findViewById(R.id.text_thumbnail);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public KontakAdapter(Context context, List<Kontak> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kontak_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Kontak contact = contactListFiltered.get(position);
        holder.name.setText(contact.getNama_kontak());
        holder.phone.setText(contact.getNo_hp());
        holder.text_thumbnail.setText(contact.getNama_kontak());

        if(contact.getNo_hp().equals("empty")){
            Glide.with(context).load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQiZSMtcZ3iQz-C09z2XAkYukrdsxrXRvrRl6myil68joLMHUM").into(holder.thumbnail);
        }else {
            Glide.with(context)
                    .load("http://el-contact.000webhostapp.com/group/" + contact.getNo_hp())
//                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.thumbnail);
        }

        Random r = new Random();
        int red= r.nextInt(255 - 0 + 1)+0;
        int green=r.nextInt(255 - 0 + 1)+0;
        int blue=r.nextInt(255 - 0 + 1)+0;

        GradientDrawable draw = new GradientDrawable();
        draw.setShape(GradientDrawable.OVAL);
        draw.setColor(Color.rgb(red,green,blue));

        holder.thumbnail.setBackground(draw);
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<Kontak> filteredList = new ArrayList<>();
                    for (Kontak row : contactList) {
                        if (row.getNama_kontak().toLowerCase().contains(charString.toLowerCase()) || row.getNama_kontak().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Kontak>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Kontak contact);
    }
}

