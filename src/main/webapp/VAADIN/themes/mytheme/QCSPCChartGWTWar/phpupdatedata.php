<?php
header('Content-type: application/json; charset=utf-8');
srand();


$ret = (array(
   'SPCChart' => 
  (array(
     'SampleData' => 
    (array(
       'SampleIntervalRecords' => 
      array (
        (array(
           'SampleValues' => 
          array (
             27.531315151486279,
             33.957716040224042,
             24.310097827061817,
             28.282642847792765,
             30.290851881826502,
          ),
           'BatchCount' => 0,
           'TimeStamp' => 1371830829074,
           'Note' => '',
        )),
        (array(
           'SampleValues' => 
          array (
             27.444285005240214,
             34.389306456150962,
             28.0203674441636,
             33.271533599693662,
             36.830557155827499,
          ),
           'BatchCount' => 1,
           'TimeStamp' => 1371831729074,
           'Note' => '',
        )),
        (array(
           'SampleValues' => 
          array (
            35.213216201092592,
            32.939407410180877,
            33.664855579761628,
            34.173141246091333,
            24.576683179863725,
          ),
           'BatchCount' => 2,
           'TimeStamp' => 1371832629074,
           'Note' => '',
        )),
        (array(
           'SampleValues' => 
          array (
            27.898302097237174,
            25.906531082892915,
            26.950768095191137,
            30.812058501916457,
            31.085075984847936,
          ),
           'BatchCount' => 3,
           'TimeStamp' => 1371833529074,
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
