<?php

/*
 * Following code will search a product
 */

// array for JSON response
$response = array();


// include db connect class
require 'sps_db_config.php';

// check for products fields
if (isset($_POST['nome']) && isset($_POST['preco']) && isset($_POST['quantidade']) && isset($_POST['idCategorias'])) {
    $nome = $_POST['nome'];
	$preco = $_POST['preco'];
	$quantidade = $_POST['quantidade'];

	if ($_POST['idCategorias']==null)
	{
		$idCategorias=1;
	}
	else {
			$idCategorias=$_POST['idCategorias'];
		}

    $db = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
    if ($db->connect_errno) {
        echo "Failed to connect to MySQL: " . $db->connect_error;
    }

    /* change character set to utf8 */
    if (!$db->set_charset("utf8")) {
        printf("Error loading character set utf8: %s\n", $db->error);
    }

	$db->query("INSERT IGNORE INTO Produto (nome, preco, quantidade, Categorias_idCategorias) VALUES (\"$nome\",\"$preco\",\"$quantidade\",\"$idCategorias\");");

    $response["success"] = 1;
	$response["tamanho"] = 1;
    // echoing JSON response
    echo json_encode($response);
}   else {
    // no products found
    $response["success"] = 0;
	$response["tamanho"] = 0;
	
    // echo no products JSON
    echo json_encode($response);
}

?>
