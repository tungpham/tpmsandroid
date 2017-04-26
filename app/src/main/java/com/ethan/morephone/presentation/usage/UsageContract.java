package com.ethan.morephone.presentation.usage;

import android.content.Context;

import com.android.morephone.data.entity.usage.Usage;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

/**
 * Created by Ethan on 4/26/17.
 */

public interface UsageContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isActive);

        void showUsage(Usage usage);

    }

    interface Presenter extends BasePresenter {

        void loadUsage(Context context);

    }
}
