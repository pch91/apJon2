package com.example.scorp.apjon2.DAO;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ComunsDAO {

    FirebaseDatabase database;
    DatabaseReference Refdatabase;
    private Object obj;

    public ComunsDAO() {
        if(database == null) {
            this.database = FirebaseDatabase.getInstance();
        }
        if(Refdatabase == null) {
            Refdatabase = database.getReference();
        }
    }

    public <T> void setChildEventiListener(final Class<T> oclass, final String UsertID, final String key
                                            , final FireBaseCalback fireBaseCalbackAdd, final FireBaseCalback fireBaseCalbackEdit
                                            , final FireBaseCalback fireBaseCalbackRemove, final FireBaseCalback fireBaseCalbackMoved)
            throws IllegalAccessException, InstantiationException {

        Refdatabase.child(oclass.getSimpleName().toLowerCase() + "/" + UsertID).addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                List<T> list = new ArrayList<T>();
                doevent(dataSnapshot,list,oclass,UsertID,key,fireBaseCalbackAdd);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                List<T> list = new ArrayList<T>();
                doevent(dataSnapshot,list,oclass,UsertID,key,fireBaseCalbackEdit);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                List<T> list = new ArrayList<T>();
                doevent(dataSnapshot,list,oclass,UsertID,key,fireBaseCalbackRemove);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                List<T> list = new ArrayList<T>();
                doevent(dataSnapshot,list,oclass,UsertID,key,fireBaseCalbackMoved);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public <T> void setEventiListener(final Class<T> oclass, final String UsertID, final String key, final FireBaseCalback fireBaseCalback)
            throws IllegalAccessException, InstantiationException {


        Refdatabase.child(oclass.getSimpleName().toLowerCase() + "/" + UsertID).addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<T> list = new ArrayList<T>();
                doevent(dataSnapshot,list,oclass,UsertID,key,fireBaseCalback);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public <T> void getObject(final Class<T> oclass, final String UsertID, final String key, final FireBaseCalback fireBaseCalback)
            throws IllegalAccessException, InstantiationException {

        //DatabaseReference thisref = Refdatabase.child(oclass.getSimpleName().toLowerCase()+"/UsertID");
        Refdatabase.child(oclass.getSimpleName().toLowerCase() + "/" + UsertID).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<T> list = new ArrayList<T>();
                doevent(dataSnapshot,list,oclass,UsertID,key,fireBaseCalback);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("erro Database",databaseError.getMessage());
            }
        });
    }

    private <T> void doevent(DataSnapshot dataSnapshot, List<T> list, Class<T> oclass, String UsertID,
                             String key, FireBaseCalback fireBaseCalback){

        DataSnapshot el = dataSnapshot;

        for (DataSnapshot childSnapshot: el.getChildren()) {
            obj = childSnapshot.getValue(oclass);
            try {
                Method methodmethod;
                methodmethod = oclass.getMethod("getId", null);
                try {
                    if(key.isEmpty() || ((String)methodmethod.invoke(obj)).equals(key)) {
                        list.add(oclass.cast(obj));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        fireBaseCalback.onCalback(list);
    }

}
