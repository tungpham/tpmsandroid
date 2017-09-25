package com.ethan.morephone.presentation.contact;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ethan.morephone.R;
import com.ethan.morephone.widget.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.List;

public final class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements RecyclerViewFastScroller.BubbleTextGetter {
    private final List<String> items;

    public ContactAdapter(int numberOfItems) {
        List<String> items = new ArrayList<>();
        java.util.Random r = new java.util.Random();
        for (int i = 0; i < numberOfItems; i++)
            items.add(((char) ('A' + r.nextInt('Z' - 'A'))) + " " + Integer.toString(i));
        java.util.Collections.sort(items);
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = items.get(position);
        holder.setText(text);
    }

    @Override
    public String getTextToShowInBubble(final int pos) {
        return Character.toString(items.get(pos).charAt(0));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        private ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.text);
        }

        public void setText(CharSequence text) {
            textView.setText(text);
        }
    }
}
