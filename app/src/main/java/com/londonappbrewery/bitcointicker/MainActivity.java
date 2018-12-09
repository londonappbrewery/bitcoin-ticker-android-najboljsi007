package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";
    private final String LOG_TAG = "Bitcoin";


    // Member Variables:
    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String currency = parent.getItemAtPosition(position).toString();
                Log.d(LOG_TAG, "Currenry selected: " + currency);
                String queryUrl = BASE_URL + currency;
                letsDoSomeNetworking(queryUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(LOG_TAG, "Nothing selected on spinner!");
            }
        });

    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(final String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.d(LOG_TAG, "Status code: " + statusCode);
                Log.d(LOG_TAG, "JSON Data: " + response.toString());
                BitcoinDataModel bitcoinData = BitcoinDataModel.fromJson(response);
                updateUI(bitcoinData);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] herders, Throwable e, JSONObject response) {
                Log.d(LOG_TAG, "Status code: " + statusCode);
                Log.d(LOG_TAG, "Failure: " + e.toString());
                Toast.makeText(MainActivity.this, "Fetch failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(BitcoinDataModel bitcoinData) {
        mPriceTextView.setText(bitcoinData.mPrice);
    }
}
