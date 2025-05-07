package com.datastore.trade.query.repository;


import com.datastore.trade.query.model.TradeHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeHistoryRepository extends MongoRepository<TradeHistory, String> {}