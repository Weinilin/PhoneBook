package com.project.app.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.app.R;
import com.project.app.controllers.PhoneBookController;
import com.project.app.models.ResponseCode;
import com.project.app.models.ResponseModel;

public class AddContactActivity extends AppCompatActivity {

    private final Context maincontext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_phone_book_layout);

        PhoneBookController phoneBookController = new PhoneBookController(this);

        final ImageButton imageButton = findViewById(R.id.insert_button);
        imageButton.setOnClickListener(v -> {
            final EditText name = findViewById(R.id.name);
            final EditText phoneNumber = findViewById(R.id.phoneNumber);
            final EditText address = findViewById(R.id.address);

            if (address.getText().toString().trim().isEmpty() || name.getText().toString().trim().isEmpty() || phoneNumber.getText().toString().trim().isEmpty()) {
                Toast.makeText(maincontext, "Data cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    ResponseModel responseModel = phoneBookController.addContact(name.getText().toString().trim(), phoneNumber.getText().toString().trim(), address.getText().toString().trim());
                    Toast.makeText(maincontext, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    if (responseModel.getCode() == ResponseCode.SUCCESS) {
                        name.setText("");
                        phoneNumber.setText("");
                        address.setText("");
                        finish();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

        });
    }
}
