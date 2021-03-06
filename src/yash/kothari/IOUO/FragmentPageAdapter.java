package yash.kothari.IOUO;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentPageAdapter extends FragmentPagerAdapter {

	public FragmentPageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			return IO.getInstance();
		case 1:
			return UO.getInstance();
		default:
			break;
		}		

		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}
}
