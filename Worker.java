package com.example.mbg;

import java.io.Serializable;

public class Worker implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id;
	String name;
	String image;
	String primaryContact;
	String secondaryContact;
	String address;
	String profession;
	String rate;
	String type;
	
	boolean isSelected;

	public Worker() {
		
	}
	
	public Worker( String name, String image, String primaryContact,
			String secondaryContact, String address, String profession,
			String rate, String type) {
		
		super();
		
		this.name = name;
		this.image = image;
		this.primaryContact = primaryContact;
		this.secondaryContact = secondaryContact;
		this.address = address;
		this.profession = profession;
		this.rate = rate;
		this.type = type;
		this.isSelected = false;
	}
	public String toString()
    {
        return this.name;
    }

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public Worker(int id, String name,String image,  String primaryContact,
			String secondaryContact, String address, String profession,
			String rate, String type) {
		
		super();
		this.id = id;
		this.name = name;
		this.image = image;
		this.primaryContact = primaryContact;
		this.secondaryContact = secondaryContact;
		this.address = address;
		this.profession = profession;
		this.rate = rate;
		this.type = type;
	}
	
	
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrimaryContact() {
		return primaryContact;
	}
	public void setPrimaryContact(String primaryContact) {
		this.primaryContact = primaryContact;
	}
	public String getSecondaryContact() {
		return secondaryContact;
	}
	public void setSecondaryContact(String secondaryContact) {
		this.secondaryContact = secondaryContact;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
	
	
	
}
