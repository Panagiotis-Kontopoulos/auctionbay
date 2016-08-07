package com.ted.auctionbay.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.items.Item;
import com.ted.auctionbay.jpautils.EntityManagerHelper;

public class QueryAuctionImpl implements QueryAuction{

	@Override
	public int numOfAuctions() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT count(*) FROM auction");
		int num = Integer.parseInt(query.getResultList().get(0).toString());
	
		return num;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Auction> getAuctions(int startpage, int endpage) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		/*String sql = "SELECT a.AuctionID, a.ItemID, a.Seller, a.Title, a.BuyPrice, a.FirstBid, a.StartTime, a.EndTime, c.Name"+ 
				" FROM auction a, aitem_has_category ihc, category c" +
				" WHERE a.ItemID = ihc.ItemID and ihc.CategoryID = c.CategoryID";
		*/
		Query query = em.createNativeQuery("SELECT * FROM auction",Auction.class);
		//Query query = em.createNativeQuery(sql,Auction.class);
		query.setFirstResult(startpage);
		query.setMaxResults(endpage);
		
		return query.getResultList();
		
	}

	@Override
	public int getNumOfBids(int auction_id) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT count(rba.AuctionID) FROM registereduser_bidsin_auction rba"
				+" WHERE rba.AuctionID LIKE ?1 GROUP BY rba.AuctionID");
		query.setParameter(1, auction_id).getFirstResult();
		List<?> list = query.getResultList();
		if(!list.isEmpty()){
			return Integer.parseInt(list.get(0).toString());
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Auction> getAuctionsByCategory(int startpage, int endpage, String category) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT a.AuctionID,a.ItemID,a.Seller,a.Title,a.BuyPrice,a.FirstBid,a.StartTime,a.EndTime "
				+ "FROM auction a,category c,item_has_category ihc, item i"
				+ " where a.ItemID = i.ItemID and i.ItemID = ihc.ItemID and ihc.CategoryID = c.CategoryID"
				+ " and c.Name = ?1",Auction.class) ;
		
		query.setParameter(1, category);
		query.setFirstResult(startpage);
		query.setMaxResults(endpage);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Auction getDetails(int ItemID) {
		System.out.println("getting auction details with id: " + ItemID);
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM auction WHERE ItemID=?",Auction.class);
		query.setParameter(1, ItemID);
		List<Auction> Set = query.getResultList();
		return Set.get(0);
	}

	@Override
	public int maxAuctionID() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		int maxID;
		Object idSet  =  em.createNamedQuery("Auction.auctionMaxID").getResultList().get(0);
		if(idSet == null) {
			maxID = 0;
		} else {
			maxID = Integer.parseInt(idSet.toString()) + 1;
		}
		return maxID;
	}

	@Override
	public int submitAuction(Auction auction) {
		
		try {
			EntityManager em = EntityManagerHelper.getEntityManager();
			em.persist(auction);
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			return -1;
		}
		return 0;
	}

	@Override
	public int deleteAuction(int auctionID) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("DELETE FROM auction WHERE AuctionID=?",Auction.class);
		query.setParameter(1, auctionID);
		return 0;
	}

	@Override
	public float getHighestBid(int auction_id) {
		//System.out.println("auction_id: " + auction_id);
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT MAX(BidPrice) FROM registereduser_bidsin_auction WHERE AuctionID=?");
		query.setParameter(1, auction_id);
		
		float highestBid;
		List bidList = query.getResultList();
		System.out.println(bidList.get(0));
		if(bidList.get(0) == null || bidList.isEmpty()) {
			highestBid=0;
		} else {
			highestBid = Float.parseFloat(bidList.get(0).toString());
		}
		
		
		return highestBid;
	}

	@Override
	public boolean alreadyBidded(String username, int itemID) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT COUNT(*) FROM registereduser_bidsin_auction rba, auction a "
				+ "WHERE rba.AuctionID = a.AuctionID AND rba.Bidder_Username=?1 AND a.ItemID=?2 ");
		query.setParameter(1, username);
		query.setParameter(2, itemID);
		int number = Integer.parseInt(query.getResultList().get(0).toString());
		
		if(number != 0){
			return true;
		}
		
		return false;
	}
	
	

}
