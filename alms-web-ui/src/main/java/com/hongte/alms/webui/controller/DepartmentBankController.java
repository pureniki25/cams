package com.hongte.alms.webui.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 张贵宏 on 2018/7/6 18:07
 */
@Controller
@RequestMapping("/cams")
public class DepartmentBankController {
    @RequestMapping("/departmentBank")
    public String departmentBank() {
        return "/cams/departmentBank" ;
    }
    
    @RequestMapping("/invoice")
    public String invoice() {
        return "/cams/invoice" ;
    }
    
    @RequestMapping("/product")
    public String product() {
        return "/cams/product" ;
    }
    @RequestMapping("/customer")
    public String customer() {
        return "/cams/customer" ;
    }
    
    @RequestMapping("/balance")
    public String balance() {
        return "/cams/balance" ;
    }
    
    @RequestMapping("/buy")
    public String buy() {
        return "/cams/buy" ;
    }
    @RequestMapping("/fee")
    public String fee() {
        return "/cams/fee" ;
    }
    
    @RequestMapping("/sellAndBuy")
    public String sellAndBuy() {
        return "/cams/sellAndBuy" ;
    }
    @RequestMapping("/cash")
    public String cash() {
        return "/cams/cash" ;
    }
    
    
    @RequestMapping("/pay")
    public String pay() {
        return "/cams/pay" ;
    }
    @RequestMapping("/income")
    public String income() {
        return "/cams/income" ;
    }

    @RequestMapping("/chengben")
    public String chengBen() {
        return "/cams/chengben" ;
    }
    
    @RequestMapping("/jt")
    public String jt() {
        return "/cams/jt" ;
    }


    @RequestMapping("/temporary")
    public String temporary() {
        return "/cams/temporary";
    }
    
    /**
     * 领料表
     * @return
     */
    
    @RequestMapping("/pick")
    public String pick() {
        return "/cams/pick" ;
    }
    
    
    /**
     * 工资表
     * @return
     */
    
    @RequestMapping("/salary")
    public String salary() {
        return "/cams/salary" ;
    }
    
    /**
     * 入库表
     * @return
     */
    @RequestMapping("/store")
    public String store() {
        return "/cams/store" ;
    }
    
    
    
    @RequestMapping("/repayment")
    public String repayment() {
        return "/cams/repayment" ;
    }
    
    @RequestMapping("/assets")
    public String assets() {
        return "/cams/assets" ;
    }
    @RequestMapping("/inventory")
    public String inventory() {
        return "/cams/inventory" ;
    }
    @RequestMapping("/subjectRest")
    public String subjectRest() {
        return "/cams/subjectRest" ;
    }
      
    @RequestMapping("/customerRest")
    public String customerRest() {
        return "/cams/customerRest" ;
    }
    
    @RequestMapping("/subjectFirst")
    public String subjectFirst() {
        return "/cams/subjectFirst" ;
    }
    
    @RequestMapping("/customerFirst")
    public String customerFirst() {
        return "/cams/customerFirst" ;
    }
    
    @RequestMapping("/profit")
    public String profit() {
        return "/cams/profit" ;
    }


}
