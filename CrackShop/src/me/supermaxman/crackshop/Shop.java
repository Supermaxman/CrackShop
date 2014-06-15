package me.supermaxman.crackshop;

import java.io.Serializable;

public class Shop implements Serializable{
	
	
	private static final long serialVersionUID = -6463004346864219743L;
	private String loc;
	private int id;
	private int amount;
	private int price;
	public Shop(String loc, int id, int amt, int price) {
		this.setLoc(loc);
		this.setId(id);
		this.setAmount(amt);
		this.setPrice(price);
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}

}
