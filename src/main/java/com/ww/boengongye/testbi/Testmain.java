package com.ww.boengongye.testbi;


import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Value;
import smartbi.catalogtree.ICatalogElement;
import smartbi.sdk.ClientConnector;
import smartbi.sdk.service.businessview.BusinessViewService;
import smartbi.sdk.service.catalog.CatalogElement;
import smartbi.sdk.service.catalog.CatalogService;
import smartbi.sdk.service.user.UserManagerService;

import java.util.List;

public class Testmain {

    public static void main(String[] args) {
        ClientConnector conn = new ClientConnector("http://172.20.88.43:18080/smartbi");
        conn.open("lmz", "12345"); // 以管理员身份登录

        CatalogService catalogService = new CatalogService(conn);
//
        System.out.println(catalogService.getRootElements());
        System.out.println(catalogService.getChildElements("PUBLIC_PAGES"));
//        System.out.println(catalogService.getChildElements("ROLE_MANAGER"));
//        System.out.println(catalogService.getCatalogElementById("Iff808081017e71047104f9ad017e712cee6e017f"));
        List<? extends ICatalogElement> datas= catalogService.getChildElements("Iff808081017e71047104f9ad017e712cee6e017f");
        for( ICatalogElement d:datas){
            System.out.println(d.getAlias());
            System.out.println(d.getId());
            System.out.println(d.getCustomImage());
            System.out.println(d.isHasChild());
            System.out.println(catalogService.getChildElements(d.getId()));
        }
        String DATA = "admin1";
        byte[] data = Base64.encode(DATA.getBytes());
        System.out.println("BASE64加密：" + new String(data));
        UserManagerService us=new UserManagerService(conn);
//        us.
//        System.out.println(us.getAllDepartments());
////        System.out.println(us.createDepartment());
//        System.out.println(us.getAllUsers());
//        System.out.println(us.getAllRoles());


        //        ICatalogElement dd =new  ICatalogElement();
//        BusinessViewService bvs= new BusinessViewService(conn);
//        System.out.println(bvs.loadViewData("I2c94d8a9018114301430491f01811e035a5600b3",1));
        conn.close();
    }
}




class A implements  ICatalogElement{

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public boolean isHasChild() {
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getExtended() {
        return null;
    }

    @Override
    public void setExtended(String s) {

    }

    @Override
    public boolean isHiddenInBrowse() {
        return false;
    }

    @Override
    public boolean isShowOnPC() {
        return false;
    }

    @Override
    public boolean isShowOnPad() {
        return false;
    }

    @Override
    public boolean isShowOnPhone() {
        return false;
    }

    @Override
    public String getCustomImage() {
        return null;
    }

    @Override
    public String getCustomMobileImage() {
        return null;
    }

    @Override
    public String getMobileImageId() {
        return null;
    }

    @Override
    public boolean isDetectChild() {
        return false;
    }
}




//打开透视分析获取透视分析数据
		/*
		InvokeResult insight = conn.remoteInvoke("InsightService","openQuery",new Object[]{"I2c94d8a90180eefaeefabd730180f908ff7d015f",""});
		JSONObject result = JSONObject.fromObject(insight.getResult());
		String clientId = result.getString("clientId");
		System.out.println(clientId);
		Integer pageNo = new Integer(0);
		InvokeResult result2 = conn.remoteInvoke("InsightService", "getCellData", new Object[]{clientId,pageNo}); // 用户名
		System.out.println(result2.getResult());
		*/

//获取数据集数据
//        BusinessViewService bs = new BusinessViewService(conn);
//        String bizViewId = "I2c94d8a9018112831283256e01811ea2b02e432f";//数据集id
//        //给数据集传参，多个参数之间以逗号分隔，json数组格式参数-转字符串传递
//        Object pobj = new Object();
////        pobj="[{'id':'OutputParameter.I4028826a015ec2b0c2b0d173015ec2b6d19d008d.销售区域','value':'华南'},{ 'id' :'OutputParameter.I4028826a015ec2b0c2b0d173015ec2b6d19d008d.销售城市' ,'value':'海口'}]";
//        String paramsJsonArrStr=pobj.toString();
//        //示例：[{"id":"OutputParameter.I2c90909013e1b3f80113e1c195870038.地区名称参数","value":"华南"},{ "id" :"OutputParameter.I2c90909013e1b3f80113e1c195870038.与地区关联的城市" ,"value":"深圳,厦门"}]
//        int rowsPerPage = 20; //加载数据时每页返回的行数
//        boolean getTotalRows = true; //是否获取总行数
//        ViewMetaData vd = bs.openLoadDataView(bizViewId, paramsJsonArrStr, rowsPerPage, getTotalRows);
//        String cId =  vd.getClientId(); //获取打开查询后的客户端标识
//        int totalRows = (int) vd.getTotalRowCount(); //获取总行数
//
//        int pageNum = 0; //页码(第1页的页码为0，第2页的页码为1，如此类推)
//        List<List<String>> list = bs.loadViewData(cId, pageNum);
//        System.out.println(list.toString());
//
//
//