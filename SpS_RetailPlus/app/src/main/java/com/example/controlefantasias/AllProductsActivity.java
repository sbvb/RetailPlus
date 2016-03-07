package com.example.controlefantasias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AllProductsActivity extends ListActivity {

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> productsList;

	// url to get all products list
	private static String url_all_products = "http://54.69.170.202/servidor/sps_listar_produtos.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_TAMANHO = "tamanho";
	private static final String TAG_NOME = "nome";
	private static final String TAG_QUANTIDADE = "quantidade";
	private static final String TAG_PRECO = "preco";
	private static final String TAG_SEXO = "sexo";
	private static final String TAG_ID = "fantasiaId";

	Button btnAdd;

	// products JSONArray
	JSONArray products = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_products);


		// Hashmap for ListView
		productsList = new ArrayList<HashMap<String, String>>();

		// Loading products in Background Thread
		new LoadAllProducts().execute();


		// Get listview
		ListView lv = getListView();

		// on seleting single product
		// launching Edit Product Screen
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem

				String produtoId = ((TextView) view.findViewById(R.id.fantasiaId)).getText()
						.toString();

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						EditProductActivity.class);
				// sending data to next activity
				in.putExtra(TAG_ID, produtoId);

				// starting new activity and expecting some response back
				startActivityForResult(in, 100);
			}
		});

		btnAdd = (Button) findViewById(R.id.btnAdd);

		// button click event
		btnAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), NewProductActivity.class);
				startActivity(i);
			}
		});
	}


	// Response from Edit Product Activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if result code 100
		if (resultCode == 100) {
			// if result code 100 is received 
			// means user edited/deleted product
			// reload this screen again
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}

	}


	/**
	 * Background Async Task to Load all product by making HTTP Request
	 * */
	class LoadAllProducts extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AllProductsActivity.this);
			pDialog.setMessage("Loading products. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);
			
			// Check your log cat for JSON reponse
			Log.d("All Products: ", json.toString());
			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);
				int tamanho_total = json.getInt(TAG_TAMANHO);
				if (success == 1) {
					// products found
					// Getting Array of Products
					products = json.getJSONArray(TAG_PRODUCTS);

					// looping through All Products
					for (int i = 0; i < tamanho_total; i++) {
						JSONObject c = products.getJSONObject(i);

						// Storing each json item in variable
						String nome = c.getString(TAG_NOME);
						String quantidade = c.getString(TAG_QUANTIDADE);
						String tamanho = c.getString(TAG_TAMANHO);
						String preco = c.getString(TAG_PRECO);
						String sexo = c.getString(TAG_SEXO);
						String fantasiaId = c.getString(TAG_ID);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_NOME, nome);
						map.put(TAG_QUANTIDADE, quantidade);
						map.put(TAG_TAMANHO, tamanho);
						map.put(TAG_PRECO, preco);
						map.put(TAG_SEXO, sexo);
						map.put(TAG_ID, fantasiaId);

						// adding HashList to ArrayList
						productsList.add(map);
					}
				} else {
					// no products found
/*					// Launch Add New product Activity
					Intent i = new Intent(getApplicationContext(),
							NewProductActivity.class);
					// Closing all previous activities
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
	*/			}
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
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							AllProductsActivity.this, productsList,
							R.layout.list_item, new String[] { TAG_NOME, TAG_TAMANHO, TAG_SEXO, TAG_QUANTIDADE, TAG_PRECO, TAG_ID},
							new int[] { R.id.nome, R.id.tamanho, R.id.sexo, R.id.quantidade, R.id.preco, R.id.fantasiaId});
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}