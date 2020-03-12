package com.mark.task.repository;

import com.mark.task.repository.entity.ImageMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMetadataRepository extends JpaRepository<ImageMetaData, Long> {

    boolean existsByName(String name);

    @Query(nativeQuery = true, value = "SELECT  id, creation_date, name, size FROM  image_meta_data OFFSET floor(random() * (SELECT COUNT(*) FROM image_meta_data)) LIMIT 1")
    ImageMetaData findRandom();
}
