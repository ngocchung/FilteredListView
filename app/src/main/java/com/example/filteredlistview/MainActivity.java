package com.example.filteredlistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private HighlightArrayAdapter mHighlightArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Listview sample data
        String products[] = {"HP Probook 4440s", "Nokia 8800", "Lumia 950", "Dell Inspiron", "HTC One X", "HTC Wildfire S", "HTC Sense", "HTC Sensation XE",
                "iPhone 4S", "Samsung Galaxy Note 800",
                "Samsung Galaxy S3", "MacBook Air", "Mac Mini", "MacBook Pro"};

        ListView listView = (ListView) findViewById(R.id.listview);
        EditText editText = (EditText) findViewById(R.id.inputSearch);

        // Adding items to listview
        mHighlightArrayAdapter = new HighlightArrayAdapter(this, R.layout.list_item, R.id.product_name, products);
        listView.setAdapter(mHighlightArrayAdapter);

        // Enabling Search Filter
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                mHighlightArrayAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }
}
