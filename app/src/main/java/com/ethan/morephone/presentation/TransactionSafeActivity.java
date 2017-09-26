
package com.ethan.morephone.presentation;

import android.app.Activity;
import android.os.Bundle;

/**
 * A common superclass that keeps track of whether an {@link Activity} has saved its state yet or
 * not.
 */
public abstract class TransactionSafeActivity extends Activity {

    private boolean mIsSafeToCommitTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsSafeToCommitTransactions = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIsSafeToCommitTransactions = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsSafeToCommitTransactions = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mIsSafeToCommitTransactions = false;
    }

    public boolean isSafeToCommitTransactions() {
        return mIsSafeToCommitTransactions;
    }
}
