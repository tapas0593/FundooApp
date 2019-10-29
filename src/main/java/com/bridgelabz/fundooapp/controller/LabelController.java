package com.bridgelabz.fundooapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.bridgelabz.fundooapp.dto.LabelDTO;
import com.bridgelabz.fundooapp.dto.TotalNotes;
import com.bridgelabz.fundooapp.model.Label;
import com.bridgelabz.fundooapp.response.Response;
import com.bridgelabz.fundooapp.service.LabelService;

@RestController
@RequestMapping("fundoo/label")
@CrossOrigin(origins="http://localhost:4200",exposedHeaders= {"jwt_token"})
public class LabelController {

	@Autowired
	LabelService labelService;

	@PostMapping("/create")
	public ResponseEntity<Response> createLabel(@RequestBody LabelDTO labelDTO,
			@RequestHeader("jwt_token") String token) {
		Label label = labelService.createLabel(labelDTO, token);
		Response response = new Response("Label created successfully.", 201, label);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/edit")
	public ResponseEntity<Response> editLabel(@RequestBody Label label, @RequestHeader("jwt_token") String token) {
		Label updatedlabel = labelService.editLabel(label, token);	
		Response response = new Response("Label updated successfully.", 200, updatedlabel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{labelId}")
	public ResponseEntity<Response> deletelabel(@RequestHeader("jwt_token") String token, @PathVariable long labelId) {
		labelService.deleteLabel(labelId, token);
		Response response = new Response("Label deleted successfully.", 200);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("getLabel")
	public ResponseEntity<Response> getLabel(@RequestParam("labelName") String labelName, @RequestHeader("jwt_token") String token) {
		Label label = labelService.getLabel(labelName, token);
		Response response = new Response("Label Found.", 302, label);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@GetMapping("/getLabels")
	public ResponseEntity<Response> getlabels(@RequestHeader("jwt_token") String token) {
		List<Label> labels = labelService.getLabels(token);
		Response response = new Response("Labels Found.", 302, labels);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@GetMapping("/getLabeledNotes")
	public ResponseEntity<Response> getLabeledNotes(@RequestParam("labelId") long labelId,
			@RequestHeader("jwt_token") String token) {
		List<TotalNotes> labeledNotes = labelService.getLabeledNotes(labelId, token);
		Response response = new Response("Labeled Notes", 200, labeledNotes);
		return new ResponseEntity<>(response, HttpStatus.OK);	}
}
