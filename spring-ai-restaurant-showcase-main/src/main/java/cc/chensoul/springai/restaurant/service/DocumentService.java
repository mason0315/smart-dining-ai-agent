package cc.chensoul.springai.restaurant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文档加载服务
 * 负责文档的加载、分割和向量化存储
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final VectorStore vectorStore;
    private final ResourceLoader resourceLoader;

    /**
     * 加载文档到向量存储
     *
     * @param filePath 文档路径
     */
    public void loadDocuments(String filePath) {
        log.info("开始加载文档: {}", filePath);

        // 加载文档
        Resource resource = resourceLoader.getResource(filePath);
        MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                .withHorizontalRuleCreateDocument(true)
                .withIncludeCodeBlock(false)
                .withIncludeBlockquote(false)
                .withAdditionalMetadata("filename", resource.getFilename())
                .build();
        MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
        List<Document> documents = reader.get();
        log.info("成功加载 {} 个文档", documents.size());

        // 分割文档
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> splitDocuments = splitter.apply(documents);
        log.info("文档分割完成，共 {} 个片段", splitDocuments.size());

        // 存储到向量数据库
        vectorStore.add(splitDocuments);

        log.info("成功加载 {} 个文档片段到向量存储", splitDocuments.size());
    }

}
