package com.example.myapplication;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.db.Todo;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private Context context;
    private List<Todo> todoList;

    public ToDoAdapter(Context context, List<Todo> todoList) {
        this.context = context;
        this.todoList = todoList;
    }

    @Override
    public ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.todo_item, parent, false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ToDoViewHolder holder, int position) {
        Todo todoItem = todoList.get(position);
        holder.titleTextView.setText(todoItem.getTitle());
        holder.dateTextView.setText(todoItem.getDate());

        holder.detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", todoItem.getId());  // Pass the id of Todo item
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void updateTodoList(List<Todo> newTodoList) {
        todoList = newTodoList;
        notifyDataSetChanged();
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView dateTextView;
        public Button detailsButton;

        public ToDoViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.todo_title);
            dateTextView = itemView.findViewById(R.id.todo_date);
            detailsButton = itemView.findViewById(R.id.detailbutton);
        }
    }
}
