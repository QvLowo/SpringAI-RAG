package com.qvl.springairag.controller;

import com.qvl.springairag.service.DocumentReader;
import com.qvl.springairag.utils.DateTool;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;

import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;


import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;

    private final VectorStore vectorStore;

    private final DocumentReader documentReader;

    // 全部跑好才回應
    @GetMapping("/chat")
    public String chat(@RequestParam String prompt) {
        return this.chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    // 流氏回應，邊想邊回，像打字機輸出
    @GetMapping(value = "/flux-chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> fluxChat(@RequestParam String chatId, @RequestParam String prompt) {
        return this.chatClient.prompt(prompt)
                .tools(new DateTool())

                // 讓ai有記憶
                .advisors(advisor -> advisor.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                // 紀錄token數
                .advisors(new SimpleLoggerAdvisor())
                .user(prompt)
                .stream().content();
    }

    /**
     * 圖片上傳 + 提示詞，讓AI處理並回應
     * @param image 上傳圖片
     * @param prompt 提示詞
     * @return AI回應
     */
    @PostMapping("/image")
    public String getImageInfo(@RequestParam MultipartFile image, @RequestParam String prompt) {
        return this.chatClient.prompt()
                .user(user -> user.text(prompt)
                        .media(MimeTypeUtils.parseMimeType(image.getContentType()), image.getResource()))
                .call()
                .content();
    }

    @PostMapping("/report")
    public String getReportAnalysis(@RequestParam MultipartFile report, @RequestParam String prompt) {
        return this.chatClient.prompt()
                .user(user -> user.text(prompt)
                        .media())
                .advisors(advisor->advisor.param(QuestionAnswerAdvisor.FILTER_EXPRESSION, "report analysis"))
                .call()
                .content();
    }

    @GetMapping("/pdf/read")
    public List<Document> readPdf(){
        return documentReader.readPdf();
    }

    @GetMapping("/pdf/import")
    public void importPdf(){
        documentReader.saveInVector();
    }

    @GetMapping("/pdf/search")
    public List<Document> searchPdf(String prompt){
        return documentReader.search(prompt);
    }


}
