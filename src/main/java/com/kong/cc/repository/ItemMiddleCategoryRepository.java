package com.kong.cc.repository;

import com.kong.cc.entity.ItemMajorCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kong.cc.entity.ItemMiddleCategory;

public interface ItemMiddleCategoryRepository extends JpaRepository<ItemMiddleCategory, Integer> {

    ItemMiddleCategory findByItemCategoryName(String itemCategoryName);


}