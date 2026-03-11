package com.ska.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ska.model.sync_queue.SyncQueueItem;


@Repository
public interface SyncQueueItemRepository extends JpaRepository<SyncQueueItem, Long> {

}
