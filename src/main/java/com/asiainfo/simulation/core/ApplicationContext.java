package com.asiainfo.simulation.core;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.asiainfo.simulation.aop.advice.IAdvice;
import com.asiainfo.simulation.condition.Conditional;
import com.asiainfo.simulation.condition.ICondition;
import com.asiainfo.simulation.support.Context;

public class ApplicationContext implements BeanFactory {

	private Map<String, Object> beans = new HashMap<String, Object>();
	
	public ApplicationContext(String path) throws Exception {
		
		SAXBuilder sb = new SAXBuilder();
		Document doc = sb.build(this.getClass().getClassLoader().getResourceAsStream(path));
		final Element root = doc.getRootElement();	//获取根结点
		
		this.parseContext(root);
		this.createBean(root);
		this.ioc(root);
		this.aop(root);
		//aop cache实现
		//startup实现
	}

	@Override
	public Object getBean(String name) {
		return name == null ? null : beans.get(name);
	}
	
	//解析上下文
	private void parseContext(Element root) {

		//读取配置参数
		List<Element> contextList = root.getChildren("context");
		for (Element element : contextList) {
			
			List<Element> listParam = element.getChildren("param");
			for (Element paramElement : listParam) {
				
				String key = paramElement.getAttributeValue("name");
				String value = paramElement.getAttributeValue("value");
				Context.put(key, value);
			}
		}
	}
	
	//创建bean
	private void createBean(Element root) throws Exception {

		List<Element> list = root.getChildren("bean");	//取名为bean的所有元素		
		//创建bean
		for (Element element : list) {
			
			String id = element.getAttributeValue("id");	//取id值 
			String cl = element.getAttributeValue("class");	//取class值
			String conArg = element.getAttributeValue("contructor-arg");	//构造参数
			//判断condition的值
			Conditional conditional = Class.forName(cl).getAnnotation(Conditional.class);
			if (conditional != null) {
				
				Class<?> clazz = conditional.value();
				if (clazz != null) {
					ICondition con = (ICondition) clazz.newInstance();
					if (!con.execute())
						continue;
				}
			}
			
			Object obj;
			if (conArg == null) {
				obj = Class.forName(cl).newInstance();
			} else {
				Constructor<?> constructor = Class.forName(cl).getConstructor(String.class);
				obj = constructor.newInstance(conArg);
			}
			
			if (beans.get(id) != null)
				throw new RuntimeException("error in build " + obj.getClass().getName() + ", caused by duplicate bean name");
			beans.put(id, obj);
		}
	}
	
	//装配依赖
	private void ioc(Element root) throws Exception {
		
		List<Element> list = root.getChildren("bean");	//取名为bean的所有元素
		//装配依赖
		for (Element element : list) {
			
			String id = element.getAttributeValue("id");	//取id值 
			String cl = element.getAttributeValue("class");	//取class值
			//判断condition的值
			Conditional conditional = Class.forName(cl).getAnnotation(Conditional.class);
			if (conditional != null) {
				
				Class<?> clazz = conditional.value();
				if (clazz != null) {
					ICondition con = (ICondition) clazz.newInstance();
					if (!con.execute())
						continue;
				}
			}
			
			List<Element> propertyList = (List<Element>) element.getChildren("property");
			if (propertyList == null)
				continue;
			
			for (Element propertyElement : propertyList) {
				
				String name = propertyElement.getAttributeValue("name");
				String ref = propertyElement.getAttributeValue("ref");
				String value = propertyElement.getAttributeValue("value");
				String type = propertyElement.getAttributeValue("type");
				Class<?> propertyClazz = null;
				
				if (type != null) {
					try {
						propertyClazz = Class.forName(type);
					} catch (Exception ex) {
						System.out.println("name=" + name + ", type=" + type);
						propertyClazz = (type.equals("int")) ? int.class : (type.equals("boolean")) ? boolean.class : String.class;
					}
				}
					
				Object obj = beans.get(id);
				//拼凑方法名，实现setUserDao方法 
				String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
				if (value != null) {
					Method m1 = obj.getClass().getMethod(methodName, propertyClazz);
					if (propertyClazz == int.class) {
						m1.invoke(obj, Integer.parseInt(value));
					} else if (propertyClazz == boolean.class) {
						m1.invoke(obj, Boolean.parseBoolean(value));
					} else {
						m1.invoke(obj, value);
					}
					continue;
			    }
				
				Object bean = beans.get(ref);	//UserDaoImpl instance
				Class<?> clazz = bean.getClass().getInterfaces().length == 0 ? bean.getClass() : bean.getClass().getInterfaces()[0];
				
				//利用反射机制获取方法对象 
				Method m = obj.getClass().getMethod(methodName, clazz);
				m.invoke(obj, bean);	//调用set方法
			}
		}
	}
	
	//aop代理
	private void aop(Element root) throws Exception {
		
		//aop代理
		List<Element> aopList = root.getChildren("aop");	//取名为aop的所有元素
		for (Element element : aopList) {
			
			String proxy = element.getAttributeValue("proxy");	//取id值
			Object proxyBean = Class.forName(proxy).newInstance();
			List<Element> propertyList = (List<Element>) element.getChildren("property");
			if (propertyList == null)
				continue;
			
			String aopName = null;
			for (Element propertyElement : propertyList) {
				
				String name = propertyElement.getAttributeValue("name");
				String ref = propertyElement.getAttributeValue("ref");
				//拼凑方法名，实现set方法 
				String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
				Object bean = beans.get(ref);
				
				Class<?> clazz;
				if (bean instanceof IAdvice) {
					clazz = IAdvice.class;
				} else {
					clazz = Object.class;
					aopName = ref;
				}
				
				//利用反射机制获取方法对象 
				Method m = proxyBean.getClass().getMethod(methodName, clazz);
				m.invoke(proxyBean, bean);	//调用set方法
			}
			
			String proxyName = "getProxy";
			Method m = proxyBean.getClass().getMethod(proxyName);
			
			beans.put(aopName, m.invoke(proxyBean));
		}
	}
}
