package com.bridgelabz.fundooapp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundooapp.dto.CollaboratorDTO;
import com.bridgelabz.fundooapp.exception.UserException;
import com.bridgelabz.fundooapp.model.Collaborator;
import com.bridgelabz.fundooapp.repository.CollaboratorRepository;
import com.bridgelabz.fundooapp.repository.UserRepository;
import com.bridgelabz.fundooapp.util.JWTUtil;

@Service
public class CollaboratorServiceImpl implements CollaboratorService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CollaboratorRepository collaboratorRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void addCollaborator(CollaboratorDTO collaboratorDTO, String token) {
		System.out.println("inside Add collaborator Service ");

		long userId = JWTUtil.verifyToken(token);
		if (collaboratorDTO.getSharedToUserId() != userId) {
			collaboratorRepository.findByNoteIdAndSharedToUserId(collaboratorDTO.getNoteId(),
					collaboratorDTO.getSharedToUserId());
			System.out.println("Inside if()");
			Collaborator collaborator = modelMapper.map(collaboratorDTO, Collaborator.class);
			collaborator.setSharedToUserId(collaboratorDTO.getSharedToUserId());
			collaborator.setOwnerUserId(userId);
			System.out.println(collaborator);
			collaboratorRepository.save(collaborator);
		} else {
			throw new UserException(404, "Collaborator Already Present!!");
		}
	}

	@Override
	public void removeCollaborator(CollaboratorDTO collaboratorDTO, String token) {
		System.out.println("inside Remove collaborator Service ");
		System.out.println("Collab DTO: " + collaboratorDTO);
		System.out.println("Collab DTO Note ID: " + collaboratorDTO.getNoteId());
		System.out.println("Collab DTO: Shared To UserID" + collaboratorDTO.getSharedToUserId());

		JWTUtil.verifyToken(token);
		Collaborator collaborator = collaboratorRepository
				.findByNoteIdAndSharedToUserId(collaboratorDTO.getNoteId(), collaboratorDTO.getSharedToUserId())
				.orElseThrow(() -> new UserException(404, "Collaborator not found"));
		System.out.println("collab id: " + collaborator.getCollaboratorId());
		collaboratorRepository.deleteById(collaborator.getCollaboratorId());
	}

	@Override
	public void removeCollaborator(Long noteId, String sharedToEmailId, String token) {
		JWTUtil.verifyToken(token);
//		Long sharedToUserId = userRepository.findByEmailId(sharedToEmailId).orElseThrow(() -> new UserException(404, "Collaborator not found"));
		Long sharedToUserId = userRepository.findByEmailId(sharedToEmailId).map(user -> {
			return user.getId();
		}).orElseThrow(() -> new UserException(404, "User not found"));
		Collaborator collaborator = collaboratorRepository.findByNoteIdAndSharedToUserId(noteId, sharedToUserId)
				.orElseThrow(() -> new UserException(404, "Collaborator not found"));
		System.out.println("collab id: " + collaborator.getCollaboratorId());
		collaboratorRepository.deleteById(collaborator.getCollaboratorId());
	}

}
