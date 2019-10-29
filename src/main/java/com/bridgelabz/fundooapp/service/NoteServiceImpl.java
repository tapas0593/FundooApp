package com.bridgelabz.fundooapp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundooapp.dto.CollabUserInfo;
import com.bridgelabz.fundooapp.dto.NoteDTO;
import com.bridgelabz.fundooapp.dto.TotalNotes;
import com.bridgelabz.fundooapp.exception.UserException;
import com.bridgelabz.fundooapp.model.Label;
import com.bridgelabz.fundooapp.model.Note;
import com.bridgelabz.fundooapp.model.User;
import com.bridgelabz.fundooapp.repository.CollaboratorRepository;
import com.bridgelabz.fundooapp.repository.LabelRepository;
import com.bridgelabz.fundooapp.repository.NoteRepository;
import com.bridgelabz.fundooapp.repository.UserRepository;
import com.bridgelabz.fundooapp.util.JWTUtil;

@Service
public class NoteServiceImpl implements NoteService {

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private LabelRepository labelRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CollaboratorRepository collabRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Note createNote(NoteDTO noteDTO, String token) {
		Long userId = JWTUtil.verifyToken(token);
		Note newNote = modelMapper.map(noteDTO, Note.class);
		newNote.setUserId(userId);
		LocalDateTime currentDateTime = LocalDateTime.now();
		newNote.setCreatedDateAndTime(currentDateTime);
		newNote.setUpdatedDateAndTime(currentDateTime);
		if (noteDTO.isArchived()) {
			newNote.setPin(false);
		}
		newNote = noteRepository.save(newNote);
		return newNote;
	}

//	@Override
//	public Note updateNote(NoteDTO newNote, long noteId, String token) {
//		long userId = JWTUtil.verifyToken(token);
//		Optional<Note> noteToBeUpdated = noteRepository.findByUserIdAndNoteId(userId, noteId);
//		Note updatedNote = noteToBeUpdated.map(existingNote -> {
//			existingNote.setTitle(newNote.getTitle() != null ? newNote.getTitle() : noteToBeUpdated.get().getTitle());
//			existingNote.setDescription(newNote.getDescription() != null ? newNote.getDescription()
//					: noteToBeUpdated.get().getDescription());
//			return existingNote;
//		}).orElseThrow(() -> new UserException(404, "Note Not Found."));
//		updatedNote.setUpdatedDateAndTime(LocalDateTime.now());
//		return noteRepository.save(updatedNote);
//	}

	@Override
	public Note updateNote(Note note, String token) {
		long userId = JWTUtil.verifyToken(token);
		Note updatedNote = noteRepository.findByUserIdAndNoteId(userId, note.getNoteId()).map(existingNote -> {
			existingNote.setTitle(note.getTitle() != null ? note.getTitle() : existingNote.getTitle());
			existingNote.setDescription(
					note.getDescription() != null ? note.getDescription() : existingNote.getDescription());
			existingNote.setTrash(note.isTrash());
			existingNote.setArchived(!note.isTrash() && note.isArchived());
			existingNote.setPin(!note.isTrash() && !note.isArchived() && note.isPin());
			existingNote.setUpdatedDateAndTime(LocalDateTime.now());
			existingNote.setColor(note.getColor() != null ? note.getColor() : existingNote.getColor());
			return existingNote;
		}).orElseThrow(() -> new UserException(404, "Note Not Found."));

		return noteRepository.save(updatedNote);
	}

	@Override
	public String deleteNote(long noteId, String token) {
		Long userId = JWTUtil.verifyToken(token);
		try {
			noteRepository.deleteByNoteIdAndUserId(noteId, userId);
		} catch (Exception e) {
			throw new UserException(404, "Note ID: " + noteId + " not found.");
		}
		return "Note with Note ID: " + noteId + " has been deleted successfully.";
	}

	@Override
	public List<Note> getNotes(String token) {
		long userId;
		try {
			userId = JWTUtil.verifyToken(token);
		} catch (Exception e) {
			throw new UserException(404, "Token Not valid");
		}
		return noteRepository.findByUserId(userId, false, false);
	}

	@Override
	public List<TotalNotes> getAllNotes(String token, boolean isTrash, boolean isArchived) {
		long userId;
		try {
			userId = JWTUtil.verifyToken(token);
		} catch (Exception e) {
			throw new UserException(404, "Token Not valid");
		}
//		List<Note> notes = noteRepository.findByUserId(userId, isTrash, isArchived);
		List<Note> notes = noteRepository.findAll().stream().filter(
				note -> note.getUserId() == userId && note.isArchived() == isArchived && note.isTrash() == isTrash)
				.collect(Collectors.toList());
		System.out.println("All Notes: " + notes);
		List<Long> noteIds = collabRepository.findNoteIdBySharedToUserId(userId).orElse(new ArrayList<Long>());
		if (!noteIds.isEmpty()) {
			notes.addAll(noteRepository.findNotesByNoteIdIn(noteIds).orElse(new ArrayList<Note>()));
		}
		List<TotalNotes> allNotes = new ArrayList<>();

		System.out.println("Notes: " + notes);
		for (Note note : notes) {
			List<User> users;
			List<CollabUserInfo> collabUserInfos = new ArrayList<>();
			List<Long> userIds = collabRepository.findSharedToUserIdByNoteId(note.getNoteId())
					.orElse(new ArrayList<Long>());
			System.out.println("User Ids: " + userIds);
			if (!userIds.isEmpty()) {
				System.out.println("User Id:" + userIds);
				users = userRepository.findUsersIn(userIds).orElseThrow(() -> new UserException(404, "User not found"));
				System.out.println("Users:  " + users);
				collabUserInfos = users.stream().map(this::setInfoToDto).collect(Collectors.toList());
			}
			allNotes.add(new TotalNotes(note, collabUserInfos));

		}

		return allNotes;
	}

