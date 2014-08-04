package yash.kothari.MoneyOwed;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	private ViewPager vp;
	private FragmentPageAdapter fpa;
	private ActionBar bar;
	
	private MenuItem itmCreate;
	private MenuItem itmEdit;
	private MenuItem itmDelete;

	private List<OwedItem> ioItems = new ArrayList<OwedItem>();
	private List<OwedItem> uoItems = new ArrayList<OwedItem>();

	private String tabState; 
	public int listItemIndex;
	
	//Constants
	private static final int IO_FRAGMENT_ID = 0;
	private static final int UO_FRAGMENT_ID = 1;
	private static final int CREATE_REQUEST = 0;
	private static final int EDIT_REQUEST = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setupViewPager();
		setupActionBarTabs();
	}

	private void setupViewPager() {
		vp = (ViewPager) findViewById(R.id.pager);
		fpa = new FragmentPageAdapter(getSupportFragmentManager());
		vp.setAdapter(fpa);	
	}

	private void setupActionBarTabs() {
		bar = getActionBar();
		bar.setDisplayShowTitleEnabled(false);
		bar.setDisplayShowHomeEnabled(false);
		
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.addTab(bar.newTab().setText("IO").setTabListener(this));
		bar.addTab(bar.newTab().setText("UO").setTabListener(this));

		vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) { //actionbar tabs are in sync with page you're on
				bar.setSelectedNavigationItem(arg0);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			@Override
			public void onPageScrollStateChanged(int arg0){}
		});
	}

	@Override
	public void onBackPressed(){} //back button is disabled to avoid moving to ItemCreationActivity

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mif = getMenuInflater();
		mif.inflate(R.menu.menu_list_item, menu); //inflate menu of three buttons
		
		itmCreate = menu.findItem(R.id.btnCreate);
		itmEdit = menu.findItem(R.id.btnEdit);
		itmDelete = menu.findItem(R.id.btnDelete);
		
		editDeleteEnabled(false);
		return super.onCreateOptionsMenu(menu);
	}

	
	public void editDeleteEnabled(boolean enabled) {
		//enable/disable and ungrey/grey menu items
		if(enabled) {
			itmCreate.setEnabled(false);
			itmCreate.getIcon().setAlpha(130);

			itmEdit.setEnabled(true);
			itmEdit.getIcon().setAlpha(255);

			itmDelete.setEnabled(true);
			itmDelete.getIcon().setAlpha(255);
		}
		else {
			itmCreate.setEnabled(true);
			itmCreate.getIcon().setAlpha(225);

			itmEdit.setEnabled(false);
			itmEdit.getIcon().setAlpha(130);

			itmDelete.setEnabled(false);
			itmDelete.getIcon().setAlpha(130);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle clicks on the action bar items
		switch (item.getItemId()) {
		case R.id.btnCreate:
			createNewItem();
			return true;
		case R.id.btnEdit:
			editItem();
			return true;
		case R.id.btnDelete:
			deleteItem();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void createNewItem() {
		Intent intent = new Intent(MainActivity.this, ItemCreationActivity.class);

		if(tabState == "IO") {
			intent.putParcelableArrayListExtra("toCreateListItem", (ArrayList<OwedItem>) ioItems);
		}
		else {
			intent.putParcelableArrayListExtra("toCreateListItem", (ArrayList<OwedItem>) uoItems);
		}
		
		startActivityForResult(intent, CREATE_REQUEST);
	}

	private void editItem() {
		Intent intent = new Intent(MainActivity.this, ItemCreationActivity.class);
		
		if(tabState == "IO") {
			intent.putExtra("toEditListItem", ioItems.get(listItemIndex));
		}
		else {
			intent.putExtra("toEditListItem", uoItems.get(listItemIndex));
		}
		
		startActivityForResult(intent, EDIT_REQUEST);	
	}

	private void deleteItem() {
		
		if(tabState == "IO") {
			ioItems.remove(listItemIndex);
			((IO) fpa.getItem(IO_FRAGMENT_ID)).populateListView();
		}
		else {
			uoItems.remove(listItemIndex);
			((UO) fpa.getItem(UO_FRAGMENT_ID)).populateListView();
		}
		
		editDeleteEnabled(false);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {		
		// Check which request we're responding to
		if (requestCode == CREATE_REQUEST) {
			// Make sure the request was successful
			if(resultCode == RESULT_OK) {
				addNewListItem(data);
			}
		}
		
		if (requestCode == EDIT_REQUEST) {
			if (resultCode == RESULT_OK) {
				addUpdatedEditedListItem(data);
			}
			else {
				editDeleteEnabled(false);
			}
		}
	}
	
	private void addNewListItem(Intent data) {
		if(tabState == "IO") {
			ioItems = data.getParcelableArrayListExtra("createdListItem"); //gets list of items "attached" to intent that comes from ItemCreationActivity
			((IO) fpa.getItem(IO_FRAGMENT_ID)).populateListView();
		}
		else {
			uoItems = data.getParcelableArrayListExtra("createdListItem"); //gets list of items "attached" to intent that comes from ItemCreationActivity
			((UO) fpa.getItem(UO_FRAGMENT_ID)).populateListView();
		}
	}

	private void addUpdatedEditedListItem(Intent data) {
		OwedItem editedListItem = data.getParcelableExtra("editedListItem");
		
		if(tabState == "IO") {
			ioItems.set(listItemIndex, editedListItem);
			((IO) fpa.getItem(IO_FRAGMENT_ID)).populateListView();
		}
		else {
			uoItems.set(listItemIndex, editedListItem);
			((UO) fpa.getItem(UO_FRAGMENT_ID)).populateListView();
		}
		
		editDeleteEnabled(false);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		vp.setCurrentItem(tab.getPosition());

		if(tab.getPosition() == IO_FRAGMENT_ID) {
			tabState = "IO";
		}
		else {
			tabState = "UO";
		}
	}

	public List<OwedItem> getListItems() {
		if(tabState == "IO") {
			return ioItems;
		}
		else {
			return uoItems;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {}
}