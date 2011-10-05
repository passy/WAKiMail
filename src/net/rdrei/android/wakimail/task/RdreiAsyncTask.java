package net.rdrei.android.wakimail.task;

import roboguice.inject.InjectorProvider;
import roboguice.util.RoboAsyncTask;

/**
 * Custom async task class to work around a few oddities in RoboGuice.
 * @author pascal
 *
 * @param <ResultT>
 */
public abstract class RdreiAsyncTask<ResultT> extends RoboAsyncTask<ResultT> {
	
	public RdreiAsyncTask() {
		// Work around RoboGuice bug #93
		// https://code.google.com/p/roboguice/issues/detail?id=93	
		((InjectorProvider)contextProvider.get()).getInjector()
			.injectMembers(this);
	}
	
	protected String formatResourceString(int resId, java.lang.Object... formatArgs) {
		return context.getString(resId, formatArgs);
	}
}
