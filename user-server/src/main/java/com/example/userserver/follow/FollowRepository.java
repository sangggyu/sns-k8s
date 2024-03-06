package com.example.userserver.follow;

import com.example.userserver.user.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {
    Follow findByUserIdAndFollowId(int userId, int followId);

    @Query(value = "SELECT new com.example.userserver.user.UserInfo(u.userId, u.username, u.email) " +
            "FROM Follow f, User u WHERE f.userId = : userId and u.userId = f.follwerId")
    List<UserInfo> findFollowersByUserId(@Param("userId") int userId);

    @Query(value = "SELECT new com.example.userserver.user.UserInfo(u.userId, u.username, u.email) " +
            "FROM Follow f, User u WHERE f.follwerId = : userId and u.userId = f.userId")
    List<UserInfo> findFollowingByUserId(@Param("userId") int userId);
}
