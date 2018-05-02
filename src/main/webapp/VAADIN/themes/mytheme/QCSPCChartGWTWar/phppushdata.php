<?php
header('Content-type: application/json; charset=utf-8');

srand();

$ret = array(
				"SampleValues"=> array(
					27.53 + rand(0,5),
					33.95 + rand(0,5),
					24.31 + rand(0,5),
					28.28 + rand(0,5),
					30.29 + rand(0,5),
				),
				"BatchCount"=> 0,
				"TimeStamp"=> 1371830829074,
				"Note"=> "" 
			);
      echo json_encode($ret); 


exit();
?>
