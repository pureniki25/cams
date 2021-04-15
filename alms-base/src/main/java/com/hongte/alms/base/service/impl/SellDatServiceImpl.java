package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.CamsConstant;
import com.hongte.alms.base.enums.SubjectEnum;
import com.hongte.alms.base.mapper.SellDatMapper;
import com.hongte.alms.base.service.*;
import com.hongte.alms.base.vo.cams.RestProductVo;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.CamsUtil;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 销售单表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2019-02-07
 */
@Service("SellDatService")
public class SellDatServiceImpl extends BaseServiceImpl<SellDatMapper, SellDat> implements SellDatService {
    @Autowired
    @Qualifier("CamsProductPropertiesService")
    private CamsProductPropertiesService camsProductPropertiesService;

    @Autowired
    @Qualifier("ProductDatService")
    private ProductDatService productDatService;

    @Autowired
    @Qualifier("CamsCompanyService")
    private CamsCompanyService camsCompanyService;

    @Autowired
    @Qualifier("JtDatService")
    private JtDatService jtDatService;

    @Autowired
    @Qualifier("BuyDatService")
    private BuyDatService buyDatService;

    @Override
    public SellDat addSellDat(String productPropertiesName, String companyName, String productCode, String customerCode, String accountPeriod,
                              String produceDate, String invoiceNumber, String rowNumber, String calUnit, String number, String unitPrice,
                              String hanShuiJine, String buHanShuiJine, String taxRate, String originalTax, String localhostTax, String importType, Integer idx) {
        CamsProductProperties properties = camsProductPropertiesService.selectOne(new EntityWrapper<CamsProductProperties>().eq("product_properties_name", productPropertiesName));
        if (!StringUtil.isEmpty(number)) {//保留2位小数
            DecimalFormat df = new DecimalFormat("#0.00");
            number = df.format(Double.valueOf(number)).toString();
        } else {
            number = "";
        }

        Date date = DateUtil.getDate(produceDate);
        produceDate = DateUtil.formatDate(date);
        int month = DateUtil.getMonth(DateUtil.getDate(produceDate));
        String documentNo = "";
        /*获取单据号 */
        Wrapper<SellDat> wrapperProductDat = new EntityWrapper<SellDat>();
        wrapperProductDat.eq("company_code", companyName);
        wrapperProductDat.and(" month(produce_date)=" + month);
        wrapperProductDat.orderBy("document_no", false);
        List<SellDat> sellDats = selectList(wrapperProductDat);
        if (sellDats.size() == 0) {
            documentNo = "0001";
        } else {
            SellDat dat = selectOne(new EntityWrapper<SellDat>().eq("company_code", companyName).eq("invoice_number", invoiceNumber));
            if (dat != null) {
                documentNo = dat.getDocumentNo();
            } else {
                documentNo = CamsUtil.generateCode(sellDats.get(0).getDocumentNo());
            }
        }

        /*获取行号 */
        Wrapper<SellDat> wrapperProductDat2 = new EntityWrapper<SellDat>();
        wrapperProductDat2.eq("company_code", companyName);
        wrapperProductDat2.eq("invoice_number", invoiceNumber);
        wrapperProductDat2.orderBy("row_number", false);
        List<SellDat> sellDats2 = selectList(wrapperProductDat2);
        Collections.sort(sellDats2, new Comparator<SellDat>() {
            @Override
            public int compare(SellDat o1, SellDat o2) {
                return Integer.valueOf(o2.getRowNumber()) - (Integer.valueOf(o1.getRowNumber()));
            }
        });

        if (sellDats2.size() == 0) {
            rowNumber = "1";
        } else {
            rowNumber = CamsUtil.generateCode(sellDats2.get(0).getRowNumber());
        }


        SellDat sellDat = new SellDat();
        sellDat.setDocumentNo(documentNo);
        sellDat.setidx(idx);
        sellDat.setCompanyCode(companyName);
        sellDat.setProduceCode(productCode);
        sellDat.setCustomerCode(customerCode);
        sellDat.setAccountPeriod(accountPeriod);
        sellDat.setProduceDate(produceDate);
        sellDat.setDueDate(produceDate);
        sellDat.setOpenDate(produceDate);
        sellDat.setInvoiceNumber(invoiceNumber);
        sellDat.setRowNumber(rowNumber);
        sellDat.setCalUnit(calUnit);
        sellDat.setNumber(number);
        sellDat.setUnitPrice(unitPrice);
        sellDat.setOriginalAmount(buHanShuiJine);
        sellDat.setLocalAmount(buHanShuiJine);
        sellDat.setTaxRate(CamsUtil.getTax(taxRate));
        sellDat.setOriginalTax(originalTax);
        sellDat.setLocalhostTax(localhostTax);
        sellDat.setDanJuLeiXing("13");
        sellDat.setYeWuLeiXing("11");
        sellDat.setKuaiJiNianDu(String.valueOf(DateUtil.getYear(new Date())));
        sellDat.setCreateTime(new Date());
        sellDat.setMoBan("销售商品清单");
        sellDat.setSellType(productPropertiesName);
        sellDat.setImportType(importType);

        insertOrUpdate(sellDat);

         // 贸易型公司结转销售成本
       // if(isMaoYiXing(companyName)){
//            generateXiaoShouChengben(sellDat);
//        }
        return sellDat;
    }

