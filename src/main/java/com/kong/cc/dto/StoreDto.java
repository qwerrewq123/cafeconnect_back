package com.kong.cc.dto;

import java.time.LocalTime;
import java.util.Date;

import com.kong.cc.entity.Member;
import com.kong.cc.entity.ShopOrder;
import com.kong.cc.entity.Repair;
import com.kong.cc.entity.Stock;
import com.kong.cc.entity.Store;
import com.kong.cc.entity.WishItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDto {
	private Integer storeCode;
	
	private String storeName;
	private String storeAddress;
	private String storeAddressNum;
	private String storePhone;
	
	private LocalTime storeOpenTime;
	private LocalTime storeCloseTime;
	private String storeCloseDate;
	
	private String ownerName;
	private String ownerPhone;
	private String managerName;
	private String managerPhone;
	
	private Date contractPeriodStart;
	private Date contractPeriodEnd;
	private Date contractDate;
	private Date openingDate;
	private String storeStatus;
	 
	private Integer memberNum;
	
	// 추가
	private String stockCount;
	
	public Store toEntity() {
		Store store = Store.builder()
				.storeCode(storeCode)
				.storeName(storeName)
				.storeAddress(storeAddress)
				.storeAddressNum(storeAddressNum)
				.storeCode(storeCode)
				.storeName(storeName)
				.storeAddress(storeAddress)
				.storePhone(storePhone)
				.storeOpenTime(storeOpenTime)
				.storeCloseTime(storeCloseTime)
				.storeCloseDate(storeCloseDate)
				.ownerName(ownerName)
				.ownerPhone(ownerPhone)
				.managerName(managerName)
				.managerPhone(managerPhone)
				.contractPeriodStart(contractPeriodStart)
				.contractPeriodEnd(contractPeriodEnd)
				.contractDate(contractDate)
				.openingDate(openingDate)
				.storeStatus(storeStatus)
				.build();
		
		if(memberNum!=null) {
			store.setMember(Member.builder().memberNum(memberNum).build());
		}
		
		return store;
	}
}
