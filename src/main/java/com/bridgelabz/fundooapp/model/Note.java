package com.bridgelabz.fundooapp.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.bridgelabz.fundooapp.dto.CollabUserInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Entity
@ToString
@Table(name = "notes")
public class Note {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long noteId;
	private long userId;
	private String title;
	private String description;
	private boolean isArchived;
	private boolean isTrash;
	private boolean isPin;
	private String color;
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Label> labels;
	@DateTimeFormat
	private LocalDateTime reminder;
	@DateTimeFormat
	private LocalDateTime createdDateAndTime;
	@DateTimeFormat
	private LocalDateTime updatedDateAndTime;

}
