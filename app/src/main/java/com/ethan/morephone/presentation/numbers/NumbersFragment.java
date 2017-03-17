package com.ethan.morephone.presentation.numbers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.NumberEntity;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.dial.DialActivity;
import com.ethan.morephone.presentation.message.conversation.ConversationsActivity;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;
import com.ethan.morephone.presentation.numbers.adapter.NumbersAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 3/16/17.
 */

public class NumbersFragment extends BaseFragment implements NumbersAdapter.OnItemNumberClickListener{

    public static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";

    public static NumbersFragment getInstance() {
        return new NumbersFragment();
    }

    private NumbersAdapter mNumbersAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_numbers, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.setTitleActionBar(toolbar, "");

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerSpacingItemDecoration(getContext(), R.dimen.item_number_space));

        List<NumberEntity> numberEntities = new ArrayList<>();
        numberEntities.add(new NumberEntity("1", "+17606215500"));
        numberEntities.add(new NumberEntity("2", "+18052284394"));
        numberEntities.add(new NumberEntity("3", "+1766880099"));
        numberEntities.add(new NumberEntity("4", "+1777889933"));

        mNumbersAdapter = new NumbersAdapter(numberEntities, this);
        recyclerView.setAdapter(mNumbersAdapter);

        return view;
    }

    @Override
    public void onItemClick(int pos) {

    }

    @Override
    public void onItemCall(int pos) {
        Intent intent = new Intent(getActivity(), DialActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemMessage(int pos) {
        NumberEntity numberEntity = mNumbersAdapter.getData().get(pos);
        Intent intent = new Intent(getActivity(), ConversationsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PHONE_NUMBER, numberEntity.phoneNumber);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
