package com.ethan.morephone.presentation.contact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.contact.Contact;
import com.ethan.morephone.R;
import com.ethan.morephone.widget.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.List;

public final class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements RecyclerViewFastScroller.BubbleTextGetter {
    private List<Contact> mContacts;

    private ContactItemClick mContactItemClick;

    public ContactAdapter(Context context, List<Contact> contacts, ContactItemClick contactItemClick) {
        this.mContacts = contacts;
        mContactItemClick = contactItemClick;
    }

    public void replaceData(List<Contact> contacts) {
        mContacts = contacts;
        notifyDataSetChanged();
    }

    public List<Contact> getData() {
        return mContacts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = mContacts.get(position);
        holder.textDisplayName.setText(contact.getDisplayName());
        holder.textDisplayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContactItemClick != null) mContactItemClick.onContactItemClick();
            }
        });
    }

    @Override
    public String getTextToShowInBubble(final int pos) {
        return Character.toString(mContacts.get(pos).getDisplayName().charAt(0));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textDisplayName;

        private ViewHolder(View itemView) {
            super(itemView);
            this.textDisplayName = (TextView) itemView.findViewById(R.id.text);
        }
    }

    public interface ContactItemClick {
        void onContactItemClick();
    }
}
