package com.example.MangoWafflee.Repository;

import com.example.MangoWafflee.Entity.SmileEntity;
import com.example.MangoWafflee.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SmileRepository extends JpaRepository<SmileEntity, Long> {
    public List<SmileEntity> findByUser(UserEntity user);
    public List<SmileEntity> findByUser_Nickname(String nickname);

    @Query("SELECT COUNT(s) FROM SmileEntity s WHERE s.user = :user AND s.date > :date")
    int countByUserAndDateAfter(@Param("user") UserEntity user, @Param("date") LocalDate date);

    @Query("SELECT s FROM SmileEntity s WHERE s.user.nickname = :nickname AND YEAR(s.date) = :year AND MONTH(s.date) = :month")
    List<SmileEntity> findByNicknameAndYearAndMonth(@Param("nickname") String nickname, @Param("year") int year, @Param("month") int month);

    @Query("SELECT s FROM SmileEntity s WHERE s.user.nickname = :nickname AND YEAR(s.date) = :year AND MONTH(s.date) = :month AND DAY(s.date) = :day")
    List<SmileEntity> findByNicknameAndYearAndMonthAndDay(@Param("nickname") String nickname, @Param("year") int year, @Param("month") int month, @Param("day") int day);
}
