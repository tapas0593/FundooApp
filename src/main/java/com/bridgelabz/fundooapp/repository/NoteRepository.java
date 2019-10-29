package com.bridgelabz.fundooapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bridgelabz.fundooapp.model.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

	Optional<Note> findByUserIdAndNoteId(long userId, long noteId);

	@Query("from Note n where n.isArchived=:isArchived and n.isTrash=:isTrash and n.userId= :userId")
	List<Note> findByUserId(long userId, boolean isTrash, boolean isArchived);

	@Query("from Note n where n.reminder is not null and n.userId= :userId")
	List<Note> findAllReminder(long userId);

//	@Modifying
//	@Transactional
//	@Query(value = "UPDATE notes n SET reminder= null WHERE note_id = :noteId AND user_id = :userId",nativeQuery = true)
//	void markReminderAsNull(long userId, long noteId);

//	@Modifying(clearAutomatically = true)
//    @Query("UPDATE Note n SET n.reminder= NULL WHERE n.noteId = :noteId AND n.userId = :userId")
//    int markReminderAsNull(@Param("userId") long userId, @Param("noteId") long noteId);

	@Query(value = "select * from notes where note_id in :note_ids", nativeQuery = true)
	Optional<List<Note>> findNotesByNoteIdIn(@Param("note_ids") List<Long> noteIds);

	@Query(value = "delete from notes where note_id=:noteId and user_id=:userId", nativeQuery = true)
	void deleteByNoteIdAndUserId(Long noteId, Long userId);
}
