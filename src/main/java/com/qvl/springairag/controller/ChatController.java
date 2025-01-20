package com.qvl.springairag.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;

    @GetMapping("/chat")
    public String chat(@RequestParam String prompt) {
        return this.chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    @GetMapping("/fluxChat")
    public Flux<String> fluxChat(@RequestParam String prompt) {
        return this.chatClient.prompt()
                .user(prompt)
                .stream().content();
    }
}
