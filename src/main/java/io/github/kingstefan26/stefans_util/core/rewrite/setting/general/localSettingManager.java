package io.github.kingstefan26.stefans_util.core.rewrite.setting.general;

import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class localSettingManager {
    basicModule parent;
    ArrayList<AbstractSetting> settingList;
    public localSettingManager(basicModule parent){
        this.parent = parent;
        settingList = new ArrayList<>();
    }
    public void add(AbstractSetting s){
        settingList.add(s);
    }
    public void addBulk(AbstractSetting... s){
        settingList.addAll(Arrays.asList(s));
    }
    public AbstractSetting get(String name){
        for(AbstractSetting set : settingList){
            if(Objects.equals(set.name, name)){
                return set;
            }
        }
        throw new IllegalArgumentException();
    }
}
