package com.kong.cc.repository;

import com.kong.cc.entity.ItemMiddleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kong.cc.entity.ItemSubCategory;

public interface ItemSubCategoryRepository extends JpaRepository<ItemSubCategory, Integer> {

    ItemSubCategory findByItemCategoryName(String itemCategoryName);


}