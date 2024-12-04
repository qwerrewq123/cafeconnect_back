package com.kong.cc.service;

import com.kong.cc.dto.MenuDto;
import com.kong.cc.dto.SalesDto;
import com.kong.cc.dto.SalesListDto;
import com.kong.cc.dto.SalesMenuDto;
import com.kong.cc.entity.Menu;
import com.kong.cc.entity.QMenu;
import com.kong.cc.entity.QSales;
import com.kong.cc.entity.QShopOrder;
import com.kong.cc.entity.QStore;
import com.kong.cc.entity.Sales;
import com.kong.cc.entity.Store;
import com.kong.cc.repository.ItemRepository;
import com.kong.cc.repository.MenuRepository;
import com.kong.cc.repository.SalesRepository;
import com.kong.cc.repository.StoreRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalesManagementServiceImpl implements SalesManagementService {

    private final SalesRepository salesRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final ItemRepository itemRepository;

    @Override
    public List<MenuDto> menuList() {
        return this.menuRepository.findAll().stream().map(Menu::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void salesWrite(List<SalesDto> salesList) throws Exception {

        for(SalesDto saleDto : salesList){

            Sales sale = new Sales();
            sale.setSalesAmount(saleDto.getSalesAmount());
            sale.setSalesCount(saleDto.getSalesCount());
            sale.setSalesStatus(saleDto.getSalesStatus());
            sale.setMenu(Menu.builder().menuCode(saleDto.getMenuCode()).build());
            sale.setStoreSa(Store.builder().storeCode(saleDto.getStoreCode()).build());

            salesRepository.save(sale);
        }
    }

    @Override
    public List<SalesMenuDto> salesAnalysis(Integer storeCode
//            , String periodType
            , Integer categoryId) {
        QSales sales = QSales.sales;
        QStore store = QStore.store;
        QShopOrder shopOrder = QShopOrder.shopOrder;
        QMenu menu = QMenu.menu;

        // 오늘 날짜
        LocalDate today = LocalDate.now();

        // 30일 전, 60일 전 계산
        LocalDate thirtyDaysAgo = today.minusDays(30);
        LocalDate sixtyDaysAgo = today.minusDays(60);

        // LocalDate -> java.sql.Date 변환
        java.sql.Date thity = java.sql.Date.valueOf(thirtyDaysAgo);
        java.sql.Date sixty = java.sql.Date.valueOf(sixtyDaysAgo);

        // 전달 데이터 조회 (오늘 ~ 30일 전)
//        List<SalesDto> salesLastMonth = salesRepository.findByDates(thity, sixty);

        // 전전달 데이터 조회 (30일 전 ~ 60일 전)
//        List<SalesDto> salesPreLastMonth = salesRepository.findByDates(sixtyDaysAgo, thirtyDaysAgo);


        // 전월 vs 오늘 데이터 비교
        List<Tuple> menuSalesData = jpaQueryFactory
                .select(
                        menu.menuCode,
                        menu.menuPrice,
                        menu.menuCategory.menuCategoryNum,
                        sales.salesCount
                )
                .from(sales)
                .innerJoin(sales.menu, menu)
                .innerJoin(menu.salesList, sales)
                .where(sales.salesDate.between(thity, sixty)
                        .and(menu.menuCategory.menuCategoryNum.eq(categoryId)))
//                .groupBy(menu.menuCode, menu.menuPrice, menu.menuCategory.menuCategoryNum)
                .fetch();

        System.out.println("menuSalesData1 = " + menuSalesData);


        //수량과 매출 합계 금액(개별)
//        menuSalesData.stream().map(m -> {
//            SalesMenuDto dto = new SalesMenuDto();
//            dto.setMenuName(m.get(menu.menuName));
//            dto.setMenuPrice(m.get(menu.menuPrice));
//            dto.setMenuCategoryNum(m.get(menu.menuCategory.menuCategoryNum));

            //수량 합계(개별)
//            Integer totalCount = m.get(sales.salesCount.sum());
//            dto.setSalesCount(totalCount);

            //매출 합계 금액(개별)
//            dto.setSalesPriceSum(m.get(menu.menuPrice) * (totalCount)); // 매출 합계

            //전월 대비

//
//            return dto;
//        }).collect(Collectors.toList());

        System.out.println("menuSalesData2 = " + menuSalesData);


        //todo Dto에 전달할 때 필요 데이터
        //상품명, 수량(더해야함), 매출합계(금액x수량), 전월대비수( -60~-30 vs -30~today 수량 비교),
        // 전월대비 금액(-60~-30 vs -30~today 금액 비교), 총 주문수량(수량 더하기), 총 합계(매출합계 더하기), 전월 대비(-60~-30 vs -30~today 전체 비교)


        List<SalesMenuDto> SalesMenuList = menuSalesData.stream()
                .map(m -> {
                    SalesMenuDto menuDto = new SalesMenuDto();
                    menuDto.setMenuCode(m.get(menu.menuCode));
                    menuDto.setMenuPrice(m.get(menu.menuPrice));
                    menuDto.setMenuCategoryNum(m.get(menu.menuCategory.menuCategoryNum));
                    menuDto.setSalesCount(m.get(sales.salesCount));
                    return menuDto;
                })
                .collect(Collectors.toList());

        System.out.println("SalesMenuList = " + SalesMenuList);

        return SalesMenuList;


    }

    @Override
    public void salesTemp(SalesListDto salesList) throws Exception {
        for(SalesDto saleDto : salesList.getSalesList()) {
            Date salesDate = saleDto.getSalesDate();
            Integer storeCode = saleDto.getStoreCode();
            Integer menuCode = Integer.valueOf(saleDto.getMenuCode());  // 메뉴 코드 추가

            // salesDate와 storeCode로 해당 데이터를 조회
            List<Sales> findStore = salesRepository.findListBySalesDateAndStoreCode(salesDate, storeCode);

            if (findStore.isEmpty()) {
                // 만약 일치하는 데이터가 없다면 새로운 데이터를 추가
                Sales sale = new Sales();
                sale.setSalesAmount(saleDto.getSalesAmount());
                sale.setSalesCount(saleDto.getSalesCount());
                sale.setSalesStatus(saleDto.getSalesStatus());
                sale.setMenu(Menu.builder().menuCode(saleDto.getMenuCode()).build());
                sale.setStoreSa(Store.builder().storeCode(saleDto.getStoreCode()).build());

                salesRepository.save(sale); // 새로운 데이터 저장
            } else {
                // 일치하는 데이터가 있으면 해당 데이터를 업데이트
                boolean updated = false;  // 업데이트 여부를 체크하는 플래그
                for (Sales sale : findStore) {
                    if (sale.getMenu().getMenuCode().equals(menuCode)) {
                        // menuCode가 일치하는 데이터만 업데이트
                        sale.setSalesAmount(saleDto.getSalesAmount());
                        sale.setSalesCount(saleDto.getSalesCount());
                        sale.setSalesStatus(saleDto.getSalesStatus());
                        sale.setMenu(Menu.builder().menuCode(saleDto.getMenuCode()).build());

                        // 업데이트된 데이터 저장
                        salesRepository.save(sale);
                        updated = true;
                        break; // 일치하는 메뉴 코드가 있으면 더 이상 탐색하지 않음
                    }
                }

                // 일치하는 menuCode가 없으면 새로 추가
                if (!updated) {
                    Sales sale = new Sales();
                    sale.setSalesAmount(saleDto.getSalesAmount());
                    sale.setSalesCount(saleDto.getSalesCount());
                    sale.setSalesStatus(saleDto.getSalesStatus());
                    sale.setMenu(Menu.builder().menuCode(saleDto.getMenuCode()).build());
                    sale.setStoreSa(Store.builder().storeCode(saleDto.getStoreCode()).build());

                    salesRepository.save(sale); // 새로운 데이터 저장
                }
            }
        }
    }
    }
