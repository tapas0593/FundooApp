package com.bridgelabz.fundooapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Collaborator {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long collaboratorId;
	
	private long sharedToUserId;
	
	private long ownerUserId;
	
	private long noteId;
}
