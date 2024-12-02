package com.kong.cc.repository;

import com.kong.cc.dto.CategoryResponse;
import com.kong.cc.entity.ItemSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemSubCategoryRepository extends JpaRepository<ItemSubCategory, Integer> {

    ItemSubCategory findByItemCategoryName(String itemCategoryName);

    List<ItemSubCategory> findByItemMiddleCategorySb_ItemCategoryNum(Integer itemCategoryMiddleNum);

    //상민
    Optional<ItemSubCategory> findByItemCategoryNum(Integer subCategoryNum);

    @Query("select new com.kong.cc.dto.CategoryResponse(m.itemCategoryName,m.itemCategoryNum) from ItemSubCategory m join m.itemMiddleCategorySb n where n.itemCategoryName = :categoryName")
    List<CategoryResponse> findAllCategory(@Param("categoryName") String categoryName);
}
