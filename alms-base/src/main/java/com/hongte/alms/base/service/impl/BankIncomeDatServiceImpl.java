package com.hongte.alms.base.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.hongte.alms.base.enums.CamsConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hongte.alms.base.entity.BankIncomeDat;
import com.hongte.alms.base.entity.CustomerDat;
import com.hongte.alms.base.entity.JtDat;
import com.hongte.alms.base.entity.SalaryDat;
import com.hongte.alms.base.enums.SubjectEnum;
import com.hongte.alms.base.invoice.vo.InvoiceBankExel;
import com.hongte.alms.base.mapper.BankIncomeDatMapper;
import com.hongte.alms.base.service.BankIncomeDatService;
import com.hongte.alms.base.service.CustomerDatService;
import com.hongte.alms.base.service.JtDatService;
import com.hongte.alms.base.service.SalaryDatService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.CamsUtil;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;

/**
 * <p>
 * 银收 服务实现类
 * </p>
 *
 * @author czs
 * @since 2019-03-11
 */
@Service("BankIncomeDatService")
public class BankIncomeDatServiceImpl extends BaseServiceImpl<BankIncomeDatMapper, BankIncomeDat>
        implements BankIncomeDatService {

    @Autowired
    @Qualifier("CustomerDatService")
    CustomerDatService customerDatService;

    @Autowired
    @Qualifier("SalaryDatService")
    SalaryDatService salaryDatService;


    @Autowired
    @Qualifier("JtDatService")
    JtDatService jtDatService;

    @Override
    public void addIncomeDat(String riQi, String keMuDaiMa, String bankSubject, String bianMa, String hangHao, String pingZhengHao,
                             String bankType, String zhaiYao, String companyName, CustomerDat customerDat, String shuLiang,
                             String productDate, String faPiaoHao, String buyType, String hanShuiJine, String almsAmount,
                             String shuie, String danJia, boolean isKouChu, String uuid) throws InstantiationException, IllegalAccessException {

        String customerCode = "";
        if (customerDat != null) {
            customerCode = customerDat.getCustomerCode();
        }
        // if(!StringUtil.isEmpty(shuLiang)) {//保留2位小数
        // DecimalFormat df = new DecimalFormat("#0.00");
        // shuLiang=df.format(Double.valueOf(shuLiang)).toString();
        // }
        int month = DateUtil.getMonth(DateUtil.getDate(productDate));
        // Wrapper<BankIncomeDat> incomeDat = new EntityWrapper<BankIncomeDat>();
        // incomeDat.eq("company_name", companyName).eq("zhai_yao", zhaiYao);
        //
        // List<BankIncomeDat> incomeDats = selectList(incomeDat);
        // if (incomeDats.size() > 0) {
        // return;
        // }

        // 获取行号
        // 如
        // 果包含1002-01，1002-02 ····· 等支付科目，摘要前面不用加日期


            BankIncomeDat keMuDaiMaTmp = selectOne(new EntityWrapper<BankIncomeDat>().eq("company_name", companyName)
                .eq("zhai_yao", zhaiYao).eq("deduction_type", 1));
        BankIncomeDat incomeDat = new BankIncomeDat();
        incomeDat.setPingZhengRiQi(productDate);

        incomeDat.setPingZhengZi("记");
        incomeDat.setPingZhengHao(pingZhengHao);
        if(!zhaiYao.contains("收到客户款项")&&!zhaiYao.contains("支付供应商款项")){
            zhaiYao = riQi + " " + zhaiYao;
            incomeDat.setDuiZhangRiQi(productDate);
        }else{
            incomeDat.setDuiZhangRiQi(riQi);
        }
        incomeDat.setZhaiYao(zhaiYao);
        if (keMuDaiMaTmp != null && (!keMuDaiMaTmp.equals(""))) {
            incomeDat.setKeMuDaiMa(keMuDaiMaTmp.getKeMuDaiMa());
        }
        incomeDat.setHuoBiDaiMa("RMB");
        incomeDat.setHuiLv("1");
        incomeDat.setShuLiang(shuLiang);
        incomeDat.setDanJia(danJia);
        incomeDat.setZhiDanRen("系统主管");
        incomeDat.setShenHeRen("");
        incomeDat.setGuoZhangRen("");
        incomeDat.setFuDanJuShu("0");
        incomeDat.setShiFouYiGuoZhang("0");
        incomeDat.setMoBan("11记账凭证");
        incomeDat.setCustomerCode(customerCode);
        incomeDat.setBuMen("");
        incomeDat.setYuanGong("");
        incomeDat.setTongJi("");
        incomeDat.setXiangMu("");
        incomeDat.setFuKuanFangFa("");
        incomeDat.setPiaoJuHao("");
        incomeDat.setYuanBiFuKuanJinE("0");
        incomeDat.setPingZhengLaiYuan("1");
        incomeDat.setLaiYuanPingZheng(",,,");
        incomeDat.setDaiDaYin("");
        incomeDat.setZuoFeiBiaoZhi("0");
        incomeDat.setCuoWuBiaoZhi("0");
        incomeDat.setPingZhengCeHao("00");
        incomeDat.setChuNaRen("");
        incomeDat.setCompanyName(companyName);
        incomeDat.setDeductionType("1");
        incomeDat.setCustomerCode(customerCode);
        incomeDat.setInvoiceNumber(faPiaoHao);
        incomeDat.setQiJian(String.valueOf(month));
        incomeDat.setCreateTime(new Date());

        if (bianMa != null && bianMa.equals("单位")) {
            incomeDat.setUuid(uuid);
            if (keMuDaiMa.equals(bankSubject)) {
                incomeDat.setDeductionType("0");// 不显示
                incomeDat.setKeMuDaiMa(bankSubject);
                incomeDat.setLocalAmount(hanShuiJine);
                incomeDat.setBorrowAmount("0.00");
                incomeDat.setAlmsAmount(hanShuiJine);
                if (bankType.equals("1")) {
                    incomeDat.setLocalAmount(hanShuiJine);
                    incomeDat.setBorrowAmount(hanShuiJine);
                    incomeDat.setAlmsAmount("0.00");
                } else {
                    incomeDat.setLocalAmount(hanShuiJine);
                    incomeDat.setBorrowAmount("0.00");
                    incomeDat.setAlmsAmount(hanShuiJine);
                }
            } else {
                if (bankType.equals("1")) {

                    incomeDat.setLocalAmount(hanShuiJine);
                    incomeDat.setBorrowAmount("0.00");
                    incomeDat.setAlmsAmount(hanShuiJine);
                } else {

                    incomeDat.setLocalAmount(hanShuiJine);
                    incomeDat.setBorrowAmount(hanShuiJine);
                    incomeDat.setAlmsAmount("0.00");
                }
                if (bankType.equals("1")) {
                    incomeDat.setKeMuDaiMa(SubjectEnum.CLIENT_SUBJECT.getValue().toString());
                    incomeDat.setZhaiYao(zhaiYao);
                } else {
                    incomeDat.setKeMuDaiMa(SubjectEnum.CUSTOMER_SUBJECT.getValue().toString());
                }
                incomeDat.setDeductionType("1");// 显示
                incomeDat.setDanWei(customerDat.getCustomerCode());

            }

            incomeDat.setBankType(bankType);

            incomeDat.setHangHao(hangHao);
            incomeDat.setPingZhengHao(pingZhengHao);
            insertOrUpdate(incomeDat);

        } else if (bianMa != null && bianMa.equals("费用")) {
            pingZhengHao = getPingZhengHao(companyName, month);
            //如果是应付工资(2151)或者是现金支付(1001),先查看有没有已经存在的2151或1001的凭证号，有的话取已经有的凭证号，因为2151和1001的凭证号要保持一致
            if (keMuDaiMa.equals("2151")) {
                BankIncomeDat cashDat = getYhzfDat(companyName, "1001", productDate);
                if (cashDat != null) {
                    pingZhengHao = cashDat.getPingZhengHao();
                }
            }
            if (keMuDaiMa.equals("1001")) {
                BankIncomeDat yingFuDat = getYhzfDat(companyName, "2151", productDate);
                if (yingFuDat != null) {
                    pingZhengHao = yingFuDat.getPingZhengHao();
                }
            }
            incomeDat.setDeductionType("1");// 不能抵扣
            incomeDat.setBankType(bankType);
            incomeDat.setZhaiYao(zhaiYao);
            incomeDat.setKeMuDaiMa(keMuDaiMa);
            if (StringUtil.isEmpty(uuid)) {
                incomeDat.setUuid(UUID.randomUUID().toString());
            } else {
                incomeDat.setUuid(uuid);
            }


            incomeDat.setPingZhengHao(pingZhengHao);

            BankIncomeDat bankDat = ClassCopyUtil.copyObject(incomeDat, BankIncomeDat.class);
            bankDat.setZhaiYao(zhaiYao);
            bankDat.setLocalAmount(hanShuiJine);
            bankDat.setBankType(bankType);
            bankDat.setDeductionType("0");
            bankDat.setKeMuDaiMa(bankSubject);
            bankDat.setPingZhengHao(pingZhengHao);

            if (bankType.equals("1")) {// 收入
                if (keMuDaiMa.equals("5503-01")) {// 如果是利息，银行科目和费用科目位置要调换,并且费用金额是负数
                    bankDat.setHangHao("2");
                    incomeDat.setHangHao("1");

                    incomeDat.setLocalAmount("-" + hanShuiJine);
                    incomeDat.setBorrowAmount("-" + hanShuiJine);
                    incomeDat.setAlmsAmount("0.00");

                    bankDat.setLocalAmount("-" + hanShuiJine);
                    bankDat.setBorrowAmount("0.00");
                    bankDat.setAlmsAmount("-" + hanShuiJine);
                    insertOrUpdate(incomeDat);
                    insertOrUpdate(bankDat);

                } else {
                    bankDat.setHangHao("1");
                    incomeDat.setHangHao("2");

                    incomeDat.setLocalAmount(hanShuiJine);
                    incomeDat.setBorrowAmount("0.00");
                    incomeDat.setAlmsAmount(hanShuiJine);

                    bankDat.setLocalAmount(hanShuiJine);
                    bankDat.setBorrowAmount(hanShuiJine);
                    bankDat.setAlmsAmount("0.00");

                    insertOrUpdate(bankDat);
                    insertOrUpdate(incomeDat);
                }

            } else {
                //要扣除上月代缴个人社保，个人所得税，公积金
                if (isKouChu) {
                    incomeDat.setHangHao("1");

                    incomeDat.setLocalAmount(hanShuiJine);
                    incomeDat.setBorrowAmount(hanShuiJine);
                    incomeDat.setAlmsAmount("0.00");

                    //获取上月的日期
                    Date lastDate = DateUtil.addMonth2Date(-1, DateUtil.getDate(productDate));
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar cale = Calendar.getInstance();
                    cale.setTime(lastDate);
                    // 获取前月的最后一天
                    cale.add(Calendar.MONTH, 1);
                    cale.set(Calendar.DAY_OF_MONTH, 0);
                    lastDate = cale.getTime();
                    String lastPingZhengRiQi = DateUtil.formatDate(lastDate);


                    BankIncomeDat geRenSuoDeShui = selectOne(new EntityWrapper<BankIncomeDat>().eq("company_name", companyName).eq("ke_mu_dai_ma", "2171-12").eq("ping_zheng_ri_qi", productDate).eq("alms_amount","0"));
                    BankIncomeDat geRenSheBao = selectOne(new EntityWrapper<BankIncomeDat>().eq("company_name", companyName).eq("ke_mu_dai_ma", "1133-01").eq("ping_zheng_ri_qi", lastPingZhengRiQi).eq("alms_amount","0"));
                    BankIncomeDat gongJiJin = selectOne(new EntityWrapper<BankIncomeDat>().eq("company_name", companyName).eq("ke_mu_dai_ma", "1133-03").eq("ping_zheng_ri_qi", lastPingZhengRiQi).eq("alms_amount","0"));
                    int i = 1;
                    BigDecimal restAlmsAmount = new BigDecimal(hanShuiJine);
                    if (geRenSuoDeShui != null) {
                        BankIncomeDat geRenSuoDeShuiDat = ClassCopyUtil.copyObject(incomeDat, BankIncomeDat.class);
                        geRenSuoDeShuiDat.setId(null);
                        geRenSuoDeShuiDat.setZhaiYao("收回代垫个税");
                        geRenSuoDeShuiDat.setBankType(bankType);
                        geRenSuoDeShuiDat.setDeductionType("0");
                        geRenSuoDeShuiDat.setKeMuDaiMa(geRenSuoDeShui.getKeMuDaiMa());
                        geRenSuoDeShuiDat.setPingZhengHao(pingZhengHao);
                        geRenSuoDeShuiDat.setLocalAmount(geRenSuoDeShui.getLocalAmount());
                        geRenSuoDeShuiDat.setBorrowAmount("0.00");
                        geRenSuoDeShuiDat.setAlmsAmount(geRenSuoDeShui.getLocalAmount());
                        geRenSuoDeShuiDat.setHangHao(String.valueOf(i));
                        i++;
                        restAlmsAmount = restAlmsAmount.subtract(new BigDecimal(geRenSuoDeShui.getLocalAmount()));
                        insertOrUpdate(geRenSuoDeShuiDat);
                    }
                    if (geRenSheBao != null) {
                        //查询上月扣的社保对应的工资单里，看看是否没有发工资，但是有交社保的数据，如果有，要单独另外生成凭证
                        List<SalaryDat> salarys = salaryDatService.selectList(new EntityWrapper<SalaryDat>().eq("company_name", companyName)
                                .eq("salary_date", lastPingZhengRiQi));
                        BigDecimal noSalarySheBao = BigDecimal.ZERO;
                        for (SalaryDat salaryDat : salarys) {
                            if (StringUtil.isEmpty(salaryDat.getBenQiShouRu())) {
                                noSalarySheBao = noSalarySheBao.add(new BigDecimal(salaryDat.getGeRenSheBaoSum()));
                            }
                        }
                        BigDecimal geRenSheBaoSum = new BigDecimal(geRenSheBao.getLocalAmount());
                        if (noSalarySheBao.doubleValue() > 0) {
                            BankIncomeDat noSalarySheBaoDat = ClassCopyUtil.copyObject(incomeDat, BankIncomeDat.class);
                            noSalarySheBaoDat.setId(null);
                            noSalarySheBaoDat.setZhaiYao("其他应收款-代垫个人社保");
                            noSalarySheBaoDat.setBankType(bankType);
                            noSalarySheBaoDat.setDeductionType("0");
                            noSalarySheBaoDat.setKeMuDaiMa("2181");
                            noSalarySheBaoDat.setPingZhengHao(pingZhengHao);
                            noSalarySheBaoDat.setLocalAmount(noSalarySheBao.toString());
                            noSalarySheBaoDat.setBorrowAmount("0.00");
                            noSalarySheBaoDat.setAlmsAmount(noSalarySheBao.toString());
                            noSalarySheBaoDat.setHangHao(String.valueOf(i));
                            insertOrUpdate(noSalarySheBaoDat);
                            geRenSheBaoSum = geRenSheBaoSum.subtract(noSalarySheBao); //减去没有交工资的那部分社保
                            i++;
                        }
                        BankIncomeDat geRenSheBaoDat = ClassCopyUtil.copyObject(incomeDat, BankIncomeDat.class);
                        geRenSheBaoDat.setId(null);
                        geRenSheBaoDat.setZhaiYao("收回代垫社保");
                        geRenSheBaoDat.setBankType(bankType);
                        geRenSheBaoDat.setDeductionType("0");
                        geRenSheBaoDat.setKeMuDaiMa(geRenSheBao.getKeMuDaiMa());
                        geRenSheBaoDat.setPingZhengHao(pingZhengHao);
                        geRenSheBaoDat.setLocalAmount(geRenSheBaoSum.toString());
                        geRenSheBaoDat.setBorrowAmount("0.00");
                        geRenSheBaoDat.setAlmsAmount(geRenSheBaoSum.toString());
                        geRenSheBaoDat.setHangHao(String.valueOf(i));
                        restAlmsAmount = restAlmsAmount.subtract(geRenSheBaoSum).subtract(noSalarySheBao);
                        i++;
                        insertOrUpdate(geRenSheBaoDat);
                    }
                    if (gongJiJin != null) {
                        BankIncomeDat gongJiJinDat = ClassCopyUtil.copyObject(incomeDat, BankIncomeDat.class);
                        gongJiJinDat.setId(null);
                        gongJiJinDat.setZhaiYao("收回代垫公积金");
                        gongJiJinDat.setBankType(bankType);
                        gongJiJinDat.setDeductionType("0");
                        gongJiJinDat.setKeMuDaiMa(gongJiJin.getKeMuDaiMa());
                        gongJiJinDat.setPingZhengHao(pingZhengHao);
                        gongJiJinDat.setLocalAmount(gongJiJin.getLocalAmount());
                        gongJiJinDat.setBorrowAmount("0.00");
                        gongJiJinDat.setAlmsAmount(gongJiJin.getLocalAmount());
                        gongJiJinDat.setHangHao(String.valueOf(i));
                        restAlmsAmount = restAlmsAmount.subtract(new BigDecimal(gongJiJin.getLocalAmount()));
                        i++;
                        insertOrUpdate(gongJiJinDat);
                    }
                    //如果是负数，相当于是现金支付的科目，方向是'借'
                    if (restAlmsAmount.doubleValue() < 0) {
                        bankDat.setId(null);
                        bankDat.setLocalAmount(restAlmsAmount.abs().toString());
                        bankDat.setBorrowAmount(restAlmsAmount.abs().toString());
                        bankDat.setAlmsAmount("0.00");
                        bankDat.setHangHao(String.valueOf(i));
                        insertOrUpdate(bankDat);
                    } else {
                        bankDat.setId(null);
                        bankDat.setLocalAmount(restAlmsAmount.toString());
                        bankDat.setBorrowAmount("0.00");
                        bankDat.setAlmsAmount(restAlmsAmount.toString());
                        bankDat.setHangHao(String.valueOf(i));
                        insertOrUpdate(bankDat);
                        if (new BigDecimal(hanShuiJine).compareTo(new BigDecimal(almsAmount)) == 0) {
                            insertOrUpdate(incomeDat);
                        }

                    }


                } else {
                    bankDat.setHangHao("2");
                    incomeDat.setHangHao("1");

                    incomeDat.setLocalAmount(hanShuiJine);
                    incomeDat.setBorrowAmount(hanShuiJine);
                    incomeDat.setAlmsAmount("0.00");

                    bankDat.setLocalAmount(almsAmount);
                    bankDat.setBorrowAmount("0.00");
                    bankDat.setAlmsAmount(almsAmount);

                    insertOrUpdate(incomeDat);
                    insertOrUpdate(bankDat);
                }
            }

        } else if (bianMa != null && (bianMa.equals("税收") || bianMa.equals("社保") || bianMa.equals("公积金"))) {
            if (keMuDaiMa.equals(bankSubject)) {
                ;
                ;
            } else {
                incomeDat.setDeductionType("1");// 显示
                incomeDat.setBankType(bankType);
                incomeDat.setLocalAmount(hanShuiJine);
                incomeDat.setBorrowAmount(hanShuiJine);
                incomeDat.setAlmsAmount("0.00");
                incomeDat.setKeMuDaiMa(keMuDaiMa);
                incomeDat.setHangHao(hangHao);
                incomeDat.setPingZhengHao(pingZhengHao);
                incomeDat.setZhaiYao(zhaiYao);
            }
            insertOrUpdate(incomeDat);
        }

        //重新将银收银付排序
        List<BankIncomeDat> dats = selectList(new EntityWrapper<BankIncomeDat>().eq("company_name", companyName).eq("ping_zheng_ri_qi", productDate).orderBy("bank_type,uuid,dui_zhang_ri_qi", true));
        String pingZhengHaoNew = "000";
        BankIncomeDat temp = null;
        //重新排序凭证号
        for (BankIncomeDat dat : dats) {
            if (temp != null && temp.getUuid().equals(dat.getUuid())) {
                dat.setPingZhengHao(temp.getPingZhengHao());
            } else {
                pingZhengHaoNew = CamsUtil.generateCode(pingZhengHaoNew);
                dat.setPingZhengHao(pingZhengHaoNew);
            }
            temp = dat;
            updateById(dat);

        }
        List<BankIncomeDat> dats2 = selectList(new EntityWrapper<BankIncomeDat>().eq("company_name", companyName).eq("ping_zheng_ri_qi", productDate).notLike("zhai_yao", "利息").orderBy("ping_zheng_hao,alms_amount", true));
        Integer hanghao = 1;
        String pingZhenghao = null;
        //重新排序行号
        for (BankIncomeDat dat : dats2) {
            if (pingZhenghao == null) {
                dat.setHangHao("1");
            } else if (pingZhenghao != null && pingZhenghao.equals(dat.getPingZhengHao())) {
                dat.setHangHao(String.valueOf((Integer.valueOf(hanghao) + 1)));

            } else if (pingZhenghao != null && !pingZhenghao.equals(dat.getPingZhengHao())) {
                dat.setHangHao("1");
            }
            hanghao = Integer.valueOf(dat.getHangHao());
            pingZhenghao = dat.getPingZhengHao();
            updateById(dat);

        }
    }

    private String getLastDanWeiPingZhengHao(String companyName, int month) {
        Wrapper<BankIncomeDat> wrapperIncomeDat = new EntityWrapper<BankIncomeDat>();
        wrapperIncomeDat.eq("company_name", companyName);
        wrapperIncomeDat.and("month(ping_zheng_ri_qi)=" + month);
        wrapperIncomeDat.and("customer_code is not null");
        wrapperIncomeDat.orderBy("ping_zheng_hao", false);
        List<BankIncomeDat> bankIncomeDast = selectList(wrapperIncomeDat);
        if (bankIncomeDast.size() > 0) {
            return bankIncomeDast.get(0).getPingZhengHao();
        } else {
            return null;
        }
    }

    private String getSamePingZhengHao(String companyName, int month) {
        Wrapper<BankIncomeDat> wrapperincomeDat = new EntityWrapper<BankIncomeDat>();
        wrapperincomeDat.eq("company_name", companyName);
        wrapperincomeDat.and("month(ping_zheng_ri_qi)=" + month);
        List<BankIncomeDat> incomeDats = selectList(wrapperincomeDat);
        if (incomeDats.size() > 0) {
            return incomeDats.get(0).getPingZhengHao();
        } else {
            return null;
        }
    }

    @Override
    public void addIncomeExcels(List<InvoiceBankExel> incomes, String companyName, String openDate, String bankSubject,
                                String bankType) throws InstantiationException, IllegalAccessException, ParseException {
        int month = DateUtil.getMonth(DateUtil.getDate(openDate));
        List<InvoiceBankExel> customerExcels = new ArrayList();
        List<InvoiceBankExel> feeExcels = new ArrayList();
        List<InvoiceBankExel> shuiExcels = new ArrayList();
        List<InvoiceBankExel> sheBaoExcels = new ArrayList();
        List<InvoiceBankExel> gongJiJinExcels = new ArrayList();
        if (incomes.size() == 0) {
            return;
        }
        for (InvoiceBankExel excel : incomes) {
            if (excel.getBianMa() != null && excel.getBianMa().equals("单位")) {
                customerExcels.add(excel);
            } else {
                feeExcels.add(excel);
            }
        }
        // ************************先插入单位类型的银收数据****************************//
        /* 获取凭证号 */
        String pingZhengHao = getPingZhengHao(companyName, month);
        if (customerExcels.size() > 0) {
            if (bankType.equals("1")) {// 收入
                BigDecimal totalShouRu = BigDecimal.valueOf(0);
                for (int i = 0; i < customerExcels.size(); i++) {
                    totalShouRu = totalShouRu
                            .add(BigDecimal.valueOf(Double.valueOf(customerExcels.get(i).getShouRu())));
                }
                InvoiceBankExel shouRuExcelTemp = ClassCopyUtil.copyObject(customerExcels.get(0),
                        InvoiceBankExel.class);
                shouRuExcelTemp.setBianMa(bankSubject);
                shouRuExcelTemp.setShouRu(totalShouRu.toString());
                shouRuExcelTemp.setDuiFangHuMing("收到客户款项");
                customerExcels.add(0, shouRuExcelTemp);
            } else {
                BigDecimal totalZhiChu = BigDecimal.valueOf(0);
                for (int i = 0; i < customerExcels.size(); i++) {
                    totalZhiChu = totalZhiChu
                            .add(BigDecimal.valueOf(Double.valueOf(customerExcels.get(i).getZhiChu())));
                }
                InvoiceBankExel shouRuExcelTemp = ClassCopyUtil.copyObject(customerExcels.get(0),
                        InvoiceBankExel.class);
                shouRuExcelTemp.setBianMa(bankSubject);
                shouRuExcelTemp.setZhiChu(totalZhiChu.toString());
                shouRuExcelTemp.setDuiFangHuMing("支付供应商款项");
                customerExcels.add(shouRuExcelTemp);
            }
            String custoemrUuid = UUID.randomUUID().toString();
            for (int i = 0; i < customerExcels.size(); i++) {

                String jine = "";
                String customerType = "";// 1 :客户 2：供应商
                if (bankType.equals("1")) {// 收入
                    jine = customerExcels.get(i).getShouRu();
                    customerType = "1";
                } else {
                    jine = customerExcels.get(i).getZhiChu();
                    customerType = "2";
                }
                CustomerDat customerDat = null;
                BigDecimal amount = new BigDecimal(Double.valueOf(jine)).setScale(2, BigDecimal.ROUND_HALF_UP);
                jine = amount.toString();
                if (bankType.equals("1") && i > 0) {
                    customerDat = customerDatService.addCustomerDat(customerExcels.get(i).getDuiFangHuMing(),
                            customerType, companyName, openDate);
                } else if (bankType.equals("2") && i < customerExcels.size() - 1) {
                    customerDat = customerDatService.addCustomerDat(customerExcels.get(i).getDuiFangHuMing(),
                            customerType, companyName, openDate);
                }
                addIncomeDat(customerExcels.get(i).getRiQi(), customerExcels.get(i).getBianMa(), bankSubject, "单位", String.valueOf(i + 1), pingZhengHao,
                        bankType, customerExcels.get(i).getDuiFangHuMing(), companyName, customerDat, "0", openDate, "",
                        null, jine, jine, "0", "0", false, custoemrUuid);

            }

        }

        // **********************非单位银收分成固定税后科目和社保科目和普通费用科目********************************
        for (Iterator<InvoiceBankExel> it = feeExcels.iterator(); it.hasNext(); ) {
            InvoiceBankExel excel = it.next();
            if (excel.getBianMa() != null
                    && (excel.getBianMa().equals(SubjectEnum.ZENG_ZHI_SHUI_SUBJECT.getValue().toString())
                    || excel.getBianMa().equals(SubjectEnum.CHENG_JIAN_SHUI_SUBJECT.getValue().toString())
                    || excel.getBianMa().equals(SubjectEnum.JIAO_YU_SUBJECT.getValue().toString())
                    || excel.getBianMa().equals(SubjectEnum.DI_FANG_JIAO_YU_SUBJECT.getValue().toString())
                    || excel.getBianMa().equals(SubjectEnum.YIN_HUA_SUBJECT.getValue().toString())
                    || excel.getBianMa().equals(SubjectEnum.YING_JIAO_GE_REN_SUBJECT.getValue().toString())
                    || excel.getBianMa().equals(SubjectEnum.YING_JIAO_SUBJECT.getValue().toString()))) {
                shuiExcels.add(excel);
                it.remove();
            }
            if (excel.getBianMa() != null
                    && (excel.getBianMa().equals(SubjectEnum.SHE_BAO_GONG_SI_SUBJECT.getValue().toString())
                    || excel.getBianMa().equals(SubjectEnum.SHE_BAO_GE_REN_SUBJECT.getValue().toString()))) {
                sheBaoExcels.add(excel);
                it.remove();
            }

            if (excel.getBianMa() != null
                    && (excel.getBianMa().equals(SubjectEnum.GONG_JI_JIN_GONG_SI_SUBJECT.getValue().toString()) || excel
                    .getBianMa().equals(SubjectEnum.GONG_JI_JIN_GE_REN_SUBJECT.getValue().toString()))) {
                gongJiJinExcels.add(excel);
                it.remove();
            }
        }
        // **********************插入非单位银收费用数据********************************
        mergeList(feeExcels, bankType);
        boolean isYfGongZi = false; // 是否有银付工资
        Integer index = 0;
        if (feeExcels.size() > 0) {
            for (int i = 0; i < feeExcels.size(); i++) {

                String jine = "";
                if (bankType.equals("1")) {// 收入
                    jine = feeExcels.get(i).getShouRu();
                } else {
                    jine = feeExcels.get(i).getZhiChu();
                    if (feeExcels.get(i).getBianMa().equals("2151")) { // 有银行支付的工资，需要把对应月份的计提工资转成银行支付工资

                        // *****************计提工资转现金或银付********************//
                        dealYhzf(feeExcels.get(i), companyName, bankSubject, pingZhengHao, bankType, openDate);
                        // *****************看看有没有银行支付工资*********************//
                        boolean isHave = isHaveYhzf(companyName, feeExcels.get(i).getBianMa(), openDate);

                        //如果存在银行支付工资或者没有存在银行支付工资并且金额为0的跳过
                        if (isHave || (!isHave && StringUtil.isEmpty(jine))) {
                            continue;
                        }

                    }
                }

                BigDecimal amount = new BigDecimal(Double.valueOf(jine)).setScale(2, BigDecimal.ROUND_HALF_UP);
                jine = amount.toString();
                addIncomeDat(feeExcels.get(i).getRiQi(), feeExcels.get(i).getBianMa(), bankSubject, "费用", String.valueOf(i + 1), pingZhengHao,
                        bankType, feeExcels.get(i).getDuiFangHuMing(), companyName, null, "0", openDate, "", null, jine,
                        jine, "0", "0", false, null);
            }

        }

        // **********************插入非单位，每月固定税收银付数据********************************

        if (shuiExcels.size() > 0) {
            pingZhengHao = getPingZhengHao(companyName, month);
            BigDecimal shuiShouSum = BigDecimal.valueOf(0);
            for (int i = 0; i < shuiExcels.size(); i++) {
                shuiShouSum = shuiShouSum.add(BigDecimal.valueOf(Double.valueOf(shuiExcels.get(i).getZhiChu())));
            }
            InvoiceBankExel shuiShouExcepTemp = ClassCopyUtil.copyObject(shuiExcels.get(0), InvoiceBankExel.class);
            shuiShouExcepTemp.setBianMa(bankSubject);
            shuiShouExcepTemp.setZhiChu(shuiShouSum.toString());
            shuiShouExcepTemp.setDuiFangHuMing("支付上月税金");
            shuiExcels.add(shuiShouExcepTemp);
            for (int i = 0; i < shuiExcels.size(); i++) {
                String jine = "";
                if (bankType.equals("1")) {// 收入
                    jine = shuiExcels.get(i).getShouRu();
                } else {
                    jine = shuiExcels.get(i).getZhiChu();
                }
                BigDecimal amount = new BigDecimal(Double.valueOf(jine)).setScale(2, BigDecimal.ROUND_HALF_UP);
                jine = amount.toString();
                addIncomeDat(shuiExcels.get(i).getRiQi(), shuiExcels.get(i).getBianMa(), bankSubject, "税收", String.valueOf(i + 1), pingZhengHao,
                        bankType, shuiExcels.get(i).getDuiFangHuMing(), companyName, null, "0", openDate, "", null,
                        jine, jine, "0", "0", false, null);
            }
        }

        // **********************插入非单位，每月固定社保银付数据********************************
        if (sheBaoExcels.size() > 0) {
            pingZhengHao = getPingZhengHao(companyName, month);
            BigDecimal sheBaoSum = BigDecimal.valueOf(0);
            for (int i = 0; i < sheBaoExcels.size(); i++) {
                sheBaoSum = sheBaoSum.add(BigDecimal.valueOf(Double.valueOf(sheBaoExcels.get(i).getZhiChu())));
            }
            InvoiceBankExel sheBaoExcelTemp = ClassCopyUtil.copyObject(sheBaoExcels.get(0), InvoiceBankExel.class);
            sheBaoExcelTemp.setBianMa(bankSubject);
            sheBaoExcelTemp.setZhiChu(sheBaoSum.toString());
            sheBaoExcelTemp.setDuiFangHuMing("支付本月社保");
            sheBaoExcels.add(sheBaoExcelTemp);
            for (int i = 0; i < sheBaoExcels.size(); i++) {
                String jine = "";
                if (bankType.equals("1")) {// 收入
                    jine = sheBaoExcels.get(i).getShouRu();
                } else {
                    jine = sheBaoExcels.get(i).getZhiChu();
                }
                BigDecimal amount = new BigDecimal(Double.valueOf(jine)).setScale(2, BigDecimal.ROUND_HALF_UP);
                jine = amount.toString();
                addIncomeDat(sheBaoExcels.get(i).getRiQi(), sheBaoExcels.get(i).getBianMa(), bankSubject, "社保", String.valueOf(i + 1), pingZhengHao,
                        bankType, sheBaoExcels.get(i).getDuiFangHuMing(), companyName, null, "0", openDate, "", null,
                        jine, jine, "0", "0", false, null);
            }
        }

        // **********************插入非单位，每月固定公积金银付数据********************************
        if (gongJiJinExcels.size() > 0) {
            pingZhengHao = getPingZhengHao(companyName, month);
            BigDecimal gongJiJinSum = BigDecimal.valueOf(0);
            for (int i = 0; i < gongJiJinExcels.size(); i++) {
                gongJiJinSum = gongJiJinSum.add(BigDecimal.valueOf(Double.valueOf(gongJiJinExcels.get(i).getZhiChu())));
            }
            InvoiceBankExel gongJiJinExcelTemp = ClassCopyUtil.copyObject(gongJiJinExcels.get(0),
                    InvoiceBankExel.class);
            gongJiJinExcelTemp.setBianMa(bankSubject);
            gongJiJinExcelTemp.setZhiChu(gongJiJinSum.toString());
            gongJiJinExcelTemp.setDuiFangHuMing("支付公积金");
            gongJiJinExcels.add(gongJiJinExcelTemp);
            for (int i = 0; i < gongJiJinExcels.size(); i++) {
                String jine = "";
                if (bankType.equals("1")) {// 收入
                    jine = gongJiJinExcels.get(i).getShouRu();
                } else {
                    jine = gongJiJinExcels.get(i).getZhiChu();
                }
                BigDecimal amount = new BigDecimal(Double.valueOf(jine)).setScale(2, BigDecimal.ROUND_HALF_UP);
                jine = amount.toString();
                addIncomeDat(gongJiJinExcels.get(i).getRiQi(), gongJiJinExcels.get(i).getBianMa(), bankSubject, "公积金", String.valueOf(i + 1),
                        pingZhengHao, bankType, gongJiJinExcels.get(i).getDuiFangHuMing(), companyName, null, "0",
                        openDate, "", null, jine, jine, "0", "0", false, null);
            }
        }

    }

    @Override
    public String getPingZhengHao(String companyName, int month) {
        String pingZhengHao = "";
        Wrapper<BankIncomeDat> wrapperIncomeDat = new EntityWrapper<BankIncomeDat>();
        wrapperIncomeDat.eq("company_name", companyName);
        wrapperIncomeDat.and("month(ping_zheng_ri_qi)=" + month);
        wrapperIncomeDat.orderBy("ping_zheng_hao", false);
        List<BankIncomeDat> bankIncomeDats = selectList(wrapperIncomeDat);
        if (bankIncomeDats.size() == 0) {
            pingZhengHao = "001";
        } else {
            pingZhengHao = CamsUtil.generateCode(bankIncomeDats.get(0).getPingZhengHao());

        }
        return pingZhengHao;

    }

    public static void mergeList(List<InvoiceBankExel> list, String bankType) {
        HashMap<String, InvoiceBankExel> map = new HashMap<String, InvoiceBankExel>();
        List<InvoiceBankExel> zhiFuGongZiList = new ArrayList<InvoiceBankExel>();
        for (InvoiceBankExel excel : list) {
            if (map.containsKey(excel.getBianMa())) {
                if (bankType.equals("1")) {// 收入
                    Double originalAmount = Double.valueOf(map.get(excel.getBianMa()).getShouRu());
                    Double newAmount = Double.valueOf(excel.getShouRu());
                    excel.setShouRu(String.valueOf((originalAmount + newAmount)));
                } else {
                    if (!excel.getBianMa().equals("2151")) {
                        if (StringUtil.isEmpty(excel.getZhiChu())) {
                            continue;
                        }
                        Double originalAmount = Double.valueOf(map.get(excel.getBianMa()).getZhiChu());
                        Double newAmount = Double.valueOf(excel.getZhiChu());
                        excel.setZhiChu(String.valueOf((originalAmount + newAmount)));
                    } else {
                        if (excel.getBianMa().equals("2151") && StringUtil.isEmpty(excel.getZhaiYao())) {  //如果是应付工资科目，但是摘要为空，跳过
                            continue;
                        }
                        zhiFuGongZiList.add(excel);
                    }

                }

            }

            if (excel.getBianMa().equals("2151") && StringUtil.isEmpty(excel.getZhaiYao())) {  //如果是应付工资科目，但是摘要为空，跳过
                continue;
            }
            map.put(excel.getBianMa(), excel);
        }
        list.clear();
        list.addAll(map.values());
        list.addAll(zhiFuGongZiList);
    }

    @Override
    public void delete(String companyName, String openDate, String bankSubject)
            throws InstantiationException, IllegalAccessException {
        delete(new EntityWrapper<BankIncomeDat>().eq("company_name", companyName).eq("ping_zheng_ri_qi", openDate));

    }

    /**
     * 是否有银行支付科目
     */
    @Override
    public boolean isHaveYhzf(String companyName, String bankSubject, String openDate) {
        List<BankIncomeDat> dats = selectList(new EntityWrapper<BankIncomeDat>().eq("company_name", companyName)
                .eq("ke_mu_dai_ma", bankSubject).eq("ping_zheng_ri_qi", openDate));
        if (dats.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public BankIncomeDat getYhzfDat(String companyName, String bankSubject, String openDate) {
        List<BankIncomeDat> dats = selectList(new EntityWrapper<BankIncomeDat>().eq("company_name", companyName)
                .eq("ke_mu_dai_ma", bankSubject).eq("ping_zheng_ri_qi", openDate));
        if (dats.size() > 0) {
            return dats.get(0);
        } else {
            return null;
        }
    }

    /**
     * 处理银行支付工资
     *
     * @throws ParseException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void dealYhzf(InvoiceBankExel excel, String companyName, String bankSubject, String pingZhengHao,
                          String bankType, String openDate) throws ParseException, InstantiationException, IllegalAccessException {
        // *****************看看有没有银行支付工资*********************//
        boolean isHave = isHaveYhzf(companyName, excel.getBianMa(), openDate);

        if (isHave) {
            return;
        }

        // 如果摘要没有日期，则挂账
        if (StringUtil.isEmpty(excel.getZhaiYao())) {
            return;
        }
        // 如果摘要有日期，有支出金额，计提工资转为银行支出
        if (!StringUtil.isEmpty(excel.getZhaiYao()) && !StringUtil.isEmpty(excel.getZhiChu())
                && !excel.getZhiChu().equals("0.00")) {
            Date dateTemp = DateUtil.getDate(CamsUtil.getYYMMDate(excel.getZhaiYao()));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cale = Calendar.getInstance();
            cale.setTime(dateTemp);
            // 获取前月的最后一天
            cale.add(Calendar.MONTH, 1);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            String salaryDate = format.format(cale.getTime());
            List<JtDat> jtDats = jtDatService
                    .selectList(new EntityWrapper<JtDat>().eq("open_date", salaryDate).eq("company_name", companyName).eq("zhai_yao", "计提本月工资"));
            BigDecimal bankPay = BigDecimal.ZERO;
            BigDecimal cashPay = BigDecimal.ZERO;
            BigDecimal jtPay = BigDecimal.ZERO;
            String uuid = UUID.randomUUID().toString();
            if (jtDats.size() > 0) {
                for (JtDat jtDat : jtDats) {
                    jtPay = jtPay.add(new BigDecimal(jtDat.getBorrowAmount())).setScale(2,BigDecimal.ROUND_HALF_UP);
                }
                if (!StringUtil.isEmpty(excel.getZhiChu())) {
                    bankPay = new BigDecimal(excel.getZhiChu()).setScale(2,BigDecimal.ROUND_HALF_UP);
                }
                cashPay = jtPay.subtract(bankPay);
                if (cashPay.abs().doubleValue() > 0 && bankPay.doubleValue() > 0) {
                    addIncomeDat(excel.getRiQi(), "2151", "1001", "费用", "1", pingZhengHao, bankType, "支付工资", companyName, null,
                            "0", openDate, "", null, cashPay.toString(), jtPay.toString(), "0", "0", true, uuid);
                    addIncomeDat(excel.getRiQi(), "2151", bankSubject, "费用", "1", pingZhengHao, bankType, "支付工资", companyName, null,
                            "0", openDate, "", null, jtPay.toString(), bankPay.toString(), "0", "0", false, uuid);
                } else {
                    addIncomeDat(excel.getRiQi(), "2151", bankSubject, "费用", "1", pingZhengHao, bankType, "支付工资", companyName, null,
                            "0", openDate, "", null, bankPay.toString(), bankPay.toString(), "0", "0", true, uuid);


                }

            }
        }

        // 如果摘要有日期，支出金额为0，计提工资转为现金支付
        if ((!StringUtil.isEmpty(excel.getZhaiYao()) && StringUtil.isEmpty(excel.getZhiChu()))
                || (excel.getZhiChu() != null && excel.getZhiChu().equals("0.00"))) {
            String uuid = UUID.randomUUID().toString();
            Date dateTemp = DateUtil.getDate(CamsUtil.getYYMMDate(excel.getZhaiYao()));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cale = Calendar.getInstance();
            cale.setTime(dateTemp);
            // 获取前月的最后一天
            cale.add(Calendar.MONTH, 1);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            String salaryDate = format.format(cale.getTime());
            List<JtDat> jtDats = jtDatService
                    .selectList(new EntityWrapper<JtDat>().eq("open_date", salaryDate).eq("company_name", companyName).eq("ke_mu_dai_ma", "2151"));
            BigDecimal amount=BigDecimal.ZERO;
            for(JtDat jtDat:jtDats){
                amount=amount.add(new BigDecimal(jtDat.getLocalAmount()));
            }
            if (jtDats.size() > 0) {
                addIncomeDat(excel.getRiQi(), "2151", "1001", "费用", "1", pingZhengHao, bankType, "支付工资", companyName, null, "0",
                        openDate, "", null, amount.toString(), amount.toString(), "0", "0", true, null);

            }
        }
    }

    // if(isYfGongZi) { //没有银付工资，所有计提工资全部转化为现金支付
    // List<JtDat> jtDats= jtDatService.selectList(new
    // EntityWrapper<JtDat>().ge("salary_date", salaryBeginDate).le("salary_date",
    // salaryEndDate).eq("company_name", companyName));
    // if(jtDats.size()>0) {
    // addIncomeDat("1001",bankSubject,"费用","1",pingZhengHao,bankType,"支付工资",
    // companyName, null, "0", openDate,
    // "", null, jtDats.get(0).getLocalAmount(), jtDats.get(0).getLocalAmount(),
    // "0", "0");
    // }
    // }else {
    // List<JtDat> jtDats= jtDatService.selectList(new
    // EntityWrapper<JtDat>().ge("salary_date", salaryBeginDate).le("salary_date",
    // salaryEndDate).eq("company_name", companyName));
    // BigDecimal bankPay=BigDecimal.ZERO;
    // BigDecimal cashPay=BigDecimal.ZERO;
    // BigDecimal jtPay=BigDecimal.ZERO;
    // if(jtDats.size()>0) {
    // jtPay=new BigDecimal(jtDats.get(0).getLocalAmount());
    // if(!StringUtil.isEmpty(feeExcels.get(index).getZhiChu())) {
    // bankPay=new BigDecimal(feeExcels.get(index).getZhiChu());
    // }
    // cashPay=jtPay.subtract(bankPay);
    // if(cashPay.doubleValue()>0&&bankPay.doubleValue()>0) {
    // addIncomeDat("1001",bankSubject,"费用","1",pingZhengHao,bankType,"支付工资",
    // companyName, null, "0", openDate,
    // "", null, cashPay.toString(), cashPay.toString(), "0", "0");
    // addIncomeDat("2151",bankSubject,"费用","1",pingZhengHao,bankType,"支付工资",
    // companyName, null, "0", openDate,
    // "", null, bankPay.toString(), bankPay.toString(), "0", "0");
    // }else {
    // addIncomeDat("2151",bankSubject,"费用","1",pingZhengHao,bankType,"支付工资",
    // companyName, null, "0", openDate,
    // "", null, bankPay.toString(), bankPay.toString(), "0", "0");
    // }
    //
    // }
    // }


    @Override
    public void updateZhiFuShuiJin(String companyName, String openDate) throws Exception {
        // 筛选含有税金科目的银行支付模块记录
        String[] shuiJinObject = {"2171-16", "2171-10", "2171-09", "2171-06", "2171-12", "2171-15", "2171-14",
                "2171-13", "2171-08", "2171-02"};
        List<BankIncomeDat> bankIncomeDats = selectList(new EntityWrapper<BankIncomeDat>()
                .eq("company_name", companyName).eq("ping_zheng_ri_qi", openDate).in("ke_mu_dai_ma", shuiJinObject));
        String uuid = "";
        BigDecimal shuiJinSum = BigDecimal.ZERO;
        if (bankIncomeDats.size() > 0) {
            for (BankIncomeDat dat : bankIncomeDats) {
                uuid = dat.getUuid();
                shuiJinSum = shuiJinSum.add(new BigDecimal(dat.getLocalAmount()));
            }

            BankIncomeDat bankIncomeDat = selectOne(
                    new EntityWrapper<BankIncomeDat>().eq("company_name", companyName).eq("ping_zheng_ri_qi", openDate)
                            .eq("ke_mu_dai_ma", SubjectEnum.ZHI_FU_SUBJECT.getValue()).eq("uuid", uuid));
            // 查找是否存在1002-01的银行支付记录，如果有更新，如果没有插入一条支付记录
            if (bankIncomeDat != null) {
                bankIncomeDat.setLocalAmount(shuiJinSum.toString());
                bankIncomeDat.setAlmsAmount(shuiJinSum.toString());
                updateById(bankIncomeDat);
            } else {
                BankIncomeDat sumDat = ClassCopyUtil.copyObject(bankIncomeDats.get(0), BankIncomeDat.class);
                sumDat.setHangHao(String.valueOf(bankIncomeDats.size() + 1));
                sumDat.setKeMuDaiMa(SubjectEnum.ZHI_FU_SUBJECT.getValue().toString());
                sumDat.setLocalAmount(shuiJinSum.toString());
                sumDat.setZhaiYao("支付上月税金");
                sumDat.setBorrowAmount("0");
                sumDat.setAlmsAmount(shuiJinSum.toString());
                sumDat.setDeductionType("0");
                insert(sumDat);
            }
        }

    }

    @Transactional
    @Override
    public void tranferJtTaxToZhiFu(List<JtDat> list) {
//		String[] shuiJinObject = { "2171-16", "2171-10", "2171-09", "2171-06", "2171-15", "2171-14",
//				"2171-13", "2171-08", "2171-02", "5402" };
//		//查找所有税金科目，待处理的
//		List<JtDat> jtDats=jtDatService.selectList(new EntityWrapper<JtDat>().eq("is_deal", "0").in("ke_mu_dai_ma", shuiJinObject));
//		//把所有记录按公司分组
//		Map<String, List<JtDat>> map= jtDats.stream().collect(Collectors.groupingBy(JtDat::getCompanyName));
//		for(String companyName:map.keySet()) {
//			List<JtDat> list=map.get(companyName);
//			BankIncomeDat incomeDat=null;
//			String uuid=UUID.randomUUID().toString();
//			//如果这个公司的计提月份加上1个月，等于当前日期的月份，说明要新增支付记录，否则跳过
//			Date nextMonth= DateUtil.addMonth2Date(1, DateUtil.getDate(list.get(0).getPingZhengRiQi()));
//			String nextDate=DateUtil.formatDate(DateUtil.getMonthDayStart((nextMonth)));
//			String nowDate=DateUtil.formatDate(DateUtil.getMonthDayStart(new Date()));
//			if(!nextDate.equals(nowDate)) {
//				continue;
//			}
//			
        String companyName = list.get(0).getCompanyName();
        String uuid = UUID.randomUUID().toString();
        BankIncomeDat incomeDat = null;
        Date nextDate = DateUtil.addMonth2Date(1, DateUtil.getDate(list.get(0).getPingZhengRiQi()));
        String pingZhengHao = getPingZhengHao(companyName, DateUtil.getMonth(nextDate));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.setTime(nextDate);
        // 获取前月的最后一天
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        nextDate = cale.getTime();
        BigDecimal shuiJin = BigDecimal.ZERO;
        for (int i = 0; i < list.size(); i++) {

            incomeDat = new BankIncomeDat();
            incomeDat.setUuid(uuid);
            incomeDat.setPingZhengRiQi(DateUtil.formatDate(nextDate));
            incomeDat.setPingZhengZi("记");
            incomeDat.setPingZhengHao(pingZhengHao);
            incomeDat.setZhaiYao(list.get(i).getZhaiYao().replace("计提", "支付"));
            incomeDat.setHuoBiDaiMa("RMB");
            incomeDat.setHuiLv("1");
            incomeDat.setBankType("2");
            incomeDat.setShuLiang("0");
            incomeDat.setDanJia("0");
            incomeDat.setZhiDanRen("系统主管");
            incomeDat.setShenHeRen("");
            incomeDat.setGuoZhangRen("");
            incomeDat.setFuDanJuShu("0");
            incomeDat.setShiFouYiGuoZhang("0");
            incomeDat.setMoBan("11记账凭证");
            incomeDat.setCustomerCode("");
            incomeDat.setBuMen("");
            incomeDat.setYuanGong("");
            incomeDat.setTongJi("");
            incomeDat.setXiangMu("");
            incomeDat.setFuKuanFangFa("");
            incomeDat.setPiaoJuHao("");
            incomeDat.setYuanBiFuKuanJinE("0");
            incomeDat.setPingZhengLaiYuan("1");
            incomeDat.setLaiYuanPingZheng(",,,");
            incomeDat.setDaiDaYin("");
            incomeDat.setZuoFeiBiaoZhi("0");
            incomeDat.setCuoWuBiaoZhi("0");
            incomeDat.setPingZhengCeHao("00");
            incomeDat.setChuNaRen("");
            incomeDat.setCompanyName(companyName);
            incomeDat.setDeductionType("1");
            incomeDat.setInvoiceNumber("");
            incomeDat.setQiJian(String.valueOf(DateUtil.getMonth(nextDate)));
            incomeDat.setCreateTime(new Date());
            incomeDat.setHangHao(String.valueOf(i + 1));
            if (list.get(i).getKeMuDaiMa().equals("5701")
                    || list.get(i).getKeMuDaiMa().equals("5502-20") || list.get(i).getKeMuDaiMa().equals("5502-22") || list.get(i).getKeMuDaiMa().equals("5502-21")) {
                incomeDat.setLocalAmount(list.get(i).getLocalAmount());
                incomeDat.setBorrowAmount("0");
                incomeDat.setAlmsAmount(list.get(i).getLocalAmount());
                incomeDat.setKeMuDaiMa("1002-01"); //银行支付科目

            } else {
                incomeDat.setLocalAmount(list.get(i).getLocalAmount());
                incomeDat.setBorrowAmount(list.get(i).getLocalAmount());
                incomeDat.setAlmsAmount("0");
                incomeDat.setKeMuDaiMa(list.get(i).getKeMuDaiMa());
            }

            if (list.get(i).getKeMuDaiMa().equals("5402")) {
                incomeDat.setLocalAmount(shuiJin.toString());
                incomeDat.setBorrowAmount("0");
                incomeDat.setAlmsAmount(shuiJin.toString());
                incomeDat.setKeMuDaiMa("1002-01"); //银行支付科目
            }
            shuiJin = shuiJin.add(new BigDecimal(incomeDat.getLocalAmount()));
            insert(incomeDat);
            list.get(i).setIsDeal(1);
            jtDatService.updateById(list.get(i));


        }
    }


    @Override
    public void saveIncomeDat(List<BankIncomeDat> addDats, String openDate, String companyName, String customerCode) {
        int month = DateUtil.getMonth(DateUtil.getDate(openDate));
        String pingZhengHao = getPingZhengHao(companyName, month);
        BankIncomeDat incomeDat = null;
        String uuid = UUID.randomUUID().toString();
        for (BankIncomeDat dat : addDats) {
            incomeDat = new BankIncomeDat();
            incomeDat.setUuid(uuid);
            incomeDat.setPingZhengRiQi(openDate);
            incomeDat.setPingZhengZi("记");
            incomeDat.setPingZhengHao(pingZhengHao);
            incomeDat.setBankType("2");
            incomeDat.setZhaiYao(dat.getZhaiYao());
            incomeDat.setKeMuDaiMa(dat.getKeMuDaiMa());
            incomeDat.setHuoBiDaiMa("RMB");
            incomeDat.setHuiLv("1");
            incomeDat.setShuLiang("0");
            incomeDat.setDanJia("0");
            incomeDat.setZhiDanRen("系统主管");
            incomeDat.setShenHeRen("");
            incomeDat.setGuoZhangRen("");
            incomeDat.setFuDanJuShu("0");
            incomeDat.setShiFouYiGuoZhang("0");
            incomeDat.setMoBan("11记账凭证");
            incomeDat.setCustomerCode(dat.getDanWei());
            incomeDat.setBuMen("");
            incomeDat.setYuanGong("");
            incomeDat.setTongJi("");
            incomeDat.setXiangMu("");
            incomeDat.setFuKuanFangFa("");
            incomeDat.setPiaoJuHao("");
            incomeDat.setYuanBiFuKuanJinE("0");
            incomeDat.setPingZhengLaiYuan("1");
            incomeDat.setLaiYuanPingZheng(",,,");
            incomeDat.setDaiDaYin("");
            incomeDat.setZuoFeiBiaoZhi("0");
            incomeDat.setCuoWuBiaoZhi("0");
            incomeDat.setPingZhengCeHao("00");
            incomeDat.setChuNaRen("");
            incomeDat.setCompanyName(companyName);
            incomeDat.setDeductionType("1");
            incomeDat.setInvoiceNumber(dat.getInvoiceNumber());
            incomeDat.setQiJian(String.valueOf(month));
            incomeDat.setCreateTime(new Date());

            incomeDat.setKeMuDaiMa(dat.getKeMuDaiMa());
            incomeDat.setLocalAmount(dat.getLocalAmount());
            incomeDat.setBorrowAmount(dat.getBorrowAmount());
            incomeDat.setAlmsAmount(dat.getAlmsAmount());
            incomeDat.setHangHao(dat.getHangHao());
            incomeDat.setDanWei(customerCode);
            incomeDat.setCustomerCode(customerCode);
            incomeDat.setDeductionType("1");
            insert(incomeDat);
        }

    }

    @Override
    public void saveCommonDat(BankIncomeDat incomeDat,String openDate, String companyName,  String preKeMuDaiMa, String aftKeMuDaiMa) {
        String pingZhengHao=getPingZhengHao(companyName,DateUtil.getMonth(DateUtil.getDate(openDate)));
        String uuid = UUID.randomUUID().toString();
        // 第一行的凭证记录
        BankIncomeDat preIncomeDat=new BankIncomeDat();
        BeanUtils.copyProperties(incomeDat,preIncomeDat);
        preIncomeDat.setCompanyName(companyName);
        preIncomeDat.setUuid(uuid);
        preIncomeDat.setBankType(CamsConstant.BankTypeEnum.ZHI_CHU.getValue().toString());
        preIncomeDat.setPingZhengHao(pingZhengHao);
        preIncomeDat.setPingZhengRiQi(openDate);
        preIncomeDat.setBorrowAmount(incomeDat.getLocalAmount());
        preIncomeDat.setAlmsAmount("0.00");
        preIncomeDat.setKeMuDaiMa(preKeMuDaiMa);
        preIncomeDat.setHangHao("1");
        insert(preIncomeDat);
        // 第二行凭证
        BankIncomeDat aftIncomeDat=new BankIncomeDat();
        BeanUtils.copyProperties(incomeDat,aftIncomeDat);
        aftIncomeDat.setCompanyName(companyName);
        aftIncomeDat.setUuid(uuid);
        aftIncomeDat.setBankType(CamsConstant.BankTypeEnum.ZHI_CHU.getValue().toString());
        aftIncomeDat.setPingZhengHao(pingZhengHao);
        aftIncomeDat.setPingZhengRiQi(openDate);
        aftIncomeDat.setBorrowAmount("0.00");
        aftIncomeDat.setAlmsAmount(incomeDat.getLocalAmount());
        aftIncomeDat.setKeMuDaiMa(aftKeMuDaiMa);
        aftIncomeDat.setHangHao("2");
        insert(aftIncomeDat);

    }

    public static void main(String[] args) {
		//获取上月的日期
		Date lastDate=DateUtil.addMonth2Date(-1, DateUtil.getDate("2020-07-26"));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cale = Calendar.getInstance();
		cale.setTime(lastDate);
		// 获取前月的最后一天
		cale.add(Calendar.MONTH, 1);
		cale.set(Calendar.DAY_OF_MONTH, 0);
		lastDate=cale.getTime();
		String lastPingZhengRiQi=DateUtil.formatDate(lastDate);

    }
}



