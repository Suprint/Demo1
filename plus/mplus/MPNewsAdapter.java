package com.mpay.plus.mplus;

import java.util.Vector;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mpay.plus.database.News;

/**
 * @author quyenlm.vn@gmail.com
 * */
public class MPNewsAdapter extends FragmentPagerAdapter {// implements IconPagerAdapter {
	public final static String TAG = "MPFragmentAdapter";
	private Vector<News> mMenu;
	private int mCount = 0;
	private int iType = 0;
	
	/**
	 * type = 0: display image,
	 * type = 1: display image with title and description
	 * */
    public MPNewsAdapter(FragmentManager fm, Vector<News> menu, int type) {
        super(fm);
        mMenu = menu;
        mCount = mMenu.size();
        iType = type;
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentNews.newInstance(mMenu.elementAt(position), iType);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return mMenu.elementAt(position).getTitle();
    }

//    @Override
//    public int getIconResId(int position) {
//      return Util.getIconId(MPFragmentAdapter.this, mMenu.elementAt(position).getImage());
//    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}