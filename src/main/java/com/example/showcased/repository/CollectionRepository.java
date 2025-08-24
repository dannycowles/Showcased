package com.example.showcased.repository;

import com.example.showcased.entity.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    @Query(value ="""
        SELECT c.user_id, c.collection_id, c.collection_name, c.is_private,
               SUBSTRING_INDEX(GROUP_CONCAT(si.poster_path ORDER BY sc.rank_num), ',', 5) as posters,
               COUNT(sc.show_id) as show_count,
               c.num_likes
        FROM user_collections c
        LEFT JOIN shows_in_collections sc ON c.collection_id = sc.collection_id
        LEFT JOIN show_info si ON sc.show_id = si.show_id
        WHERE c.user_id = :userId
        GROUP BY c.user_id, c.collection_id, c.collection_name, c.is_private
    """, nativeQuery = true)
    Page<Object[]> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value ="""
        SELECT c.user_id, c.collection_id, c.collection_name, c.is_private,
               SUBSTRING_INDEX(GROUP_CONCAT(si.poster_path ORDER BY sc.rank_num), ',', 5) as posters,
               COUNT(sc.show_id) as show_count,
               c.num_likes
        FROM user_collections c
        LEFT JOIN shows_in_collections sc ON c.collection_id = sc.collection_id
        LEFT JOIN show_info si ON sc.show_id = si.show_id
        WHERE c.user_id = :userId AND UPPER(c.collection_name) LIKE UPPER(CONCAT('%', :name, '%'))
        GROUP BY c.collection_id, c.collection_id, c.collection_name, c.is_private
    """, nativeQuery = true)
    Page<Object[]> findByUserIdAndCollectionNameContainingIgnoreCase(@Param("userId") Long userId, @Param("name") String name, Pageable pageable);


    @Query(value ="""
        SELECT c.user_id, c.collection_id, c.collection_name, c.is_private,
               SUBSTRING_INDEX(GROUP_CONCAT(si.poster_path ORDER BY sc.rank_num), ',', 5) as posters,
               COUNT(sc.show_id) as show_count,
               c.num_likes
        FROM user_collections c
        LEFT JOIN shows_in_collections sc ON c.collection_id = sc.collection_id
        LEFT JOIN show_info si ON sc.show_id = si.show_id
        WHERE c.user_id = :userId AND c.is_private = false
        GROUP BY c.user_id, c.collection_id, c.collection_name, c.is_private
    """, nativeQuery = true)
    Page<Object[]> findByUserIdAndPrivateCollectionFalse(@Param("userId") Long userId, Pageable pageable);

    @Query(value ="""
        SELECT c.user_id, c.collection_id, c.collection_name, c.is_private,
               SUBSTRING_INDEX(GROUP_CONCAT(si.poster_path ORDER BY sc.rank_num), ',', 5) as posters,
               COUNT(sc.show_id) as show_count,
               c.num_likes
        FROM user_collections c
        LEFT JOIN shows_in_collections sc ON c.collection_id = sc.collection_id
        LEFT JOIN show_info si ON sc.show_id = si.show_id
        WHERE c.user_id = :userId AND UPPER(c.collection_name) LIKE UPPER(CONCAT('%', :name, '%')) AND c.is_private = false
        GROUP BY c.collection_id, c.collection_id, c.collection_name, c.is_private
    """, nativeQuery = true)
    Page<Object[]> findByUserIdAndPrivateCollectionFalseAndCollectionNameContainingIgnoreCase(@Param("userId") Long userId, @Param("name") String name, Pageable pageable);

    boolean existsByUserIdAndCollectionName(Long userId, String collectionName);
    boolean existsByUserIdAndCollectionId(Long userId, Long collectionId);

    @Modifying
    @Query("UPDATE Collection c SET c.numLikes = c.numLikes + 1 WHERE c.collectionId = :commentId")
    void incrementLikes(@Param("commentId") Long commentId);

    @Modifying
    @Query("UPDATE Collection c SET c.numLikes = c.numLikes - 1 WHERE c.collectionId = :commentId")
    void decrementLikes(@Param("commentId") Long commentId);
}
