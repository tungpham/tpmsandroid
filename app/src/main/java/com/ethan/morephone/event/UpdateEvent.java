package com.ethan.morephone.event;

/**
 * Created by Ethan on 3/11/17.
 */

public class UpdateEvent {

    private boolean mIsUpdate;

    public UpdateEvent(boolean isUpdate) {
        this.mIsUpdate = isUpdate;
    }

    public boolean isUpdate() {
        return mIsUpdate;
    }
}
