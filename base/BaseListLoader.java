package com.mpay.ui.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.mpay.plus.database.Item;

/**
 * A Base Loader that loads all of items in Background
 * 
 * @author quyenlm.vn@gmail.com
 */
public class BaseListLoader extends AsyncTaskLoader<List<Item>> {
	private final InterestingConfigChanges mLastConfig = new InterestingConfigChanges();
	private List<Item> mItems;
	private PackageIntentReceiver mPackageObserver;
	private Bundle mBundle = null;
	
	public BaseListLoader(Context context, Bundle bundleInfo) {
		super(context);
		this.mBundle = bundleInfo;
	}

	/**
	 * This is where the bulk of our work is done. This function is called
	 * in a background thread and should generate a new set of data to be
	 * published by the loader.
	 */
	@Override
	public List<Item> loadInBackground() {
		// Create corresponding array of entries and load their labels.
		List<Item> entries = new ArrayList<Item>();
		// Done!
		return entries;
	}

	/**
	 * Called when there is new data to deliver to the client. The super
	 * class will take care of delivering it; the implementation here just
	 * adds a little more logic.
	 */
	@Override
	public void deliverResult(List<Item> apps) {
		if (isReset()) {
			// An async query came in while the loader is stopped. We
			// don't need the result.
			if (apps != null) {
				onReleaseResources(apps);
			}
		}
		List<Item> oldApps = apps;
		mItems = apps;

		if (isStarted()) {
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(apps);
		}

		// At this point we can release the resources associated with
		// 'oldApps' if needed; now that the new result is delivered we
		// know that it is no longer in use.
		if (oldApps != null) {
			onReleaseResources(oldApps);
		}
	}

	/**
	 * Handles a request to start the Loader.
	 */
	@Override
	protected void onStartLoading() {
		if (mItems != null) {
			// If we currently have a result available, deliver it
			// immediately.
			deliverResult(mItems);
		}

		// Start watching for changes in the app data.
		if (mPackageObserver == null) {
			mPackageObserver = new PackageIntentReceiver(this);
		}

		// Has something interesting in the configuration changed since we
		// last built the app list?
		boolean configChange = mLastConfig.applyNewConfig(getContext()
				.getResources());

		if (takeContentChanged() || mItems == null || configChange) {
			// If the data has changed since the last time it was loaded
			// or is not currently available, start a load.
			forceLoad();
		}
	}

	/**
	 * Handles a request to stop the Loader.
	 */
	@Override
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	/**
	 * Handles a request to cancel a load.
	 */
	@Override
	public void onCanceled(List<Item> apps) {
		super.onCanceled(apps);

		// At this point we can release the resources associated with 'apps'
		// if needed.
		onReleaseResources(apps);
	}

	/**
	 * Handles a request to completely reset the Loader.
	 */
	@Override
	protected void onReset() {
		super.onReset();

		// Ensure the loader is stopped
		onStopLoading();

		// At this point we can release the resources associated with 'apps'
		// if needed.
		if (mItems != null) {
			onReleaseResources(mItems);
			mItems = null;
		}

		// Stop monitoring for changes.
		if (mPackageObserver != null) {
			getContext().unregisterReceiver(mPackageObserver);
			mPackageObserver = null;
		}
	}

	/**
	 * Helper function to take care of releasing resources associated with
	 * an actively loaded data set.
	 */
	protected void onReleaseResources(List<Item> apps) {
		// For a simple List<> there is nothing to do. For something
		// like a Cursor, we would close it here.
	}
			
	protected Bundle getBundle() {
		return mBundle;
	}
}