package com.symphony.devsol;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class MessageData {
    private String title;
    private String url;
    private List<String> existing;
    private List<String> added;
}
