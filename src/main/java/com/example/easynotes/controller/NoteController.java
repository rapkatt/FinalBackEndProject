package com.example.easynotes.controller;

import com.example.easynotes.exception.ResourceNotFoundException;
import com.example.easynotes.model.Note;
import com.example.easynotes.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/")
public class NoteController {

    @Autowired
    NoteRepository noteRepository;

    @GetMapping("/notes")
    public String getAllNotes(Model model) {
        List<Note> note = noteRepository.findAll();

        if (note != null)
            model.addAttribute("notes", note);
        return "notes";
    }

    @GetMapping("/addNotes")
    public String AddNotes() {
        return "addNotes";
    }

    @GetMapping(value = "/update/{id}")
    public String updateCustomer(@PathVariable("id") Integer id, Model model) {
        Note note = noteRepository.findNoteById(id);
        System.out.println(note);

        if (note != null) {
            model.addAttribute("note", note);
        }
        return "update";
    }


    @PostMapping("/addNotes")
    public String createNote(Note note) {
        noteRepository.save(note);
        return "redirect:/notes";
    }

    @GetMapping("/notes/{id}")
    public Note getNoteById(@PathVariable(value = "id") Integer noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
    }

    @PostMapping(value = "/notes/{id}")
    public String removeCustomer(@PathVariable("id") Integer id) {
        noteRepository.deleteById(id);
        return "redirect:/notes";
    }

    @PostMapping(value = "/update")
    public String updateCustomer(@Valid Note note, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "update";
        }
        boolean exists = noteRepository.existsById(note.getId());
        if (exists) {
            noteRepository.deleteById(note.getId());
            noteRepository.save(note);
        }

        return "redirect:/notes";
    }


}
