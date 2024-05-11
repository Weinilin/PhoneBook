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
import com.project.app.models.PhoneBook;

public class EditContactActivity extends AppCompatActivity {
    private final Context mainContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_phone_book_layout);
        PhoneBook model = (PhoneBook) getIntent().getSerializableExtra("current_contact");
        final EditText name = findViewById(R.id.nameUpdate);
        final EditText phoneNumber = findViewById(R.id.phoneNumberUpdate);
        final EditText address = findViewById(R.id.addressUpdate);
        final ImageButton imageButton = findViewById(R.id.update_button);
        PhoneBookController phoneBookController = new PhoneBookController(mainContext);

        assert model != null;
        name.setText(model.getName());
        phoneNumber.setText(model.getPhoneNumber());
        address.setText(model.getAddress());

        imageButton.setOnClickListener(v -> {
            if (address.getText().toString().trim().isEmpty() || name.getText().toString().trim().isEmpty() || phoneNumber.getText().toString().trim().isEmpty()) {
                Toast.makeText(mainContext, "Data cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                ResponseModel responseModel = phoneBookController.editContact(name.getText().toString().trim(), phoneNumber.getText().toString().trim(), address.getText().toString().trim(), model.getId());
                Toast.makeText(mainContext, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                if (responseModel.getCode() == ResponseCode.SUCCESS)
                    finish();
            }
        });


    }
}
