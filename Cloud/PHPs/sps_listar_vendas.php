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

$result = $db->query("SELECT idvenda,create_time FROM venda ORDER BY create_time asc;") or die(mysql_error());

// check for empty result
if ($result->num_rows > 0) {
    // looping through all results
    $response["products"] = array();
 
	$tamanho=0;	

    while ($row = $result->fetch_array()) {
        // temp user array
        $product = array();
		
		$idvenda = $row["idvenda"];
		
		$product["create_time"] = $row["create_time"];
		
		$result2 = $db->query("SELECT Cliente_idCliente,Produto_idProduto,quantidade FROM venda_prod WHERE venda_idvenda='$idvenda';") or die(mysql_error());
		$row2 = $result2->fetch_array();
		
 		$product["quantidade"] = $row2["quantidade"];

		$idCliente = $row2["Cliente_idCliente"];
				
		$idProduto = $row2["Produto_idProduto"];
		
		$result3 = $db->query("SELECT nome FROM Cliente WHERE idCliente='$idCliente';") or die(mysql_error());
		$row3 = $result3->fetch_array();
		
		$product["nomeCliente"] = $row3["nome"];

		$result4 = $db->query("SELECT nome, preco FROM Produto  WHERE idProduto='$idProduto';") or die(mysql_error());
		$row4 = $result4->fetch_array();
		
		$product["nomeProduto"] = $row4["nome"];
		$product["preco"] = "R$".$row4["preco"]*$product["quantidade"].",00";
		
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
