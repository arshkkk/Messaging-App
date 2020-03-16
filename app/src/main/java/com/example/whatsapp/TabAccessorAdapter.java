package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAccessorAdapter extends FragmentPagerAdapter
{
    int noOfTabs;


    public TabAccessorAdapter(@NonNull FragmentManager fm, int noOfTabs) {
        super(fm);
        this.noOfTabs= noOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
            ChatsFragments chatsFragments= new ChatsFragments();
            return chatsFragments;
            case 1:
                GroupFragment groupFragment= new GroupFragment();
                return groupFragment;
            case 2:
            ContactFragment contactFragment = new ContactFragment();
            return contactFragment;

                default: return null;
        }

    }


    @Override
    public int getCount() {
        return noOfTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch(position)
        {
            case 0:
               return "Chats";
            case 1:
                return "Group";
            case 2:
               return "Contacts";
            default: return null;
        }    }
}
