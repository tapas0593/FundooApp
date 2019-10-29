package com.bridgelabz.fundooapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundooapp.dto.CollabUserInfo;
import com.bridgelabz.fundooapp.dto.LabelDTO;
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
public class LabelServiceImpl implements LabelService {

	@Autowired
	private LabelRepository labelRepository;
	@Autowired
	private NoteRepository noteRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private CollaboratorRepository collabRepository;
	@Autowired
	private UserRepository userRepository;

	@Override
	public Label createLabel(LabelDTO labelDTO, String token) {
		long userId = JWTUtil.verifyToken(token);
		Label label = modelMapper.map(labelDTO, Label.class);
		label.setUserId(userId);
		return labelRepository.save(label);
	}

	@Override
	public Label editLabel(Label label, String token) {
		long userId = JWTUtil.verifyToken(token);
		System.out.println(label.getLabelId());
		Optional<Label> labelTobeUpdated = labelRepository.findByUserIdAndLabelId(userId, label.getLabelId());
		Label updatedlabel = labelTobeUpdated.map(existingNote -> {
			existingNote.setLabelName(
					label.getLabelName() != null ? label.getLabelName() : labelTobeUpdated.get().getLabelName());
			return existingNote;
		}).orElseThrow(() -> new UserException(404, "Label Not found"));

		return labelRepository.save(updatedlabel);
	}

	@Override
	public void deleteLabel(long labelId, String token) {
		JWTUtil.verifyToken(token);
		try {
			labelRepository.deleteById(labelId);
		} catch (Exception e) {
			throw new UserException(404, "Label not found.");
		}
	}

	@Override
	public Label getLabel(String labelName, String token) {
		JWTUtil.verifyToken(token);
		return labelRepository.findByLabelName(labelName).orElseThrow(() -> new UserException(404, "Label not found"));
	}

	@Override
	public List<Label> getLabels(String token) {
		long userId = JWTUtil.verifyToken(token);
		return labelRepository.findByUserId(userId).orElseThrow(() -> new UserException(404, "Invalid token"));
	}

	@Override
	public List<TotalNotes> getLabeledNotes(long labelId, String token) {
		JWTUtil.verifyToken(token);
		List<Long> noteIds = labelRepository.findNoteIdsByLabel(labelId);
		List<Note> notes = new ArrayList<>();
		if (!noteIds.isEmpty()) {
			notes = noteRepository.findNotesByNoteIdIn(noteIds).orElse(new ArrayList<Note>());
		} else {
			throw new UserException(404, "No notes found.");
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

}
