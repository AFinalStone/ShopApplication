package com.shi.xianglixiangqin.model;

import java.io.Serializable;

/***
 * 积分商品套餐颜色支付方式对象
 * @author SHI
 * 2016-12-21 15:05:55
 */
public class GoodsIntegralPackageColorBuyTypeModel implements Serializable {
    /**方式(0是积分+金额，1是全积分)**/
    private int Buytype;
    /**使用金额**/
    private double Useje;
    /**使用积分**/
    private long Usejf;

    public int getBuytype() {
        return Buytype;
    }

    public void setBuytype(int buytype) {
        Buytype = buytype;
    }

    public double getUseje() {
        return Useje;
    }

    public void setUseje(double useje) {
        Useje = useje;
    }

    public long getUsejf() {
        return Usejf;
    }

    public void setUsejf(long usejf) {
        Usejf = usejf;
    }
}
