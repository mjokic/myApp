package com.example.wifi.myapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;

public class BaseActivity {

//    private ProgressDialog mProgressDialog;
//    private Activity activity;

//    public BaseActivity(Activity activity){
//        this.activity = activity;
//    }

//    public void showProgressDialog(){
//        mProgressDialog = new ProgressDialog(this.activity);
//        mProgressDialog.setIndeterminate(true);
//        mProgressDialog.setMessage("Loading...");
//        mProgressDialog.setCancelable(false);
//        mProgressDialog.show();
//    }
//
//    public void hideProgressDialog(){
//        mProgressDialog.dismiss();
//    }


    public static ProgressDialog progressDialog;

    public void showProgressDialog(Activity activity){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgressDialog(){
        progressDialog.dismiss();
    }


}
