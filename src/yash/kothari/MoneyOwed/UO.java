package yash.kothari.MoneyOwed;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UO extends ListFragment {

	private static UO instance;
	private List<OwedItem> uoItems = new ArrayList<OwedItem>();

	public static UO getInstance() { //singleton
		if(instance == null) {
			instance = new UO();
		}
		return instance;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		
		registerListItemLongClickListener();
		populateListView();
	}
	
	public void populateListView() { //adapts list of OwedItems to ListView
		uoItems = ((MainActivity) getActivity()).getUOList();
		ArrayAdapter<OwedItem> listAdapter = new ItemListAdapter();
		setListAdapter(listAdapter);
	}
	
	public class ItemListAdapter extends ArrayAdapter<OwedItem> {
		
		public ItemListAdapter() {
			super(getActivity(), R.layout.list_item, uoItems);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;

			if(itemView == null) {
				itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
			}

			OwedItem listItem = uoItems.get(position);

			TextView txtAmount = (TextView) itemView.findViewById(R.id.item_txtCost);
			txtAmount.setText(listItem.getAmount());

			TextView txtName = (TextView) itemView.findViewById(R.id.item_txtName);
			txtName.setText(listItem.getPersonName());

			TextView txtDetails = (TextView) itemView.findViewById(R.id.item_txtDetails);
			txtDetails.setText(listItem.getDetails());

			return itemView;
		}
	}

	private void registerListItemLongClickListener() {
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
				((MainActivity) getActivity()).editDeleteEnabled(true);
				((MainActivity) getActivity()).listItemIndex = pos;
				return true;
			}
		});
	}
}
