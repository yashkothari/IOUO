package yash.kothari.MoneyOwed;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	public List<OwedItem> outItems = new ArrayList<OwedItem>();
	public List<OwedItem> inItems = new ArrayList<OwedItem>();
	public ListView list;
	public OwedItem newItem;
	public String tabState;
	//Intent intentToCreate;
	//Intent intentToMain;
	
	private final int CREATE_REQUEST = 0;
	private final int EDIT_REQUEST = 1;
	private int listItemIndex;
	
	private MenuItem itmCreate;
    private MenuItem itmEdit;
    private MenuItem itmDelete;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		populateListView();
		registerListItemLongClickListener();
    }

	@Override
    public void onBackPressed(){} //back button is disabled to avoid moving to ItemCreationActivity
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	MenuInflater mif = getMenuInflater();
    	mif.inflate(R.menu.menu_list_item, menu);
    	itmCreate = menu.findItem(R.id.btnCreate);
    	itmEdit = menu.findItem(R.id.btnEdit);
    	itmDelete = menu.findItem(R.id.btnDelete);
    	editDeleteEnabled(false);
    	return super.onCreateOptionsMenu(menu);
    }
    
    private void editDeleteEnabled(boolean enabled) {

		if(enabled) {
			itmCreate.setEnabled(false);
			itmCreate.getIcon().setAlpha(130);
			
        	itmEdit.setEnabled(true);
        	itmEdit.getIcon().setAlpha(255);
        	
        	itmDelete.setEnabled(true);
        	itmDelete.getIcon().setAlpha(255);
		}
		else{
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
        // Handle presses on the action bar items
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
		intent.putParcelableArrayListExtra("toCreateListItem", (ArrayList<OwedItem>) outItems);
		startActivityForResult(intent, CREATE_REQUEST);		
	}
    
	private void editItem() {
		Intent intent = new Intent(MainActivity.this, ItemCreationActivity.class);
		intent.putExtra("toEditListItem", outItems.get(listItemIndex));
		startActivityForResult(intent, EDIT_REQUEST);	
	}
	
    private void deleteItem() {
		outItems.remove(listItemIndex);
		populateListView();
		editDeleteEnabled(false);
	}

    
    //
	// this section gets the new OwedItems from the ItemCreationActivity, adds them to the list, and populates the listview
    //
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == CREATE_REQUEST) {
	    	if(resultCode == RESULT_OK) {
	    		addNewListItem(data);
	    	}
	    }
		if (requestCode == EDIT_REQUEST) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	        	addUpdatedEditedListItem(data);
	        }
	        else {
	        	editDeleteEnabled(false);
	        }
	    }
		populateListView();
	}

	private void addNewListItem(Intent data) {
		outItems = data.getParcelableArrayListExtra("createdListItem"); //gets list of items "attached" to intent that comes from ItemCreationActivity
	}
	
	private void addUpdatedEditedListItem(Intent data) {
		OwedItem editedListItem = data.getParcelableExtra("editedListItem");
		outItems.set(listItemIndex, editedListItem);
		editDeleteEnabled(false);
	}

	
	//List stuff
	private void populateListView() { //adapts list of OwedItems to ListView
		list = (ListView) findViewById(R.id.listView);
		ArrayAdapter<OwedItem> adapterOweOut = new ItemListAdapter();

		list.setAdapter(adapterOweOut);
	}

	private class ItemListAdapter extends ArrayAdapter<OwedItem> {
		public ItemListAdapter() {
			super(MainActivity.this, R.layout.list_item, outItems);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			
			if(itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
			}
			
			OwedItem currentItem = outItems.get(position);
			
			TextView txtAmount = (TextView) itemView.findViewById(R.id.item_txtCost);
			txtAmount.setText(currentItem.getAmount());
			
			TextView txtName = (TextView) itemView.findViewById(R.id.item_txtName);
			txtName.setText(currentItem.getPersonName());
			
			TextView txtDetails = (TextView) itemView.findViewById(R.id.item_txtDetails);
			txtDetails.setText(currentItem.getDetails());
			
			return itemView;
		}
	}
	
    private void registerListItemLongClickListener() {
    	list.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int pos, long id) {
            	
            	editDeleteEnabled(true);
            	listItemIndex = pos;
                return true;
            }
        });
	}
}