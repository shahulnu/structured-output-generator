package com.springai.structured_output_generator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String getBook() {
        return this.chatClient.prompt("Generate  5 book name with title, author, publicationYear and ISBN.")
                .call()
                .content();
    }

    public Book getBookObject() {
        return this.chatClient.prompt("Generate  1 book name with title, author, publicationYear and ISBN.")
                .call()
                .entity(Book.class);
    }

    public List<Book> getBooks() {
        return this.chatClient.prompt("Generate  5 book name with title, author, publicationYear and ISBN.")
                .call()
                .entity(new ParameterizedTypeReference<List<Book>>() {});
    }

    public Map<String, Book> getBookMap() {
        return this.chatClient.prompt("Generate  5 book name with title, author, publicationYear and ISBN, and use book number as key for the output map.")
                .call()
                .entity(new ParameterizedTypeReference<Map<String, Book>>() {});
    }

    public Flux<Book> getBooksStream() {
        String message = "Generate  15 book name with title, author, publicationYear and ISBN";
        
        // Creates a BeanOutputConverter to convert the response into a List<Book> from the API
        var converter = new BeanOutputConverter<>(new ParameterizedTypeReference<List<Book>>() {
        });

        // Sends a prompt to the chat client, passing a dynamic message for the book details
        // The message can be customized via a query parameter
        Flux<String> flux = this.chatClient.prompt()
                .user(u -> u.text("""
                                  %s
                                  {format}
                                """.formatted(message)) // Insert the dynamic message here
                        .param("format", converter.getFormat())) // Pass any additional parameters, such as format
                .stream() // Stream the response content
                .content(); // Get the content (raw text response)

        // Collect the streamed content into a single string
        String content = flux.collectList().block().stream().collect(Collectors.joining());

        // Convert the content (raw string) into a List<Book> using the converter
        List<Book> actorFilms = converter.convert(content);

        // Return the list of books as a Flux<Book> to stream them asynchronously
        return Flux.fromIterable(actorFilms); // Convert the List<Book> into a Flux stream
    }


}
