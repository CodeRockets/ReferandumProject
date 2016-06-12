package com.coderockets.referandumproject.fragment;

import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;

import com.coderockets.referandumproject.R;

import org.androidannotations.annotations.EFragment;

import java.io.File;

@EFragment(R.layout.fragment_askquestion_layout)
public class AskQuestionFragment extends BaseFragment {

    public void AskQuestionFragmentInit() {
        Uri uri = FileProvider.getUriForFile(getActivity(), "com.coderockets.referandum", new File("abc"));

    }
}
