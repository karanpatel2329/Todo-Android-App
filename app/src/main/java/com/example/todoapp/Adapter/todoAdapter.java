package com.example.todoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewTask;
import com.example.todoapp.MainActivity;
import com.example.todoapp.Model.todoModel;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DatabaseHandler;

import java.util.List;

public class todoAdapter extends RecyclerView.Adapter<todoAdapter.ViewHolder> {
    private List<todoModel> todolist;
    private MainActivity activity;
    private DatabaseHandler db;

    public todoAdapter(DatabaseHandler db,MainActivity activity){
        this.db= db;
        this.activity= activity;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDatabase();
        todoModel item = todolist.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolen(item.getStatus(0)));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    db.updateStatus(item.getId(),1);
                }
                else {
                    db.updateStatus(item.getId(),0);
                }
            }
        });
    }

    private boolean toBoolen(int n){
        return n!=0;
    }

    public void setTasks(List<todoModel> todolist){
        this.todolist = todolist;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return todolist.size();
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout,parent,false);
        return new ViewHolder(itemView);
    }

    public void deleteItem(int position){
        todoModel item = todolist.get(position);
        Log.e("addd", String.valueOf(item.getTask()));

        db.deleteTask(item.getId());
        todolist.remove(position);
        notifyItemRemoved(position);

    }

    public void editItem(int position){
        todoModel item = todolist.get(position);
        Log.e("edit", String.valueOf(item.getId()));
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());
        Log.e("edit",item.getTask()+" --- "+item.getId());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(),AddNewTask.TAG);
    }

    public Context getContext() {
        return activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        ViewHolder(View view){
            super(view);
            task=view.findViewById(R.id.checkbox);
        }

    }
}
