package com.dormventory.service;

import com.amazonaws.services.textract.model.Block;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReceiptParsingServiceTest {
    @InjectMocks
    private ReceiptParsingService receiptParsingService;
    
    @Test
    void parseReceiptBlocks_WithQuantityAndPrice_Success() {
        Block quantityBlock = new Block()
            .withBlockType("LINE")
            .withText("2 Milk");
            
        Block priceBlock = new Block()
            .withBlockType("LINE")
            .withText("$5.99");
            
        List<Block> blocks = Arrays.asList(quantityBlock, priceBlock);
        
        List<ParsedItem> result = receiptParsingService.parseReceiptBlocks(blocks);
        
        assertEquals(1, result.size());
        ParsedItem parsedItem = result.get(0);
        assertEquals("Milk", parsedItem.getName());
        assertEquals(5.99f, parsedItem.getPrice());
        assertEquals(2, parsedItem.getQuantity());
    }

    @Test
    void parseReceiptBlocks_NoQuantity_Success() {
        Block itemBlock = new Block()
            .withBlockType("LINE")
            .withText("Bread");
            
        Block priceBlock = new Block()
            .withBlockType("LINE")
            .withText("$3.99");
            
        List<Block> blocks = Arrays.asList(itemBlock, priceBlock);
        
        List<ParsedItem> result = receiptParsingService.parseReceiptBlocks(blocks);
        
        assertEquals(1, result.size());
        ParsedItem parsedItem = result.get(0);
        assertEquals("Bread", parsedItem.getName());
        assertEquals(3.99f, parsedItem.getPrice());
        assertEquals(1, parsedItem.getQuantity());
    }

    @Test
    void parseReceiptBlocks_MultipleItems_Success() {
        List<Block> blocks = Arrays.asList(
            new Block().withBlockType("LINE").withText("2 Milk"),
            new Block().withBlockType("LINE").withText("$5.99"),
            new Block().withBlockType("LINE").withText("Bread"),
            new Block().withBlockType("LINE").withText("$3.99")
        );
        
        List<ParsedItem> result = receiptParsingService.parseReceiptBlocks(blocks);
        
        assertEquals(2, result.size());
        assertEquals("Milk", result.get(0).getName());
        assertEquals("Bread", result.get(1).getName());
    }

    @Test
    void parseReceiptBlocks_InvalidFormat_ReturnsEmpty() {
        List<Block> blocks = Arrays.asList(
            new Block().withBlockType("LINE").withText("Invalid"),
            new Block().withBlockType("LINE").withText("Format")
        );
        
        List<ParsedItem> result = receiptParsingService.parseReceiptBlocks(blocks);
        
        assertTrue(result.isEmpty());
    }
} 