<?php 
	$tracker_url = "http://layar.mere-su.dk/tracker.php";
	$search_word = $_GET["SEARCHBOX"];
	
	//Google-stuff to get ZIP
	$Rlat = $_GET['lat'];
	$Rlon = $_GET['lon'];
	$zip = 0;
	$googleData = file_get_contents("http://maps.googleapis.com/maps/api/geocode/json?latlng=".$Rlat.",".$Rlon."&sensor=false");
	$google_o = json_decode($googleData);
	$dataComp = $google_o->results[0]->{'address_components'};
	
	for ($u = 0; $u < count($dataComp); $u++){
		$dataTypes = $dataComp[$u]->types;
		for ($e = 0; $e < count($dataTypes); $e++){
			if($dataTypes[$e] == "postal_code"){
				$zip = $dataComp[$u]->long_name;
			}
		}
	}

	$data = file_get_contents("http://api.dba.dk/public/v1/ads?q=".$search_word."&ps=40&pn=1&zip=".$zip);
	$json_o = json_decode($data);
	
	$numItems = count($json_o->ads);
	
	$resArray = array(
		"layer" => "tingtilsalg",
		"errorString" => "ok",
		"errorCode" => 0
	);

	$itemArray = array();
	for ($i = 0; $i<$numItems; $i++){
		
		$id = $json_o->ads[$i]->{'ad-external-reference-id'};
		$title =  $json_o->ads[$i]->title;
		$description = $json_o->ads[$i]->description;
		$price = $json_o->ads[$i]->price;
		$link = $tracker_url . "?url=" . $json_o->ads[$i]->{'listing-url'}->Href;
		$picture = $json_o->ads[$i]->{'all-pictures'}->{'thumbnail-pictures'}[0]->link->Href;
		$lon = $json_o->ads[$i]->{'ad-address'}->longitude;
		$lat = $json_o->ads[$i]->{'ad-address'}->latitude;
		
		if($lon != 0){
			$item = array(
				"id" => $id,
				"anchor" => array(
					"geolocation" => array(
						"lat" => $lat,
						"lon"=> $lon
					)
				),
				"text" => array(
					"title" => $title,
					"description" => $description,
					"footnote" => "Pris: " . $price . " kr."
				),
				"imageURL" => $picture,
				"actions" => array( 
						array(
						"uri" => $link,
						"label" => "Besøg siden",
						"activityType" => 1,
						"contentType" => "text/html",
						"method" => "GET"
					)
				)
			);
			array_push($itemArray, $item);
		}
	}
	$resArray["hotspots"] = $itemArray;
	
	echo json_encode($resArray);
?>