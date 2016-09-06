package com.ted.auctionbay.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import com.ted.auctionbay.entities.users.messages.Conversation;
import com.ted.auctionbay.jpautils.EntityManagerHelper;

public class QueryConversationImpl implements QueryConversation{

	@SuppressWarnings("unchecked")
	@Override
	public List<Conversation> getInboxMessages(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT c.conversationID, c.Recipient, c.Sender,"
				+ " c.Subject, c.DateCreated, c.isRead, c.MessageText "
				+ "FROM conversation c"
				+ " WHERE c.Recipient = ?1", Conversation.class);
		
		
		
		query.setParameter(1,username);
		
		List<Conversation> resultSet = query.getResultList();
		
		//List resultSet  =  em.createNamedQuery("Message.inbox").setParameter("username",username).getResultList();
		return resultSet;
	}

	
	@Override
	public List<Conversation> getSentMessages(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT c.conversationID, c.Recipient, c.Sender,"
				+ " c.Subject, c.DateCreated, c.isRead, c.MessageText "
				+ "FROM conversation c"
				+ " WHERE c.Sender = ?1", Conversation.class);
		
		query.setParameter(1,username);
		List<Conversation> resultSet = query.getResultList();
		//List resultSet  =  em.createNamedQuery("Message.sent").setParameter("username",username).getResultList();
		return resultSet;
	}

	@Override
	public int countNewMessages(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT COUNT(c.conversationID)"
				+ " FROM conversation c"
				+ " WHERE c.Recipient = ?1 AND c.isRead = 0");
		
		query.setParameter(1, username);
		int count = -1;
		if(query.getResultList().get(0) == null){
			count = 0;
		} else {
			count = Integer.parseInt(query.getResultList().get(0).toString());
		}
		
		return count;
	}

	@Override
	public int markAsRead(int messageID) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		//Query query = em.createNativeQuery("SELECT * FROM message WHERE MessageID=?1",Message.class);
		System.out.println("markAsRead");
		Query query = em.createNativeQuery("UPDATE conversation SET isRead=1 WHERE conversationID=?1");
		query.setParameter(1, messageID);
		query.executeUpdate();
		//Message m = (Message) query.getResultList().get(0);
		//m.setIsRead(Byte.parseByte("1".toString()));
		
		/*try {
			em.persist(m);
		}catch (PersistenceException pe) {
			pe.printStackTrace();
			return 1;
		}*/
		
		return 0;
	}

	@Override
	public int getMaxConversationID() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT MAX(conversationID) FROM conversation");
		int maxID = -1;
		if(query.getResultList().get(0) == null){
			maxID=0;
		} else {
			maxID = Integer.parseInt(query.getResultList().get(0).toString()) + 1;
		}
		return maxID;
	}

	@Override
	public int getMaxMessageID() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		List list = em.createNamedQuery("Message.maxID").getResultList();
		int maxID = -1;
		if(list.get(0) == null){
			maxID=0;
		} else {
			maxID = Integer.parseInt(list.get(0).toString()) + 1;
		}
		return maxID;
	}

	@Override
	public int submitMessage(Conversation conversation) {
		try {
			EntityManager em = EntityManagerHelper.getEntityManager();
			em.persist(conversation);
			
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			return 1;
		}
		return 0;
		
	}


	/*@Override
	public int deleteMessage(String username, int messageID) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("DELETE FROM conversation WHERE MessageID=?1 AND RegisteredUser=?2");
		query.setParameter(1, messageID);
		query.setParameter(2, username);
		query.executeUpdate();
		
		Query query2 = em.createNativeQuery("SELECT * FROM conversation WHERE MessageID=?1").setParameter(1, messageID);
		List messages = query2.getResultList();
		if(messages.size() == 0){
			System.out.println("Going to delete message with id: " + messageID);
			Query query3 = em.createNativeQuery("DELETE FROM conversation WHERE MessageID=?1").setParameter(1, messageID);
			if(query3.executeUpdate() != 0){
				System.out.println("Message with id: " + messageID + " deleted !!!");
				System.out.println("");
				return 0;
			}
		}
		System.out.println("Returning 1");
		return 1;
	}*/

	
	@Override
	public int deleteMessage(String username, int conversationID) {
		
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("DELETE FROM conversation WHERE conversationID=?1 AND Recipient=?2");
		query.setParameter(1, conversationID);
		query.setParameter(2, username);
		if(query.executeUpdate() != 0){
			
			System.out.println("Message with id: " + conversationID + " deleted !!!");
			return 0;
		}
		
		
		return 1;
	}

	@Override
	public List<Object[]> inbox(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT c.conversationID, c.Recipient, c.Sender,"
				+ " c.Subject, c.DateCreated, c.isRead, c.MessageText"
				+ " FROM conversation c"
				+ " WHERE c.Recipient = ?1");
		
		query.setParameter(1, username);
		//List<Object[]> res = query.getResultList();
		return query.getResultList();
	}


	@Override
	public List<Object[]> sent(String username) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Query query = em.createNativeQuery("SELECT c.conversationID, c.Recipient, c.Sender,"
				+ " c.Subject, c.DateCreated, c.isRead, c.MessageText"
				+ " FROM conversation c"
				+ " WHERE c.Sender = ?1");
		
		
		query.setParameter(1, username);
		//List<Object[]> res = query.getResultList();
		//System.out.println(query.getResultList());
		return query.getResultList();
	}

}