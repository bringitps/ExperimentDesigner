 var TimeXBarR= 
 {
		    "StaticProperties": {
		        "Canvas": {
		            "Width": 1024,
		            "Height": 768
		        }
		    },
		    "SPCChart": {
		        "InitChartProperties": {
		            "SPCChartType": "MEAN_RANGE_CHART",
		            "ChartMode": "Time",
		            "NumSamplesPerSubgroup": 5,
		            "NumDatapointsInView": 12,
		            "TimeIncrementMinutes": 15
		        },
		        "Scrollbar": {
		            "EnableScrollBar": true,
		            "ScrollbarPosition": "SCROLLBAR_POSITION_MIN"
		        },
		        "SampleData": {
		            "SampleIntervalRecords": []
		        },				
		        "TableSetup": {
		            "HeaderStringsLevel": "HEADER_STRINGS_LEVEL2",
		            "EnableInputStringsDisplay": true,
		            "EnableCategoryValues": false,
		            "EnableCalculatedValues": false,
		            "EnableTotalSamplesValues": false,
		            "EnableNotes": false,
		            "EnableTimeValues": false,
		            "EnableNotesToolTip": false,
		            "EnableAlarmStatusValues": false,
		            "TableBackgroundMode": "TABLE_NO_COLOR_BACKGROUND",
		            "TableAlarmEmphasisMode": "ALARM_HIGHLIGHT_BAR",
		            "ChartAlarmEmphasisMode": "ALARM_HIGHLIGHT_SYMBOL",
		            "ChartData": {
		                "Title": "Variable Control Chart (X-Bar R)",
		              
		                "Operation": "Threading",
		                "SpecificationLimits": "27.0 to 35.0",
		                "Operator": "Pecorazzi",
		                "Machine": "#11",
		                "Gauge": "#8645",
		                "UnitOfMeasure": "0.0001 inch",
		                "ZeroEquals": "zero",
		                "DateString": "7/04/2013",
		                "NotesMessage": "Control limits prepared May 10",
		                "NotesHeader": "NOTES",
		                "CalculatedItemDecimals": 1,
		                "ProcessCapabilityDecimals": 2,
		                "SampleItemDecimals": 1,
		                "ProcessCapabilitySetup": {
		                    "LSLValue": 25,
		                    "USLValue": 40,
		                    "EnableCPK": false,
		                    "EnableCPM": false,
		                    "EnablePPK": false
		                }
		            }
		        },
		        "Events": {
		            "EnableDataToolTip": true,
		            "EnableNotesToolTip": true

		        },
		        "Methods": {
		            "AutoCalculateControlLimits": true,
		            "AutoScaleYAxes": true,
		            "RebuildUsingCurrentData": true
		        }
		    }
		};
 
	   