package com.bridgelabz.fundooapp.service;

import com.bridgelabz.fundooapp.dto.CollaboratorDTO;

public interface CollaboratorService {

	public void addCollaborator(CollaboratorDTO collaboratorDTO, String token);

	void removeCollaborator(CollaboratorDTO collaboratorDTO, String token);

	public void removeCollaborator(Long noteId, String sharedToEmailId, String token);
}
