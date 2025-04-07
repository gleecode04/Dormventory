package com.dormventory.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {
    @Mock
    private AmazonS3 s3Client;
    
    @Mock
    private MultipartFile file;
    
    @InjectMocks
    private S3Service s3Service;
    
    private static final String BUCKET_NAME = "test-bucket";
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(s3Service, "bucketName", BUCKET_NAME);
    }
    
    @Test
    void uploadFile_Success() throws IOException {
        String fileName = "test.jpg";
        String contentType = "image/jpeg";
        byte[] content = "test content".getBytes();
        
        when(file.getOriginalFilename()).thenReturn(fileName);
        when(file.getContentType()).thenReturn(contentType);
        when(file.getSize()).thenReturn((long) content.length);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(content));
        
        String result = s3Service.uploadFile(file);
        
        assertTrue(result.startsWith("receipts/"));
        assertTrue(result.endsWith(fileName));
        
        ArgumentCaptor<PutObjectRequest> requestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        verify(s3Client).putObject(requestCaptor.capture());
        
        PutObjectRequest capturedRequest = requestCaptor.getValue();
        assertEquals(BUCKET_NAME, capturedRequest.getBucketName());
        assertEquals(result, capturedRequest.getKey());
    }
    
    @Test
    void uploadFile_IOError_ThrowsException() throws IOException {
        when(file.getInputStream()).thenThrow(new IOException("Test error"));
        
        assertThrows(IOException.class, () -> s3Service.uploadFile(file));
    }
    
    @Test
    void getBucketName_ReturnsConfiguredName() {
        assertEquals(BUCKET_NAME, s3Service.getBucketName());
    }
} 