    public int countLowercaseLetters(String string) {
        return (int) string.chars().filter(Character::isLowerCase).count();
    }

    /**
     * 判断是否贸易型
     * @return
     */
    public Boolean isMaoYiXing(String companyName){
        CamsCompany camsCompany=camsCompanyService.selectOne(new EntityWrapper<CamsCompany>().eq("company_name",companyName).eq("is_del",0));
        if(camsCompany!=null&&camsCompany.getCompanyType().equals(CamsConstant.CompanyTypeEnum.MAO_YI_XING.getValue())){
             return true;
        }
        return  false;
    }

    // 贸易型公司结转销售成本
    private void generateXiaoShouChengben(SellDat sellDat) {
        RestProductVo vo = new RestProductVo();
        vo.setCompanyName(sellDat.getCompanyCode());
//        vo.setBeginDate(DateUtil.getDate("2020-01-01"));
//        vo.setEndDate(DateUtil.getDate("2021-01-31"));
        vo.setProductCode(sellDat.getProduceCode());
        Page<RestProductVo> page = productDatService.inventoryPage(vo);
        List<RestProductVo> list = page.getRecords();
        if(CollectionUtils.isNotEmpty(list)){
            Double kuCunLiang=Double.valueOf(list.get(0).getRestKuCunLiang());
            if(kuCunLiang>=0){
                // 结转销售成本
                JtDat dat=new JtDat();
                dat.setZhaiYao("结转本月销售成本");
                dat.setHuoBiDaiMa("RMB");
                dat.setHuiLv("1");
                dat.setShuLiang(sellDat.getNumber());
                dat.setDanJia(sellDat.getUnitPrice());
                dat.setLocalAmount(sellDat.getLocalAmount());
                dat.setPingZhengRiQi(sellDat.getOpenDate());
                dat.setPingZhengLaiYuan("1");
                dat.setPingZhengZi("记");
                dat.setZhiDanRen("系统主管");
                dat.setShenHeRen("");
                dat.setGuoZhangRen("");
                dat.setFuDanJuShu("0");
                dat.setShiFouYiGuoZhang("0");
                dat.setMoBan("11记账凭证");
                dat.setCustomerCode(sellDat.getCustomerCode());
                dat.setBuMen("");
                dat.setYuanGong("");
                dat.setTongJi("");
                dat.setXiangMu("");
                dat.setFuKuanFangFa("");
                dat.setPiaoJuHao("");
                dat.setYuanBiFuKuanJinE("0");
                dat.setPingZhengLaiYuan("1");
                dat.setLaiYuanPingZheng(",,,");
                dat.setDaiDaYin("");
                dat.setZuoFeiBiaoZhi("0");
                dat.setCuoWuBiaoZhi("0");
                dat.setPingZhengCeHao("00");
                dat.setChuNaRen("");
                dat.setCompanyName(sellDat.getCompanyCode());
                dat.setDeductionType("1");
                dat.setInvoiceNumber(sellDat.getInvoiceNumber());
                dat.setQiJian(String.valueOf(DateUtil.getMonth(DateUtil.getDate(sellDat.getOpenDate()))));
                dat.setCreateTime(new Date());
                jtDatService.saveCommonDat(dat,dat.getPingZhengRiQi(),dat.getCompanyName(), SubjectEnum.JIE_ZHUAN_XIAO_SHOU_CHENG_BEN_5401_01.getValue().toString(),SubjectEnum.KU_CUN_SHANG_PIN_1234.getValue().toString());
            }
//            else{
//                // 生成咱估单
//                BuyDat buyDat=new BuyDat();
//                BeanUtils.copyProperties(sellDat,buyDat);
//                buyDat.setBuyType(CamsConstant.InvoiceTypeEnum.PU_PIAO.getDesc().toString());
//                buyDat.setTaxRate("0");
//                buyDat.setLocalhostTax("0");
//                buyDat.setOriginalTax("0");
//                buyDat.setMoBan("暂估单");
//                buyDat.setIsZanGu("Y");
//                buyDatService.insert(buyDat);
//            }
		}else{
            // 生成咱估单
            BuyDat buyDat=new BuyDat();
            BeanUtils.copyProperties(sellDat,buyDat);
            buyDat.setBuyType(CamsConstant.InvoiceTypeEnum.PU_PIAO.getDesc().toString());
            buyDat.setTaxRate("0");
            buyDat.setLocalhostTax("0");
            buyDat.setDeductionType("1");
            buyDat.setMoBan("暂估单");
            buyDat.setOriginalTax("0");
            buyDat.setIsZanGu("Y");
            buyDatService.insert(buyDat);
        }
    }

    public static void main(String[] args) {
        System.out.println(String.valueOf(DateUtil.getYear(new Date())));
    }
}
