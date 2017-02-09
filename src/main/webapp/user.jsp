<%@ page import="com.bringit.experiment.dal.Test"%>  
<jsp:useBean id="obj" class="com.bringit.experiment.bll.SysUser">  
</jsp:useBean>  
<jsp:setProperty property="*" name="obj"/>  
  
<%

	int i=Test.createSysUserTest();  
	if(i>0)  
		out.print("You are successfully registered");  

        
  
%>  