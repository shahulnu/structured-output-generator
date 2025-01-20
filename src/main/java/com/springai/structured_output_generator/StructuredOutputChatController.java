package com.springai.structured_output_generator;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class StructuredOutputChatController {

    public final ChatService chatService;

    public StructuredOutputChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/text")
    public String getBook() {
        return chatService.getBook();
    }

    @GetMapping("/book")
    public Book getBookObject() {
        return chatService.getBookObject();
    }

    @GetMapping("/books")
    public List<Book> getBooks() {
        return chatService.getBooks();
    }

    @GetMapping("/books/map")
    public Map<String, Book> getBookMap() {
        return chatService.getBookMap();
    }

    @GetMapping("/books/stream")
    public Flux<Book> getBooksStream() {
        return chatService.getBooksStream();
    }
    
}
