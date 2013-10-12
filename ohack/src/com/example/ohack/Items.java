package com.example.ohack;

public class Items implements Comparable<Items>{
	private String ItemID;
	private String EndDate;
	private String price;
	private String title;
	
	public void setTitle (String _title)
	{
		title = _title;
	}
	
	public void setItemID (String id)
	{
		ItemID = id;
	}
	
	public void setEndDate (String inEndDate)
	{
		EndDate = inEndDate;
	}
	
	public void setprice (String inPrice)
	{
		price = inPrice;
	}
	
	public String getItemID()
	{
		return ItemID;
	}
	
	public String getEndDate()
	{
		return EndDate;
	}
	
	public String getprice()
	{
		return price;
	}

	public String getTitle()
	{
		return title;
	}
	
	@Override
	public int compareTo(Items another) {
		float comparePrice = Float.parseFloat(another.getprice());
		float thisPrice = Float.parseFloat(this.price);
		//ascending order
		return (int) (thisPrice - comparePrice);
 
	}
}
