package com.example.controlefantasias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.apache.http.NameValuePair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import org.apache.http.message.BasicNameValuePair;


import android.app.Activity;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class NewProductActivity extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jParser = new JSONParser();
	JSONParser jsonParser = new JSONParser();

	EditText inputNome;
	EditText inputPreco;
	EditText inputQuantidade;
	private Spinner spinner_tamanho;

	// JSON Node names
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_TAMANHO = "tamanho";
	private static final String TAG_ID = "id";

	// products JSONArray
	JSONArray products = null;
	JSONArray products2 = null;

	ArrayList<String> categorias = new ArrayList<String>();

	private static String url_buscar_tamanhos = "http://54.69.170.202/servidor/sps_buscar_para_adicionar_produto.php";
	private static String url_create_product = "http://54.69.170.202/servidor/sps_criar_produto.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.example.controlefantasias.R.layout.add_product);

		// Edit Text
		inputNome = (EditText)findViewById(R.id.inputNome);
		inputPreco = (EditText) findViewById(R.id.inputPreco);
		inputQuantidade = (EditText) findViewById(R.id.inputQuantidade);

		new CarregarCategorias().execute();

		// Create button
		Button btnCreateProduct = (Button) findViewById(com.example.controlefantasias.R.id.btnCreateProduct);

		// button click event
		btnCreateProduct.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				new CreateNewProduct().execute();
			}
		});
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

		switch (position) {
			case 0:
				// Whatever you want to happen when the first item gets selected
				break;
			case 1:
				// Whatever you want to happen when the second item gets selected
				break;
			case 2:
				// Whatever you want to happen when the thrid item gets selected
				break;

		}
	}

	class CarregarCategorias extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(NewProductActivity.this);
			pDialog.setMessage("Loading products. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// getting JSON string from URL
			List<NameValuePair> params2 = new ArrayList<NameValuePair>();
			JSONObject json2 = jParser.makeHttpRequest(url_buscar_tamanhos, "GET", params2);

			try {
				// Checking for SUCCESS TAG
				int success = json2.getInt(TAG_SUCCESS);
			int tamanho_total = json2.getInt(TAG_TAMANHO);
			if (success == 1) {
				// products found
				// Getting Array of Products
				products2 = json2.getJSONArray(TAG_PRODUCTS);

				// looping through All Products
				for (int i = 0; i < tamanho_total; i++) {
					JSONObject c = products2.getJSONObject(i);

					// Storing each json item in variable
					String tamanho = c.getString(TAG_TAMANHO);

					// adding HashList to ArrayList
					categorias.add(tamanho);
				}

			} else {

		}
		} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
		/*			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, categorias);

					Spinner spinner = (Spinner) findViewById(R.id.spinner_tamanho);
					spinner.setAdapter(adapter);
		*/	}
			});

		}

	}


	/**
	 * Background Async Task to Create new product
	 * */
	class CreateNewProduct extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(NewProductActivity.this);
			pDialog.setMessage("Creating Product..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			String nome = inputNome.getText().toString();
			String preco = inputPreco.getText().toString();
			String quantidade = inputQuantidade.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("nome", nome));
			params.add(new BasicNameValuePair("preco", preco));
			params.add(new BasicNameValuePair("quantidade", quantidade));
			params.add(new BasicNameValuePair("idCategorias", null));
			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_product,
					"POST", params);

			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
					startActivity(i);

					// closing this screen
					finish();
				} else {
					// failed to create product
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
		}

	}
}
