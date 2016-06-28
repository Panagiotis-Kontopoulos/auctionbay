package com.ted.auctionbay.dao;

import java.util.List;

import com.ted.auctionbay.entities.users.Pendinguser;
import com.ted.auctionbay.entities.users.Registereduser;
import com.ted.auctionbay.entities.users.User;

public interface QueryUser {

	public boolean userExists(String username);
	
	public boolean fetchPendingByUsername(String username);
	
	public int count_registered();
	
	public int count_pending();
	
	public boolean user_validator(String username, String password);
	
	public List<Pendinguser>  getPendingUsers();
	
	public int getAddressMaxID();
	
	@SuppressWarnings("unchecked")
	public List<Registereduser> getGroupsOfUsers(int startpage, int pagesize);
	
	public int registeredNumber();
	
	public void accept_user(String username);
	
	public int registerUser(User ruser);
	
}
