package com.ethan.morephone.presentation.contact.editor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.BaseFragment;

/**
 * Created by truongnguyen on 9/28/17.
 */

public class ContactEditorFragment extends BaseFragment {


    public static ContactEditorFragment getInstance() {
        return new ContactEditorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_creation, container, false);

        BaseActivity baseActivity = (BaseActivity) getActivity();

        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        baseActivity.setTitleActionBar(mToolbar, getString(R.string.contact_creation_label));

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_contact_creation, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                getActivity().finish();
                break;

            case R.id.menu_done:

                break;

            default:
                break;
        }
        return true;
    }
}
