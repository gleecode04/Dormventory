package com.dormventory.service;

import com.amazonaws.services.textract.model.Block;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReceiptParsingService {
    private static final Pattern PRICE_PATTERN = Pattern.compile("\\$?(\\d+\\.\\d{2})");
    private static final Pattern QUANTITY_PATTERN = Pattern.compile("^(\\d+)\\s+");
    
    public List<ParsedItem> parseReceiptBlocks(List<Block> blocks) {
        List<ParsedItem> items = new ArrayList<>();
        Map<String, Float> priceMap = new HashMap<>();
        
        // First pass: identify price lines and their positions
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            if (block.getBlockType().equals("LINE")) {
                String text = block.getText();
                Matcher priceMatcher = PRICE_PATTERN.matcher(text);
                
                if (priceMatcher.find()) {
                    float price = Float.parseFloat(priceMatcher.group(1));
                    // Look for item description in previous line
                    if (i > 0) {
                        Block prevBlock = blocks.get(i - 1);
                        if (prevBlock.getBlockType().equals("LINE")) {
                            String itemDesc = prevBlock.getText();
                            // Remove quantity if present
                            Matcher quantityMatcher = QUANTITY_PATTERN.matcher(itemDesc);
                            if (quantityMatcher.find()) {
                                int quantity = Integer.parseInt(quantityMatcher.group(1));
                                itemDesc = itemDesc.substring(quantityMatcher.end()).trim();
                                items.add(new ParsedItem(itemDesc, price, quantity));
                            } else {
                                items.add(new ParsedItem(itemDesc, price, 1));
                            }
                        }
                    }
                }
            }
        }
        
        return items;
    }
}

@Data
@AllArgsConstructor
public class ParsedItem {
    private String name;
    private float price;
    private int quantity;
} 