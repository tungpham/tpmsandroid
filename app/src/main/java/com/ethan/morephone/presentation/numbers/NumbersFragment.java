package com.ethan.morephone.presentation.numbers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.NumberEntity;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;
import com.ethan.morephone.presentation.numbers.adapter.NumbersAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 3/16/17.
 */

public class NumbersFragment extends BaseFragment implements NumbersAdapter.OnItemNumberClickListener{

    public static NumbersFragment getInstance() {
        return new NumbersFragment();
    }

    private NumbersAdapter mNumbersAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_numbers, container, false);

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
}
