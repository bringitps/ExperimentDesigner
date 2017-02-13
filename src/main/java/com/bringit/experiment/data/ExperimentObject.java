package com.bringit.experiment.data;

import java.util.Arrays;
import java.util.Date;

public class ExperimentObject 
{
	
    private int id;   
	private boolean fullParsed; //If XML file has a schema error this flag must be set to false 
    
    //XML HEADER
    private String ExperimentName;
    
    //ExperimentFields Nodes
    public ExperimentResult[] experimentResultNodes;
    
     
    public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public boolean isFullParsed() {
		return fullParsed;
	}



	public void setFullParsed(boolean fullParsed) {
		this.fullParsed = fullParsed;
	}



	public String getExperimentName() {
		return ExperimentName;
	}



	public void setExperimentName(String experimentName) {
		ExperimentName = experimentName;
	}



	public ExperimentResult[] getExperimentResultNodes() {
		return experimentResultNodes;
	}



	public void setExperimentResultNodes(ExperimentResult[] experimentResult) {
		this.experimentResultNodes = experimentResult;
	}
	
	public static class ExperimentResult{
		private String id;
        private String moduleSerialNumber;
        private String fpbcSerialNumber;
        private String dieId;
        private String waferId;
        private String enId;
        private String workOrder;
        private String dateTime;
        private String memsToMagnetOutlineX;
        private String memsToMagnetOutlineY;
        private String angle;
        private String testResult;
        
        public ExperimentResult(){
			this.id = null;
			this.moduleSerialNumber = null;
			this.fpbcSerialNumber = null;
			this.dieId = null;
			this.waferId = null;
			this.enId = null;
			this.workOrder = null;
			this.dateTime = null;
			this.memsToMagnetOutlineX = null;
			this.memsToMagnetOutlineY = null;
			this.angle = null;
			this.testResult = null;
        }
        
		public ExperimentResult(String id, String moduleSerialNumber, String fpbcSerialNumber, String dieId,
				String waferId, String enId, String workOrder, String dateTime, String memsToMagnetOutlineX,
				String memsToMagnetOutlineY, String angle, String testResult) {
			this.id = id;
			this.moduleSerialNumber = moduleSerialNumber;
			this.fpbcSerialNumber = fpbcSerialNumber;
			this.dieId = dieId;
			this.waferId = waferId;
			this.enId = enId;
			this.workOrder = workOrder;
			this.dateTime = dateTime;
			this.memsToMagnetOutlineX = memsToMagnetOutlineX;
			this.memsToMagnetOutlineY = memsToMagnetOutlineY;
			this.angle = angle;
			this.testResult = testResult;
		}
        
	    	public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getModuleSerialNumber() {
				return moduleSerialNumber;
			}

			public void setModuleSerialNumber(String moduleSerialNumber) {
				this.moduleSerialNumber = moduleSerialNumber;
			}

			public String getFpbcSerialNumber() {
				return fpbcSerialNumber;
			}

			public void setFpbcSerialNumber(String fpbcSerialNumber) {
				this.fpbcSerialNumber = fpbcSerialNumber;
			}

			public String getDieId() {
				return dieId;
			}

			public void setDieId(String dieId) {
				this.dieId = dieId;
			}

			public String getWaferId() {
				return waferId;
			}

			public void setWaferId(String waferId) {
				this.waferId = waferId;
			}

			public String getEnId() {
				return enId;
			}

			public void setEnId(String enId) {
				this.enId = enId;
			}

			public String getWorkOrder() {
				return workOrder;
			}

			public void setWorkOrder(String workOrder) {
				this.workOrder = workOrder;
			}

			public String getDateTime() {
				return dateTime;
			}

			public void setDateTime(String dateTime) {
				this.dateTime = dateTime;
			}

			public String getMemsToMagnetOutlineX() {
				return memsToMagnetOutlineX;
			}

			public void setMemsToMagnetOutlineX(String memsToMagnetOutlineX) {
				this.memsToMagnetOutlineX = memsToMagnetOutlineX;
			}

			public String getMemsToMagnetOutlineY() {
				return memsToMagnetOutlineY;
			}

			public void setMemsToMagnetOutlineY(String memsToMagnetOutlineY) {
				this.memsToMagnetOutlineY = memsToMagnetOutlineY;
			}

			public String getAngle() {
				return angle;
			}

			public void setAngle(String angle) {
				this.angle = angle;
			}

			public String getTestResult() {
				return testResult;
			}

			public void setTestResult(String testResult) {
				this.testResult = testResult;
			}

    }



	
}



