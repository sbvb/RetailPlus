package com.example.controlefantasias;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainScreenActivity extends Activity{
	
	Button btnViewProducts;
	Button btnViewSales;
	Button btnNewSale;
	Button btnViewClients;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);
		
		// Buttons
		btnViewProducts = (Button) findViewById(R.id.btnViewProducts);

		btnViewSales = (Button) findViewById(R.id.btnViewSales);

		btnNewSale = (Button) findViewById(R.id.btnNewSale);

		btnViewClients = (Button) findViewById(R.id.btnViewClients);


		// view products click event
		btnViewProducts.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching All products Activity
				Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
				startActivity(i);
				
			}
		});
		

		btnViewClients.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching create new product activity
				Intent i = new Intent(getApplicationContext(), AllClientsActivity.class);
				startActivity(i);
				
			}
		});

		// view products click event
		btnNewSale.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), NewSale.class);
				startActivity(i);

			}
		});

		btnViewSales.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), AllSales.class);
				startActivity(i);

			}
		});

	}
}
