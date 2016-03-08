<?php

/*
 * Following code will search a product
 */

// array for JSON response
$response = array();


// include db connect class
require 'sps_db_config.php';

// check for products fields
if (isset($_POST['nome_produto']) && isset($_POST['tamanho_produto'])) {
    $nome_produto = $_POST['nome_produto'];
	$tamanho_produto = $_POST['tamanho_produto'];


    $db = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
    if ($db->connect_errno) {
        echo "Failed to connect to MySQL: " . $db->connect_error;
    }

    /* change character set to utf8 */
    if (!$db->set_charset("utf8")) {
        printf("Error loading character set utf8: %s\n", $db->error);
    }

if ($tamanho_produto == null)
{
$result = $db->query("SELECT nome, quantidade, tamanho FROM Produto WHERE nome='$nome_produto' ORDER BY nome_produto asc;") or die(mysql_error());
} else if($nome_produto == null)
	{
	$result = $db->query("SELECT nome, quantidade, tamanho FROM Produto WHERE tamanho='$tamanho_produto' ORDER BY nome_produto asc;") or die(mysql_error());	
	} else {
		$result = $db->query("SELECT nome, quantidade, tamanho FROM Produto WHERE nome='$nome_produto' AND tamanho='$tamanho_produto' ORDER BY nome_produto asc;") or die(mysql_error());	
	}


// check for empty result
if ($result->num_rows > 0) {
    // looping through all results
    $response["products"] = array();
 
	$tamanho=0;	
	
    while ($row = $result->fetch_array()) {
        // temp user array
        $product = array();
		
		$product["produto"] = $row["nome"];

        $product["tamanho"] = $row["tamanho"];
		
		$product["quantidade"] = $row["quantidade"];

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
}}

?>
