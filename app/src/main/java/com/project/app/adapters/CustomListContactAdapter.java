package com.project.app.adapters;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.project.app.activities.EditContactActivity;
import com.project.app.activities.MainActivity;
import com.project.app.models.PhoneBook;
import com.project.app.R;

import java.util.List;

public class CustomListContactAdapter extends ArrayAdapter<PhoneBook> implements View.OnClickListener {
    private final Context mainContext;
    private final MainActivity current;

    static class ViewHolder {
        TextView name;
        TextView phoneNumberAndAddress;

        ImageButton buttonEditPhoneBook;

        ImageButton buttonDeletePhoneNumber;

    }

    public CustomListContactAdapter(List<PhoneBook> data, Context mainContext, MainActivity current) {
        super(mainContext, R.layout.list_item_layout, data);
        this.mainContext = mainContext;
        this.current = current;
    }

    @Override
    public void onClick(View v) {
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        PhoneBook phoneBook = getItem(position);
        ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_layout, parent, false);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.nameListItem);
            holder.phoneNumberAndAddress = convertView.findViewById(R.id.phoneNumberAndAddressListItem);
            holder.buttonEditPhoneBook = convertView.findViewById(R.id.buttonEditContact);
            holder.buttonDeletePhoneNumber = convertView.findViewById(R.id.buttonDeleteContact);
            // Initialize other views here
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        View result;
        result = convertView;

        Animation animation = AnimationUtils.loadAnimation(mainContext, R.anim.slide_animation);
        result.startAnimation(animation);

        assert phoneBook != null;
        holder.name.setText(phoneBook.getName());

        holder.phoneNumberAndAddress.setText(String.format("%s\n\n%s", phoneBook.getPhoneNumber(), phoneBook.getAddress()));
        holder.buttonEditPhoneBook.setTag(position);
        holder.buttonDeletePhoneNumber.setTag(position);

        holder.buttonEditPhoneBook.setOnClickListener(v -> {

            int position1 = (Integer) v.getTag();
            PhoneBook object = getItem(position1);
            Intent editContactActivity = new Intent(mainContext.getApplicationContext(), EditContactActivity.class);
            editContactActivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
            editContactActivity.putExtra("current_contact", object);
            mainContext.getApplicationContext().startActivity(editContactActivity);

        });

        holder.buttonDeletePhoneNumber.setOnClickListener(v -> {
            int position12 = (Integer) v.getTag();
            PhoneBook object = getItem(position12);
            assert object != null;
            current.showDeleteDialog(object.getId());
        });

        return convertView;
    }
}