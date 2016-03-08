<?php

/*
 * Following code will search a product
 */

// array for JSON response
$response = array();


// include db connect class
require 'sps_db_config.php';

// check for products fields
if (isset($_POST['clienteId'])) {
    $clienteId = $_POST['clienteId'];

    $db = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
    if ($db->connect_errno) {
        echo "Failed to connect to MySQL: " . $db->connect_error;
    }

    /* change character set to utf8 */
    if (!$db->set_charset("utf8")) {
        printf("Error loading character set utf8: %s\n", $db->error);
    }


$result = $db->query("SELECT nome, cpf, telefone FROM Cliente WHERE idCliente='$clienteId';") or die(mysql_error());

    
// check for empty result
if ($result->num_rows > 0) {
    // looping through all results
    $response["product"] = array();
	
    $product = array();
	
	$row = $result->fetch_array();	
	
	$product["nome"] = $row["nome"];

    $product["cpf"] = $row["cpf"];
		
	$product["telefone"] = $row["telefone"];

	array_push($response["product"], $product);
		
	
    $response["success"] = 1;
	$response["tamanho"] = 1;
    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
	$response["tamanho"] = 0;
	
    // echo no products JSON
    echo json_encode($response);
}
}

?>
