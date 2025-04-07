package com.dormventory.service;

import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.model.*;
import com.dormventory.model.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final AmazonTextract textractClient;
    private final S3Service s3Service;

    public List<String> extractItemsFromReceipt(MultipartFile receiptImage) throws IOException {
        // Upload to S3
        String s3Key = s3Service.uploadFile(receiptImage);

        // Call Textract
        DetectDocumentTextRequest request = new DetectDocumentTextRequest()
            .withDocument(new Document()
                .withS3Object(new S3Object()
                    .withBucket(s3Service.getBucketName())
                    .withName(s3Key)));

        DetectDocumentTextResult result = textractClient.detectDocumentText(request);

        // Parse the results - this is a simple implementation
        // You might want to enhance this with more sophisticated receipt parsing logic
        List<String> items = new ArrayList<>();
        for (Block block : result.getBlocks()) {
            if (block.getBlockType().equals("LINE")) {
                // Basic filtering - you should implement more robust parsing
                if (block.getText().matches(".*\\d+\\.\\d{2}.*")) { // Lines with prices
                    items.add(block.getText());
                }
            }
        }

        return items;
    }
} 