package com.kong.cc.service;


import com.kong.cc.dto.ItemMajorCategoryForm;

import java.sql.Date;
import java.util.List;

import com.kong.cc.dto.ItemDto;

public interface SalesManagementMainService {

    List<ItemDto> itemRevenue(Integer storeCode, Date startDate, Date endDate) throws Exception;



}
