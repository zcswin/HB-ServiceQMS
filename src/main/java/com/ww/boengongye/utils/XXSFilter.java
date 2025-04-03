package com.ww.boengongye.utils;



import java.lang.reflect.Field;

public class XXSFilter {

public static String checkStr(String str){

    if(null!=str){
        str=str.replaceAll("<script>","");
        str=str.replaceAll( "</script>","");
        str=str.replaceAll("<","");
        str=str.replaceAll(">","");
//        str=str.replaceAll("on*","");
    }


    return str;
}

    public static String checkStr2(String str){

        if(null!=str){
            str=str.replaceAll("<script>","");
            str=str.replaceAll( "</script>","");

        }


        return str;
    }
    /**
     * 根据属性名获取属性值
     *
     * @param fieldName
     * @param object
     * @return
     */
    public static String getFieldValueByFieldName(String fieldName, Object object) {
        try {
            Field field = object.getClass().getField(fieldName);
            //设置对象的访问权限，保证对private的属性的访问

            return  (String)field.get(object);
        } catch (Exception e) {

            return null;
        }
    }

    public static void main(String[] args) throws IllegalAccessException {
//       System.out.println(checkStr("<<<dsfasdfasf>>>dddon*daad<script>dssdd</script>"));
//
//
//        /**
//         *  返回Class 对象所表示的类或接口的所有可访问公共字段。
//         */
//        Field[] f1= TAnnals.class.getFields();
//        System.out.println("TAnnals类里面的公共字段属性的个数为：" +f1.length+"个，分别为：");
//        for(int i=0;i<f1.length;i++){
//            String attributeName=f1[i].getName();
//            System.out.println(attributeName);
//        }
//
//        /**
//         * 返回 Class 对象所表示的类或接口所声明的所有字段，
//         * 包括公共、保护、默认（包）访问和私有字段，但不包括继承的字段。
//         */
//        Field[] f=TAnnals.class.getDeclaredFields();
//        System.out.println("TAnnals类里面的所有字段属性的个数为："+f.length+"个，分别为：");
//        for(int i=0;i<f.length;i++){
//            String attributeName=f[i].getName();
//            System.out.println(attributeName);
//        }
//
//        TAnnals TAnnals=new TAnnals();
//        TAnnals.setBusinessIncome("sssssssssssssadsfdsf");
//        TAnnals.setCreateTime("<<<dsfasdfasf>>>dddon*daad<script>dssdd</script>");
//        System.out.println(getFieldValueByFieldName("createTime",TAnnals));
//        //给TAnnals对象赋值
//        for(int i=0;i<f.length;i++){
//            //获取属相名
//            String attributeName=f[i].getName();
//            //将属性名的首字母变为大写，为执行set/get方法做准备
//            String methodName=attributeName.substring(0,1).toUpperCase()+attributeName.substring(1);
//            try{
//                //获取TAnnals类当前属性的setXXX方法（私有和公有方法）
//                /*Method setMethod=TAnnals.class.getDeclaredMethod("set"+methodName);*/
//                //获取TAnnals类当前属性的setXXX方法（只能获取公有方法）
//                Method setMethod=TAnnals.class.getMethod("set"+methodName,String.class);
//                //执行该set方法
//                setMethod.invoke(TAnnals,checkStr(getFieldValueByFieldName(attributeName,TAnnals)));
//            }catch (NoSuchMethodException e) {
////                try {
////                    Method setMethod=TAnnals.class.getMethod("set"+methodName,int.class);
////                    setMethod.invoke(TAnnals,123);
////                } catch (Exception e2) {
//////                    f[i].set(TAnnals,2222);
////                }
//
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//
//        }
//        //从TAnnals对象取值
//        for(int i=0;i<f.length;i++){
//            //获取属相名
//            String attributeName=f[i].getName();
//            //将属性名的首字母变为大写，为执行set/get方法做准备
//            String methodName=attributeName.substring(0,1).toUpperCase()+attributeName.substring(1);
//            Object result;
//            try{
//                //获取TAnnals类当前属性的setXXX方法（私有和公有方法）
//                /*Method setMethod=TAnnals.class.getDeclaredMethod("set"+methodName);*/
//                //获取TAnnals类当前属性的setXXX方法（只能获取公有方法）
//                Method getMethod=TAnnals.class.getMethod("get"+methodName);
//                //执行该set方法
//                result=getMethod.invoke(TAnnals);
//
//            }catch(NoSuchMethodException | InvocationTargetException e){
//                result=f[i].get(TAnnals);
//            }
//            System.out.println("属性："+attributeName+"="+result);
////            System.out.println("属性2："+attributeName+"="+TAnnals.toString());
//        }

    }

}
