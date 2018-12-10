package com.example.scorp.apjon2.viewHolder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import com.xwray.groupie.ViewHolder;
import com.example.scorp.apjon2.R;


public class TaskListViewHouder extends ViewHolder {

    //elementos da viel que vai dentro do recicle view
    public TextView Desc;

    public TaskListViewHouder(@NonNull View rootView) {
        super(rootView);

        Desc = itemView.findViewById(R.id.tasldescRc);
    }
}

