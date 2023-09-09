package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.db.Todo;
import com.example.myapplication.db.TodoDao;
import com.example.myapplication.db.TodoDatabase;

public class DetailActivity extends AppCompatActivity {
    private TextView textViewTitle;
    private TextView textViewDescription;
    private TextView textViewDate;
    private TextView textViewStandort;
    private int id;

    private TodoDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Access Database
        dao = TodoDatabase.getInstance(getApplicationContext()).getTodoDao();

        id = getIntent().getIntExtra("id", -1);
        Todo todo = dao.getTodoById(id);

        if (todo != null) {
            textViewTitle = findViewById(R.id.todo_title);
            textViewTitle.setText(todo.getTitle());

            textViewDescription = findViewById(R.id.todo_descr);
            textViewDescription.setText(todo.getDescription());

            textViewStandort = findViewById(R.id.bearbeitungs_screen_standort);
            textViewStandort.setText(todo.getLocation());

            textViewDate = findViewById(R.id.bearbeitungs_screen_datum);
            textViewDate.setText(todo.getDate());
        } else {
            Toast.makeText(this, "To-Do not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        ImageButton backButton = findViewById(R.id.image_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and go back
                finish();
            }
        });

        Button editButton = findViewById(R.id.bearbeitungs_screen_button_bearbeiten);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the EditActivity and pass the todo id
                Intent intent = new Intent(DetailActivity.this, EditActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        Button deleteButton = findViewById(R.id.bearbeitungs_screen_button_loeschen);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailActivity.this)
                        .setTitle("Delete todo")
                        .setMessage("Are you sure you want to delete this todo?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete the todo from the database
                                dao.delete(todo);

                                Toast.makeText(DetailActivity.this, "To-Do deleted", Toast.LENGTH_SHORT).show();

                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

    }
}
