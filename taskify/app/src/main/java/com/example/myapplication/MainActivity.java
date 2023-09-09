package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.db.Todo;
import com.example.myapplication.db.TodoDao;
import com.example.myapplication.db.TodoDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Hauptaktivitätsklasse, die die Liste der To-Dos anzeigt.
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView RecyclerView;
    private ToDoAdapter Adapter;
    private List<Todo> TodoList;
    private TodoDao dao;

    /**
     * Diese Methode initialisiert die Aktivität.
     * @param savedInstanceState Ein Bundle-Objekt, das den zuvor gespeicherten Instanzzustand enthält.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Definiert das RecyclerView
        RecyclerView = findViewById(R.id.recyclerview);

        // Zugriff auf die TodoDatabase und den DAO erhalten
        dao = TodoDatabase.getInstance(getApplicationContext()).getTodoDao();

        // Toolbar einrichten
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Definiert die FloatingActionButton und setzt einen Klick-Listener, der die CreateActivity startet
        FloatingActionButton fabAddTodo = findViewById(R.id.fab_add_todo);
        fabAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Diese Methode wird aufgerufen, wenn die Aktivität fortgesetzt wird.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadTodos();
    }

    /**
     * Diese Methode lädt die To-Dos aus der Datenbank und aktualisiert das RecyclerView.
     */
    private void loadTodos() {
        // Alle To-Dos abrufen
        TodoList = dao.getAllTodos();
        if (Adapter == null) {
            // Wenn der Adapter noch nicht definiert ist, erstellen Sie einen neuen und setzen Sie ihn auf das RecyclerView
            Adapter = new ToDoAdapter(this, TodoList);
            RecyclerView.setAdapter(Adapter);
            RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            // Wenn der Adapter bereits definiert ist, aktualisieren Sie einfach die To-Do-Liste
            Adapter.updateTodoList(TodoList);
        }
    }
}
