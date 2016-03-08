<?php

/*
 * Following code will list all the events
 */

// array for JSON response
$response = array();


// include db connect class
require 'sps_db_config.php';



    $db = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
    if ($db->connect_errno) {
        echo "Failed to connect to MySQL: " . $db->connect_error;
    }

    /* change character set to utf8 */
    if (!$db->set_charset("utf8")) {
        printf("Error loading character set utf8: %s\n", $db->error);
    }



$result = $db->query("SELECT idCliente, nome, cpf, telefone FROM Cliente ORDER BY nome asc;") or die(mysql_error());


// check for empty result
if ($result->num_rows > 0) {
    // looping through all results
    $response["products"] = array();
 
	$tamanho=0;	

    while ($row = $result->fetch_array()) {
        // temp user array
        $product = array();

		$product["clienteId"] = $row["idCliente"];
		
		$product["nome"] = $row["nome"];

 		$product["cpf"] = $row["cpf"];

		$product["telefone"] = $row["telefone"];
			
		array_push($response["products"], $product);
		$tamanho++;			
	}


    $response["success"] = 1;
	$response["tamanho"] = $tamanho;
    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
	$response["tamanho"] = 0;

    // echo no products JSON
    echo json_encode($response);
}

?>
