package com.qvl.springairag.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;

    // 全部跑好才回應
    @GetMapping("/chat")
    public String chat(@RequestParam String prompt) {
        return this.chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    // 流氏回應，邊想邊回，像打字機輸出
    @GetMapping(value="/fluxChat",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> fluxChat(@RequestParam String prompt) {
        return this.chatClient.prompt()
                .user(prompt)
                .stream().content();
    }
}
