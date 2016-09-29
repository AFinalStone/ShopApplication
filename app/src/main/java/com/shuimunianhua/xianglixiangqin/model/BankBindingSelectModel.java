package com.shuimunianhua.xianglixiangqin.model;

/**
 * Created by SHI on 2016/9/18 15:45
 *
*/
public class BankBindingSelectModel {
    String ID;
    String Name;
    public String getID() {
        return ID;
    }
    public void setID(String iD) {
        ID = iD;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    @Override
    public boolean equals(Object o) {
        if( o instanceof BankBindingSelectModel && o != null){

            BankBindingSelectModel tempBankBindingSelectModel = (BankBindingSelectModel) o;
            String id = tempBankBindingSelectModel.getID();
            if(ID.equals(id)){
                return true;
            }
        }
        return false;
    }
}
