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



$result = $db->query("SELECT idProduto, nome, quantidade, preco, Categorias_idCategorias FROM Produto ORDER BY nome asc;") or die(mysql_error());


// check for empty result
if ($result->num_rows > 0) {
    // looping through all results
    $response["products"] = array();
 
	$tamanho=0;	

    while ($row = $result->fetch_array()) {
        // temp user array
        $product = array();

		$product["fantasiaId"] = $row["idProduto"];
		
		$product["nome"] = $row["nome"];

 		$product["quantidade"] = $row["quantidade"];

		$product["preco"] = "R$".$row["preco"];
		
		$categoria = $row["Categorias_idCategorias"];
		
		$result2 = $db->query("SELECT sexo, categoria, tamanho FROM Categorias WHERE idCategorias = \"$categoria\";") or die(mysql_error());
		$cat = $result2->fetch_array();
		
		if ($cat["sexo"]==0){
			$product["sexo"] = "fem";
		}
		else $product["sexo"] = "masc";
			
		$product["tamanho"] = $cat["categoria"]." - ".$cat["tamanho"];
	
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
