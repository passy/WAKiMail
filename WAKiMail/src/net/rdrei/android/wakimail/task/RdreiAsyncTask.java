package net.rdrei.android.wakimail.task;

import java.util.concurrent.Executor;

import roboguice.util.RoboAsyncTask;
import android.content.Context;
import android.os.Handler;

/**
 * Custom async task class to work around a few oddities in RoboGuice.
 * @author pascal
 *
 * @param <ResultT>
 */
public abstract class RdreiAsyncTask<ResultT> extends RoboAsyncTask<ResultT> {

	protected RdreiAsyncTask(Context context, Executor executor) {
		super(context, executor);
	}

	protected RdreiAsyncTask(Context context, Handler handler, Executor executor) {
		super(context, handler, executor);
	}

	protected RdreiAsyncTask(Context context, Handler handler) {
		super(context, handler);
	}

	protected RdreiAsyncTask(Context context) {
		super(context);
	}

	protected String formatResourceString(int resId, java.lang.Object... formatArgs) {
		return this.context.getString(resId, formatArgs);
	}
}
