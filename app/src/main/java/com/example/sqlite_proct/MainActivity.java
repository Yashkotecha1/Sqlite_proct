package com.example.sqlite_proct;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    EditText editTextName, editTextPrice, editTextId;
    Button buttonAdd, buttonUpdate, buttonDelete, buttonView;
    TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextId = findViewById(R.id.editTextId);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonView = findViewById(R.id.buttonView);
        textViewResult = findViewById(R.id.textViewResult);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);

        loadProducts();

        buttonAdd.setOnClickListener(view -> addProduct());
        buttonUpdate.setOnClickListener(view -> updateProduct());
        buttonDelete.setOnClickListener(view -> deleteProduct());
        buttonView.setOnClickListener(view -> displayProducts());
    }

    private void loadProducts() {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllProducts();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                double price = cursor.getDouble(2);
                productList.add(new Product(id, name, price));
            }
        }

        cursor.close();
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);
    }
    private void addProduct() {
        String name = editTextName.getText().toString();
        double price = Double.parseDouble(editTextPrice.getText().toString());

        if (databaseHelper.addProduct(name, price)) {
            Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to Add Product", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayProducts() {
        Cursor cursor = databaseHelper.getAllProducts();
        if (cursor.getCount() == 0) {
            textViewResult.setText("No products found");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        while (cursor.moveToNext()) {
            stringBuilder.append("ID: ").append(cursor.getInt(0)).append("\n");
            stringBuilder.append("Name: ").append(cursor.getString(1)).append("\n");
            stringBuilder.append("Price: ").append(cursor.getDouble(2)).append("\n\n");
        }

        textViewResult.setText(stringBuilder.toString());
    }

    private void updateProduct() {
        int id = Integer.parseInt(editTextId.getText().toString());
        String name = editTextName.getText().toString();
        double price = Double.parseDouble(editTextPrice.getText().toString());

        if (databaseHelper.updateProduct(id, name, price)) {
            Toast.makeText(this, "Product Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to Update Product", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteProduct() {
        int id = Integer.parseInt(editTextId.getText().toString());

        if (databaseHelper.deleteProduct(id)) {
            Toast.makeText(this, "Product Deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to Delete Product", Toast.LENGTH_SHORT).show();
        }
    }
}
