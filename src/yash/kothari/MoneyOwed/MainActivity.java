package yash.kothari.MoneyOwed;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
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
	private int editPosition;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		populateListView();
		registerNewItemBtnClickListener();
		registerForContextMenu(list);
    }

    @Override
    public void onBackPressed(){} //back button is disabled to avoid moving to ItemCreationActivity
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
    	super.onCreateContextMenu(menu, v, menuInfo);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu_list_item, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {    	
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	editPosition = info.position;

    	switch (item.getItemId()) { //////////////////change intent so that it gets a previous intent and reuses it
        	case R.id.itemEdit:
        		try{
				Intent intent = new Intent(MainActivity.this, ItemCreationActivity.class);
				intent.putExtra("toEditListItem", outItems.get(editPosition));
				startActivityForResult(intent, EDIT_REQUEST);
        		return true;
        		}
        		catch(Exception e)
        		{
        			Log.i("ex", e.toString());
        		}
        	case R.id.itemDelete:
                outItems.remove(editPosition);
                populateListView();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == CREATE_REQUEST) {
	    	if(resultCode == RESULT_OK) {
	    		createNewListItem(data);
	    	}
	    }
		if (requestCode == EDIT_REQUEST) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	        	updateEditedListItem(data);
	        }
	    }
		populateListView();
	}

	private void createNewListItem(Intent data) {
		outItems = data.getParcelableArrayListExtra("createdListItem"); //gets list of items "attached" to intent that comes from ItemCreationActivity
	}
	
	private void updateEditedListItem(Intent data) {
		OwedItem editedListItem = data.getParcelableExtra("editedListItem");
		outItems.set(editPosition, editedListItem);
	}

	private void populateListView() { //adapts list of OwedItems to ListView
		list = (ListView) findViewById(R.id.listView);
		ArrayAdapter<OwedItem> adapterOweOut = new ItemListAdapter();

		list.setAdapter(adapterOweOut);
	}
	
    private void registerNewItemBtnClickListener() {
    	Button newItemBtn = (Button) findViewById(R.id.btnNewItem);
    	
    	newItemBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ItemCreationActivity.class);
				intent.putParcelableArrayListExtra("toCreateListItem", (ArrayList<OwedItem>) outItems);
				startActivityForResult(intent, CREATE_REQUEST);
			}
		});		
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
}