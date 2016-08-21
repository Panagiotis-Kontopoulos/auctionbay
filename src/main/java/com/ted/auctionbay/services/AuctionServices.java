package com.ted.auctionbay.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.items.Category;

public interface AuctionServices {

	public List<Auction> getAuctions(int startpage, int endpage, String type);
	
	public List<Object[]> getCategories(String type);
	
	public int numOfAuctions(String type);
	
	public Auction getDetails(int ItemID);
	
	public int getNumOfBids(int auction_id);
	
	public int getAuctionIDByItem(int item_id);
	
	public float getHighestBid(int auction_id);
	
	public List<Auction> getAuctionsByCategory(int start, int end, String Category, String type);
	
	public int createAuction(String username, JSONObject auction_params);
	
	public int deleteAuction(String username, int auctionID, int itemID);
	
	public int editAuction(String username, JSONObject params);
	
	public int submitBid(String username, int itemID, float bid_amount);
	
	public int buyItem(String username, int itemID);
	
	public JSONArray getBidHistory(int auctionID);
	
	public List<Auction> advancedSearch(String keywords, List<String> Categories, String Location, String minBid, String maxBid);
	
	public int delAuction(String Username, int auctionID, int ItemID);
	
	public List<Auction> getExpiredAuctions();
	
	public List<Object[]> BidderExpiredAuctions(String username);
}
