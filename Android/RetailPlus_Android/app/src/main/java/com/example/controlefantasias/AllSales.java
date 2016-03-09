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

public class AllSales extends ListActivity {

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> productsList;

	// url to get all products list
	private static final String url_all_clients = "http://54.69.170.202/servidor/sps_listar_vendas.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_NOME = "nome";
	private static final String TAG_CPF = "cpf";
	private static final String TAG_TELEFONE = "telefone";
	private static final String TAG_ID = "clienteId";

	Button btnAdd;

	// products JSONArray
	JSONArray products = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_sales);


		// Hashmap for ListView
		productsList = new ArrayList<HashMap<String, String>>();

		// Loading products in Background Thread
		new LoadAllSales().execute();


		// Get listview
		ListView lv = getListView();



		btnAdd = (Button) findViewById(R.id.btnAdd);


	}





	class LoadAllSales extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AllSales.this);
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
						String produto = c.getString("nomeProduto");
						String cliente = c.getString("nomeCliente");
						String data = c.getString("create_time");
						String quantidade = c.getString("quantidade");
						String precoTotal = c.getString("preco");

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put("produto", produto);
						map.put("cliente", cliente);
						map.put("data", data);
						map.put("quantidade", quantidade);
						map.put("precoTotal", precoTotal);
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
							AllSales.this, productsList,
							R.layout.list_item_sale, new String[] { "produto", "cliente", "data", "quantidade","precoTotal"},
							new int[] { R.id.produto, R.id.cliente, R.id.data, R.id.quantidade,R.id.precoTotal});
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}