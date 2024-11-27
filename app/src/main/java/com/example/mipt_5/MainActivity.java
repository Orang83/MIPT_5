package com.example.mipt_5;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvItems;
    private TextView tvStatus;
    private ArrayAdapter listAdapter;
    private Switch swUseAsyncTask;
    private EditText filterEditText;
    private String[] currencyArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.lvItems = findViewById(R.id.lv_items);
        this.tvStatus = findViewById(R.id.tv_status);
        this.swUseAsyncTask = findViewById(R.id.sw_use_async_task);
        this.filterEditText = findViewById(R.id.filterEditText);

        this.listAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
        this.lvItems.setAdapter(this.listAdapter);
    }

    public void onBtnFilterData(View view) {
        String currencyCode = filterEditText.getText().toString().toUpperCase().trim();
        if (TextUtils.isEmpty(currencyCode)){
            Toast.makeText(MainActivity.this, "Please enter a currency code", Toast.LENGTH_SHORT).show();
        } else{
            List<String> filteredCurrencies = new ArrayList<>();
            for(String currency : currencyArray){
                if (currency.contains(currencyCode)){
                    filteredCurrencies.add(currency.trim());
                }
            }
            String filteredSentence = TextUtils.join("\n", filteredCurrencies);
            tvStatus.setText(filteredSentence);
        }
    }
    public void onBtnGetDataClick(View view) {
        this.tvStatus.setText("Data loading... ");
        if (this.swUseAsyncTask.isChecked()) {
            getDataByAsyncTask();
            Toast.makeText(this, "Using Async task ", Toast.LENGTH_LONG).show();
        } else {
            getDataByThread();
            Toast.makeText(this, "Using Thread and Runnable", Toast.LENGTH_LONG).show();
        }
    }

    public void getDataByAsyncTask() {
        new DataLoader() {
            @Override
            public void onPostExecute(String result) {
                tvStatus.setText(result);
                currencyArray = result.split("\n");
            }
        }.execute("https://www.floatrates.com/daily/eur.xml");

    }

    public void getDataByThread() {

        Runnable getDataAndDisplayRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    final String result = DataReader.getValuesFromApi("https://www.floatrates.com/daily/eur.xml");

                    Runnable updateUIRunnable = new Runnable() {
                        @Override
                        public void run() {
                            tvStatus.setText(result);
                        }
                    };
                    runOnUiThread(updateUIRunnable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}