package yash.kothari.MoneyOwed;

import android.os.Parcel;
import android.os.Parcelable;

public class OwedItem implements Parcelable {
	
	private String personName;
	private String amount;
	private String details;

	public OwedItem (String personName, String amount, String details)
	{
		this.personName = personName;
		this.amount = amount;
		this.details = details;
	}

	public String getPersonName() {return personName;}
	public void setPersonName(String value) {personName = value;}
	
	public String getAmount(){return amount;}
	public void setAmount(String value) {amount = value;}

	public String getDetails() {return details;}
	public void setDetails(String value) {details = value;}

	public void DeleteItem()
	{
		setPersonName(null);
		setAmount(null);
		setDetails(null);

		//find better way to destruct instance
	}

	public void EditItem(String editedItemName, String editedPersonName, String editedAmount, String editedDetails)
	{
		setPersonName(editedPersonName);
		setAmount(editedAmount);
		setDetails(editedDetails);
	}

	public static final Parcelable.Creator<OwedItem> CREATOR = new Creator<OwedItem>() {  
		 public OwedItem createFromParcel(Parcel source) {  
		     OwedItem mOwedItem = new OwedItem(null, null, null);  
		     mOwedItem.setAmount(source.readString());  
		     mOwedItem.setPersonName(source.readString());  
		     mOwedItem.setDetails(source.readString());  
		     return mOwedItem;  
		 }  
		 public OwedItem[] newArray(int size) {  
		     return new OwedItem[size];  
		 }  
		    };
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(getAmount());
		parcel.writeString(getPersonName());
		parcel.writeString(getDetails());
		
		
	}
}
