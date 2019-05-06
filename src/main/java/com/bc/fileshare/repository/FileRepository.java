package com.bc.fileshare.repository;

import com.bc.fileshare.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {

	@Query("select f from File f inner join f.users u where f.fileId = :file_id and u.username = :username")
	Optional<File> findFileByIdAndUsername(@Param("file_id")int id, @Param("username")String username);

}
