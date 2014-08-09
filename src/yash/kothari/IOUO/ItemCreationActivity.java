package yash.kothari.IOUO;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ItemCreationActivity extends Activity {
		
	private EditText edtAmount;
	private EditText edtName;
	private EditText edtDetails;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		edtAmount = (EditText) findViewById(R.id.edtCost);
		edtName = (EditText) findViewById(R.id.edtName);
		edtDetails = (EditText) findViewById(R.id.edtDetails);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_item);
	
		registerDoneBtnClickListener();
	
		if(getIntent().hasExtra("toEditListItem")) {
			OwedItem editItem = getIntent().getParcelableExtra("toEditListItem");
			edtAmount.setText(editItem.getAmount());
			edtName.setText(editItem.getPersonName());
			edtDetails.setText(editItem.getDetails());
		}
	}

	private void registerDoneBtnClickListener() {
		Button btnDone = (Button) findViewById(R.id.btnDone);
		
		edtAmount = (EditText) findViewById(R.id.edtCost);     // idk why I need this here again... it crashes without it
		edtName = (EditText) findViewById(R.id.edtName);       //
		edtDetails = (EditText) findViewById(R.id.edtDetails); //
		
		btnDone.setOnClickListener(new View.OnClickListener() {
		
			String amount;
			String name;
			String details;
			
			@Override
			public void onClick(View v) {
				Intent intent = null;
				
				amount = edtAmount.getText().toString();
				name = edtName.getText().toString();
				details = edtDetails.getText().toString();
								
				if(amount.isEmpty() || name.isEmpty())
				{
					Toast.makeText(ItemCreationActivity.this, "Amount and name fields must be filled.", Toast.LENGTH_LONG).show();
				}
				else
				{	
					if(getIntent().hasExtra("toEditListItem")) {
						OwedItem editedItem = new OwedItem(name, amount, details);
						
						intent = new Intent(ItemCreationActivity.this, MainActivity.class);
						intent.putExtra("editedListItem", (Parcelable) editedItem);
					}
					else if(getIntent().hasExtra("toCreateListItem")) {
						OwedItem newItem = new OwedItem(name, amount, details);
						List<OwedItem> outItems = getIntent().getParcelableArrayListExtra("toCreateListItem");
						outItems.add(newItem);
						
						intent = new Intent(ItemCreationActivity.this, MainActivity.class);
						intent.putParcelableArrayListExtra("createdListItem", (ArrayList<OwedItem>) outItems);
					}
					
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
	}

}