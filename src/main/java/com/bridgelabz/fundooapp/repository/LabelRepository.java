package com.bridgelabz.fundooapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bridgelabz.fundooapp.model.Label;

public interface LabelRepository extends JpaRepository<Label, Long> {

	Optional<Label> findByUserIdAndLabelId(long userId, long labelid);

	Optional<List<Label>> findByUserId(long userId);

	@Query(value = "select * from notes_labels where labels_label_id = :labelId", nativeQuery = true)
	List<Long> findNoteIdsByLabel(long labelId);

	Optional<Label> findByLabelName(String labelName);
}
