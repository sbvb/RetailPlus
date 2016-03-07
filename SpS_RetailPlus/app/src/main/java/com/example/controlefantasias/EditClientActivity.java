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

public class EditClientActivity extends Activity {

	EditText txtNome;
	EditText txtCpf;
	EditText txtTelefone;

	Button btnSave;


	private static String clienteId;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();
	JSONParser jParser = new JSONParser();
	// single client url
	private static final String url_client_details = "http://54.69.170.202/servidor/sps_buscar_detalhes_cliente.php";

	// url to update client
	private static final String url_client_update = "http://54.69.170.202/servidor/sps_atualizar_cliente.php";


	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCT = "product";
	private static final String TAG_ID = "clienteId";
	private static final String TAG_NOME = "nome";
	private static final String TAG_CPF = "cpf";
	private static final String TAG_TELEFONE = "telefone";

	private static String nomeAux;
	private static String cpfAux;
	private static String telefoneAux;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_client);

		// getting client details from intent
		Intent i = getIntent();

		// getting client id (pid) from intent
		clienteId = i.getStringExtra(TAG_ID);

		// save button
		btnSave = (Button) findViewById(R.id.btnSave);

		// Getting complete client details in background thread

		txtNome = (EditText) findViewById(R.id.inputNome);
		txtCpf = (EditText) findViewById(R.id.inputCpf);
		txtTelefone = (EditText) findViewById(R.id.inputTelefone);

		// save button click event
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// starting background task to update client
				new SaveClientDetails().execute();
			}
		});


	}

	@Override
	protected void onStart() {
		super.onStart();
		new GetClientDetails().execute();
	}

	/**
	 * Background Async Task to Get complete client details
	 * */
	class GetClientDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditClientActivity.this);
			pDialog.setMessage("Loading client details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting client details in background thread
		 * */
		protected String doInBackground(String... params) {
			// Building Parameters
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair(TAG_ID, clienteId));

			// getting JSON Object
			// Note that create client url accepts POST method
			JSONObject json = jParser.makeHttpRequest(url_client_details, "POST", params1);

					int success;
					try {

						// json success tag
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							// successfully received client details
							JSONArray productObj = json.getJSONArray(TAG_PRODUCT); // JSON Array

							// get first client object from JSON Array
							JSONObject product = productObj.getJSONObject(0);

							// display client data in EditText
							nomeAux=product.getString(TAG_NOME);
							cpfAux=product.getString(TAG_CPF);
							telefoneAux=product.getString(TAG_TELEFONE);

						}else{
							// client with pid not found
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
			txtCpf.setText(cpfAux);
			txtTelefone.setText(telefoneAux);
		}
	}

	/**
	 * Background Async Task to  Save client Details
	 * */
	class SaveClientDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditClientActivity.this);
			pDialog.setMessage("Saving client ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Saving client
		 * */
		protected String doInBackground(String... args) {
			String nome = txtNome.getText().toString();
			String cpf = txtCpf.getText().toString();
			String tel = txtTelefone.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("nome", nome));
			params.add(new BasicNameValuePair("cpf", cpf));
			params.add(new BasicNameValuePair("telefone", tel));
			params.add(new BasicNameValuePair(TAG_ID, clienteId));

			// getting JSON Object
			// Note that create client url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_client_update,"POST", params);

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created client
					Intent i = new Intent(getApplicationContext(), AllClientsActivity.class);
					startActivity(i);

					// closing this screen
					finish();
				} else {
					// failed to create client
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
			// dismiss the dialog once client uupdated
			pDialog.dismiss();
		}
	}

}
