package io.github.kingstefan26.stefans_util.service;

import net.minecraft.client.Minecraft;

import static io.github.kingstefan26.stefans_util.service.ServiceStatusEnum.*;

public abstract class Service {
    public Minecraft mc = Minecraft.getMinecraft();
    public String name;
    public ServiceStatusEnum serviceStatus = NULL;
    public Service(String name){this.name = name;}
    public void rootStart(){
        serviceStatus = INITIALISING;
        start();
        serviceStatus = RUNNING;
    }
    public void rootStop(){
        serviceStatus = STOPPING;
        stop();
        serviceStatus = STOPPED;
    }
    public void setErrorState(){
        serviceStatus = ERRORED;
    }
    public abstract void start();
    public abstract void stop();
}