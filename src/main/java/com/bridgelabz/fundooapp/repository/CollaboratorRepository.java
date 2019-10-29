package com.bridgelabz.fundooapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bridgelabz.fundooapp.model.Collaborator;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {

	Optional<Collaborator> findByNoteIdAndSharedToUserId(Long noteId, Long collabUserId);

	@Query(value = "SELECT note_id FROM collaborator WHERE shared_to_user_id= :userId", nativeQuery = true)
	Optional<List<Long>> findNoteIdBySharedToUserId(Long userId);

	@Query(value = "SELECT shared_to_user_id FROM collaborator WHERE note_id= :noteId", nativeQuery = true)
	Optional<List<Long>> findSharedToUserIdByNoteId(Long noteId);

//	@Query(value = "SELECT note_id FROM collaborator WHERE user_id= :user_id", nativeQuery = true)
//	Optional<List<Long>> findNoteIdByUserId(long userId);

}
