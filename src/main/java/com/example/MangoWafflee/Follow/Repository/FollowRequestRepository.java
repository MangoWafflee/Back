package com.example.MangoWafflee.Follow.Repository;

import com.example.MangoWafflee.Follow.Entity.FollowRequest;
import com.example.MangoWafflee.Follow.Entity.FollowRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRequestRepository extends JpaRepository<FollowRequest, Long> {
    @Query("SELECT fr FROM FollowRequest fr WHERE (fr.sender.id = :user1Id AND fr.receiver.id = :user2Id OR fr.sender.id = :user2Id AND fr.receiver.id = :user1Id) AND fr.status = :status")
    Optional<FollowRequest> findPendingRequest(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id, @Param("status") FollowRequestStatus status);

    @Query("SELECT fr FROM FollowRequest fr WHERE fr.sender.id = :senderId")
    List<FollowRequest> findAllBySenderId(@Param("senderId") Long senderId);

    @Query("SELECT fr FROM FollowRequest fr WHERE fr.receiver.id=:receiverId")
    List<FollowRequest> findByReceiverId(@Param("receiverId") Long receiverId);
}
