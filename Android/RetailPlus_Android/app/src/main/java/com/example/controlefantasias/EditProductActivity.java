package com.example.controlefantasias;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditProductActivity extends Activity {

	EditText txtNome;
	EditText txtPreco;
	EditText txtQuantidade;
	EditText txtCreatedAt;
	Button btnSave;
	Button btnDelete;

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
	private static final String TAG_ID = "fantasiaId";
	private static final String TAG_NOME = "nome";
	private static final String TAG_PRECO = "preco";
	private static final String TAG_QUANTIDADE = "quantidade";

	private static String nomeAux;
	private static String precoAux;
	private static String quantidadeAux;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_product);

		// getting product details from intent
		Intent i = getIntent();

		// getting product id (pid) from intent
		produtoId = i.getStringExtra(TAG_ID);

		// save button
		btnSave = (Button) findViewById(com.example.controlefantasias.R.id.btnSave);

		// Getting complete product details in background thread

		txtNome = (EditText) findViewById(R.id.inputNome);
		txtPreco = (EditText) findViewById(R.id.inputPreco);
		txtQuantidade = (EditText) findViewById(R.id.inputQuantidade);

		// save button click event
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// starting background task to update product
				new SaveProductDetails().execute();
			}
		});


	}

	@Override
	protected void onStart() {
		super.onStart();
		new GetProductDetails().execute();
	}

	/**
	 * Background Async Task to Get complete product details
	 * */
	class GetProductDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditProductActivity.this);
			pDialog.setMessage("Loading product details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting product details in background thread
		 * */
		protected String doInBackground(String... params) {
			// Building Parameters
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair(TAG_ID, produtoId));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jParser.makeHttpRequest(url_product_details, "POST", params1);

					int success;
					try {
						// check your log for json response
						Log.d("Single Product Details", json.toString());

						// json success tag
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							// successfully received product details
							JSONArray productObj = json.getJSONArray(TAG_PRODUCT); // JSON Array

							// get first product object from JSON Array
							JSONObject product = productObj.getJSONObject(0);

							// display product data in EditText
							nomeAux=product.getString(TAG_NOME);
							precoAux=product.getString(TAG_PRECO);
							quantidadeAux=product.getString(TAG_QUANTIDADE);

						}else{
							// product with pid not found
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
			// dismiss the dialog once got all details
			pDialog.dismiss();
			txtNome.setText(nomeAux);
			txtPreco.setText(precoAux);
			txtQuantidade.setText(quantidadeAux);
		}
	}

	/**
	 * Background Async Task to  Save product Details
	 * */
	class SaveProductDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditProductActivity.this);
			pDialog.setMessage("Saving product ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Saving product
		 * */
		protected String doInBackground(String... args) {
			String nome = txtNome.getText().toString();
			String preco = txtPreco.getText().toString();
			String quantidade = txtQuantidade.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("nome", nome));
			params.add(new BasicNameValuePair("preco", preco));
			params.add(new BasicNameValuePair("quantidade", quantidade));
			params.add(new BasicNameValuePair(TAG_ID, produtoId));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_product_update,"POST", params);

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
			// dismiss the dialog once product uupdated
			pDialog.dismiss();
		}
	}

}
