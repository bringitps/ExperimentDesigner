<%@ page import="com.bringit.experiment.dal.Test"%>  
<jsp:useBean id="obj" class="com.bringit.experiment.bll.SysUser">  
</jsp:useBean>  
<jsp:setProperty property="*" name="obj"/>  
  
<%

	//int i=Test.createSysUserTest(); 
	/*if(Test.buildDbSchema())  
		out.print("DB Schema and Table created.");
	else
		out.print("Customer Failed.");
	//out.print(Test.getPropertyTest());
  */
  out.print(Test.buildDbSchemaAndTable());
%>  