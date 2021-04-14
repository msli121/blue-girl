package com.blue.girl.server.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagMergeRequest {
    private int fileId;
    private List<TagItem> tags;
}
