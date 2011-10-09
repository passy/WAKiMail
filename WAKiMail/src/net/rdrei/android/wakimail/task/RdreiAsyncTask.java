package net.rdrei.android.wakimail.task;

import java.util.concurrent.Executor;

import android.os.Handler;
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
		super();
		inject();
	}
	
	public RdreiAsyncTask(Executor executor) {
		super(executor);
		inject();
	}

	public RdreiAsyncTask(Handler handler, Executor executor) {
		super(handler, executor);
		inject();
	}

	public RdreiAsyncTask(Handler handler) {
		super(handler);
		inject();
	}
	
	private void inject() {
		// Work around RoboGuice bug #93
		// https://code.google.com/p/roboguice/issues/detail?id=93	
		((InjectorProvider)contextProvider.get()).getInjector()
			.injectMembers(this);
	}

	protected String formatResourceString(int resId, java.lang.Object... formatArgs) {
		return context.getString(resId, formatArgs);
	}
}
