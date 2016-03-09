<?php

/*
 * Following code will search a product
 */

// array for JSON response
$response = array();


// include db connect class
require 'sps_db_config.php';

// check for products fields
if (isset($_POST['preco']) && isset($_POST['produtoId']) && isset($_POST['clienteId']) && isset($_POST['quantidade'])) {
    $preco = $_POST['preco'];
	$produtoId = $_POST['produtoId'];
	$clienteId = $_POST['clienteId'];
	$quantidade = $_POST['quantidade'];
	if ($quantidade==""){
		$quantidade=0;
		}
	
    $db = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
    if ($db->connect_errno) {
        echo "Failed to connect to MySQL: " . $db->connect_error;
    }

    /* change character set to utf8 */
    if (!$db->set_charset("utf8")) {
        printf("Error loading character set utf8: %s\n", $db->error);
    }
	$time = date("Y-m-d H:i:s",time());
	$data_venda = strtotime($time);


	$db->query("INSERT IGNORE INTO venda (create_time) VALUES (FROM_UNIXTIME('$data_venda'));");
	
	$result = $db->query("SELECT idvenda FROM venda WHERE create_time=FROM_UNIXTIME('$data_venda');") or die(mysql_error());
 
	$row = $result->fetch_array();	

	$idVenda = $row["idvenda"];

	
	$db->query("INSERT IGNORE INTO venda_prod (Cliente_idCliente,Produto_idProduto,venda_idvenda,quantidade) VALUES (\"$clienteId\",\"$produtoId\",\"$idVenda\",\"$quantidade\");");	

	
	$response["quantidade"] = $quantidade;

    $response["idVenda"] = $idVenda;
		
	$response["produtoId"] = $produtoId;

	$response["clienteId"] = $clienteId;	
	
    $response["success"] = 1;
	$response["tamanho"] = 1;
    // echoing JSON response
    echo json_encode($response);

}   else {
    $response["success"] = 0;
	$response["tamanho"] = 0;
	
    // echo no products JSON
    echo json_encode($response);
}

?>
