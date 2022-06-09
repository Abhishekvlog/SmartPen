package com.dexter.blepoc;

import android.util.Log;

import com.smart.pen.core.PenApplication;

public class SmartPenApplication extends PenApplication {
    private static SmartPenApplication instance=null;

    static SmartPenApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("ranzn", "onCreate: Applicatoin Lanched");
        instance = this;
    }
}
