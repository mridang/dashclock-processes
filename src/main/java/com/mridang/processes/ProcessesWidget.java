package com.mridang.processes;

import org.acra.ACRA;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.google.android.apps.dashclock.api.ExtensionData;

/*
 * This class is the main class that provides the widget
 */
public class ProcessesWidget extends ImprovedExtension {

	/*
	 * (non-Javadoc)
	 * @see com.mridang.battery.ImprovedExtension#getIntents()
	 */
	@Override
	protected IntentFilter getIntents() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.mridang.battery.ImprovedExtension#getTag()
	 */
	@Override
	protected String getTag() {
		return getClass().getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mridang.battery.ImprovedExtension#getUris()
	 */
	@Override
	protected String[] getUris() {
		return null;
	}

	/*
	 * @see
	 * com.google.android.apps.dashclock.api.DashClockExtension#onUpdateData
	 * (int)
	 */
	@Override
	protected void onUpdateData(int intReason) {

		Log.d(getTag(), "Getting the status of the processes");
		ExtensionData edtInformation = new ExtensionData();
		setUpdateWhenScreenOn(true);

		try {

			Log.d(getTag(), "Calculating the number of tasks and services");
			ActivityManager mgrActivity = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
			Integer intServices = mgrActivity.getRunningServices(Integer.MAX_VALUE).size();
			Integer intRecents = mgrActivity.getRecentTasks(Integer.MAX_VALUE, 2).size() - 1;
			Integer intProcess = mgrActivity.getRunningAppProcesses().size();
			String strServices = getQuantityString(R.plurals.service, intServices, intServices);
			String strRecents = getQuantityString(R.plurals.activity, intRecents, intRecents);

			Log.d(getTag(), String.format("%d running processes", intProcess));

			edtInformation.clickIntent(new Intent(Intent.ACTION_VIEW).setClassName("com.android.settings", "com.android.settings.RunningServices"));
			edtInformation.status(Integer.toString(intRecents + intServices));
			edtInformation.expandedTitle(getString(R.string.and, strRecents, strServices));
			edtInformation.expandedBody(getQuantityString(R.plurals.process, intProcess, intProcess));
			edtInformation.visible(true);

		} catch (Exception e) {
			edtInformation.visible(false);
			Log.e(getTag(), "Encountered an error", e);
			ACRA.getErrorReporter().handleSilentException(e);
		}

		edtInformation.icon(R.drawable.ic_dashclock);
		doUpdate(edtInformation);

	}

	/*
	 * (non-Javadoc)
	 * @see com.mridang.alarmer.ImprovedExtension#onReceiveIntent(android.content.Context, android.content.Intent)
	 */
	@Override
	protected void onReceiveIntent(Context ctxContext, Intent ittIntent) {
		onUpdateData(UPDATE_REASON_MANUAL);
	}

}