<?php
header('Content-type: application/json; charset=utf-8');
srand();


$ret = 
(array(
   'SPCChart' => 
  (array(
     'InitChartProperties' => 
    (array(
       'SPCChartType' => 'INDIVIDUAL_RANGE_CHART',
       'ChartMode' => 'Batch',
       'NumSamplesPerSubgroup' => 1,
       'NumDatapointsInView' => 12,
       'TimeIncrementMinutes' => 15,
    )),
     'ChartPositioning' => 
    (array(
       'GraphStartPosX' => 0.125,
    )),
     'Scrollbar' => 
    (array(
       'EnableScrollBar' => true,
    )),
     'TableSetup' => 
    (array(
       'HeaderStringsLevel' => 'HEADER_STRINGS_LEVEL3',
       'EnableInputStringsDisplay' => true,
       'EnableCategoryValues' => false,
       'EnableCalculatedValues' => false,
       'EnableTotalSamplesValues' => false,
       'EnableNotes' => false,
       'EnableTimeValues' => true,
       'EnableNotesToolTip' => true,
       'TableBackgroundMode' => 'TABLE_SINGLE_COLOR_BACKGROUND_GRIDCELL',
       'BackgroundColor1' => 'WHITE',
       'BackgroundColor2' => 'GRAY',
       'TableAlarmEmphasisMode' => 'ALARM_HIGHLIGHT_BAR',
       'ChartAlarmEmphasisMode' => 'ALARM_HIGHLIGHT_SYMBOL',
       'ChartData' => 
      (array(
         'Title' => 'Individual Range Chart',
         'PartNumber' => '283501',
         'ChartNumber' => '17',
         'PartName' => 'TransmissionCasingBolt',
         'Operation' => 'Threading',
         'SpecificationLimits' => '27.0 to 35.0',
         'Operator' => 'J.Fenamore',
         'Machine' => '#11',
         'Gauge' => '#8645',
         'UnitOfMeasure' => '0.0001inch',
         'ZeroEquals' => 'zero',
         'DateString' => '7/04/2013',
         'NotesMessage' => 'ControllimitspreparedMay10',
         'NotesHeader' => 'NOTES',
      )),
    )),
     'Events' => 
    (array(
       'EnableDataToolTip' => true,
       'AlarmStateEventEnable' => true,
    )),
     'SampleData' => 
    (array(
       'SampleIntervalRecords' => 
      array (
        (array(
           'SampleValues' => 
          array (
            27.531315151486279,
          ),
           'BatchCount' => 0,
           'TimeStamp' => 1371830829074,
           'Note' => '',
        )),
        (array(
           'SampleValues' => 
          array (
            27.444285005240214,
          ),
           'BatchCount' => 1,
           'TimeStamp' => 1371831729074,
           'Note' => '',
        )),
        (array(
           'SampleValues' => 
          array (
            35.213216201092592,
          ),
           'BatchCount' => 2,
           'TimeStamp' => 1371832629074,
           'Note' => '',
        )),
        (array(
           'SampleValues' => 
          array (
            27.898302097237174,
          ),
           'BatchCount' => 3,
           'TimeStamp' => 1371833529074,
           'Note' => '',
        )),
        (array(
           'SampleValues' => 
          array (
            22.945498739895271,
          ),
           'BatchCount' => 4,
           'TimeStamp' => 1371834429074,
           'Note' => '',
        )),
        (array(
           'SampleValues' => 
          array (
            25.757197681039116,
          ),
           'BatchCount' => 5,
           'TimeStamp' => 1371835329074,
           'Note' => '',
        )),
        (array(
           'SampleValues' => 
          array (
            34.045031694399327,
          ),
           'BatchCount' => 6,
           'TimeStamp' => 1371836229074,
           'Note' => '',
        )),
        (array(
           'SampleValues' => 
          array (
            33.893820803904475,
          ),
           'BatchCount' => 7,
           'TimeStamp' => 1371837129074,
           'Note' => '',
        )),
        (array(
           'SampleValues' => 
          array (
            35.856892224090331,
          ),
           'BatchCount' => 8,
           'TimeStamp' => 1371838029074,
           'Note' => '',
        )),
        (array(
           'SampleValues' => 
          array (
            30.749686153841765,
          ),
           'BatchCount' => 9,
           'TimeStamp' => 1371838929074,
           'Note' => '',
        )),
        (array(
           'SampleValues' => 
          array (
            35.272384950080252,
          ),
           'BatchCount' => 10,
           'TimeStamp' => 1371839829074,
           'Note' => '',
        )),
      ),
    )),
     'Methods' => 
    (array(
       'AutoCalculateControlLimits' => true,
       'AutoScaleYAxes' => true,
       'RebuildUsingCurrentData' => true,
    )),
  )),
));

     echo json_encode($ret);  

exit();
?>