	private CollabUserInfo setInfoToDto(User user) {
		System.out.println(user.getFirstName() + " " + user.getLastName());
		System.out.println(user.getEmailId());
		return new CollabUserInfo(user.getFirstName() + " " + user.getLastName(), user.getEmailId());
	}

	@Override
	public String addLabelToNote(long noteId, long labelId, String token) {
		long userId = JWTUtil.verifyToken(token);
		Note updatedNote = noteRepository.findByUserIdAndNoteId(userId, noteId).map(note -> {
			Label label = labelRepository.findById(labelId)
					.orElseThrow(() -> new UserException(404, "Label not found"));
			note.getLabels().add(label);
			note.setUpdatedDateAndTime(LocalDateTime.now());
			return note;
		}).orElseThrow(() -> new UserException(404, "Note not found."));
		noteRepository.save(updatedNote);
		return "Label added to the note.";
	}

	@Override
	public String removeLabel(long noteId, long labelId, String token) {
		long userId = JWTUtil.verifyToken(token);
		Note note = noteRepository.findByUserIdAndNoteId(userId, noteId)
				.orElseThrow(() -> new UserException(404, "Notes not found"));
		Label label = labelRepository.findByUserIdAndLabelId(userId, labelId)
				.orElseThrow(() -> new UserException(404, "Notes not found"));
		note.getLabels().remove(label);
		noteRepository.save(note);
		return "Label removed successfully.";
	}

//	@Override
//	public List<Note> getArchivedNote(String token) {
//		System.out.println("Inside Archived Notes");
//		long userId = JWTUtil.verifyToken(token);
//		return noteRepository.findByUserId(userId, false, true);
//	}
//
//	@Override
//	public List<Note> getTrashedNote(String token) {
//		System.out.println("Inside Trashed Notes");
//		long userId = JWTUtil.verifyToken(token);
//		return noteRepository.findByUserId(userId, true, false);
//	}

	@Override
	public Note addReminder(long noteId, LocalDateTime reminder, String token) {
		System.out.println("Inside Add reminder Service");
		long userId = JWTUtil.verifyToken(token);
		Note newNote = noteRepository.findByUserIdAndNoteId(userId, noteId).map(existingNote -> {
			if (reminder.isAfter(LocalDateTime.now())) {
				existingNote.setReminder(reminder);
			} else {
				throw new UserException(406, "Date not Acceptable.");
			}
			return existingNote;
		}).orElseThrow(() -> new UserException(404, "Note Not Found."));
		return noteRepository.save(newNote);
	}

	@Override
	public Note removeReminder(long noteId, String token) {
		System.out.println("Inside Remove Reminder Service");
		long userId = JWTUtil.verifyToken(token);
		Note newNote = noteRepository.findByUserIdAndNoteId(userId, noteId).map((existingNote) -> {
			existingNote.setReminder(null);
			return existingNote;
		}).orElseThrow(() -> new UserException(404, "Note Not Found."));
		return noteRepository.save(newNote);
	}

	@Override
	public List<Note> getReminderNotes(String token) {
		long userId = JWTUtil.verifyToken(token);
		return noteRepository.findAllReminder(userId);
	}

//	@Override
	public List<TotalNotes> getReminderNotes1(String token) {
		long userId = JWTUtil.verifyToken(token);
//		return noteRepository.findAllReminder(userId);
		List<Note> notes = noteRepository.findAllReminder(userId);
		List<Long> noteIds = collabRepository.findNoteIdBySharedToUserId(userId).orElse(new ArrayList<Long>());
		if (!noteIds.isEmpty()) {
			notes.addAll(noteRepository.findNotesByNoteIdIn(noteIds).orElse(new ArrayList<Note>()));
		}
		List<TotalNotes> allNotes = new ArrayList<>();

		System.out.println("Notes: " + notes);
		for (Note note : notes) {
			List<User> users;
			List<CollabUserInfo> collabUserInfos = new ArrayList<>();
			List<Long> userIds = collabRepository.findSharedToUserIdByNoteId(note.getNoteId())
					.orElse(new ArrayList<Long>());
			System.out.println("User Ids: " + userIds);
			if (!userIds.isEmpty()) {
				System.out.println("User Id:" + userIds);
				users = userRepository.findUsersIn(userIds).orElseThrow(() -> new UserException(404, "User not found"));
				System.out.println("Users:  " + users);
				collabUserInfos = users.stream().map(this::setInfoToDto).collect(Collectors.toList());
			}
			allNotes.add(new TotalNotes(note, collabUserInfos));
		}

		return allNotes;
	}

//	@Override
//	public String addLabelToNote(long noteId, long labelId, String token) {
//		noteRepository.findById(noteId).map(note -> {
//			Label label = labelRepository.findById(labelId).get();
//			note.getLabels().add(label);
//			note.setUpdatedDateAndTime(LocalDateTime.now());
//			return note;
//		}).ifPresent(noteRepository::save);
//		return "Label added to note.";
//}

}
