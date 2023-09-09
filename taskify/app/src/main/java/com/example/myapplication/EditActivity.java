package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.db.Todo;
import com.example.myapplication.db.TodoDao;
import com.example.myapplication.db.TodoDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    private TextView dateTextView;
    private Calendar calendar;
    private Button saveButton;

    private TodoDao dao;
    private Todo todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dao = TodoDatabase.getInstance(getApplicationContext()).getTodoDao();

        int id = getIntent().getIntExtra("id", -1);
        todo = dao.getTodoById(id);

        if (todo == null) {
            Toast.makeText(this, "To-Do not found", Toast.LENGTH_SHORT).show();
            finish();
            return; //fail
        }

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // This will close current activity and return to the previous one
            }
        });


        EditText titleEditText = findViewById(R.id.horizontalEditText);
        EditText descriptionEditText = findViewById(R.id.descriptionEditText);

        titleEditText.setText(todo.getTitle());
        descriptionEditText.setText(todo.getDescription());

        dateTextView = findViewById(R.id.date_text);
        calendar = Calendar.getInstance();

        updateTextViewWithCurrentDate();

        findViewById(R.id.change_date_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(year, monthOfYear, dayOfMonth);

            updateTextViewWithCurrentDate();
        }
    };

    private void updateTextViewWithCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateStr = sdf.format(calendar.getTime());
        dateTextView.setText(dateStr);
    }

    private void saveData() {
        String updatedTitle = ((EditText) findViewById(R.id.horizontalEditText)).getText().toString().trim();
        String updatedDescription = ((EditText) findViewById(R.id.descriptionEditText)).getText().toString().trim();
        String updatedDate = dateTextView.getText().toString().trim();

        todo.setTitle(updatedTitle);
        todo.setDescription(updatedDescription);
        todo.setDate(updatedDate);

        dao.update(todo);

        Toast.makeText(EditActivity.this, "Data saved", Toast.LENGTH_SHORT).show();

        finish();
    }
}