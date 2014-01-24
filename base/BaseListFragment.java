package com.mpay.ui.base;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.mpay.plus.database.Item;

/**
 * @author quyenlm.vn@gmail.com
 * */
public class BaseListFragment extends SherlockListFragment implements
		LoaderManager.LoaderCallbacks<List<Item>> {
	public static final String TAG = "BaseListFragment";
	private Bundle mBundle = null;

	// This is the Adapter being used to display the list's data.
	private BaseListAdapter mAdapter;

	// // If non-null, this is the current filter the user has provided.
	// private String mCurFilter;
	// private OnQueryTextListenerCompat mOnQueryTextListenerCompat;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setControls();
	}

	protected void setControls() {
		// Give some text to display if there is no data. In a real
		// application this would come from a resource.
		setEmptyText("No applications");

		// We have a menu item to show in action bar.
		setHasOptionsMenu(true);

		// Create an empty adapter we will use to display the loaded data.
		mAdapter = new BaseListAdapter(getActivity());
		setListAdapter(getAdapder());

		// Start out with a progress indicator.
		setListShown(false);

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, getBundle(), this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Place an action bar item for searching.
		// MenuItem item = menu.add("Search");
		// item.setIcon(android.R.drawable.ic_menu_search);
		// item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		// View searchView = SearchViewCompat.newSearchView(getActivity());
		// if (searchView != null) {
		// SearchViewCompat.setOnQueryTextListener(searchView,
		// new OnQueryTextListenerCompat() {
		// @Override
		// public boolean onQueryTextChange(String newText) {
		// // Called when the action bar search text has changed. Since this
		// // is a simple array adapter, we can just have it do the
		// filtering.
		// mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
		// mAdapter.getFilter().filter(mCurFilter);
		// return true;
		// }
		// });
		// item.setActionView(searchView);
		// }
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Insert desired behavior here.
	}

	@Override
	public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created. This
		// sample only has one Loader with no arguments, so it is simple.
		return new BaseListLoader(getActivity(), args);
	}

	@Override
	public void onLoadFinished(Loader<List<Item>> loader, List<Item> data) {
		// Set the new data in the adapter.
		getAdapder().setData(data);

		// The list should now be shown.
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<Item>> loader) {
		// Clear the data in the adapter.
		getAdapder().setData(null);
	}

	protected BaseListAdapter getAdapder() {
		return mAdapter;
	}

	protected void setAdapder(BaseListAdapter adapter) {
		this.mAdapter = adapter;
	}

	protected void setBundle(Bundle info) {
		this.mBundle = info;
	}

	protected Bundle getBundle() {
		return mBundle;
	}

	public static BaseListFragment newInstance(Bundle info) {
		BaseListFragment instance = new BaseListFragment();
		instance.setBundle(info);
		return instance;
	}
}