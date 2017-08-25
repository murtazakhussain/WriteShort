package com.genieapps.writeshort;

import android.content.Context;

import com.genieapps.writeshort.helpers.FirebaseManager;

import java.util.ArrayList;

/**
 * Created on 8/23/2017.
 */

public class TestClass {
    public void foo(Context context){
        new FirebaseManager( context ).getNotes( new FirebaseManager.DataReadyListener() {
            @Override
            public void onDataReady( ArrayList< ? extends Object > list ) {

            }
        } );
    }
}
