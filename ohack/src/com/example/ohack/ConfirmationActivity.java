package com.example.ohack;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConfirmationActivity extends Activity {
	String[] address = {"1080 N 7th Street, San Jose, CA 95112", 
						"2330 Marinship Way,Suite 200A Sausalito, CA 94965",
						"Breakthrough Silicon Valley c/o Hoover Middle School 1635 Park Avenue San Jose, CA 95126"};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirmation);
		
		Bundle extras = getIntent().getExtras();
		int qty = extras.getInt("QUANTITY");
		float total = extras.getFloat("TOTAL");
		String shipping = extras.getString("SHIPPING_ADDRESS");
		String[] titlePrice = extras.getStringArray("TITLE_PRICE");
		
        LinearLayout layout = (LinearLayout) findViewById(R.id.confirmation);
        
        TextView qtytext1 = new TextView(this);
        qtytext1.setText(qty + " items shipped");
        qtytext1.setTextSize(25);
        qtytext1.setKeyListener(null);
        qtytext1.setPadding(5, 10, 0, 20);
        layout.addView(qtytext1);

		for (int i = 0; i < titlePrice.length; i++)
		{
	        EditText itemtext = new EditText(this);
	        itemtext.setText(titlePrice[i]);
	        itemtext.setKeyListener(null);
	        layout.addView(itemtext);
	        itemtext.setPadding(5, 20, 0, 10);
		}		
		
		TextView ship = new TextView(this);
		ship.setText("Shipped to:");
		ship.setTextSize(25);
		ship.setKeyListener(null);
		ship.setPadding(5, 70, 0, 40);
        layout.addView(ship);
        
        TextView shipaddr = new TextView(this);
        shipaddr.setText(shipping);
        shipaddr.setTextSize(15);
        shipaddr.setKeyListener(null);
        shipaddr.setPadding(5, 10, 0, 20);
        layout.addView(shipaddr);
        
		TextView totaltext = new TextView(this);
		totaltext.setText("Total Donation: $" + total);
		totaltext.setTextSize(25);
		totaltext.setKeyListener(null);
		totaltext.setPadding(5, 100, 0, 10);
        layout.addView(totaltext);
        
        TextView thanks = new TextView(this);
        thanks.setText("Thank you!");
        thanks.setTextSize(25);
        thanks.setKeyListener(null);
        thanks.setPadding(5, 60, 0, 10);
        //thanks.setGravity(Gravity.CENTER);
        layout.addView(thanks);
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.confirmation, menu);
		return true;
	}

}
