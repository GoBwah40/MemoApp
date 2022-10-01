package com.matt.memoapi.controller;

import java.util.Collection;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.matt.memoapi.domain.Memo;
import com.matt.memoapi.repository.MemoRepository;

@RestController //Intermediare avec le front qui gere les requetes HTTP
public class MemoController {
	
	@Autowired
	private MemoRepository repo;
	
	@GetMapping("/")
	public ResponseEntity<Collection<Memo>> getMemos(){
		return ResponseEntity.ok().body(repo.findAll());
	}
	
	@PostMapping("/")
	public ResponseEntity<Long> createMemo() {
		try {
			if (repo.count() > 50){
				return ResponseEntity.badRequest().build();
			}
			Memo memo = new Memo();
			memo.setId(null);
			memo.setDescription("");
			memo.setTitle("");
			repo.save(memo);
			
			return ResponseEntity.ok().body(memo.getId());
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public void modifMemo(RequestEntity<MemoForm> request, @PathVariable("id") final Long id ) {
		
		try {
			Optional<Memo> memoOptional = repo.findById(id);
			
			if (memoOptional.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}

			Memo memo = memoOptional.get();
			MemoForm body = request.getBody();
			if (body == null){
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			} 

			memo.setDescription(body.getContent());
			memo.setTitle(body.getTitle());
			repo.save(memo);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} 	
	}
	
	@DeleteMapping("/{id}")
	public void deleteMemo(@PathVariable("id") final Long id){
		try {
			repo.deleteById(id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Memo> getMemo(@PathVariable("id") final Long id ){
		try {
			Optional<Memo> memoOptional = repo.findById(id);
			
			if (memoOptional.isEmpty()){
				return ResponseEntity.badRequest().build();	
			}
			
			Memo memo = memoOptional.get();
			
			return ResponseEntity.ok().body(memo);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
}

class MemoForm {
	private String title;
	private String content;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	
	public MemoForm(String title, String content) {
		this.title = title;
		this.content = content;
	}
	
	
}
