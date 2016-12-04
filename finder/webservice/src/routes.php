<?php
// Routes

$app->get('/objeto/list', function ($request, $response) {

	$db = $this->db;
	foreach($db->query('SELECT * FROM objeto') as $row){
		//dados do objeto
		$result['id']         = $row['id'];
		$result['titulo']     = $row['titulo'];
		$result['descricao']  = $row['descricao'];
		$result['contato']    = $row['contato'];

		$return[] = $result;
	};

	return $response->withJson($return);
});

$app->post('/objeto/new', function ($request, $response) {
	$base_photo_url = __DIR__ . DIRECTORY_SEPARATOR . '..' . DIRECTORY_SEPARATOR . 'public' . DIRECTORY_SEPARATOR . 'images' . DIRECTORY_SEPARATOR;

	$dados = $request->getParsedBody();

	$foto = $dados['foto'];

	$db = $this->db;
 	$sth = $db->prepare("INSERT INTO objeto(titulo, descricao, contato) VALUES (:titulo, :descricao, :contato)");

 	$objeto    = json_decode($dados['objeto'], true);
 
  $insertObjeto['titulo']         = $objeto['titulo'];
  $insertObjeto['descricao']   = $objeto['descricao'];
  $insertObjeto['contato']     = $objeto['contato'];

	$sth->execute($insertObjeto);

 	$lastInsertId = $db->lastInsertId('objeto_id_seq');
 	$insertProfessor['id'] = $lastInsertId;
	 	
 	$imgFileName = "{$base_photo_url}{$lastInsertId}.png";
 	$file = fopen($imgFileName, "w");
 	fwrite($file, $foto); 	

 	return $response->withJson($insertObjeto);
});