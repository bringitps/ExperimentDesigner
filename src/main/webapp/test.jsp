<%@ page import="com.bringit.experiment.dal.Test"%>  
<jsp:useBean id="obj" class="com.bringit.experiment.bll.SysUser">  
</jsp:useBean>  
<jsp:setProperty property="*" name="obj"/>  
  
<%

	//int i=Test.createSysUserTest(); 
	//if(Test.buildDbSchemaAndTable())  
	//	out.print("DB Schema and Table created.");  
	out.print(Test.buildDbSchemaAndTable());

        
  
%>  