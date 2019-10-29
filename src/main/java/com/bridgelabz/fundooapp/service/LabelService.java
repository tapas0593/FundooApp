package com.bridgelabz.fundooapp.service;

import java.util.List;

import com.bridgelabz.fundooapp.dto.LabelDTO;
import com.bridgelabz.fundooapp.dto.TotalNotes;
import com.bridgelabz.fundooapp.model.Label;

public interface LabelService {

	Label createLabel(LabelDTO labelDTO, String token);

	Label editLabel(Label label, String token);

	void deleteLabel(long labelId, String token);
	
	List<Label> getLabels(String token);
	
	public List<TotalNotes> getLabeledNotes(long labelId, String token);

	Label getLabel(String labelName, String token);

}
