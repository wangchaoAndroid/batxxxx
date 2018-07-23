package com.fenghuang.component_base.listener;

/**
 * Create by wangchao on 2018/7/21 16:58
 *
 * 说到工厂模式，顺带提一下，可以搭自动注册的顺风车，例如：
 1. 创建一个工厂类接口IFactor，提供创建product的方法
 2. 创建工厂管理类FactoryManager,作为工厂对外的接口，提供外部调用的创建product的方法
 3. 为每个product创建一个factory，实现IFactory接口
 4. 通过添加自动注册插件的配置，将所有module中的factory注册到管理类中（类似于IComponent注册到ComponentManager中）
 5. 外部调用FactoryManager.createProduct(name)来创建具体的product

 这样就不需要手动维护具体product的注册了

 */
public interface IFactor {
    void createProduct(String name);
}
