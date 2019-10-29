package com.bridgelabz.fundooapp.service;

import java.time.LocalDateTime;
import java.util.List;

import com.bridgelabz.fundooapp.dto.NoteDTO;
import com.bridgelabz.fundooapp.dto.TotalNotes;
import com.bridgelabz.fundooapp.model.Note;

public interface NoteService {

	public Note createNote(NoteDTO noteDTO, String token);

	public Note addReminder(long noteId, LocalDateTime reminder, String token);

	public Note removeReminder(long noteId, String token);

	public Note updateNote(Note note, String token);

	public String deleteNote(long noteId, String token);

	public List<Note> getNotes(String token);

//	public List<Note> getArchivedNote(String token);
//
//	public List<Note> getTrashedNote(String token);

	public String addLabelToNote(long noteId, long labelId, String token);

	public List<Note> getReminderNotes(String token);

	public String removeLabel(long noteId, long labelId, String token);

	List<TotalNotes> getAllNotes(String token, boolean isTrash, boolean isArchived);

	public List<TotalNotes> getReminderNotes1(String token);
}
