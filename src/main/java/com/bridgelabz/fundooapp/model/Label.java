package com.bridgelabz.fundooapp.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "labels")
public class Label {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long labelId;
	private Long userId;
	@ManyToMany(mappedBy = "labels", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Note> notes;
	@Column(nullable = false)
	private String labelName;
}
