package com.qvl.springairag.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentReader {

    private final VectorStore vectorStore;

    public List<Document> readPdf() {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {

            Resource[] pdfResources = resolver.getResources("classpath:/pdf/*.pdf");

            for (Resource pdfResource : pdfResources) {
                PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource,
                        PdfDocumentReaderConfig.builder()
                                .withPageTopMargin(0)
                                .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                        .withNumberOfTopTextLinesToDelete(0)
                                        .build())
                                .withPagesPerDocument(1)
                                .build());
                return pdfReader.read();
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "讀取pdf檔案失敗");
        }
        return null;
    }

    // embedding（文字轉成向量） 存入向量資料庫
    public void saveInVector() {
        vectorStore.add(readPdf());
    }

    public List<Document> search(String prompt) {
        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(prompt)
                        .topK(5) // 抓前五筆最相似的資料
                        .similarityThreshold(0.0)
                        .build()
        );
    }

    // 按 token切分
    public List<Document> splitDocuments(List<Document> documents) {
        TokenTextSplitter splitter = new TokenTextSplitter();
        return splitter.split(documents);
    }
}
