/*
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ethan.morephone.presentation.authentication.login.google;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.authentication.AuthenticationFragment;
import com.ethan.morephone.utils.ActivityUtils;

public class LoginActivity extends BaseActivity {

    public static final String EXTRA_ACTION = "EXTRA_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        int loginAction = getIntent().getIntExtra(EXTRA_ACTION, Action.LOGIN);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof LoginFragment) return;
        LoginFragment browserFragment = LoginFragment.newInstance(loginAction);
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                browserFragment,
                R.id.content_frame,
                AuthenticationFragment.class.getSimpleName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

//    @Override
//    public Fragment getResultTargetFragment() {
//        return getSupportFragmentManager().findFragmentById(R.id.content_frame);
//    }

    public static class Action {
        public static final int LOGIN = 2000;
        public static final int LOGOUT = 2001;
        private Action() {}
    }
}
