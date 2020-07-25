package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.invoice.vo.PickExcel;
import com.hongte.alms.base.mapper.PickStoreDatMapper;
import com.hongte.alms.base.service.PickStoreDatService;
import com.hongte.alms.base.service.ProductDatService;
import com.hongte.alms.base.service.SalaryDatService;
import com.hongte.alms.base.service.SellDatService;
import com.hongte.alms.base.vo.cams.RestProductVo;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.CamsUtil;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.omg.CORBA.portable.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 领料入库表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2019-07-22
 */
@Service("PickStoreDatService")
public class PickStoreDatServiceImpl extends BaseServiceImpl<PickStoreDatMapper, PickStoreDat>
        implements PickStoreDatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PickStoreDatServiceImpl.class);

    static final String PICK_TYPE = "1"; //领料
    static final String STORE_TYPE = "2"; //入库

    @Autowired
    @Qualifier("PickStoreDatService")
    PickStoreDatService pickStoreDatService;

    @Autowired
    @Qualifier("SellDatService")
    SellDatService sellDatService;

    @Autowired
    @Qualifier("ProductDatService")
    private ProductDatService productDatService;


    @Autowired
    @Qualifier("SalaryDatService")
    private SalaryDatService salaryDatService;


    @Override
    public void importPick(MultipartFile file, String companyName, String type, String openDate, CamsSubject subject) throws Exception {
        String fileName = file.getName();
        ImportParams importParams = new ImportParams();
        List<PickExcel> excels = ExcelImportUtil.importExcel(file.getInputStream(), PickExcel.class, importParams);
        String documentNo = "";
        /*获取单据号 */
        Wrapper<PickStoreDat> wrapperPickStoreDat = new EntityWrapper<PickStoreDat>();
        wrapperPickStoreDat.eq("company_name", companyName);
        wrapperPickStoreDat.eq("open_date", openDate);
        wrapperPickStoreDat.eq("pick_store_type", type);
        wrapperPickStoreDat.orderBy("document_no", false);
        List<PickStoreDat> pickStoreDats = selectList(wrapperPickStoreDat);
        if (pickStoreDats.size() == 0) {
            documentNo = "0001";
        } else {
            documentNo = CamsUtil.generateCode(pickStoreDats.get(0).getDocumentNo());
        }


        for (int i = 0; i < excels.size(); i++) {
            PickStoreDat dat = new PickStoreDat();//借
            dat.setKuaiJiNianDu(DateUtil.getYear(new Date()) + "");
            dat.setAccountPeriod(DateUtil.getMonth(DateUtil.getDate(openDate)) + "");
            dat.setDocumentNo(documentNo); //单据号
            dat.setCustomerCode("");
            dat.setCompanyName(companyName);
            dat.setProduceDate(openDate);
            dat.setZhiDanRen("系统主管");
            dat.setHuiLv("1");
            dat.setBiZhong("RMB");
            dat.setDueDate(openDate);
            dat.setDaoQiRiQi(openDate);
            dat.setOpenDate(openDate);
            dat.setFaPiaoLeiXing("0");
            dat.setKaiPiaoBiaoZhi("0");
            dat.setRowNumber(i + 1 + "");
            dat.setProduceCode(excels.get(i).getProductCode());
            dat.setHuoWei("01");
            dat.setCalUnit(excels.get(i).getUnit().trim());
            dat.setNumber(excels.get(i).getShuLiang().trim());
            dat.setUnitPrice(excels.get(i).getDanJia().trim());
            dat.setKouLv("100");
            dat.setOriginalAmount(excels.get(i).getJine().trim()); //原币金额
            dat.setLocalAmount(excels.get(i).getJine().trim()); //本币金额
            dat.setOriginalTax("0");
            dat.setLocalhostTax("0");
            dat.setBaoZhiQi("0");
            dat.setChengBenChaYi("0");
            dat.setChengBenJinE(excels.get(i).getJine().trim());
            dat.setGuanBiBiaoZhi("0");
            dat.setDaiDaYinBiaoZhi("0");
            dat.setDaiShiXianXiaoXiangShui("0");
            dat.setPingJunChengBenJine(excels.get(i).getJine().trim());
            dat.setZengPinBiaoZhi("0");
            dat.setCreateTime(new Date());
            dat.setMoBan(subject.getTemp());
            dat.setKeMu(subject.getId());
            dat.setChuRuLeiBie(subject.getType());
            dat.setPickStoreType(Integer.valueOf(type));
            if (type.equals("1")) {
                dat.setYeWuLeiXing("19");
                dat.setDanJuLeiXing("21");

            } else {
                dat.setYeWuLeiXing("8");
                dat.setDanJuLeiXing("9");
            }
            pickStoreDatService.insert(dat);
        }
        //根据当月销售单插入入库单
        String month = DateUtil.formatDate("yyyy-MM", DateUtil.getDate(openDate));
        List<SellDat> sellDats = sellDatService.selectList(new EntityWrapper<SellDat>().eq("company_code", companyName).like("open_date", month));
        insertStroe(sellDats, companyName, openDate);

    }


    @Override
    public void addPick(String companyName, String openDate, PickStoreDat pickStoreDat) throws Exception {
        String percent = pickStoreDat.getPencent();
        String month = DateUtil.formatDate("yyyy-MM", DateUtil.getDate(openDate));
      //筛选出当月销售产品的销售单数据
        List<SellDat> sellDats = sellDatService.selectList(new EntityWrapper<SellDat>().eq("company_code", companyName).like("open_date", month));
        Iterator it=sellDats.iterator();
        while(it.hasNext()){
            SellDat dat= (SellDat) it.next();
            ProductDat product = productDatService.selectOne(new EntityWrapper<ProductDat>().eq("company_name", companyName).eq("product_code", dat.getProduceCode()));
            if (null != product && product.getProductProperties().equals("产品")) {
            }else{
                it.remove();
            }
        }
        if (sellDats == null || sellDats.size() == 0) {
            throw new Exception("该日期没有找到该开票日期的产品销售单数据");
        }

        BigDecimal sum = BigDecimal.ZERO;
        //计算当月销售单总和
        for (SellDat sellDat : sellDats) {
            sum = sum.add(new BigDecimal(sellDat.getLocalAmount() == null ? "0" : sellDat.getLocalAmount()));
        }
;

        //按照百分比计算原料成本金额
        BigDecimal chengBen = sum.multiply(new BigDecimal(percent)).setScale(2, BigDecimal.ROUND_HALF_UP);
        //自动获取当前开票日期的库存
        List<RestProductVo> kuCunDats= generatePcik(companyName,openDate,pickStoreDat);
        //计算当月库存材料总额
        BigDecimal kuCunSum=BigDecimal.ZERO;
        for (RestProductVo vo : kuCunDats) {
            kuCunSum = kuCunSum.add(new BigDecimal(vo.getRestJieCunJine() == null ? "0" : vo.getRestJieCunJine()));
        }
        List<RestProductVo> insertPickDats = new ArrayList();
        //人工费用和辅料费用总和
        BigDecimal salarySum=salaryDatService.getChengBenJine(openDate,companyName);


        //按产品分组
        Map<String, List<RestProductVo>> sellMap = kuCunDats.stream().collect(Collectors.groupingBy(RestProductVo::getProductCode));
        for (String productCode : sellMap.keySet()) {
            List<RestProductVo> list = sellMap.get(productCode);
            if (list.size() > 0) {
                RestProductVo temp = list.get(0);
                //根据比例把人工费用和辅料费用分摊到每个领料单

                //计算成本数量，单价，金额
                BigDecimal localPercent = new BigDecimal(temp.getRestJieCunJine()).divide(kuCunSum, 4, BigDecimal.ROUND_HALF_UP);
                BigDecimal localAmount = chengBen.multiply(localPercent);
                //人工成本
                BigDecimal salary=   salarySum.multiply(localPercent).setScale(4, RoundingMode.HALF_UP);
                System.out.println("成本金额：" + chengBen.toString() + ",计算百分比:" + localPercent.toString() + ",产品编码:" + temp.getProductCode() + ",人工成本" + salary+",金额:"+localAmount);
                //计算出要消耗库存的数量
                BigDecimal chengbenNum = salary.add(localAmount).divide(new BigDecimal(temp.getQiMoDanJia()), 2, BigDecimal.ROUND_HALF_UP);
                temp.setUnitPrice(new BigDecimal(temp.getQiMoDanJia()));
                temp.setPercent(localPercent);
                temp.setLocalAmount(localAmount);
                temp.setChengBenNum(chengbenNum);
                insertPickDats.add(temp);
            }
        }


        String documentNo = "";
        /*获取单据号 */
        Wrapper<PickStoreDat> wrapperPickStoreDat = new EntityWrapper<PickStoreDat>();
        wrapperPickStoreDat.eq("company_name", companyName);
        wrapperPickStoreDat.eq("open_date", openDate);
        wrapperPickStoreDat.eq("pick_store_type", PICK_TYPE);
        wrapperPickStoreDat.orderBy("document_no", false);
        List<PickStoreDat> pickStoreDats = selectList(wrapperPickStoreDat);
        if (pickStoreDats.size() == 0) {
            documentNo = "0001";
        } else {
            documentNo = CamsUtil.generateCode(pickStoreDats.get(0).getDocumentNo());
        }


        //插入领料单
        for (RestProductVo insertdat  : insertPickDats) {

            Integer i = 1;
            PickStoreDat dat = new PickStoreDat();
            dat.setKuaiJiNianDu(DateUtil.getYear(new Date()) + "");
            dat.setAccountPeriod(DateUtil.getMonth(DateUtil.getDate(openDate)) + "");
            dat.setDocumentNo(documentNo); //单据号
            dat.setCustomerCode("");
            dat.setCompanyName(companyName);
            dat.setProduceDate(openDate);
            dat.setZhiDanRen("系统主管");
            dat.setHuiLv("1");
            dat.setBiZhong("RMB");
            dat.setDueDate(openDate);
            dat.setDaoQiRiQi(openDate);
            dat.setOpenDate(openDate);
            dat.setFaPiaoLeiXing("0");
            dat.setKaiPiaoBiaoZhi("0");
            dat.setRowNumber((++i).toString());
            dat.setProduceCode(insertdat.getProductCode());
            dat.setHuoWei("01");
            dat.setCalUnit(insertdat.getUnit());
            dat.setNumber(insertdat.getChengBenNum().toString());
            dat.setUnitPrice(insertdat.getUnitPrice().toString());
            dat.setKouLv("100");
            dat.setOriginalAmount(insertdat.getLocalAmount().toString()); //原币金额
            dat.setLocalAmount(insertdat.getLocalAmount().toString()); //本币金额
            dat.setOriginalTax("0");
            dat.setLocalhostTax("0");
            dat.setBaoZhiQi("0");
            dat.setChengBenChaYi("0");
            dat.setChengBenJinE(insertdat.getLocalAmount().toString());
            dat.setGuanBiBiaoZhi("0");
            dat.setDaiDaYinBiaoZhi("0");
            dat.setDaiShiXianXiaoXiangShui("0");
            dat.setPingJunChengBenJine(insertdat.getLocalAmount().toString());
            dat.setZengPinBiaoZhi("0");
            dat.setCreateTime(new Date());
            dat.setMoBan("领料单");
            dat.setKeMu("4101-01"); //基本生产成本科目
            dat.setChuRuLeiBie("1001");
            dat.setPickStoreType(Integer.valueOf(PICK_TYPE));
            dat.setYeWuLeiXing("19");
            dat.setDanJuLeiXing("21");

            pickStoreDatService.insert(dat);
        }
        insertStroe(sellDats, companyName,openDate);
    }

    //根据销售单生成入库单
    private void insertStroe(List<SellDat> sellDats,String companyName,String openDate){
        String documentNo="";
        /*获取单据号 */
        Wrapper<PickStoreDat> wrapperPickStoreDat2 = new EntityWrapper<PickStoreDat>();
        wrapperPickStoreDat2.eq("company_name", companyName);
        wrapperPickStoreDat2.eq("open_date", openDate);
        wrapperPickStoreDat2.eq("pick_store_type", STORE_TYPE);
        wrapperPickStoreDat2.orderBy("document_no", false);
        List<PickStoreDat> pickStoreDats2 = selectList(wrapperPickStoreDat2);
        if (pickStoreDats2.size() == 0) {
            documentNo = "0001";
        } else {
            documentNo = CamsUtil.generateCode(pickStoreDats2.get(0).getDocumentNo());
        }


        //销售单即是入库单，插入入库单
        for (SellDat insertdat : sellDats) {
            ProductDat product = productDatService.selectOne(new EntityWrapper<ProductDat>().eq("company_name", companyName).eq("product_code", insertdat.getProduceCode()));
            if (null != product && product.getProductProperties().equals("产品")) {
                Integer i = 1;
                PickStoreDat dat = new PickStoreDat();
                dat.setKuaiJiNianDu(DateUtil.getYear(new Date()) + "");
                dat.setAccountPeriod(DateUtil.getMonth(DateUtil.getDate(openDate)) + "");
                dat.setDocumentNo(documentNo); //单据号
                dat.setCustomerCode("");
                dat.setCompanyName(companyName);
                dat.setProduceDate(openDate);
                dat.setZhiDanRen("系统主管");
                dat.setHuiLv("1");
                dat.setBiZhong("RMB");
                dat.setDueDate(openDate);
                dat.setDaoQiRiQi(openDate);
                dat.setOpenDate(openDate);
                dat.setFaPiaoLeiXing("0");
                dat.setKaiPiaoBiaoZhi("0");
                dat.setRowNumber((++i).toString());
                dat.setProduceCode(insertdat.getProduceCode());
                dat.setHuoWei("01");
                dat.setCalUnit(insertdat.getCalUnit());
                dat.setNumber(insertdat.getNumber());
                dat.setUnitPrice(insertdat.getUnitPrice());
                dat.setKouLv("100");
                dat.setOriginalAmount(insertdat.getLocalAmount()); //原币金额
                dat.setLocalAmount(insertdat.getLocalAmount()); //本币金额
                dat.setOriginalTax("0");
                dat.setLocalhostTax("0");
                dat.setBaoZhiQi("0");
                dat.setChengBenChaYi("0");
                dat.setChengBenJinE(insertdat.getLocalAmount());
                dat.setGuanBiBiaoZhi("0");
                dat.setDaiDaYinBiaoZhi("0");
                dat.setDaiShiXianXiaoXiangShui("0");
                dat.setPingJunChengBenJine(insertdat.getLocalAmount());
                dat.setZengPinBiaoZhi("0");
                dat.setCreateTime(new Date());
                dat.setMoBan("产成品入库单");
                dat.setKeMu("1237");
                dat.setChuRuLeiBie("1010");
                dat.setPickStoreType(Integer.valueOf(STORE_TYPE));
                dat.setYeWuLeiXing("8");
                dat.setDanJuLeiXing("9");
                pickStoreDatService.insert(dat);
            } else {
               continue;
            }

        }
    }

    @Override
    public List<RestProductVo> generatePcik(String companyName, String openDate, PickStoreDat dat) throws Exception {
        Date endDate = DateUtil.getMoneEndDate(DateUtil.getDate(openDate));
        Date beginDate = DateUtil.getYearFirst(DateUtil.getYear(endDate));
        RestProductVo vo = new RestProductVo();
        vo.setLimit(1000);
        vo.setPage(1);
        vo.setBeginDate(beginDate);
        vo.setEndDate(endDate);
        vo.setCompanyName(companyName);
        Page<RestProductVo> page = productDatService.inventoryPage(vo);
        List<RestProductVo> list = page.getRecords();
        //筛选出库存量大于0的记录
        Iterator it = list.iterator();
        while (it.hasNext()) {
            RestProductVo restProductVo = (RestProductVo) it.next();
            if (!StringUtil.isEmpty(restProductVo.getRestKuCunLiang()) && Double.valueOf(restProductVo.getRestKuCunLiang()) < 0) {
                it.remove();
                continue;
            }
            if(!StringUtil.isEmpty(restProductVo.getRestKuCunLiang())){
                ProductDat product = productDatService.selectOne(new EntityWrapper<ProductDat>().eq("company_name", companyName).eq("product_code", restProductVo.getProductCode()));
                if (null != product && product.getProductProperties().equals("材料")) {

                } else {
                    it.remove();
                }
            }

        }
        return list;
    }

    @Override
    public void secondPick( List<PickStoreDat> dats) throws Exception {
       List<String> productCodes=new ArrayList<>();
       for(PickStoreDat dat:dats){
           String[] array=dat.getBindProductCode().split(",");
           Collections.addAll(productCodes,array);
       }
        String openDate="";
        String wholeOpenDate=dats.get(0).getOpenDate();
        String companyName=dats.get(0).getCompanyName();
       //获取当月产成品的总金额
       BigDecimal sellAmountSum=BigDecimal.ZERO;
       for(String productCode:productCodes){
           openDate=dats.get(0).getOpenDate();
           openDate=DateUtil.formatDate("yyyy-MM",DateUtil.getDate(openDate));
           SellDat dat=sellDatService.selectOne(new EntityWrapper<SellDat>().eq("company_code",dats.get(0).getCompanyName()).like("open_date",openDate).eq("produce_code",productCode));
           if(dat==null){
               throw new Exception("该开票日期找不到产品:"+productCode);
           }
           sellAmountSum=sellAmountSum.add(new BigDecimal(dat.getLocalAmount()));

       }

       //计算每个领料单对应的产成品销售额占总销售额的百分比
        for(PickStoreDat dat:dats){
            BigDecimal amountSum=BigDecimal.ZERO;
            String[] array=dat.getBindProductCode().split(",");
            for(int i=0;i<array.length;i++){
                SellDat sellDat=sellDatService.selectOne(new EntityWrapper<SellDat>().eq("company_code",dats.get(0).getCompanyName()).like("open_date",openDate).eq("produce_code",array[i]));
                amountSum=amountSum.add(new BigDecimal(sellDat.getLocalAmount()));
            }
            BigDecimal percent=amountSum.divide(sellAmountSum,4, BigDecimal.ROUND_HALF_UP);
            dat.setPencent(percent.toString());
        }
        //人工费用和辅料费用总和
        BigDecimal salarySum=salaryDatService.getChengBenJine(wholeOpenDate,companyName);
        if(salarySum.doubleValue()==0){
            return;
        }
        //每个领料单格局percent计算出人工费用辅助成本
        for(PickStoreDat dat:dats){
            //人工成本
            BigDecimal salary= salarySum.multiply(new BigDecimal(dat.getPencent())).setScale(4, RoundingMode.HALF_UP);
            BigDecimal localAmount=new BigDecimal(dat.getLocalAmount()).add(salary);
            BigDecimal unitPrice=BigDecimal.ZERO;
            unitPrice=localAmount.divide(new BigDecimal(dat.getNumber()),2, BigDecimal.ROUND_HALF_UP);
            PickStoreDat updateDat=selectById(dat.getPickStoreId());
            updateDat.setLocalAmount(localAmount.toString());
            updateDat.setOriginalAmount(localAmount.toString());
            updateDat.setUnitPrice(unitPrice.toString());
            updateById(updateDat);
        }
    }

}


































































