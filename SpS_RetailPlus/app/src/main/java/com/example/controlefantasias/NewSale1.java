package com.example.controlefantasias;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewSale1 extends ListActivity {

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> productsList;

	// url to get all products list
	private static final String url_all_clients = "http://54.69.170.202/servidor/sps_listar_clientes.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_NOME = "nome";
	private static final String TAG_CPF = "cpf";
	private static final String TAG_TELEFONE = "telefone";
	private static final String TAG_ID = "clienteId";

	private static String produtoId;
	private static String nomeAux;

	// products JSONArray
	JSONArray products = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsale1);

		// getting product details from intent
		Intent i = getIntent();

		// getting product id (pid) from intent
		produtoId = i.getStringExtra("fantasiaId");
		nomeAux = i.getStringExtra(TAG_NOME);

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

				String clienteId = ((TextView) view.findViewById(R.id.clienteId)).getText()
						.toString();
				String nomeCliente = ((TextView) view.findViewById(R.id.nome)).getText()
						.toString();

				// Starting new intent
				Intent in = new Intent(getApplicationContext(), EditClientActivity.class);
				// sending data to next activity
				in.putExtra("fantasiaId", produtoId);
				in.putExtra("nomeFantasia", nomeAux);
				in.putExtra(TAG_ID, clienteId);
				in.putExtra("nomeCliente", nomeCliente);

				// starting new activity and expecting some response back
				startActivityForResult(in, 100);
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
			pDialog = new ProgressDialog(NewSale1.this);
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
			JSONObject json = jParser.makeHttpRequest(url_all_clients, "GET", params);
			
			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);
				int tamanho_total = json.getInt("tamanho");
				if (success == 1) {
					// products found
					// Getting Array of Products
					products = json.getJSONArray(TAG_PRODUCTS);

					// looping through All Products
					for (int i = 0; i < tamanho_total; i++) {
						JSONObject c = products.getJSONObject(i);

						// Storing each json item in variable
						String nome = c.getString(TAG_NOME);
						String tel = c.getString(TAG_TELEFONE);
						String cpf = c.getString(TAG_CPF);
						String clienteId = c.getString(TAG_ID);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_NOME, nome);
						map.put(TAG_TELEFONE, tel);
						map.put(TAG_CPF, cpf);
						map.put(TAG_ID, clienteId);

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
							NewSale1.this, productsList,
							R.layout.list_item_client, new String[] { TAG_NOME, TAG_CPF, TAG_TELEFONE, TAG_ID},
							new int[] { R.id.nome, R.id.cpf, R.id.telefone, R.id.clienteId});
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}