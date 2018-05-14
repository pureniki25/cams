package com.hongte.alms.common.util;

/**
 * @author zengkun
 * @since 2018/2/2
 */
public class Constant {

    //Excel存储文件夹名字
//    public static final String  EXCEL_FILE_NAME = "excelTemp//";


    public  static  final  String DEV_DEFAULT_USER="devDefaultUser";
    public  static  final  String DEFAULT_SYS_USER="sysAutoSet";
    public  static  final  String SYS_DEFAULT_USER="sysDefaultUser";
    public  static  final  String ADMIN_ID="0111130000";



    //申请减免的流程类型ID
    public static final String APPLY_DERATE_PROCEEE_TYPE_ID ="b138a92f-f796-11e7-94ed-94c69109b34a";
    //申请减免的最后一个节点值
    public static final Integer APPLY_DERATE_PROCEEE_LAST_STEP =400;
    //申请减免的第一个节点值
    public static final Integer APPLY_DERATE_PROCEEE_BEGIN_STEP =100;


    /**
     * 新增参数时,用于占位的参数值
     */
    public static final Integer SYS_PARAMETER_PLACEHOLDER = -Integer.MAX_VALUE ;
    
    /**
     * 五级分类设置业务类别-条件操作类型：1、增加
     */
    public static final String FIVE_LEVEL_CLASSIFY_CONDITION_ADD = "1";
    /**
     * 五级分类设置业务类别-条件操作类型：2、修改
     */
    public static final String FIVE_LEVEL_CLASSIFY_CONDITION_UPDATE = "2";
    /**
     * 五级分类设置业务类别-条件操作类型：3、删除
     */
    public static final String FIVE_LEVEL_CLASSIFY_CONDITION_DELETE = "3";
    /**
     * 五级分类参数类型，对应tb_sys_parameter的param_type
     */
    public static final String FIVE_LEVEL_CLASSIFY = "fiveLevelClassify";
    /**
     * 五级分类操作源：贷后定时任务
     */
    public static final String FIVE_LEVEL_CLASSIFY_OP_SOUSE_TYPE_ALMS_JOB = "1";
    /**
     * 五级分类操作源：贷后跟踪记录
     */
    public static final String FIVE_LEVEL_CLASSIFY_OP_SOUSE_TYPE_ALMS_LOG = "2";
    /**
     * 五级分类操作源：风控
     */
    public static final String FIVE_LEVEL_CLASSIFY_OP_SOUSE_TYPE_ALMS_RISK_CONTROL = "3";
    /**
     * 五级分类：首期逾期
     */
    public static final String FIVE_LEVEL_CLASSIFY_FIRST_PERIOD_OVERDUE = "首月逾期";
    /**
     * 五级分类：本金逾期
     */
    public static final String FIVE_LEVEL_CLASSIFY_PRINCIPAL_OVERDUE = "本金逾期";
    /**
     * 五级分类：利息逾期
     */
    public static final String FIVE_LEVEL_CLASSIFY_INTEREST_OVERDUE = "利息逾期";
    /**
     * 五级分类：案件情况
     */
    public static final String FIVE_LEVEL_CLASSIFY_CASE = "案件情况";
    /**
     * 五级分类：档案资料情况
     */
    public static final String FIVE_LEVEL_CLASSIFY_PROFILES = "档案资料情况";
    /**
     * 五级分类：抵押物情况
     */
    public static final String FIVE_LEVEL_CLASSIFY_GUARANTEE = "抵押物情况";
    /**
     * 五级分类：主借款人情况
     */
    public static final String FIVE_LEVEL_CLASSIFY_MAIN_BORROWER = "主借款人情况";
    /**
     * 五级分类：主借款人情况
     */
    public static final String FIVE_LEVEL_CLASSIFY_SPLIT = "#--SPLIT--#";
}
