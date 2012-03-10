package net.rdrei.android.wakimail.test.shadow;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment.SavedState;

public class FragmentManagerStub extends FragmentManager {

	@Override
	public void addOnBackStackChangedListener(OnBackStackChangedListener arg0) {
	}

	@Override
	public FragmentTransaction beginTransaction() {
		return new FragmentTransaction() {
			
			@Override
			public FragmentTransaction show(Fragment arg0) {
				return null;
			}
			
			@Override
			public FragmentTransaction setTransitionStyle(int arg0) {
				return null;
			}
			
			@Override
			public FragmentTransaction setTransition(int arg0) {
				return null;
			}
			
			@Override
			public FragmentTransaction setCustomAnimations(int arg0, int arg1,
					int arg2, int arg3) {
				return null;
			}
			
			@Override
			public FragmentTransaction setCustomAnimations(int arg0, int arg1) {
				return null;
			}
			
			@Override
			public FragmentTransaction setBreadCrumbTitle(CharSequence arg0) {
				return null;
			}
			
			@Override
			public FragmentTransaction setBreadCrumbTitle(int arg0) {
				return null;
			}
			
			@Override
			public FragmentTransaction setBreadCrumbShortTitle(CharSequence arg0) {
				return null;
			}
			
			@Override
			public FragmentTransaction setBreadCrumbShortTitle(int arg0) {
				return null;
			}
			
			@Override
			public FragmentTransaction replace(int arg0, Fragment arg1, String arg2) {
				return null;
			}
			
			@Override
			public FragmentTransaction replace(int arg0, Fragment arg1) {
				return null;
			}
			
			@Override
			public FragmentTransaction remove(Fragment arg0) {
				return null;
			}
			
			@Override
			public boolean isEmpty() {
				return false;
			}
			
			@Override
			public boolean isAddToBackStackAllowed() {
				return false;
			}
			
			@Override
			public FragmentTransaction hide(Fragment arg0) {
				return null;
			}
			
			@Override
			public FragmentTransaction disallowAddToBackStack() {
				return null;
			}
			
			@Override
			public FragmentTransaction detach(Fragment arg0) {
				return null;
			}
			
			@Override
			public int commitAllowingStateLoss() {
				return 0;
			}
			
			@Override
			public int commit() {
				return 0;
			}
			
			@Override
			public FragmentTransaction attach(Fragment arg0) {
				return null;
			}
			
			@Override
			public FragmentTransaction addToBackStack(String arg0) {
				return null;
			}
			
			@Override
			public FragmentTransaction add(int arg0, Fragment arg1, String arg2) {
				return null;
			}
			
			@Override
			public FragmentTransaction add(int arg0, Fragment arg1) {
				return null;
			}
			
			@Override
			public FragmentTransaction add(Fragment arg0, String arg1) {
				return null;
			}
		};
	}

	@Override
	public void dump(String arg0, FileDescriptor arg1, PrintWriter arg2,
			String[] arg3) {
	}

	@Override
	public boolean executePendingTransactions() {
		return false;
	}

	@Override
	public Fragment findFragmentById(int arg0) {
		return null;
	}

	@Override
	public Fragment findFragmentByTag(String arg0) {
		return null;
	}

	@Override
	public BackStackEntry getBackStackEntryAt(int arg0) {
		return null;
	}

	@Override
	public int getBackStackEntryCount() {
		return 0;
	}

	@Override
	public Fragment getFragment(Bundle arg0, String arg1) {
		return null;
	}

	@Override
	public void popBackStack() {

	}

	@Override
	public void popBackStack(String arg0, int arg1) {

	}

	@Override
	public void popBackStack(int arg0, int arg1) {

	}

	@Override
	public boolean popBackStackImmediate() {
		return false;
	}

	@Override
	public boolean popBackStackImmediate(String arg0, int arg1) {
		return false;
	}

	@Override
	public boolean popBackStackImmediate(int arg0, int arg1) {
		return false;
	}

	@Override
	public void putFragment(Bundle arg0, String arg1, Fragment arg2) {

	}

	@Override
	public void removeOnBackStackChangedListener(OnBackStackChangedListener arg0) {

	}

	@Override
	public SavedState saveFragmentInstanceState(Fragment arg0) {
		return null;
	}
}
