package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todoapp.Adapter.todoAdapter;
import com.example.todoapp.Model.todoModel;
import com.example.todoapp.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView recyclerView;
    private todoAdapter taskAdapter;
    private DatabaseHandler db;
    private FloatingActionButton fab;
    private List<todoModel> tasklist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        fab = findViewById(R.id.fab);
        db = new DatabaseHandler(this);
        db.openDatabase();
        tasklist = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new todoAdapter(db,this);
        recyclerView.setAdapter(taskAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewItemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });

        tasklist = db.getAllTask();
        Collections.reverse(tasklist);
        taskAdapter.setTasks(tasklist);

    }
    @Override
    public void handleDialogClose(DialogInterface dialog){
        tasklist = db.getAllTask();
        Collections.reverse(tasklist);
        taskAdapter.setTasks(tasklist);
        taskAdapter.notifyDataSetChanged();
    }
}