package com.jovanovic.stefan.books;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<String> book_id, book_title, book_author, book_pages, book_images, book_pdfs;

    CustomAdapter(Activity activity, Context context,
                  ArrayList<String> book_id, ArrayList<String> book_title,
                  ArrayList<String> book_author, ArrayList<String> book_pages,
                  ArrayList<String> book_images, ArrayList<String> book_pdfs) {
        this.activity = activity;
        this.context = context;
        this.book_id = book_id;
        this.book_title = book_title;
        this.book_author = book_author;
        this.book_pages = book_pages;
        this.book_images = book_images;
        this.book_pdfs = book_pdfs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.book_title_txt.setText(String.valueOf(book_title.get(holder.getAdapterPosition())));
        holder.book_author_txt.setText(String.valueOf(book_author.get(holder.getAdapterPosition())));
        holder.book_pages_txt.setText(String.valueOf(book_pages.get(holder.getAdapterPosition())));

        Glide.with(context)
                .load(book_images.get(holder.getAdapterPosition()))
                .into(holder.book_image);

        holder.download_pdf_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    Uri pdfUri = Uri.parse(book_pdfs.get(currentPosition));
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(pdfUri, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    try {
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return book_id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView book_title_txt, book_author_txt, book_pages_txt;
        ImageView book_image;
        Button download_pdf_button;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_title_txt = itemView.findViewById(R.id.book_title_txt);
            book_author_txt = itemView.findViewById(R.id.book_author_txt);
            book_pages_txt = itemView.findViewById(R.id.book_pages_txt);
            book_image = itemView.findViewById(R.id.book_image);
            download_pdf_button = itemView.findViewById(R.id.download_pdf_button);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }



}
