package com.example.controlefantasias;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewSale2 extends Activity {


	EditText txtQuantidade;

	Button btnSave;


	private static String produtoId;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();
	JSONParser jParser = new JSONParser();
	// single product url
	private static final String url_product_details = "http://54.69.170.202/servidor/sps_buscar_detalhes_produto.php";

	// url to update product
	private static final String url_product_update = "http://54.69.170.202/servidor/sps_atualizar_produto.php";


	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCT = "product";
	private static final String TAG_ID = "clienteId";
	private static final String TAG_NOME = "nome";
	private static final String TAG_PRECO = "preco";
	private static final String TAG_QUANTIDADE = "quantidade";

	private static String nomeAux;
	private static String precoAux;
	private static String quantidadeAux;
	private static String clienteId;
	private static String nomeClienteAux;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsale2);

		// getting product details from intent
		Intent i = getIntent();

		// getting product id (pid) from intent

		produtoId = i.getStringExtra("fantasiaId");
		nomeAux = i.getStringExtra("nomeFantasia");
		clienteId = i.getStringExtra("fantasiaId");
		nomeClienteAux = i.getStringExtra("nomeCliente");
		precoAux = i.getStringExtra("preco");


		// save button
		btnSave = (Button) findViewById(R.id.btnSave);

		// Getting complete product details in background thread

		txtQuantidade = (EditText) findViewById(R.id.inputQuantidade);

		// save button click event
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// starting background task to update product
				new SaveSaleDetails().execute();
			}
		});


	}


	/**
	 * Background Async Task to  Save product Details
	 * */
	class SaveSaleDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(NewSale2.this);
			pDialog.setMessage("Saving product ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Saving product
		 * */
		protected String doInBackground(String... args) {
			String quant = txtQuantidade.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("preco", precoAux));
			params.add(new BasicNameValuePair("produtoId", produtoId));
			params.add(new BasicNameValuePair("clienteId", clienteId));
			params.add(new BasicNameValuePair("quantidade", quant));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_product_update,"POST", params);

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
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
			// dismiss the dialog once product uupdated
			pDialog.dismiss();
		}
	}

}
