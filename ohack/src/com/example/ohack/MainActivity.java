/*
 * For ebay ohack hackathon
 * 
 * @author  Vinay Nagar
 */

package com.example.ohack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void login(View view) {
    	EditText user = (EditText) findViewById(R.id.userText);
    	String username = user.getText().toString();
    	EditText pass = (EditText) findViewById(R.id.passText);
    	String password = pass.getText().toString();
    	
//    	if (!(username.equals("ebaygivings_buyer") && password.equals("password"))) {
//    		// Construct the alert box
//    		showAlertBox(this);
//    	}
    	
    	Intent intent = new Intent(this, SelectActivity.class);
    	startActivity(intent);
    }
    
    protected void showAlertBox(Context cont) {
    	Builder alertBox = new Builder(cont);
    	alertBox.setTitle("Login failed");
    	alertBox.setMessage("Username/password is incorrect");
    	alertBox.setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog,int id) {
														dialog.cancel();
													}
    											});
    	AlertDialog alertDialog = alertBox.create();
		alertDialog.show();
    }
 
}
