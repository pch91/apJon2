package com.example.scorp.apjon2.item_view_holder;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import com.example.scorp.apjon2.R;
import com.example.scorp.apjon2.DAO.Task;
import com.example.scorp.apjon2.viewHolder.TaskListViewHouder;
import com.xwray.groupie.Item;

public class TaskListItem extends Item<TaskListViewHouder> {

    Task task;

    public TaskListItem(Task t) {
        this.task = t;
    }

    @Override
    public void bind(@NonNull TaskListViewHouder viewHolder, final int position) {
        viewHolder.Desc.setText(task.getDescription());
    }

    @Override
    public int getLayout() {
        return R.layout.task_list_item;
    }

    @NonNull
    @Override
    public TaskListViewHouder createViewHolder(@NonNull View itemView) {
        return new TaskListViewHouder(itemView);
    }
}
