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
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewSale2 extends Activity {

	TextView txtProduto;
	TextView txtCliente;
	EditText txtQuantidade;

	Button btnSave;


	private static String produtoId;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();
	JSONParser jParser = new JSONParser();

	// url to update sale
	private static final String url_sale_update = "http://54.69.170.202/servidor/sps_criar_venda.php";


	// JSON Node names
	private static final String TAG_SUCCESS = "success";


	private static String nomeAux;
	private static String precoAux;
	private static String clienteId;
	private static String nomeClienteAux;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsale2);

		Intent i = getIntent();

		produtoId = i.getStringExtra("fantasiaId");
		nomeAux = i.getStringExtra("nomeFantasia");
		clienteId = i.getStringExtra("clienteId");
		nomeClienteAux = i.getStringExtra("nomeCliente");
		precoAux = i.getStringExtra("preco");

		txtProduto = (TextView) findViewById(R.id.produto);
		txtCliente = (TextView) findViewById(R.id.cliente);

		txtProduto.setText("Produto: "+nomeAux);
		txtCliente.setText("Cliente: "+nomeClienteAux);

		// save button
		btnSave = (Button) findViewById(R.id.btnSave);



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

	class SaveSaleDetails extends AsyncTask<String, String, String> {


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(NewSale2.this);
			pDialog.setMessage("Saving product ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}


		protected String doInBackground(String... args) {
			String quant = txtQuantidade.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("preco", precoAux));
			params.add(new BasicNameValuePair("produtoId", produtoId));
			params.add(new BasicNameValuePair("clienteId", clienteId));
			params.add(new BasicNameValuePair("quantidade", quant));

			// getting JSON Object

			JSONObject json = jsonParser.makeHttpRequest(url_sale_update,"POST", params);

			// Check your log cat for JSON reponse
			Log.d("sale: ", json.toString());
			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {

					Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
					startActivity(i);


					finish();
				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}


		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
		}
	}

}