package com.usian.service;

import com.usian.utils.PageResult;

public interface ContentService {
    PageResult selectTbContentAllByCategoryId(Integer page, Integer rows, Long categoryId);
}
