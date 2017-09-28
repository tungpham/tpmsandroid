package com.ethan.morephone.presentation.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.contact.detail.ContactDetailActivity;
import com.ethan.morephone.presentation.contact.detail.QuickContactActivity;
import com.ethan.morephone.presentation.contact.editor.ContactEditorActivity;
import com.ethan.morephone.widget.RecyclerViewFastScroller;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by truongnguyen on 9/25/17.
 */

public class ContactFragment extends BaseFragment implements ContactAdapter.ContactItemClick, View.OnClickListener {

    public static ContactFragment getInstance() {
        return new ContactFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        final ContactAdapter adapter = new ContactAdapter(getContext(), new ArrayList<Contact>(), this);
        recyclerView.setAdapter(adapter);
        final RecyclerViewFastScroller fastScroller = (RecyclerViewFastScroller) view.findViewById(R.id.fastscroller);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                //TODO if the items are filtered, considered hiding the fast scroller here
                final int firstVisibleItemPosition = findFirstVisibleItemPosition();
                if (firstVisibleItemPosition != 0) {
                    // this avoids trying to handle un-needed calls
                    if (firstVisibleItemPosition == -1)
                        //not initialized, or no items shown, so hide fast-scroller
                        fastScroller.setVisibility(View.GONE);
                    return;
                }
                final int lastVisibleItemPosition = findLastVisibleItemPosition();
                int itemsShown = lastVisibleItemPosition - firstVisibleItemPosition + 1;
                //if all items are shown, hide the fast-scroller
                fastScroller.setVisibility(adapter.getItemCount() > itemsShown ? View.VISIBLE : View.GONE);
            }
        });
        fastScroller.setRecyclerView(recyclerView);
        fastScroller.setViewsToUse(R.layout.recycler_view_fast_scroller__fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);

        view.findViewById(R.id.button_create).setOnClickListener(this);

        Gson gson = new Gson();
        List<Contact> contacts = new ArrayList();
        Contact contact = new Contact("123", "A Truong", "0974878244", "photoUri", "phonenumberId", "Hanoi", "coderdaudat@gmail.com", "20-02-1991", "Friend", "");
        contacts.add(contact);
        contact = new Contact("1234", "A Hoang", "0974878243", "photoUri", "phonenumberId", "Hanoi", "coderdaudat@gmail.com", "20-02-1991", "Friend", "");
        contacts.add(contact);
        contact = new Contact("12345", "A Nguyen", "0974878242", "photoUri", "phonenumberId", "Hanoi", "coderdaudat@gmail.com", "20-02-1991", "Friend", "");
        contacts.add(contact);
        contact = new Contact("123456", "Bac Sac", "0974878242", "photoUri", "phonenumberId", "Hanoi", "coderdaudat@gmail.com", "20-02-1991", "Friend", "");
        contacts.add(contact);
        contact = new Contact("123457", "Co Trang", "0974878242", "photoUri", "phonenumberId", "Hanoi", "coderdaudat@gmail.com", "20-02-1991", "Friend", "");
        contacts.add(contact);
        contact = new Contact("123459", "Xe Bao Yen", "0974878242", "photoUri", "phonenumberId", "Hanoi", "coderdaudat@gmail.com", "20-02-1991", "Friend", "");
        contacts.add(contact);
        contact = new Contact("123453", "Mom", "0974878242", "photoUri", "phonenumberId", "Hanoi", "coderdaudat@gmail.com", "20-02-1991", "Friend", "");
        contacts.add(contact);
        contact = new Contact("123450", "Dad", "0974878242", "photoUri", "phonenumberId", "Hanoi", "coderdaudat@gmail.com", "20-02-1991", "Friend", "");
        contacts.add(contact);

        adapter.replaceData(contacts);

        DebugTool.logD("KQ: " + gson.toJson(contact));
        return view;
    }


    @Override
    public void onContactItemClick() {
        startActivity(new Intent(getActivity(), ContactDetailActivity.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_create:
                startActivity(new Intent(getActivity(), ContactEditorActivity.class));
                break;
            default:
                break;
        }
    }
}
