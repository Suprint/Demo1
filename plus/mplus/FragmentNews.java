package com.mpay.plus.mplus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpay.agent.R;
import com.mpay.plus.database.News;
import com.mpay.plus.lib.MPlusLib;
import com.mpay.plus.util.ImageLoader;
import com.mpay.plus.util.Util;

/**
 * Hien thi tin tuc, news, adverds duoi dang image
 * 
 * @author quyenlm.vn@gmail.com
 * 
 * */
public final class FragmentNews extends Fragment {
	public static final String TAG = "NewsFragment";
	private int iType = 0;
	private static final String KEY_CONTENT = "NewsFragment:Content";
	private News mNews = null;
	private ImageView mImageView = null;
	private ImageLoader mImageLoader;

	/**
	 * type = 0: display image, type = 1: display image with title and
	 * description
	 * */
	public static FragmentNews newInstance(News news, int type) {
		FragmentNews fragment = new FragmentNews();
		fragment.setNews(news);
		fragment.setType(type);
		return fragment;
	}

	public void setNews(News news) {
		this.mNews = news;
	}

	public void setType(int type) {
		this.iType = type;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
			mNews = (News) savedInstanceState.getSerializable(KEY_CONTENT);
		}

		mImageLoader = new ImageLoader(getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = null;

		if (iType == 0) {
			view = inflater.inflate(R.layout.old_list_item_news_image, container,
					false);
			if (mNews != null) {
				try {
					mImageView = (ImageView) view.findViewById(R.id.image);
					mImageView.setContentDescription(mNews.getDescription());

					if (Util.isUri(mNews.getImage())) {
						// load from cache or web
						ImageView image = (ImageView) view
								.findViewById(R.id.image);
						mImageLoader.DisplayImage(mNews.getImage(), image,
								R.drawable.logo_default);
					} else {
						// load default
						mImageView.setImageDrawable(Util.getImage(
								getActivity(), mNews.getImage()));
					}
				} catch (Exception ex) {
					MPlusLib.debug(TAG, "onCreateView", ex);
				}
			}
		} else {
			view = inflater.inflate(R.layout.old_list_item_news, container, false);
			if (mNews != null) {
				try {
					mImageView = (ImageView) view.findViewById(R.id.image);
					mImageView.setContentDescription(mNews.getDescription());

					if (Util.isUri(mNews.getImage())) {
						// load from cache or web
						ImageView image = (ImageView) view
								.findViewById(R.id.image);
						mImageLoader.DisplayImage(mNews.getImage(), image);
					} else {
						// load default
						mImageView.setImageDrawable(Util.getImage(
								getActivity(), mNews.getImage()));
					}

					((TextView) view.findViewById(R.id.title)).setText(mNews
							.getTitle());
					((TextView) view.findViewById(R.id.description))
							.setText(mNews.getDescription());
				} catch (Exception ex) {
					MPlusLib.debug(TAG, "onCreateView", ex);
				}
			}
		}
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(KEY_CONTENT, mNews);
	}
}