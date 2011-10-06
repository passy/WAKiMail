package net.rdrei.android.wakimail.widget;

import net.rdrei.android.wakimail.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Doesn't serve any real purpose right now, but allows us to easily
 * customize this later on. Twitter does something like a clickable menu bar
 * that inherits the button's onclick target and so on.
 * 
 * @author pascal
 */
public class Navbar extends RelativeLayout {

	public Navbar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public Navbar(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.navbarStyle);
	}

	public Navbar(Context context) {
		this(context, null);
	}
}
