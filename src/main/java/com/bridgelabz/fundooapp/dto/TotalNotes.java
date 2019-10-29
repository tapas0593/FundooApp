package com.bridgelabz.fundooapp.dto;

import java.util.List;

import com.bridgelabz.fundooapp.model.Note;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalNotes {
	private Note note; 
	private List<CollabUserInfo> collabUserInfo;
}
