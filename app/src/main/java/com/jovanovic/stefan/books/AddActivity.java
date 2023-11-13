package com.jovanovic.stefan.books;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddActivity extends AppCompatActivity {

    EditText title_input, author_input, pages_input;
    Button add_button, select_image_button, select_pdf_button;
    Uri imageUri, pdfUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title_input = findViewById(R.id.title_input);
        author_input = findViewById(R.id.author_input);
        pages_input = findViewById(R.id.pages_input);
        add_button = findViewById(R.id.add_button);
        select_image_button = findViewById(R.id.select_image_button);
        select_pdf_button = findViewById(R.id.select_pdf_button);

        select_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser("image/*", IMAGE_PICK_CODE);
            }
        });

        select_pdf_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser("application/pdf", PDF_PICK_CODE);
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = title_input.getText().toString().trim();
                String author = author_input.getText().toString().trim();
                int pages = Integer.parseInt(pages_input.getText().toString().trim());
                String imagePath = imageUri != null ? imageUri.toString() : "";
                String pdfPath = pdfUri != null ? pdfUri.toString() : "";

                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                long result = myDB.addBook(title, author, pages, imagePath, pdfPath);
                if (result != -1) {
                    boolean exists = myDB.checkBookExists(title, author);
                    if (exists) {
                        Toast.makeText(AddActivity.this, "Book added successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddActivity.this, "Book added but not found on query!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddActivity.this, "Failed to add book", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PDF_PICK_CODE = 1001;

    private void openFileChooser(String mimeType, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType(mimeType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri selectedFileUri = data.getData();
            if (requestCode == IMAGE_PICK_CODE) {
                imageUri = selectedFileUri;
            } else if (requestCode == PDF_PICK_CODE) {
                pdfUri = selectedFileUri;
            }
        }
    }
}