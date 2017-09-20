package com.bringit.experiment.data;

import org.hibernate.dialect.SQLServerDialect;
import java.sql.Types;

public class MSSQLDialect extends SQLServerDialect {

	public MSSQLDialect() {
		  super();
		  
		  registerColumnType(Types.VARCHAR, "nvarchar($l)");
		  registerColumnType(Types.BIGINT, "bigint");
		  registerColumnType(Types.BIT, "bit");
		 }
	
}
