package com.example.todoapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.todoapp.Model.todoModel;
import com.example.todoapp.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";

    private EditText newTaskText;
    private Button newTaskSaveBtn;
    private DatabaseHandler db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.DialogStyle);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_task_layout, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = Objects.requireNonNull(getView()).findViewById(R.id.new_task_text);
        newTaskSaveBtn = getView().findViewById(R.id.new_task_btn);
        Log.e("view", String.valueOf(newTaskSaveBtn));
        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdated = false;

        final Bundle bundle = this.getArguments();
        Log.e("add", String.valueOf(bundle));
        if (bundle != null){
            isUpdated = true;
            Log.e("add", String.valueOf(bundle.getInt("id")));
            String task = bundle.getString("task");
            newTaskText.setText(task);
            if(newTaskText.length()>0){
                // newTaskSaveBtn.setBackgroundColor(R.color.design_default_color_primary);
            }
        }newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTaskSaveBtn.setEnabled(false);
                   // newTaskSaveBtn.setTextColor(Color.GRAY);

                    newTaskSaveBtn.setAlpha(0);
                }
                else{
                    newTaskSaveBtn.setAlpha(1);
                    newTaskSaveBtn.setEnabled(true);
                 //   newTaskSaveBtn.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.purple_700));
                    newTaskSaveBtn.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.purple_700));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        boolean finalIsUpdated = isUpdated;
        newTaskSaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Log.e("addd", String.valueOf(bundle.getString("task")));

                    String text = newTaskText.getText().toString();
                    Log.e("view",text);
                    if(finalIsUpdated){
                        Log.e("add", String.valueOf(bundle));

                        db.updateTask(bundle.getInt("id"), text);
                    }
                    else {
                        todoModel task = new todoModel();
                        task.setId(100);
                        task.setTask(text);
                        task.setStatus(0);
                        db.insertTask(task);
                    }
                    dismiss();
                }
            });



        }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }

}

