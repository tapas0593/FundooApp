package com.bridgelabz.fundooapp.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundooapp.dto.CollaboratorDTO;
import com.bridgelabz.fundooapp.dto.NoteDTO;
import com.bridgelabz.fundooapp.dto.TotalNotes;
import com.bridgelabz.fundooapp.model.Note;
import com.bridgelabz.fundooapp.response.Response;
import com.bridgelabz.fundooapp.service.CollaboratorService;
import com.bridgelabz.fundooapp.service.NoteService;
import com.bridgelabz.fundooapp.util.JWTUtil;

@RestController
@RequestMapping("/fundoo/note")
@CrossOrigin(origins = "http://localhost:4200", exposedHeaders = { "jwt_token" })
public class NoteController {

	@Autowired
	private NoteService noteService;

	@Autowired
	private CollaboratorService collaboratorService;

	@GetMapping("/")
	public String hello() {
		return "hello world!!";
	}
	
	@PostMapping("/create")
	public ResponseEntity<Response> createNote(@RequestBody NoteDTO noteDTO, @RequestHeader("jwt_token") String token) {
		Note note = noteService.createNote(noteDTO, token);
		Response response = new Response("Note created.", 200, note);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<Response> updateNote(@RequestBody Note note, @RequestHeader("jwt_token") String token) {
		Note updatedNote = noteService.updateNote(note, token);
		Response response = new Response("Note updated.", 200, updatedNote);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/addReminder/{noteId}")
	public ResponseEntity<Response> addReminder(@PathVariable("noteId") long noteId,
			@DateTimeFormat(iso = ISO.DATE_TIME) @RequestParam LocalDateTime reminderDate,
			@RequestHeader("jwt_token") String token) {
		System.out.println("Inside Add reminder Controller");
		Note updatedNote = noteService.addReminder(noteId, reminderDate, token);
		Response response = new Response("Reminder added.", 200, updatedNote);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/removeReminder/{noteId}")
	public ResponseEntity<Response> removeReminder(@PathVariable("noteId") long noteId,
			@RequestHeader("jwt_token") String token) {
		Note updatedNote = noteService.removeReminder(noteId, token);
		Response response = new Response("Reminder removed.", 200, updatedNote);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{noteId}")
	public ResponseEntity<Response> deleteNote(@PathVariable("noteId") long noteId,
			@RequestHeader("jwt_token") String token) {
		String message = noteService.deleteNote(noteId, token);
		Response response = new Response(message, 200);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getnotes")
	public ResponseEntity<Response> getNotes(@RequestParam("isTrash") boolean isTrash,@RequestParam("isArchived") boolean isArchived, @RequestHeader("jwt_token") String token) {
//		List<Note> notes = noteService.getNotes(token);
		List<TotalNotes> notes = noteService.getAllNotes(token, isTrash, isArchived);
		Response response = new Response("Notes Found of User ID: " + JWTUtil.verifyToken(token), 302, notes);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/addlabel")
	public ResponseEntity<Response> addLabelToNote(@RequestParam("noteId") long noteId,
			@RequestParam("labelId") long labelId, @RequestHeader("jwt_token") String token) {
		String message = noteService.addLabelToNote(noteId, labelId, token);
		Response response = new Response(message, 200);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/removeLabel")
	public ResponseEntity<Response> removeLabel(@RequestParam("noteId") long noteId,
			@RequestParam("labelId") long labelId, @RequestHeader("jwt_token") String token) {
		String message = noteService.removeLabel(noteId, labelId, token);
		Response response = new Response(message, 200);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

//	@GetMapping("/getArchivedNote")
//	public ResponseEntity<Response> getArchivedNote(@RequestHeader("jwt_token") String token) {
//		List<Note> archivedNotes = noteService.getArchivedNote(token);
//		Response response = new Response("Archived Notes", 200, archivedNotes);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
//
//	@GetMapping("/getTrashedNote")
//	public ResponseEntity<Response> getTrashedNote(@RequestHeader("jwt_token") String token) {
//		List<Note> trashedNotes = noteService.getTrashedNote(token);
//		Response response = new Response("Trashed Notes", 200, trashedNotes);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}

	@GetMapping("/getReminderNotes")
	public ResponseEntity<Response> getReminderNotes(@RequestHeader("jwt_token") String token) {
//		List<Note> reminderNotes = noteService.getReminderNotes(token);
		List<TotalNotes> reminderNotes = noteService.getReminderNotes1(token);

		Response response = new Response("Reminder Notes", 200, reminderNotes);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/addCollaborator")
	public ResponseEntity<Response> addCollaborator(@RequestBody CollaboratorDTO collaboratorDTO,
			@RequestHeader("jwt_token") String token) {
		System.out.println("inside Add collaborator");
		collaboratorService.addCollaborator(collaboratorDTO, token);
		Response response = new Response("Collaborator added", 200);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

//	@PostMapping("/removeCollaborator")
//	public ResponseEntity<Response> removeCollaborator(@RequestBody CollaboratorDTO collaboratorDTO,
//			@RequestHeader("jwt_token") String token) {
//		System.out.println("inside Remove collaborator");
//		System.out.println(collaboratorDTO);
//		collaboratorService.removeCollaborator(collaboratorDTO, token);
//		Response response = new Response("Collaborator removed", 200);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
	@DeleteMapping("/removeCollaborator/{noteId}/{sharedToEmailId}")
	public ResponseEntity<Response> removeCollaborator(@PathVariable("noteId") Long noteId ,@PathVariable("sharedToEmailId") String sharedToEmailId,  
			@RequestHeader("jwt_token") String token) {
		System.out.println("inside Remove collaborator");
		System.out.println("Note ID: " + noteId);
		System.out.println("Shared To EmailID: " + sharedToEmailId);
		collaboratorService.removeCollaborator(noteId, sharedToEmailId, token);
		Response response = new Response("Collaborator removed", 200);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
