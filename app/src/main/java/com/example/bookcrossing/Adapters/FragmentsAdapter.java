package com.example.bookcrossing.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.bookcrossing.Fragments.BooksFragment;
import com.example.bookcrossing.Fragments.ChatsFragment;
import com.example.bookcrossing.Fragments.ProfileFragment;

public class FragmentsAdapter extends FragmentPagerAdapter {
    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                return new ChatsFragment();
            case 1:
                return new BooksFragment();
            case 2:
                return new ProfileFragment();
            default:
                return new ProfileFragment();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "CHATS";
        }
        if (position == 1) {
            title = "BOOKS";
        }
        if (position == 2) {
            title = "PROFILE";
        }
        return title;
    }
}
