package com.project.app.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.app.adapters.CustomListContactAdapter;
import com.project.app.controllers.PhoneBookController;
import com.project.app.models.PhoneBook;
import com.project.app.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private final Context mainContext = this;
    private final PhoneBookController phoneBookController = new PhoneBookController(this);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int currentView = R.layout.activity_main;
        setContentView(currentView);
        buildMainView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh your activity here
        // For example, reload data, update UI, etc.
        // This code will be executed when you return to this activity from another activity
        buildMainView();
    }

    public void buildMainView() {
        setContentOnListView();

        final FloatingActionButton floatingActionButton = findViewById(R.id.button);

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeToRefresh);

        final ListView listView = findViewById(R.id.listView);

        final View coordinatorLayout = findViewById(R.id.containerSearch);
        final EditText searchTextBy = coordinatorLayout.findViewById(R.id.searchText);

        resetSearchText();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            buildMainView();
            swipeRefreshLayout.setRefreshing(false);
        });

        handleAdd(floatingActionButton);

        handleSearch(searchTextBy, listView);

    }

    //On click, go to Add contact page
    private void handleAdd(FloatingActionButton floatingActionButton) {
        floatingActionButton.setOnClickListener(v -> {
            Intent addContact = new Intent(MainActivity.this, AddContactActivity.class);
            startActivity(addContact);
        });
    }

    //reset search text after each update/add
    private void resetSearchText() {
        final View coordinatorLayout = findViewById(R.id.containerSearch);
        final EditText searchTextBy = coordinatorLayout.findViewById(R.id.searchText);
        searchTextBy.setText("");
    }

    private void handleSearch(EditText searchTextBy, ListView listView) {
        RadioGroup radioGroupOptions = findViewById(R.id.radioGroupOptions);

        radioGroupOptions.setOnCheckedChangeListener((group, checkedId) -> {
            // Map RadioButton IDs to their corresponding hint texts
            String hint = getSearchCriteria("Search By Name", "Search By Phone Number", "Search By Address", checkedId);
            if (hint != null) {
                searchTextBy.setHint(hint);
            } else {
                searchTextBy.setHint("Search By Name");
            }
            filterListView(checkedId, searchTextBy.getText().toString(), listView);
        });

        // Add a TextWatcher to the EditText for search
        searchTextBy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called when the text is being changed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Get the resource ID of the selected RadioButton
                int selectedRadioButtonId = radioGroupOptions.getCheckedRadioButtonId();
                filterListView(selectedRadioButtonId, s.toString(), listView);
            }
        });
    }

    //filter listview by search text and type (search)
    private void filterListView(int checkedId, String searchTextBy, ListView listView) {
        List<PhoneBook> listOfPhoneBook = null;
        String searchCriteria = getSearchCriteria("name", "phone", "address", checkedId);

        // Perform the search using the obtained search criteria
        if (searchCriteria != null) {
            listOfPhoneBook = phoneBookController.searchContactsByText(searchTextBy, searchCriteria);
        }

        listView.setAdapter(null);

        CustomListContactAdapter listViewAdapter = new CustomListContactAdapter(listOfPhoneBook, mainContext, MainActivity.this);

        listView.setAdapter(listViewAdapter);
    }

    @Nullable
    private static String getSearchCriteria(String name, String phone, String address, int selectedRadioButtonId) {
        Map<Integer, String> radioButtonSearchCriteriaMap = new HashMap<>();
        radioButtonSearchCriteriaMap.put(R.id.radioButtonName, name);
        radioButtonSearchCriteriaMap.put(R.id.radioButtonPhoneNumber, phone);
        radioButtonSearchCriteriaMap.put(R.id.radioButtonAddress, address);

        // Get the search criteria based on the selected RadioButton
        return radioButtonSearchCriteriaMap.get(selectedRadioButtonId);
    }

    public void setContentOnListView() {
        List<PhoneBook> listOfPhoneBook = phoneBookController.getAllContacts();
        if (listOfPhoneBook == null) {
            Toast.makeText(mainContext, "Contacts not found", Toast.LENGTH_SHORT).show();
        } else {
            ListView listView = findViewById(R.id.listView);

            listView.setAdapter(null);

            CustomListContactAdapter listViewAdapter = new CustomListContactAdapter(listOfPhoneBook, mainContext, this);

            listView.setAdapter(listViewAdapter);
        }
    }

    //delete the contact
    public void showDeleteDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
        builder.setMessage("Are you sure you want to delete this contact? This action can not be undone.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    phoneBookController.deleteContact(id);
                    try {
                        buildMainView();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .setNegativeButton("No", (dialog, which) -> {
                })
                .show();
    }

}