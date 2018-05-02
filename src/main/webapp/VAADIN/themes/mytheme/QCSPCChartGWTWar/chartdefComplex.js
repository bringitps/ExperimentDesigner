 	   var WECOAndSupplementalRules= 
	   {
 			    "StaticProperties": {
 			        "Canvas": {
 			            "Width": 800,
 			            "Height": 550
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
	  		            "ScrollbarPosition":"SCROLLBAR_POSITION_UNCHANGED"
	  		        },
	  		        "TableSetup": {
	  		            "HeaderStringsLevel": "HEADER_STRINGS_LEVEL1",
	  		            "EnableInputStringsDisplay": true,
	  		            "EnableCategoryValues": false,
	  		            "EnableCalculatedValues": true,
	  		            "EnableTotalSamplesValues": true,
	  		            "EnableNotes": true,
	  		            "EnableTimeValues": true,
	  		            "EnableNotesToolTip": true,
	  		            "TableBackgroundMode": "TABLE_NO_COLOR_BACKGROUND",
	  		            "TableAlarmEmphasisMode": "ALARM_HIGHLIGHT_BAR",
	  		            "ChartAlarmEmphasisMode": "ALARM_HIGHLIGHT_SYMBOL",
	  		            "ChartData": {
	  		                "DateString": "This can be any string!",
	  		                "Title": "WECO and Supplemental Rules",
	  		                "PartNumber": "283501",
	  		                "ChartNumber": "17",
	  		                "PartName": "Transmission Casing Bolt",
	  		                "Operation": "Threading",
	  		                "SpecificationLimits": "27.0 to 35.0",
	  		                "Operator": "J. Fenamore",
	  		                "Machine": "#11",
	  		                "Gauge": "#8645",
	  		                "UnitOfMeasure": "0.0001 inch",
	  		                "ZeroEquals": "zero",
	  		                "NotesMessage": "Control limits prepared May 10",
	  		                "NotesHeader": "NOTES"
	  		            }
	  		        },
	  		        "Events": {
	  		            "EnableDataToolTip": false,
	  		            "EnableJSONDataToolTip": true,
	  		            "AlarmStateEventEnable": true
	  		        },
	  		        "PrimaryChartSetup": {

	  		  		    "NamedRuleSet":
	  				    {
	  				   	  "RuleSet": "WECOANDSUPP_RULES"
	  				    }
	  		        },
	                "SampleData": {
	                    "DataSimulation": {
	                        "StartCount": 0,
	                        "Count": 50,
	                        "Mean": 27,
	                        "Range": 5
	                    }
	                },
	  		        "Methods": {
	  		            "AutoCalculateControlLimits": true,
	  		            "AutoScaleYAxes": true,
	  		            "RebuildUsingCurrentData": true
	  		        }
	  		    }
	  		} ;		   


	   