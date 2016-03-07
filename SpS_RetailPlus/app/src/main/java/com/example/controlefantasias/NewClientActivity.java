package com.example.controlefantasias;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewClientActivity extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jParser = new JSONParser();
	JSONParser jsonParser = new JSONParser();

	EditText inputNome;
	EditText inputCpf;
	EditText inputTelefone;


	// JSON Node names
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_TAMANHO = "tamanho";
	private static final String TAG_ID = "id";



	private static String url_create_client = "http://54.69.170.202/servidor/sps_criar_cliente.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_client);

		// Edit Text
		inputNome = (EditText)findViewById(R.id.inputNome);
		inputCpf = (EditText) findViewById(R.id.inputCpf);
		inputTelefone = (EditText) findViewById(R.id.inputTelefone);


		// Create button
		Button btnCreateClient = (Button) findViewById(R.id.btnCreateClient);

		// button click event
		btnCreateClient.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				new CreateNewClient().execute();
			}
		});
	}



	/**
	 * Background Async Task to Create new client
	 * */
	class CreateNewClient extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(NewClientActivity.this);
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
			String cpf = inputCpf.getText().toString();
			String tel = inputTelefone.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("nome", nome));
			params.add(new BasicNameValuePair("cpf", cpf));
			params.add(new BasicNameValuePair("telefone", tel));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_client,"POST", params);


			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					Intent i = new Intent(getApplicationContext(), AllClientsActivity.class);
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
