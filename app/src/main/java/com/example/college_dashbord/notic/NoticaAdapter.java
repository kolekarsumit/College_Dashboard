package com.example.college_dashbord.notic;

import androidx.appcompat.app.AlertDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.college_dashbord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NoticaAdapter extends RecyclerView.Adapter<NoticaAdapter.NoticeViewAdapter> {

    private Context context;
    private ArrayList<NoticeData> list;


    public NoticaAdapter(Context context, ArrayList<NoticeData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NoticeViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.newsfilditem_layout,parent,false);
        return new NoticeViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewAdapter holder,final int position) {
        NoticeData currentItem=list.get(position);
        holder.DeletenoticeTital.setText(currentItem.getTitle());

        try {
            if (currentItem.getImage()!=null) {
                Picasso.get().load(currentItem.getImage()).into(holder.deletenoticeImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.Deletenotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("Do you want delete this Notice?");
                builder.setCancelable(true);
                builder.setPositiveButton(
                        "Notice Deleted..!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Notice");
                                reference.child(currentItem.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                notifyItemRemoved(position);
                            }
                        }
                );
                builder.setNegativeButton(
                        "Cancel delete Process",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        }
                );
                AlertDialog dialog=null;
                try {
                    dialog=builder.create();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (dialog!=null){
                    dialog.show();
                }

            }
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NoticeViewAdapter extends RecyclerView.ViewHolder {

        private Button Deletenotice;
        private TextView DeletenoticeTital;
        private ImageView deletenoticeImage;

        public NoticeViewAdapter(@NonNull View itemView) {

            super(itemView);
            Deletenotice=itemView.findViewById(R.id.Deletenotice);
            DeletenoticeTital=itemView.findViewById(R.id.DeletenoticeTital);
            deletenoticeImage=itemView.findViewById(R.id.deletenoticeImage);

        }
    }
}
