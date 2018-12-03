package com.example.scorp.apjon2.DAO;

import android.content.Context;

import java.io.IOException;

public class UserDao extends ComunsDAO {

    public void add(User u,Context c) throws IOException {
        Refdatabase.child(u.getClass().getSimpleName().toLowerCase()+"/"+u.getId()).setValue(u);
    }
}
